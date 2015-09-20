package ca.canvac.webstore.web.action;

import ca.canvac.webstore.web.form.CartActionForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RemoveItemFromCartAction extends BaseAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        CartActionForm cartForm = (CartActionForm) form;
        cartForm.getCart().removeItemById(cartForm.getWorkingItemId());
        return mapping.findForward("success");
    }

}