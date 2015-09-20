package ca.canvac.webstore.web.action;

import servletunit.struts.MockStrutsTestCase;
import ca.canvac.webstore.web.form.CartActionForm;
import ca.canvac.webstore.domain.Cart;
import ca.canvac.webstore.domain.CartItem;

public class AddItemToCartActionTest extends MockStrutsTestCase {

    public AddItemToCartActionTest(String testName){
        super(testName);
    }

    private Cart getCartFromCartForm(){
        //check cart form state
        CartActionForm cartForm = (CartActionForm) getSession().getAttribute("cartForm");
        return cartForm.getCart();
    }

    public void testNewItemParam () {

        //non-existing item request
        setRequestPathInfo("/shop/addItemToCart");
        addRequestParameter("workingItemId","-111");
        actionPerform();
        assertTrue(getCartFromCartForm().getNumberOfItems()==0);
        verifyForward("success");

        //check with a good item
        clearRequestParameters();
        setRequestPathInfo("/shop/addItemToCart");
        addRequestParameter("workingItemId","1001");
        actionPerform();
        assertTrue(getCartFromCartForm().containsItemId(1001));
        CartItem cartItem = (CartItem) getCartFromCartForm().getAllCartItems().next();
        assertEquals(cartItem.getItem().getUnitPrice(),getCartFromCartForm().getSubTotal());
        verifyForward("success");
        //add the same item again!
        setRequestPathInfo("/shop/addItemToCart");
        addRequestParameter("workingItemId","1001");
        actionPerform();
        cartItem = (CartItem) getCartFromCartForm().getAllCartItems().next();
        assertEquals(cartItem.getQuantity(),2);

    }



}
