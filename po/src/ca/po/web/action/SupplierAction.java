package ca.po.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.springframework.dao.DataIntegrityViolationException;

import ca.po.model.Supplier;
import ca.po.web.form.SupplierForm;

/**
 * 
 * @struts.action path="/listSupplier" parameter="list"
 * 
 * @struts.action path="/addSupplier" name="supplierForm" scope="request"
 *  	validate="true" input="/listSupplier.html" parameter="add"
 *   
 * @struts.action path="/deleteSupplier" parameter="delete"
 * 
 * @struts.action-forward name="list" path="/WEB-INF/jsp/listAddSupplier.jsp"
 * @struts.action-forward name="listAction" path="/listSupplier.html"
 *    
 */

public class SupplierAction extends BaseAction {

    
    public ActionForward list(ActionMapping mapping,ActionForm form,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {
        
        //store supplierList in request scope
        request.setAttribute("suppliers",getSupplierManager().getSuppliers() );
        
        //return a forward to list
        return mapping.findForward("list");
    }
   
    public ActionForward add(ActionMapping mapping,ActionForm form,
            		HttpServletRequest request,HttpServletResponse response) throws Exception {
        
        ActionForward next = getNextActionForward(mapping,request); 
        
        //if Cancel, clean-up and forward to "from" or "listAction"
        if (isCancelled(request)) {
            if (next!=null) return next;
            else return mapping.findForward("home");
        }
        
        SupplierForm supplierForm = (SupplierForm) form;
        Supplier supplier = new Supplier();
        BeanUtils.copyProperties(supplier,supplierForm);
        
        ActionMessages messages = getMessages(request);
        //try save supplier
        try {
            getSupplierManager().saveSupplier(supplier,getUser().getName());
            messages.add(ActionMessages.GLOBAL_MESSAGE, 
                    	new ActionMessage("sup.addSuccess"));
            saveMessages(request, messages);

        } 
        catch (DataIntegrityViolationException ex) {
            messages.add(ActionMessages.GLOBAL_MESSAGE, 
                	new ActionMessage("sup.integrityError"));
        	saveMessages(request, messages);
        }
        
        removeFormBean(mapping,request);
        if (next!=null) return next;
        else return mapping.findForward("listAction");
    }

    public ActionForward delete(ActionMapping mapping,ActionForm form,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {
        
        ActionMessages messages = getMessages(request);
        String supId = request.getParameter("supId");
        assert supId!=null;
        try {
            int nb = getSupplierManager().deleteSupplier(new Long(supId));
            if (nb==1) {
                messages.add(ActionMessages.GLOBAL_MESSAGE, 
                    	new ActionMessage("sup.deleteSuccess"));
            } else if (nb==0){
                messages.add(ActionMessages.GLOBAL_MESSAGE, 
                    	new ActionMessage("sup.alreadyDeleted"));
            } else assert false;
            saveMessages(request, messages);
        } 
        catch (IllegalArgumentException ex) {
            //Supplier not empty
            messages.add(ActionMessages.GLOBAL_MESSAGE, 
                    	new ActionMessage("sup.NotEmptyDeleteError"));
            saveMessages(request, messages);
        }
       	
        return mapping.findForward("listAction");
    
    }

}
