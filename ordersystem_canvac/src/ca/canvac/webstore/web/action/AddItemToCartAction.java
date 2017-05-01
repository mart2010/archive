package ca.canvac.webstore.web.action;

import ca.canvac.webstore.domain.Cart;
import ca.canvac.webstore.domain.Item;
import ca.canvac.webstore.web.form.CartActionForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddItemToCartAction extends BaseSpringAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        CartActionForm cartForm = (CartActionForm) form;
        Cart cart = cartForm.getCart();

        //workingItemId was set by controller from the request parameter set
        // inside ViewItem.jsp
        long workingItemId = cartForm.getWorkingItemId();
        if (cart.containsItemId(workingItemId)) {
            cart.incrementQuantityByItemId(workingItemId);
        } else {
            Item item = webStore.getItem(workingItemId);
            //just ignore the very unlikely non-existing itemId
            if (item != null) {
                cartForm.getCart().addItem(item);
            }
        }
        return mapping.findForward("success");
    }

}