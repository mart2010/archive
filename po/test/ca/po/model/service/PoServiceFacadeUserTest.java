package ca.po.model.service;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.springframework.dao.DataIntegrityViolationException;

import ca.po.model.Lab;
import ca.po.model.User;
import ca.po.model.type.Role;

/**
 * @author MOUELLET
 *
 */
public class PoServiceFacadeUserTest extends PoBaseServiceFacadeTest {
    
    private User one;
    private User two;

    protected void setUp() throws Exception {
        super.setUp();
        userManager = (UserManager) ctx.getBean("userManager");
        
        //create two user 
        one = new User("one", "fone","lone",Role.PO_USER,new Lab("labOne",null),"cd@com.com","pass" );
        two = new User("two", "ftwo","ltwo",Role.PO_USER,new Lab("labTwo",null),"cd@com.com","pass" );
        userManager.saveUser(one,"me");
        userManager.saveUser(two,"me");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        userManager.deleteUser(one);
        userManager.deleteUser(two);
        userManager.deleteLab(userManager.getLabByName("labOne"));
        userManager.deleteLab(userManager.getLabByName("labTwo"));
        
        //it is important to reset the ServiceFacade bean, 
        //so that thread bound Session is closed and destroyed...to check this??
        userManager = null;  
    }
    
    public PoServiceFacadeUserTest(String arg0) {
        super(arg0);
    }

    public void testSimple(){
        // get the user one
        User oneUser = (User) userManager.getUserByUsername(one.getName());
        assertTrue(!oneUser.isAdmin());
        assertEquals(oneUser.getRoles().size(),1);
               
        //I need to use user one, so that "delete" in TearDown has the same "version"
        one.addRole(Role.PO_ADMIN);
        userManager.saveUser(one,"me");
        User temp = (User) userManager.getUserByUsername(one.getName());
        assertTrue(temp.isAdmin());
        assertEquals(temp.getRoles().size(),2);
        GregorianCalendar toDayPersisted = new GregorianCalendar();
        toDayPersisted.setTime(temp.getCreatedDate());
        GregorianCalendar toDay = new GregorianCalendar();
        assertEquals(toDay.get(Calendar.DAY_OF_YEAR),toDayPersisted.get(Calendar.DAY_OF_YEAR));
        
    }
 
      
    public void testEagerFetch(){

        //kill the session
        userManager = null;
        userManager = (UserManager) ctx.getBean("userManager");
        
        // get user one with no fect and user two with fetch
        User oneUser = (User) userManager.getUserByUsername(one.getName());
        User twoUser = (User) userManager.getUserByUsername(two.getName());        
        //this should close Hibernate session
        userManager= null;
        //twoUser Roles & Labs eagerly fetched 
        assertEquals(twoUser.getRoles().size(),1);
        assertEquals(twoUser.getLabs().size(),1);
        //oneUser not eagerly fecthed.. probably due to caching this still works!
        assertEquals(oneUser.getLabs().size(),1);

        userManager = (UserManager) ctx.getBean("userManager");

    }    

    
    public void testAddUserWithSameLabRole(){
        Lab lab = userManager.getLabByName("labOne");
        User threeUser = new User("three","fthree","lthree",null,null,"cd@com.com","pass");
                
        threeUser.addLab(lab);
        threeUser.addRole(Role.PO_USER);
        userManager.saveUser(threeUser,"me");
                
        assertEquals(one.getLabs(),threeUser.getLabs());
        assertEquals(one.getRoles(),threeUser.getRoles());

        //try inserting the same username
        User four = new User("one", "ftwo","ltwo",Role.PO_USER,new Lab("labOne",null),"cd@com.com","pass" ); 
        try {
            userManager.saveUser(four,"me");
            fail("should have rejected user with same username");
        }
        catch (DataIntegrityViolationException ex){
            assertNotNull(ex);
        }

        //Local clean-up
        userManager.deleteUser(threeUser);
    }
}
