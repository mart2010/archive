package ca.canvac.webstore.web.action;

import ca.canvac.webstore.domain.Account;
import ca.canvac.webstore.web.form.AccountActionForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EditAccountFormAction extends SecureBaseAction {

    protected ActionForward doExecute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        AccountActionForm workingAcctForm = (AccountActionForm) form;
        AccountActionForm acctForm = (AccountActionForm) request.getSession()
                .getAttribute("accountForm");
        String username = acctForm.getAccount().getUserId();
        if (workingAcctForm.getAccount() == null) {
            Account account = webStore.getAccount(username);
            workingAcctForm.setAccount(account);
        }
        return mapping.findForward("success");
    }

}