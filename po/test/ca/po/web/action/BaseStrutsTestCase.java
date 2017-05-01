package ca.po.web.action;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import servletunit.struts.MockStrutsTestCase;


/**
 *  
 *
 */
public class BaseStrutsTestCase extends MockStrutsTestCase {

    protected WebApplicationContext ctx = null;

    public BaseStrutsTestCase(String name) {
        super(name);
    }
   
    
    //~ Methods Intended to be called only Once by the SubClass
    // To avoid long re-initialization of the Web Spring Context
    public void setUp() throws Exception {
        super.setUp();
        // initialize Spring
   
        MockServletContext sc = new MockServletContext("");
        
        sc.addInitParameter(ContextLoader.CONFIG_LOCATION_PARAM,
                "/WEB-INF/dataAccessContext.xml, /WEB-INF/applicationContext.xml");
        
        ServletContextListener contextListener = new ContextLoaderListener();
        ServletContextEvent event = new ServletContextEvent(sc);
        contextListener.contextInitialized(event);
        
        // magic bridge to make StrutsTestCase aware of Spring's Context
        getSession().getServletContext().setAttribute(
                WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, 
                sc.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE));
        
        ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(
    		    	getSession().getServletContext());

       
    }
    
    public void tearDown() throws Exception {
        super.tearDown();
        ctx = null;
    }
}
