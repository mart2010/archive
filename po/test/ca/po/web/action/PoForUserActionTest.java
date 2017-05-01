package ca.po.web.action;

import ca.po.model.PoLineItem;
import ca.po.model.PurchaseOrder;
import ca.po.model.service.ItemManager;
import ca.po.web.form.PoForUserForm;



/**
 * @author MOUELLET
 * Requires the pre-requisite that DB has sample data (loaded from script)
 */
public class PoForUserActionTest extends BaseStrutsTestCase  {

    
    public PoForUserActionTest(String name) throws Exception {
        super(name);
    }
    
    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    private PurchaseOrder constructNewPO() {
        ItemManager manager = (ItemManager) ctx.getBean("itemManager");
        PurchaseOrder po = new PurchaseOrder();
        po.addItem(manager.getItem(new Long("1001"),true,true));
        po.addItem(manager.getItem(new Long("1002"),true,true));
        po.setConfirmNumber("confirm Number");
        po.setProject("project");

        return po;
    }

    public void testAddRemoveItemToPoAction() throws Exception {
        //test with no currentPo in session
        setRequestPathInfo("/addItemToUserPo");
        addRequestParameter("itemId","1000");
        addRequestParameter("itemId","1000");
        actionPerform();
        verifyForward("viewUserPoAction");
        verifyNoActionErrors();
        PurchaseOrder po = (PurchaseOrder) getSession().getAttribute("currentPo");
        assertEquals( ((PoLineItem)po.getPoLineItems().get(0)).getItem().getId(),
                new Long("1000")); 
        
        //test with existing Po in session (just created) 
        clearRequestParameters();
        setRequestPathInfo("/addItemToUserPo");
        addRequestParameter("itemId","1000");
        actionPerform();

        verifyForward("viewUserPoAction");
        verifyNoActionErrors();
        po = (PurchaseOrder) getSession().getAttribute("currentPo");
        assertEquals( ((PoLineItem)po.getPoLineItems().get(0)).getQuantity(),2); 
    
        //test with existing Po in session (just created) 
        clearRequestParameters();
        setRequestPathInfo("/removeItemToUserPo");
        addRequestParameter("itemId","1000");
        actionPerform();

        verifyForward("viewUserPoAction");
        verifyNoActionErrors();
        
        po = (PurchaseOrder) getSession().getAttribute("currentPo");
        assertEquals( po.getPoLineItems().size(),0);
        
    }
   
    /* cannot work because depends on the ACEGI security for getUser()...*/
    public void testEditItemToPoAction() throws Exception {
        //test with no currentPo in session
        setRequestPathInfo("/editUserPo");
                
        actionPerform();
        verifyNoActionErrors();
        verifyActionMessages(new String[] {"po.noCurrentActivePO"});
        verifyForward("editUserPo");

        
        clearRequestParameters();
        //test with currentPo set in session 
        setRequestPathInfo("/editUserPo");
        getSession().setAttribute("currentPo", constructNewPO());
        
        actionPerform();
        verifyNoActionErrors();
        verifyForward("editUserPo");
        
        PoForUserForm poForm = (PoForUserForm) getActionForm();
        assertEquals(poForm.getConfirmNumber(),"confirm Number");
        //assertEquals(poForm.getPoLineItems().size(),2);
        
    }    
    
    
 /* cannot work because depends on the ACEGI security for getUser()...    
    public void testSavePoAction() throws Exception {
        getSession().setAttribute("currentPo",constructNewPO());
        setRequestPathInfo("/saveUserPo");
        addRequestParameter("selLabId","100");
        addRequestParameter("project","test");
        
        actionPerform();
    }
    
*/    
    
}
