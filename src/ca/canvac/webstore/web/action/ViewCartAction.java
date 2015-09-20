package ca.canvac.webstore.web.action;

import ca.canvac.webstore.web.form.CartActionForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ViewCartAction extends BaseAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        CartActionForm cartForm = (CartActionForm) form;
        // AccountActionForm acctForm = (AccountActionForm)
        // request.getSession().getAttribute("accountForm");
        String page = request.getParameter("page");

        if ("nextCart".equals(page)) {
            cartForm.getCart().getCartItemList().nextPage();
        } else if ("previousCart".equals(page)) {
            cartForm.getCart().getCartItemList().previousPage();
        }
        return mapping.findForward("success");
    }

}