package ca.po.model.dao;

import junit.framework.TestCase;
import net.sf.acegisecurity.GrantedAuthority;
import net.sf.acegisecurity.UserDetails;
import net.sf.acegisecurity.providers.dao.UsernameNotFoundException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import ca.po.model.User;
import ca.po.model.type.Role;

/**
 * @author MOUELLET
 *
 * TODO 
 */
public class UserDAOTest extends TestCase {
    private static ClassPathXmlApplicationContext ctx;
    private static UserDAO userDAO;
    static {
        String[] paths = {"/WEB-INF/dataAccessContext.xml",
                			"/WEB-INF/applicationContext.xml", 
                			"/WEB-INF/securityContext.xml"};
        ctx = new ClassPathXmlApplicationContext(paths);
        userDAO = (UserDAO) ctx.getBean("userDAO");
    } 
    
    
    
    
    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public UserDAOTest(String arg0) {
        super(arg0);
    }

    
    public void testLoadUserByUserName() {
        try {
            User user = (User) userDAO.loadUserByUsername("no user");
            fail("should have throw Exceptin");
            
        } catch (UsernameNotFoundException  ex) {
            assertNotNull(ex);
        }
        
        try {
            User user = (User) userDAO.loadUserByUsername("adminSample1");
            User userCopy = userDAO.getUserByUsername("adminSample1",true);
            
            assertEquals(user,userCopy);
            assertNotNull(user.getRoles());
            assertEquals(user.getRoles(),userCopy.getRoles());
            
            UserDetails userD = (UserDetails) userDAO.loadUserByUsername("adminSample1");
            boolean found = false;
            for (int i=0; i<userD.getAuthorities().length;i++){
                GrantedAuthority a = userD.getAuthorities()[i]; 
                if (a.getAuthority().equals(Role.PO_ADMIN.toString())) {
                    found = true;
                    break;
                }
            }
            assertTrue("admin user should have Admin right",found);            
        } catch (Exception ex) {
            fail ("shoult not throw ex");
        }
    }
    
}
