package ca.canvac.webstore.dao;

import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BaseDaoTest extends TestCase {

    protected final static ApplicationContext ctx;

    // This static block ensures that Spring's BeanFactory is only loaded
    // once for all tests
    static {
        String path = "/WEB-INF/dataAccessContext-local.xml";
        ctx = new ClassPathXmlApplicationContext(path);
    }

    

}
