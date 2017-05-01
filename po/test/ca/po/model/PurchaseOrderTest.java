package ca.po.model;

import java.math.BigDecimal;

import junit.framework.TestCase;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import ca.po.model.type.Money;
import ca.po.model.type.PoStatusCode;
import ca.po.model.type.Role;
import ca.po.util.converter.MoneyCADConverter;

/**
 * @author MOUELLET
 *
 * TODO 
 */
public class PurchaseOrderTest extends TestCase {

    private static ClassPathXmlApplicationContext ctx;
    
    static {
        ctx = new ClassPathXmlApplicationContext(new String[] 
                            {"/WEB-INF/dataAccessContext.xml",
                			 "/WEB-INF/applicationContext.xml"});        
    }
    
    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public PurchaseOrderTest(String arg0) {
        super(arg0);
    }

    public void testVariousPO() {
        //supplier check
        MoneyCADConverter converter = (MoneyCADConverter) ctx.getBean("moneyCADConverter");
    
        PurchaseOrder po = new PurchaseOrder(converter);
        Money listCad1 = Money.getCAD(1.00);
        Money listCad2 = Money.getCAD(1.00);
        po.addItem(new Item("test1",null,null,null,null,null,listCad1));
        po.addItem(new Item("test2",null,null,null,null,null,listCad2));
        assertEquals(po.getEstCalculatedCADTotalPersisted(),new BigDecimal("2.00"));

        Money listEUR1 = Money.getEUR(1.00); 
        Money listEUR2 = Money.getEUR(1.00);
        po.addItem(new Item("test3",null,null,null,null,null,listEUR1));
        po.addItem(new Item("test4",null,null,null,null,null,listEUR2));
        assertTrue(po.getEstCalculatedCADTotalPersisted().doubleValue()>4.00d);

        User user = new User("tester",null,null,Role.PO_USER,null,null,null);
        try {
            po.cancel(user);
            fail("should throw exception");
        }
        catch (IllegalAccessError ex) {
            assertNotNull(ex);
        }
        po.initialize(user,new Lab("lab",null));
        assertEquals(po.getCurrentStatusChange(),PoStatusCode.PROPOSED);
        try {
            po.cancel(user);
            fail("should throw exception");
        }
        catch (IllegalAccessError ex) {
            assertNotNull(ex);
        }
        user.addRole(Role.PO_ADMIN);
        po.cancel(user);
        assertEquals(po.getCurrentStatusChange(),PoStatusCode.CANCELLED);
    }
    
    
    
    
}
