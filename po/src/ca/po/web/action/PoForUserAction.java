package ca.po.web.action;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.springframework.orm.hibernate.HibernateOptimisticLockingFailureException;

import ca.po.model.Item;
import ca.po.model.ItemSupplied;
import ca.po.model.PoLineItem;
import ca.po.model.PurchaseOrder;
import ca.po.model.Supplier;
import ca.po.model.type.PoStatusCode;
import ca.po.web.form.PoForUserForm;


/**
 * 
 * @struts.action path="/editUserPo" name="poForUserForm" scope="session"
 * 		validate="false" parameter="edit"
 * @struts.action path="/addItemToUserPo" parameter="addItem"
 * @struts.action path="/removeItemToUserPo" parameter="removeItem"
 * @struts.action path="/saveUserPo" name="poForUserForm" scope="session"
 *  	validate="true" input="/WEB-INF/jsp/editPoByUser.jsp" parameter="save"
 * @struts.action path="/listUserPo" parameter="list"
 * @struts.action path="/goToListItem" name="poForUserForm" scope="session"
 * 		validate="false" parameter="goToListItem"
 * 
 * @struts.action-forward name="list" path="/WEB-INF/jsp/listPoByUser.jsp"
 * @struts.action-forward name="listAction" path="/listUserPo.html"
 * @struts.action-forward name="editUserPo" path="/WEB-INF/jsp/editPoByUser.jsp"
 * @struts.action-forward name="viewUserPoAction" path="/editUserPo.html"
 *    
 */

