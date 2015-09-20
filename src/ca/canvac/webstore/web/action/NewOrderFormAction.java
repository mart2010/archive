package ca.canvac.webstore.web.action;

import ca.canvac.webstore.domain.Account;
import ca.canvac.webstore.web.form.AccountActionForm;
import ca.canvac.webstore.web.form.CartActionForm;
import ca.canvac.webstore.web.form.OrderActionForm;
import org.apache.struts.action.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//this class extends the SecureBaseAction which validates (in execute method)
//that the user has logged in, and force him to do it otherwise

public class NewOrderFormAction extends SecureBaseAction {

    protected ActionForward doExecute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        AccountActionForm acctForm = (AccountActionForm) request.getSession()
                .getAttribute("accountForm");
        CartActionForm cartForm = (CartActionForm) request.getSession()
                .getAttribute("cartForm");
        if (cartForm != null) {
            //The Controller has created an instance of OrderActionForm
            //and pass it in the ActionForm parameter
            OrderActionForm orderForm = (OrderActionForm) form;
            Account account = acctForm.getAccount();
            //intialize the empty OrderActionForm to the corresponding cart
            orderForm.getOrder().initOrder(account, cartForm.getCart());
            return mapping.findForward("success");
        } else {
            ActionMessages messages = getMessages(request);
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    "order.notProcess"));
            saveMessages(request, messages);
            return mapping.findForward("failure");
        }
    }

}