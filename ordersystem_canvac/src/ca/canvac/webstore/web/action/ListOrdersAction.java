package ca.canvac.webstore.web.action;

import ca.canvac.webstore.web.form.AccountActionForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ListOrdersAction extends SecureBaseAction {

    protected ActionForward doExecute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        AccountActionForm acctForm = (AccountActionForm) form;
        String username = acctForm.getAccount().getUserId();
        request.setAttribute("orderList", webStore.getOrdersByUserId(username));
        return mapping.findForward("success");
    }

}