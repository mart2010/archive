package ca.po.model.service.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import ca.po.model.Lab;
import ca.po.model.PurchaseOrder;
import ca.po.model.User;
import ca.po.model.dao.PoDAO;
import ca.po.model.dao.PoSearchCriteria;
import ca.po.model.dao.UserDAO;
import ca.po.model.service.PoManager;
import ca.po.model.type.PoStatusCode;
import ca.po.util.SendEmailConfirmation;
import ca.po.util.converter.MoneyCADConverter;


/**
 * @author MOUELLET
 * 
 */
public class PoManagerImpl implements PoManager {
    
    private PoDAO poDAO;
    public void setPoDAO(PoDAO poDAO) {
        this.poDAO = poDAO;
    } 
    private UserDAO userDAO;
    public void setUserDAO(UserDAO userDAO){
        this.userDAO = userDAO;
    }
    
    private SendEmailConfirmation emailConfirmation;
    public void setEmailConfirmation(SendEmailConfirmation emailSender) {
        this.emailConfirmation = emailSender;
    }

    private MoneyCADConverter cadConverter;
    public void setCadConverter(MoneyCADConverter conv){
        this.cadConverter= conv;
    }
    
    public PurchaseOrder getPO(Long id, boolean fetchLineItems) {
        return this.poDAO.getPO(id,fetchLineItems);
    }
    
    public PurchaseOrder getPOWithItemsSupplied(Long id){
        return this.poDAO.getPOWithItemsSupplied(id);
    }
    
    
    
    public List getPOsByUser(User user, boolean fetchLineItems) {
        return poDAO.getPOsByUserId(user.getId(),fetchLineItems);
    }
    
    /* 
     * Fetch the lineItems, since this may be used in UI  
     */
    public List getPOsByStatusCode(PoStatusCode code, boolean fetchLineItems) {
        return this.poDAO.getPOsByStatus(code,fetchLineItems);
    }
    
    public List getReceivedPOsByCriteria(PoSearchCriteria criteria){
        return this.poDAO.getPOSByCriteria(criteria,PoStatusCode.RECEIVED);
    }
    
    
    
    // Ensure that a proper CadConverter is set, otherwise Hibernate will choke on the getCadEstimate...
    private void initializeCADConverter(PurchaseOrder po){
        if (po.getMoneyCADConverter()== null)
            po.setMoneyCADConverter(cadConverter);
    }
    
    //
    private void storeProposedPO(PurchaseOrder po, User user, Lab lab) {
        //Always initialize new Po
        if (!po.isInitialized()){
            po.initialize(user,lab);
        }
        initializeCADConverter(po);

        this.poDAO.savePO(po);
    }
    
    /*
     * Used only for saving new or saving Proposed PO
     * tuned to avoid the call to DB for user with only one Lab
     */
    public void saveProposedPO(PurchaseOrder po, User user, Long labId) {
        Lab lab;
        if (user.getLabs().size()==1) {
            lab = (Lab) user.getLabs().iterator().next();
        } //otherwise use the parameter LabId 
        else {
            assert (labId != null);
	        lab = (Lab) userDAO.getLab(labId);
        }
        
        this.storeProposedPO(po,user,lab);
    }
    
    /*
     * This method is called by Admin on a list of 'Proposed' PO
     * The transaction fails unless all PO are converted to 'Validated'
     * and saved successfully into the DB (will rollback when any Exception is thrown)
     *  
     */
    public void saveValidatedPOs(Collection purchaseOrders, User admin) {
        
        for (Iterator iter= purchaseOrders.iterator(); iter.hasNext(); ){
            PurchaseOrder po = (PurchaseOrder) iter.next();
            initializeCADConverter(po);
	
            //this throws IllegalStateException, if currentStatus different from 'Proposed'            
            po.changeStatus(PoStatusCode.VALIDATED,admin);
            poDAO.savePO(po);
        }
    }

    
    /*
     * This method can only be called by Admin on all 'validated' PO
     */
    public void saveFollowUpPO(PurchaseOrder po, PoStatusCode newStatus, User admin) {
        boolean sendReceiveConf= false;
        if (!po.getCurrentStatusChange().equals(newStatus)){
            po.changeStatus(newStatus,admin);
            if (newStatus.equals(PoStatusCode.RECEIVED))
                sendReceiveConf = true;
        }
        poDAO.savePO(po);
        //email alert for
        
        if (sendReceiveConf){
            String[] mailTo = new String[] { po.getOrderByUser().getEmail()} ;
            this.emailConfirmation.setMailTo(mailTo);
            //this method logs any Exception thrown, so it will not have an impact on current transaction
            this.emailConfirmation.sendMail(po);    
        }
    }
    
    /*
     * This method can be called by User who owns a 'Proposed' PO,
     * or by an Admin on any PO.
     */
    public void saveCancelledPO(PurchaseOrder po, User user) {
        po.cancel(user);
        initializeCADConverter(po);
        poDAO.savePO(po);
    }
        
        
        
}
    
