package ca.canvac.webstore.web.action;

import ca.canvac.webstore.domain.Account;
import ca.canvac.webstore.web.form.AccountActionForm;
import org.apache.struts.action.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SignonAction extends BaseSpringAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        //removing the form session bean for security
        request.getSession().removeAttribute("workingAccountForm");
        request.getSession().removeAttribute("accountForm");
        //call when signing-off
        if (request.getParameter("signoff") != null) {
            request.getSession().invalidate();
            return mapping.findForward("success");
        }
        //signing-in
        else {
            AccountActionForm acctForm = (AccountActionForm) form;
            String username = acctForm.getUserId();
            String password = acctForm.getPassword();
            Account account = webStore.getAccount(username, password);
            ActionMessages messages = getMessages(request);
            if (account == null) {
                messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                        "signon.noAccount"));
                saveMessages(request, messages);
                return mapping.findForward("failureSignOn");
            }
            //Sign-in successful
            else {
                String forwardAction = acctForm.getForwardAction();
                acctForm = new AccountActionForm();
                acctForm.setForwardAction(forwardAction);
                acctForm.setAccount(account);
                //for security issue, remove the password from the form
                // sesssion bean
                acctForm.getAccount().setPassword(null);
                request.getSession().setAttribute("accountForm", acctForm);
                messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                        "signon.okAccount"));
                //save in the session to preserve on the potential Redirecting
                saveMessages(request.getSession(), messages);

                if (acctForm.getForwardAction() == null
                        || acctForm.getForwardAction().length() < 1) {
                    return mapping.findForward("success");
                } else {
                    response.sendRedirect(acctForm.getForwardAction());
                    return null;
                }
            }
        }
    }

}