package ca.canvac.webstore.web.action;

import ca.canvac.webstore.domain.Order;
import ca.canvac.webstore.web.form.AccountActionForm;
import org.apache.struts.action.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ViewOrderAction extends SecureBaseAction {

    protected ActionForward doExecute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        AccountActionForm acctForm = (AccountActionForm) form;
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        Order order = webStore.getOrder(orderId);
        ActionMessages messages = getMessages(request);
        if (acctForm.getAccount().getUserId().equals(order.getUsername())) {
            request.setAttribute("order", order);
            return mapping.findForward("success");
        } else {
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    "order.notOwner"));
            saveMessages(request, messages);
            return mapping.findForward("failure");
        }
    }

}