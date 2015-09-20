package ca.canvac.webstore.web.action;

import servletunit.struts.MockStrutsTestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


    public class ActionBaseTest extends MockStrutsTestCase {

        public ActionBaseTest(String name) {
            super(name);
        }

        protected final static ApplicationContext ctx;

        // This static block ensures that Spring's BeanFactory is only loaded
        // once for all tests
        static {
            String path = "/WEB-INF/dataAccessContext-local.xml";
            ctx = new ClassPathXmlApplicationContext(path);
        }


}
