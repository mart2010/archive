package ca.po.model.service;


import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Martin
 * This needs to be changed, since it removes all content in DB
 * can be dangerous in already deployed environment 
 * 
 */
public abstract class PoBaseServiceFacadeTest extends TestCase {

    protected final static ApplicationContext ctx;

    // This static block ensures that Spring's BeanFactory is only loaded
    // once for all tests

    static {
        String[] paths = {"/WEB-INF/dataAccessContext.xml",
                			"/WEB-INF/applicationContext.xml"};
        ctx = new ClassPathXmlApplicationContext(paths);
    } 
  
    protected static ItemManager itemManager;
    protected static UserManager userManager;
    protected static SupplierManager supplierManager;
    protected static PoManager poManager;
    
    static {
        itemManager = (ItemManager) ctx.getBean("itemManager");
        userManager = (UserManager) ctx.getBean("userManager");
        supplierManager = (SupplierManager) ctx.getBean("supplierManager");
        poManager = (PoManager) ctx.getBean("poManager");
    }
    
    public PoBaseServiceFacadeTest(String arg0) {
        super(arg0);
    }

    
    
}
