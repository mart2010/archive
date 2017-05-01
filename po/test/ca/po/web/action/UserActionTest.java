package ca.po.web.action;

import java.util.List;

import ca.po.Constants;
import ca.po.model.User;
import ca.po.model.type.Role;
import ca.po.web.form.UserForm;



/**
 * @author MOUELLET
 * Requires the pre-requisite that DB has sample data (loaded from script)
 */
public class UserActionTest extends BaseStrutsTestCase {

    
    public UserActionTest(String name) throws Exception {
        super(name);
    }
    
    public void setUp() throws Exception {
        super.setUp();
        //all User-related Action require Admin Role 
        getMockRequest().setUserRole(Role.PO_ADMIN.toString());
    }
    public void tearDown() throws Exception {
        super.tearDown();
    }
 
    public void testEditUserAction() throws Exception {
        setRequestPathInfo("/editUser");
        //new user
    	addRequestParameter("username",Constants.NEW_ENTITY_REQUEST_VALUE );
    	actionPerform();
    	UserForm form = (UserForm) getActionForm(); 
    	assertNull(form.getSelLabs());
    	assertNull(form.getSelRoles());
    	assertNotNull( getSession().getAttribute("selUser") );
    	assertTrue(((List) getRequest().getAttribute("labs")).size()>0);
    	verifyForward("edit");
    	verifyNoActionErrors();
    	clearRequestParameters();
    	
    	//existing user... 
    	addRequestParameter("username","adminSample1");
    	actionPerform();
    	verifyNoActionErrors();
    	UserForm uForm = (UserForm) getActionForm(); 
    	assertTrue(form.getSelLabs().length>0);
    	assertTrue(form.getSelRoles().length>0);
    	User user = (User)getSession().getAttribute("selUser"); 
    	assertEquals(uForm.getLastName(),user.getLastName() );
    	
    	verifyForward("edit");
    	verifyNoActionErrors();
    }

    public void testSaveUser() throws Exception {
        //Setting-up Session form bean + userSel by first calling editUser 
        setRequestPathInfo("/editUser");
        addRequestParameter("username",Constants.NEW_ENTITY_REQUEST_VALUE );
    	actionPerform();
        clearRequestParameters();
    	
        setRequestPathInfo("/saveUser");
        addRequestParameter("name","username6charmin");
        addRequestParameter("firstName","first");
        addRequestParameter("lastName","last");
        addRequestParameter("email","fff@ddd.com");
        addRequestParameter("password","mmmm");
        addRequestParameter("confirmPassword","mmmm");
        addRequestParameter("selRoles",new String[] {Role.PO_ADMIN.toString()} );
        addRequestParameter("selLabs",new String[] {"lab111","LabSample2"} );
        
        actionPerform();
        UserForm uForm = (UserForm) getActionForm();
        verifyNoActionErrors();
        assertNull(uForm);
        verifyForward("list");
        
        //check the new added user
        clearRequestParameters();
        setRequestPathInfo("/editUser");
        addRequestParameter("username","username6charmin");
        actionPerform();
        verifyNoActionErrors();
        verifyForward("edit");
        UserForm userF = (UserForm) getActionForm();
        User userS = (User) getSession().getAttribute("selUser");
        assertEquals(userS.getName(),userF.getName());
        assertEquals(userS.getEmail(),userF.getEmail());
        assertEquals(userS.getRoles().size(),userF.getSelRoles().length);
        assertEquals(userS.getLabs().size(), userF.getSelLabs().length);
        
        //delete the saved user...
        clearRequestParameters();
        setRequestPathInfo("/admin/deleteUser");
        addRequestParameter("username","username6charmin");
        actionPerform();
        verifyForward("list");
        verifyNoActionErrors();
        
    }
    
 
    public void testListUserAction() throws Exception {
        
        setRequestPathInfo("/admin/listUser");
        actionPerform();
        
        verifyForward("list");
        assertTrue(((List)getRequest().getAttribute("userList")).size()>0);
    }
    
    
}
