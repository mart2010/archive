package ca.po.web.form;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;



/**
 * @struts.form name="poForUserForm"
 */
public class PoForUserForm extends BaseForm implements Serializable {
    
    private String orderFor;
    private String confirmNumber;
    private String project;

    //selLabId for PO
    private Long selLabId;

    //to have SupplierId and Quantity by Item
    private Map supIds = new HashMap();
    private Map qtys = new HashMap();
    //to have bidInfo and catalogNumber (readOnly) available to the view
    private Map bidInfos = new HashMap();
    private Map catalogNbs = new HashMap();
       
    //? see what I'm doing with this
   // private PaymentType selectedPaymentType;
    

    public void clearMaps(){
        this.getQtys().clear();
        this.getSupIds().clear();
        this.getBidInfos().clear();
        this.getCatalogNbs().clear();
    }
    
    public String getConfirmNumber() {
        return confirmNumber;
    }
    public void setConfirmNumber(String confirmNumber) {
        this.confirmNumber = confirmNumber;
    }
    public Long getSelLabId() {
        return selLabId;
    }
    public void setSelLabId(Long selLabId) {
        this.selLabId = selLabId;
    }
    public String getOrderFor() {
        return orderFor;
    }
    public void setOrderFor(String orderFor) {
        this.orderFor = orderFor;
    }

    public String getQty(String itemId){
        return (String) this.qtys.get(itemId);
    }
    /**
     * XDoclet does not seem to support Map-based property, so done in validation method
     * struts.validator type="required"
     * struts.validator-args arg0resource="po.lineItemQty"
     * struts.validator type="integer" msgkey="po.lineItemIntQty"
     */
    public void setQty(String itemId, String qty){
        this.qtys.put(itemId,qty);
    }
    public String getSupId(String itemId){
        return (String) this.supIds.get(itemId);
    }
    public void setSupId(String itemId, String supId){
        this.supIds.put(itemId,supId);
    }
    public String getBidInfo(String itemId){
        return (String) this.bidInfos.get(itemId);
    }
    public void setBidInfo(String itemId, String bidInfo){
        this.bidInfos.put(itemId,bidInfo);
    }
    public String getCatalogNb(String itemId){
        return (String) this.catalogNbs.get(itemId);
    }
    public void setCatalogNb(String itemId, String catalogNb){
        this.catalogNbs.put(itemId,catalogNb);
    }

    
    public String getProject() {
        return project;
    }
    /**
     * 
     * @struts.validator type="required"
     * @struts.validator-args arg0resource="po.project"
     */
    public void setProject(String project) {
        this.project = project;
    }
    public Map getQtys() {
        return qtys;
    }
    public Map getSupIds() {
        return supIds;
    }
    public Map getBidInfos() {
        return bidInfos;
    }
    public Map getCatalogNbs() {
        return catalogNbs;
    }

    
    public ActionErrors validate(ActionMapping mapping, 
            HttpServletRequest request) {
        // Perform validator framework validations
        ActionErrors errors = super.validate(mapping, request);
        Collection qtySet = qtys.values();

        for (Iterator iter = qtySet.iterator(); iter.hasNext();){
            String qty = (String) iter.next();
            try {
                 if (Integer.parseInt(qty) < 1){
                     errors.add("quantity", new ActionMessage("po.lineItemIntQty"));
                     break;
                 }
             }
             catch (NumberFormatException ex) {
                 errors.add("quantity", new ActionMessage("po.lineItemIntQty"));
                 break;
             }
        }
        return errors;
        
    }

    
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        
    }

}