public class PoForUserAction extends BaseAction {
    
    
    public ActionForward list(ActionMapping mapping,ActionForm form,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {
        
        //store usersList in request scope
        request.setAttribute("poList",getPoManager().getPOsByUser(getUser(),false) );
        
        //return a forward to list
        return mapping.findForward("list");
    }

    /*
     * For Admin that edit PO from the validate list, they need to be re-forwarded 
     * appropriately
     */
    public ActionForward save(ActionMapping mapping,ActionForm form,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {
    
        ActionForward next = getNextActionForward(mapping,request); 
        
        if (isCancelled(request)) {
            request.getSession().removeAttribute("currentPo");
            removeFormBean(mapping,request);
            
            if (next!=null) return next;
            else return mapping.findForward("listAction");
        }

        
        PoForUserForm poForUserForm = (PoForUserForm) form;
        PurchaseOrder currentPo = (PurchaseOrder) request.getSession().
       								getAttribute("currentPo");

        //refresh the currentPo
        refreshCurrentPo(request,poForUserForm);
        
        //if user click Refresh button, simply return to jsp
        if (request.getParameter("refresh").length()!=0) {
            return mapping.findForward("editUserPo");
        }
            
        ActionMessages messages = getMessages(request);        

        //Save the ProposedPO
        try {
            getPoManager().saveProposedPO(currentPo,getUser(),poForUserForm.getSelLabId());
            //save was successful, store a message
            messages.add(ActionMessages.GLOBAL_MESSAGE, 
                    	new ActionMessage("po.submitted"));
            saveMessages(request, messages);
        }
        catch (HibernateOptimisticLockingFailureException ex){
            messages.add(ActionMessages.GLOBAL_MESSAGE, 
                	new ActionMessage("po.optimisticLockError"));
        	saveMessages(request, messages);
        }
        
        //clean-up
        request.getSession().removeAttribute("currentPo");
        removeFormBean(mapping,request);
        
        if (next!=null) return next;
        else return mapping.findForward("listAction");
    
    }

    private void refreshCurrentPo(HttpServletRequest request, PoForUserForm form) throws Exception {
        PurchaseOrder po = (PurchaseOrder) request.getSession().
									getAttribute("currentPo");
        
        BeanUtils.copyProperties(po,form);
        //get any change for quantity and supplier
        for (Iterator iter = po.getPoLineItems().iterator(); iter.hasNext();) {
            PoLineItem lineItem = (PoLineItem) iter.next();
            //quantity is already validated to be number
            lineItem.setQuantity(Integer.parseInt(form.getQty(lineItem.getItem().getId().toString())));

            if (!lineItem.getSupplier().getId().equals(new Long(form.getSupId(lineItem.getItem().getId().toString())))) {
                Supplier supplier = getSupplierManager().getSupplier(new Long(form.getSupId(lineItem.getItem().getId().toString())),false);
                lineItem.setSupplier(supplier);
                //refresh the bidInfo in the form
                for (Iterator itemS = lineItem.getItem().getItemSupplies().iterator(); itemS.hasNext();){
                    ItemSupplied itemSupplied = (ItemSupplied) itemS.next();
                    if (itemSupplied.getSupplier().getId().equals(supplier.getId())){
                        form.setBidInfo(lineItem.getItem().getId().toString(), 
                             itemSupplied.getBidInfo());
                        form.setCatalogNb(lineItem.getItem().getId().toString(), 
                                itemSupplied.getCatalogNumber());
                    }
                }
            }

            
            /* alternative way to get that when po has not the lineItem.itemsupplied prefetch         
	            if (!lineItem.getSupplier().getId().equals(new Long(form.getSupId(lineItem.getItem().getId().toString())))) {
	                //refresh the bidInfo in the form, but first go fetch the itemSupplies...
	                List itemSupplies = getItemManager().getItemSuppliesByItems
	            		(new Long[] {lineItem.getItem().getId()}, true);
	                
	                for (Iterator itemS = itemSupplies.iterator(); itemS.hasNext();){
	                    ItemSupplied itemSupplied = (ItemSupplied) itemS.next();
	                    if (itemSupplied.getSupplier().getId().equals(new Long(form.getSupId(lineItem.getItem().getId().toString())))) {
	                        lineItem.setSupplier(itemSupplied.getSupplier());
	                        form.setBidInfo(lineItem.getItem().getId().toString(), 
	                             itemSupplied.getBidInfo());
	                        form.setCatalogNb(lineItem.getItem().getId().toString(), 
	                                itemSupplied.getCatalogNumber());
	                    }
	                }
	            }
            */
        }
    }

    
    /* Three possible scenario to enter this action :
     * 1- user needs to edit an existing PO (must have status "Proposed"), this assumes request param poId 
     * 2- user has just added a new Item or simply verifies the open PO
     *    (addItemToUserPo should have created currentPo for new, or it should already be present
     * 3- user is checking a not existing active order, resulting in a Message
     */ 
    public ActionForward edit(ActionMapping mapping,ActionForm form,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {
        
        PurchaseOrder currentPo;
        String poId = request.getParameter("poId");
        //user has requested to edit an existing PO
        if ((poId != null) && (poId.length() > 0)){
            currentPo = getPoManager().getPOWithItemsSupplied(new Long(poId));
            assert (currentPo != null) : "Request param poId is invalid!";
            
            request.getSession().setAttribute("currentPo",currentPo);
        } else //user has added more item (or request to edit current PO) 
        { 
            currentPo = (PurchaseOrder) request.getSession().
								getAttribute("currentPo");
            //scenario 3
            if (currentPo == null) {
                ActionMessages messages = getMessages(request);
                messages.add(ActionMessages.GLOBAL_MESSAGE, 
                        	new ActionMessage("po.noCurrentActivePO"));
                saveMessages(request, messages);
                return mapping.findForward("editUserPo");
            }
        }

        if ((currentPo.getCurrentStatusChange()!= null) &&
                (currentPo.getCurrentStatusChange()!= PoStatusCode.PROPOSED)) 
        	throw new IllegalAccessError("User can only edit 'Proposed' Order");
        
        //ensure that a valid CADConverter is set
        if (currentPo.getMoneyCADConverter() == null)
            currentPo.setMoneyCADConverter(getCadConverter());
        
        //refresh the view formbean
        PoForUserForm poForUserForm = (PoForUserForm) form;
        BeanUtils.copyProperties(poForUserForm,currentPo);

        if (currentPo.getLab() != null)
            poForUserForm.setSelLabId(currentPo.getLab().getId());
        
        poForUserForm.clearMaps();

        //copy the lineItems qty, supId, bidInfo and catalogNb 
        for (Iterator iter = currentPo.getPoLineItems().iterator(); iter.hasNext();) {
            PoLineItem lineItem = (PoLineItem) iter.next();
            poForUserForm.setQty(lineItem.getItem().getId().toString(),String.valueOf(lineItem.getQuantity()));
            //assign the supplier to lineItem
            poForUserForm.setSupId(lineItem.getItem().getId().toString(),
                        lineItem.getSupplier().getId().toString());
            
            //assign the bidInfo, catalogNb
            for (Iterator itemS = lineItem.getItem().getItemSupplies().iterator(); itemS.hasNext();){
                ItemSupplied itemSupplied = (ItemSupplied) itemS.next();
                if (itemSupplied.getSupplier().getId().equals(lineItem.getSupplier().getId())){
                    poForUserForm.setBidInfo(lineItem.getItem().getId().toString(), 
                         itemSupplied.getBidInfo());
                    poForUserForm.setCatalogNb(lineItem.getItem().getId().toString(), 
                         itemSupplied.getCatalogNumber());
                }
            }

            /* alternative to get that ..
	            List itemSupplies = getItemManager().getItemSuppliesByItems(new Long[] {lineItem.getItem().getId()},true);
	            for (Iterator itemS = itemSupplies.iterator(); itemS.hasNext();){
	                ItemSupplied itemSupplied = (ItemSupplied) itemS.next();
	                if (itemSupplied.getSupplier().getId().equals(lineItem.getSupplier().getId())){
	                    poForUserForm.setBidInfo(lineItem.getItem().getId().toString(), 
	                         itemSupplied.getBidInfo());
	                    poForUserForm.setCatalogNb(lineItem.getItem().getId().toString(), 
	                         itemSupplied.getCatalogNumber());
	                }
	            }
            */
            
        }
        
        return mapping.findForward("editUserPo");
    
    }
    
    //if not present create new currentPo (stored in session) and assumes itemId in request
    public ActionForward addItem(ActionMapping mapping,ActionForm form,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {
        
        String itemId = request.getParameter("itemId");
        if ((itemId == null) || (itemId.length()<1))
            throw new IllegalAccessException("Error adding empty item from the order!"); 

        PurchaseOrder currentPo = (PurchaseOrder) request.getSession().
											getAttribute("currentPo");
   
        //ensure that Po is stored with a CADConverter
        if (currentPo == null) {
            currentPo = new PurchaseOrder(getCadConverter());
        }
        //Important: requires the itemSupplies to be fetched
        Item newItem = getItemManager().getItem(new Long(itemId),false,true);
        //add by default the first Supplier associated with this item
        currentPo.addItem(newItem);

        request.getSession().setAttribute("currentPo",currentPo);        
        return mapping.findForward("viewUserPoAction");
    }
    
    public ActionForward removeItem(ActionMapping mapping,ActionForm form,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {
        
        String itemId = request.getParameter("itemId");
        if ((itemId == null) || (itemId.length()==0))
            throw new IllegalAccessException("Error removing empty item from the order!"); 

        PurchaseOrder currentPo = (PurchaseOrder) request.getSession().
											getAttribute("currentPo");
        
        if (currentPo == null) {
            throw new IllegalAccessException("Error no PO found for this user!");
        }
        
        currentPo.removeItem(getItemManager().getItem(new Long(itemId),false,false));
        
        return mapping.findForward("viewUserPoAction");
    }

    /*
     * User chooses to add other item before saving PO
     */
    public ActionForward goToListItem(ActionMapping mapping,ActionForm form,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {
        
        PoForUserForm poForUserForm = (PoForUserForm) form;

        //refresh the currentPo
        refreshCurrentPo(request,poForUserForm);
        
        return mapping.findForward("listItemAction");
    }
    
    

}
