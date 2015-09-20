package ca.canvac.webstore.web.form;

import ca.canvac.webstore.domain.Cart;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

//this class does not rely on the Validator

public class CartActionForm extends ActionForm {

    /* Private Fields */

    private Cart cart = new Cart();

    private long workingItemId;

    /* JavaBeans Properties */

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public long getWorkingItemId() {
        return workingItemId;
    }

    public void setWorkingItemId(long workingItemId) {
        this.workingItemId = workingItemId;
    }

    /* Public Methods */

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        //this was originally set to null as the itemId was String...
        workingItemId = -1;
    }
}