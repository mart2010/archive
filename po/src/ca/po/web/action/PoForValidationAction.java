package ca.po.web.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.springframework.orm.hibernate.HibernateOptimisticLockingFailureException;

import ca.po.model.ItemSupplied;
import ca.po.model.PoLineItem;
import ca.po.model.PurchaseOrder;
import ca.po.model.type.Money;
import ca.po.model.type.PoStatusCode;
import ca.po.web.form.PoForValidationFollowUpForm;

/**
 * 
 * @struts.action path="/admin/listPoForValidation" name="poForValidationFollowUpForm" scope="request"
 * 		validate="false" parameter="list"
 * @struts.action path="/admin/validatePo" name="poForValidationFollowUpForm" scope="request"
 * 		validate="false" parameter="validate"
 * @struts.action path="/admin/goToEditProposedPO" parameter="goToEditProposedPo"
 * 
 * 
 * @struts.action-forward name="list" path="/WEB-INF/jsp/listPoForValidation.jsp"
 * @struts.action-forward name="listAction" path="/admin/listPoForValidation.html"
 * @struts.action-forward name="editUserPoAction" path="/editUserPo.html"
 *    
 */

public class PoForValidationAction extends BaseAction {
    
    /*
     * This is called from different way:
     * 1- Admin wants to start a new validation session
     * 2- Admin has edited some PO and got reforwarded for continuing validation 
     * 3- Admin has pressed button Refresh and got reforwarded here
     */
    public ActionForward list(ActionMapping mapping,ActionForm form,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {

        //check to see if a current validation session is available
        Map proposedMapPOs = (Map) request.getSession().getAttribute("proposedMapPos");
        if (proposedMapPOs == null) {
            refreshProposedMapPOs(request);
            proposedMapPOs = (Map) request.getSession().getAttribute("proposedMapPos");
        }
        ActionMessages messages = getMessages(request);
        if (proposedMapPOs.isEmpty()) {
            messages.add(ActionMessages.GLOBAL_MESSAGE, 
                	new ActionMessage("pos.proposedEmpty"));
            saveMessages(request, messages);
            cleanSession(mapping,request);
            return mapping.findForward("blank"); 
         }
               
        
        PoForValidationFollowUpForm poForValidationForm = (PoForValidationFollowUpForm) form;
        
        List selPosIdsList = (List) request.getSession().getAttribute("selPosIdsList");
        Money estTotalAmount = Money.getCAD(0d);
        
        if ((selPosIdsList == null) || (selPosIdsList.size()==0)){
            //refresh the estTotAmount
            request.setAttribute("estTotalAmount",estTotalAmount.toString());
            //return a forward to list
            return mapping.findForward("list");
        }
        
        poForValidationForm.setSelPoIds((String[])selPosIdsList.toArray());
        for (int i=0; i < poForValidationForm.getSelPoIds().length ; i++){
             estTotalAmount = estTotalAmount.add(((PurchaseOrder) proposedMapPOs.get(
                     poForValidationForm.getSelPoIds()[i])).getEstCalculatedCADTotal());
        }

        //refresh the estTotAmount
        request.setAttribute("estTotalAmount",estTotalAmount.toString());
        //return a forward to list
        return mapping.findForward("list");
    }

    
    private void cleanSession(ActionMapping mapping, HttpServletRequest request){
        request.getSession().removeAttribute("proposedMapPos");
        request.getSession().removeAttribute("selPosIdsList");
        removeFormBean(mapping, request);
    }

    /* this is called when:
     * 1- Admin presses the Validate button after having selected PO
     * 2- Admin presses the Refresh button after having selected PO, and will be forwarded to the listAction
     */
    public ActionForward validate(ActionMapping mapping,ActionForm form,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {

        ActionMessages messages = getMessages(request);
        //if Cancel, clean-up session and forward to /home
        if (isCancelled(request)) {
            //clean session
            cleanSession(mapping,request);
            messages.add(ActionMessages.GLOBAL_MESSAGE, 
                	new ActionMessage("pos.validationCancel"));
            saveMessages(request, messages);
            return mapping.findForward("home");
        }

        PoForValidationFollowUpForm poForValidationForm = (PoForValidationFollowUpForm) form;
        List selPosIdsList =  Arrays.asList(poForValidationForm.getSelPoIds());
        request.getSession().setAttribute("selPosIdsList",selPosIdsList);
        
        if (selPosIdsList.size()==0) {
            messages.add(ActionMessages.GLOBAL_MESSAGE, 
                	new ActionMessage("pos.validationListEmpty"));
            saveMessages(request, messages);
            return mapping.findForward("list");
        }
        
        //if Refresh, reforward to list
        if (request.getParameter("refresh").length()!=0) {
            return mapping.findForward("listAction");
        }

        
        //save with some po selected
        Map proposedMapPOs = (Map) request.getSession().getAttribute("proposedMapPos");
        assert proposedMapPOs != null; 

        //create the list of selected ones
        List selPOs = new LinkedList();
        for (Iterator iter = selPosIdsList.iterator(); iter.hasNext();){
            selPOs.add(proposedMapPOs.get(iter.next()));
        }
        
        //validate the list of POs (one transaction)
        try {
            getPoManager().saveValidatedPOs(selPOs,getUser());
            messages.add(ActionMessages.GLOBAL_MESSAGE, 
                	new ActionMessage("pos.validationOk"));
            saveMessages(request, messages);
        } 
        catch (HibernateOptimisticLockingFailureException ex){
            messages.add(ActionMessages.GLOBAL_MESSAGE, 
                	new ActionMessage("po.optimisticLockError"));
        	saveMessages(request, messages);
        }
        
        //clean session
        cleanSession(mapping,request);
        return mapping.findForward("home");        

    }


    private void refreshProposedMapPOs(HttpServletRequest request){
        Map proposedMapPOs = new HashMap();
        for (Iterator pos = getPoManager().getPOsByStatusCode(PoStatusCode.PROPOSED,true).iterator(); pos.hasNext();) {
            PurchaseOrder po = (PurchaseOrder) pos.next();
            //ensure that a valid CADConverter is set
            po.setMoneyCADConverter(getCadConverter());
            proposedMapPOs.put(po.getPoId().toString(),po);
        }
        request.getSession().setAttribute("proposedMapPos",proposedMapPOs);
    }
    
   
    /*
     * Admin chooses to edit a PO before continuing validation (assumes 'poId' request param)
     */
    public ActionForward goToEditProposedPo(ActionMapping mapping,ActionForm form,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {
        
        assert ((request.getParameter("poId")!= null) && 
                (request.getParameter("poId").length()>0)) : "Action requires a valid 'poId' request param";

        //force a refresh on the PO Map in session
        request.getSession().removeAttribute("proposedMapPos");
        putNextActionForward(mapping,request,"goToListProposedPOsAction");
        return mapping.findForward("editUserPoAction");
    }
    
    
    
}
