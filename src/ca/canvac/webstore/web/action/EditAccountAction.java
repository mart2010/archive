package ca.canvac.webstore.web.action;

import ca.canvac.webstore.domain.Account;
import ca.canvac.webstore.web.form.AccountActionForm;
import org.apache.struts.action.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EditAccountAction extends SecureBaseAction {

    protected ActionForward doExecute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        AccountActionForm acctForm = (AccountActionForm) form;
        ActionMessages messages = getMessages(request);

        if (acctForm != null) {
            Account account = acctForm.getAccount();
            webStore.updateAccount(account);
            acctForm.setAccount(webStore.getAccount(account.getUserId()));
            //for security, remove password in form session bean and remove
            // workingAccountForm
            acctForm.getAccount().setPassword(null);
            request.getSession().setAttribute("accountForm", acctForm);
            request.getSession().removeAttribute("workingAccountForm");
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    "account.updated", account.getUserId()));
            saveMessages(request, messages);
            return mapping.findForward("success");
        } else {
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    "account.notUpdated"));
            saveMessages(request, messages);
            return mapping.findForward("failure");
        }
    }

}