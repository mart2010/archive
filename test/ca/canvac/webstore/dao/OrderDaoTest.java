package ca.canvac.webstore.dao;

import ca.canvac.webstore.domain.Order;
import ca.canvac.webstore.dao.ibatis.SqlMapOrderDao;
import org.springframework.dao.DataAccessException;
import java.util.Date;
import java.util.ArrayList;

public class OrderDaoTest extends BaseDaoTest {

    OrderDao orderDao = null;

    protected void setUp() throws Exception {
        super.setUp();
        orderDao = (OrderDao) ctx.getBean("orderDao");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        orderDao = null;
    }


    public void testOrder(){

        //test with a dummy Order
        Order order = new Order();
        order.setUsername("con000001");
        order.setOrderDate(new Date());
        order.setShipAddress1("Add1test");
        order.setShipCity("City1test");
        order.setShipCountry("Countrytest");
        order.setShipPostalZip("Postaltest");
        order.setShipProvState("Provtest");
        order.setShipToFirstName("FNametest");
        order.setShipToLastName("LNametest");
        //empty Lineitems, ok this one would be caught in the Business object Order
        order.setLineItems(new ArrayList());

        order.setFedex(null);
        try {
            orderDao.insertOrder(order);
            fail("missing Fedex, should generate DataAccessException");
        }
        catch (DataAccessException ex) {
            // System.out.println(ex.getMessage());
            assertTrue(true);
        }
        catch (Exception ex) {
            fail("Unexpected Exception:" + ex);
        }

        //correcting Order.. should accept this Order
        order.setFedex("test");
        System.out.println(order);
        try {
            orderDao.insertOrder(order);
            assertTrue(order.getOrderId() > 0);
            assertEquals(orderDao.getOrder(order.getOrderId()).getFedex(),"test");
        }
        catch (DataAccessException ex) {
            fail("Error should not generate DataAccessException");
        }
        //remove new order just added
        assertEquals(1,((SqlMapOrderDao) orderDao).removeOrder(order.getOrderId()));
    }

}
