package ca.canvac.webstore.web.action;

import ca.canvac.webstore.domain.Order;
import ca.canvac.webstore.web.form.OrderActionForm;
import org.apache.struts.action.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NewOrderAction extends SecureBaseAction {

    protected ActionForward doExecute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        OrderActionForm orderForm = (OrderActionForm) form;
        ActionMessages messages = getMessages(request);
        if (orderForm.getOrder() != null) {
            Order order = orderForm.getOrder();
            webStore.insertOrder(order);
            request.getSession().removeAttribute("workingOrderForm");
            request.getSession().removeAttribute("cartForm");
            request.setAttribute("order", order);
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    "order.submitted"));
            saveMessages(request, messages);
            return mapping.findForward("success");
        } else {
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    "order.notSubmitted"));
            saveMessages(request, messages);
            return mapping.findForward("failure");
        }
    }
}