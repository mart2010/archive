package ca.canvac.webstore.web.action;

import ca.canvac.webstore.web.form.AccountActionForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class SecureBaseAction extends BaseSpringAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        AccountActionForm acctForm = (AccountActionForm) request.getSession()
                .getAttribute("accountForm");
        if (acctForm == null || acctForm.getAccount() == null) {
            String url = request.getServletPath();
            String query = request.getQueryString();
            if (query != null) {
                request.setAttribute("signonForwardAction", url + "?" + query);
            } else {
                request.setAttribute("signonForwardAction", url);
            }
            return mapping.findForward("global-signon");
        } else {
            return doExecute(mapping, form, request, response);
        }
    }

    //action implementing this security service must implement this method
    // instead
    protected abstract ActionForward doExecute(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception;

}