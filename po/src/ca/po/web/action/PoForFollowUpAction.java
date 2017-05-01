package ca.po.web.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.springframework.orm.hibernate.HibernateOptimisticLockingFailureException;

import ca.po.model.PurchaseOrder;
import ca.po.model.type.PaymentType;
import ca.po.model.type.PoStatusCode;
import ca.po.util.DateUtil;
import ca.po.web.form.PoForValidationFollowUpForm;

/**
 * 
 * @struts.action path="/admin/listPoForFollowUp" name="poForValidationFollowUpForm" scope="request"
 * 		validate="false" parameter="list"
 * @struts.action path="/admin/saveFollowUpPo" name="poForValidationFollowUpForm" scope="request"
 * 		validate="true" input="/WEB-INF/jsp/listPoForFollowUp.jsp" parameter="save"
 * 
 * @struts.action-forward name="list" path="/WEB-INF/jsp/listPoForFollowUp.jsp"
 * @struts.action-forward name="listAction" path="/admin/listPoForFollowUp.html"
 *    
 */

public class PoForFollowUpAction extends BaseAction {
    
    /*
     * This is called from different way:
     * 1- Admin wants to start a new FollowUp session
     * 2- Admin saved the follow-up and return here to see a refresh view
     * 
     */
    public ActionForward list(ActionMapping mapping,ActionForm form,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {

        ActionMessages messages = getMessages(request);
        
        //check to see if a current followUp session is available
        Map validatedMapPos = (Map) request.getSession().getAttribute("validatedMapPos");
        Map orderedMapPos = (Map) request.getSession().getAttribute("orderedMapPos");
        
        if (validatedMapPos == null) {
            assert orderedMapPos == null;
            List validatedPos = getPoManager().getPOsByStatusCode(PoStatusCode.VALIDATED,true);
            List orderedPos = getPoManager().getPOsByStatusCode(PoStatusCode.ORDERED,true);
            
            validatedMapPos = new HashMap();
            orderedMapPos = new HashMap();
            PurchaseOrder po;
            for (Iterator iter = validatedPos.iterator(); iter.hasNext();){
                po = (PurchaseOrder) iter.next();
                validatedMapPos.put(po.getPoId().toString(),po);
            }
            for (Iterator iter = orderedPos.iterator(); iter.hasNext();){
                po = (PurchaseOrder) iter.next();
                orderedMapPos.put(po.getPoId().toString(),po);
            }
            
            
            if (validatedMapPos.isEmpty() && orderedMapPos.isEmpty()) {
                messages.add(ActionMessages.GLOBAL_MESSAGE, 
                    	new ActionMessage("pos.followUpEmpty"));
                saveMessages(request, messages);
                cleanSession(mapping,request);
                return mapping.findForward("blank");
                
            } else
            	request.getSession().setAttribute("validatedMapPos",validatedMapPos);
        		request.getSession().setAttribute("orderedMapPos",orderedMapPos);            
        }
        
        //build look-up, if not yet available
        if (request.getSession().getAttribute("paymentTypes") == null) 
            request.getSession().setAttribute("paymentTypes",PaymentType.PAYMENT_TYPES.values()) ;
        if (request.getSession().getAttribute("statusForValidated") == null)
            request.getSession().setAttribute("statusForValidated",PoStatusCode.getLIST_FOR_VALIDATED()) ;
        if (request.getSession().getAttribute("statusForOrdered") == null)
            request.getSession().setAttribute("statusForOrdered",PoStatusCode.getLIST_FOR_ORDERED()) ;

        //populate the form
        PoForValidationFollowUpForm posForm = (PoForValidationFollowUpForm) form;
        posForm.setSelPoIds(new String[]{});
        List posColl = new ArrayList(validatedMapPos.values());
        posColl.addAll(orderedMapPos.values());
        
        for (Iterator iter = posColl.iterator(); iter.hasNext();){
            PurchaseOrder po = (PurchaseOrder) iter.next();
            posForm.setConfirmNb(po.getPoId().toString(),po.getConfirmNumber());
            if (po.getPaymentType()!=null) 
                posForm.setPayType(po.getPoId().toString(),po.getPaymentType().toString());
            posForm.setStatusCode(po.getPoId().toString(),po.getCurrentStatusChange().toString());
            if (po.getRevisedEffectiveCADTotal()!=null) 
                posForm.setRevisedCADTotal(po.getPoId().toString(),po.getRevisedEffectiveCADTotal().toString());
            if (po.getRevisedEffectiveCADTax()!=null) 
                posForm.setRevisedCADTax(po.getPoId().toString(),po.getRevisedEffectiveCADTax().toString());
            if (po.getEstimateReceptionDate()!=null) 
                posForm.setEstimateReceptionDate(po.getPoId().toString(),
                        DateUtil.convertDateToDefaultString(po.getEstimateReceptionDate()));

        }
        
        
        //return a forward to list
        return mapping.findForward("list");

    }

    
    private void cleanSession(ActionMapping mapping, HttpServletRequest request){
        request.getSession().removeAttribute("validatedMapPos");
        request.getSession().removeAttribute("orderedMapPos");
        request.getSession().removeAttribute("paymentTypes");
        request.getSession().removeAttribute("statusForValidated");
        request.getSession().removeAttribute("statusForOrdered");
        
        
    }

    /* this is called when:
     * 1- Admin presses the Save button after having selected PO
     * 2- Admin presses the Cancel button to abandon the follow-up
     */
    public ActionForward save(ActionMapping mapping,ActionForm form,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {

        ActionMessages messages = getMessages(request);
        //if Cancel, clean-up session and forward to /home
        if (isCancelled(request)) {
            cleanSession(mapping,request);
            messages.add(ActionMessages.GLOBAL_MESSAGE, 
                	new ActionMessage("pos.followUpCancel"));
            saveMessages(request, messages);
            return mapping.findForward("home");
        }
        
        PoForValidationFollowUpForm posForm = (PoForValidationFollowUpForm) form;

        if (posForm.getSelPoIds().length == 0) {
            messages.add(ActionMessages.GLOBAL_MESSAGE, 
                	new ActionMessage("pos.followUpListEmpty"));
            saveMessages(request, messages);
            return mapping.findForward("list");
        }

        Map validatedMapPos = (Map) request.getSession().getAttribute("validatedMapPos");
        Map orderedMapPos = (Map) request.getSession().getAttribute("orderedMapPos");
        PurchaseOrder po;

        for (int i=0; i<posForm.getSelPoIds().length; i++){
            String poId = posForm.getSelPoIds()[i];
            po = (PurchaseOrder) validatedMapPos.get(poId);
            if (po==null) {
                po = (PurchaseOrder) orderedMapPos.get(poId);
            }
            assert po != null;
            //update PO attributes            
            po.setPaymentType(PaymentType.GET_PAYMENT_TYPE(posForm.getPayType(poId)));
            po.setConfirmNumber(posForm.getConfirmNb(poId));
            if (posForm.getRevisedCADTotal(poId).length()==0)
                po.setRevisedEffectiveCADTotal(null);
            else po.setRevisedEffectiveCADTotal(new BigDecimal(posForm.getRevisedCADTotal(poId)));
            if (posForm.getRevisedCADTax(poId).length()==0)
                po.setRevisedEffectiveCADTax(null);
            else po.setRevisedEffectiveCADTax(new BigDecimal(posForm.getRevisedCADTax(poId))); 
            PoStatusCode newStatus = PoStatusCode.getInstance(posForm.getStatusCode(poId));
            
            if (posForm.getEstimateReceptionDate(poId).length()>1){
                po.setEstimateReceptionDate(DateUtil.convertDefaultStringToDate(posForm.getEstimateReceptionDate(poId)));
            }
                
            
            //save the followUp
            try {
                getPoManager().saveFollowUpPO(po,newStatus,getUser());
                    messages.add(ActionMessages.GLOBAL_MESSAGE, 
                            new ActionMessage("pos.followUpOk",po.getPoId()));
                	saveMessages(request, messages);
            }
            catch (HibernateOptimisticLockingFailureException ex){
	                messages.add(ActionMessages.GLOBAL_MESSAGE, 
	                    	new ActionMessage("po.optimisticLockError"));
	            	saveMessages(request, messages);
            }
        }
        
        cleanSession(mapping,request);
        return mapping.findForward("listAction");
        
    }

   
    
}
