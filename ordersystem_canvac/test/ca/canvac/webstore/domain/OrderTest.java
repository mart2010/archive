package ca.canvac.webstore.domain;

import junit.framework.TestCase;
import java.math.BigDecimal;


public class OrderTest extends TestCase {

    public OrderTest(String name) {
        super(name);
    }
    
    public void testInitOrder() {
        Order order = new Order();
        Account account = new Account();
        account.setUserId("dummy");
        Cart cart=null;
        //null cart
        try {
            order.initOrder(account,cart);
            fail("Error: did not throw IllegalArgumentException");
        }
        catch (IllegalArgumentException ex) {
            assertEquals(ex.getMessage(),"Cannot initialize Order with null cart parameter");
        }
        cart = new Cart();
        //empty cart
        try {
            order.initOrder(account,cart);
            fail("Error: did not throw IllegalArgumentException");
        }
        catch (IllegalArgumentException ex) {
            assertEquals(ex.getMessage(),"Cannot initialize Order with empty cart parameter");
        }
        Item item = new Item();
        item.setName("dummy");
        item.setUnitPrice(new BigDecimal("1.00") );
        cart.addItem(item);
        assertEquals(cart.getNumberOfItems(),1);

        try {
            order.initOrder(account,cart);
            assertTrue(true);
        }
        catch (Exception ex) {
            fail("should not throw Excpetion: " + ex);
        }

    }




}
