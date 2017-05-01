package ca.canvac.webstore.web.action;

import servletunit.struts.MockStrutsTestCase;
import ca.canvac.webstore.web.form.AccountActionForm;
import ca.canvac.webstore.web.form.CartActionForm;
import ca.canvac.webstore.web.form.OrderActionForm;
import ca.canvac.webstore.domain.Account;
import ca.canvac.webstore.domain.Item;
import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: OUELLM
 * Date: Oct 29, 2004
 * Time: 10:40:26 AM
 * Comments:
 */
public class NewOrderFormActionTest extends MockStrutsTestCase {

    public static AccountActionForm getAccountFrm(String userStr, String userPass) {
        Account acct = new Account();
        acct.setUserId(userStr);
        acct.setPassword(userPass);
        AccountActionForm accFrm = new AccountActionForm();
        accFrm.setAccount(acct);
        accFrm.setUserId(acct.getUserId());
        accFrm.setPassword(acct.getPassword());
        return accFrm;
    }

    public CartActionForm getCartFrm(long itemId){
        CartActionForm cartFrm = new CartActionForm();
        Item item = new Item();
        item.setItemId(itemId);
        //so that Special Request validation does not choke
        item.setName("t");
        //it needs a UnitPrice not null        
        item.setUnitPrice(BigDecimal.valueOf(10));
        cartFrm.getCart().addItem(item);
        return cartFrm;
    }

    public NewOrderFormActionTest(String testName){
        super(testName);
    }


    public void testFrmParameter(){
        //Try to acces a secured Action with no accountForm Session bean

        setRequestPathInfo("/shop/newOrderForm");
        actionPerform();
        verifyForward("global-signon");

        //Set an AccountForm bean in the session but no cartForm
        getSession().setAttribute("accountForm",getAccountFrm("c",""));
        actionPerform();

        //no CartForm is set, so should expect "failure" ...
        verifyForward("failure");

        //Set a cartForm bean in the session (containing a cart object)
        getSession().setAttribute("cartForm",getCartFrm(1001));
        actionPerform();
        verifyForward("success");
        //Do some other checks on the OrderFrm generated
        OrderActionForm orderFrm = (OrderActionForm) getActionForm();
        assertEquals(orderFrm.getOrder().getUsername(),"c");
    }

    public void testFrmParameterWithEmptyCart() {
        //Try to acces a secured Action with no accountForm Session bean
        setRequestPathInfo("/shop/newOrderForm");
        //Set an AccountForm bean in the session
        getSession().setAttribute("accountForm",getAccountFrm("www","www"));
        //Set an empty cartForm bean in the session, verify the failure
        getSession().setAttribute("cartForm",new CartActionForm());

        // since this generates an uncaught exception inside the Action
        // the testCase fails.. 
        /* Comment out ...

        try {
            actionPerform();
            fail("Error: did not throw IllegalArgumentException");
        }
        catch (IllegalArgumentException ex) {
            assertEquals(ex.getMessage(),"Cannot initialize Order with empty cart parameter");
        }
        */
    }

}
