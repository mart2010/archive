package ca.canvac.webstore.web.action;

import ca.canvac.webstore.domain.CartItem;
import ca.canvac.webstore.web.form.CartActionForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;

public class UpdateCartQuantitiesAction extends BaseAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        CartActionForm cartForm = (CartActionForm) form;
        for (Iterator cartItems = cartForm.getCart().getAllCartItems(); cartItems
                .hasNext();) {
            CartItem cartItem = (CartItem) cartItems.next();
            long itemId = cartItem.getItem().getItemId();
            try {
                int quantity = Integer.valueOf(
                        request.getParameter(String.valueOf(itemId)))
                        .intValue();
                cartForm.getCart().setQuantityByItemId(itemId, quantity);
                if (quantity < 1)
                    cartItems.remove();
            } catch (NumberFormatException e) {
                //ignore on purpose
            }
        }
        return mapping.findForward("success");
    }

}