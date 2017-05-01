package ca.po.web.action;

import java.util.List;

import ca.po.model.Category;
import ca.po.model.Item;
import ca.po.model.Product;
import ca.po.model.type.Role;
import ca.po.web.form.ItemForm;
import ca.po.web.form.NameDescForm;



/**
 * @author MOUELLET
 * Requires the pre-requisite that DB has sample data (loaded from script)
 */
public class ItemActionTest extends BaseStrutsTestCase  {

    
    public ItemActionTest(String name) throws Exception {
        super(name);
    }
    
    public void setUp() throws Exception {
        super.setUp();
        //all User-related Action require User Role 
        getMockRequest().setUserRole(Role.PO_USER.toString());
        getMockRequest().setRemoteUser("userTest");
    }
    public void tearDown() throws Exception {
        super.tearDown();
    }

    private void validateListItem(){
        List cats = (List) getRequest().getAttribute("categories");
        assertTrue(cats.size()>0);
        Category selectedCat =   (Category) getRequest().getSession().
        			getAttribute("selCategory");
        
        List prods = (List) getRequest().getAttribute("products");
        assertEquals(selectedCat, ((Product)prods.get(0) ).getCategory());
        
        List items = (List) getRequest().getAttribute("items");
        Product selectedProd =   (Product) getRequest().getSession().
		getAttribute("selProduct");
        
        assertEquals(selectedProd, ((Item)items.get(0) ).getProduct());
    }
    
    public void testListItemAction() throws Exception {
        
        //with no selCatId, selProdId set as input parameter 
        //and no current Selection in session (typically Action first call)
        setRequestPathInfo("/listItem");
        actionPerform();
        verifyForward("list");
        verifyNoActionErrors();
        validateListItem();
        
        clearRequestParameters();
        
        //check with paramater only 
        setRequestPathInfo("/listItem");
        addRequestParameter("selCatId","1001");
        addRequestParameter("selProdId","1003");
        actionPerform();
        verifyForward("list");
        verifyNoActionErrors();
        validateListItem();
    }

    public void testAddNewCatProd() throws Exception {
        
        setRequestPathInfo("/newCatProd");
        addRequestParameter("catOrProd","category");
        addRequestParameter("catName","cat5minchar");

        actionPerform();
        verifyNoActionErrors();

		//to be finished..
        NameDescForm form = (NameDescForm) getActionForm();
        form.setName("cat");
                
    }
    
  

    public void testNewEditItem() throws Exception {
        setRequestPathInfo("/editItem");
        addRequestParameter("itemId","-1");
        getRequest().getSession().setAttribute("selProduct",new Product());
        
        actionPerform();
        verifyNoActionErrors();
        ItemForm itemForm = (ItemForm) getActionForm();
        assertTrue(itemForm.getCurrencies().size()>2);
        assertEquals(itemForm.getItemSupplier(0).getSupplier().getId(),"-1");
        assertNotNull(getSession().getAttribute("selItem"));
        
        clearRequestParameters();
        //check a forward to /goToListSupplier
        setRequestPathInfo("/goToListSupplier");
        addRequestParameter("name","itemName");
        addRequestParameter("selSupplierId[0]","200");
        actionPerform();
        itemForm = (ItemForm) getActionForm();
        assertEquals(itemForm.getName(),"itemName");
        assertEquals(itemForm.getItemSupplier(0).getSupplier().getId(),"200");
    }


	public void testSaveItem() throws Exception {
        setRequestPathInfo("/saveItem");
        
        addRequestParameter("name","itemname");
        addRequestParameter("manufacturer","itemmanufacturer");
        addRequestParameter("distributor","item");
        addRequestParameter("format","item");
        addRequestParameter("number","item");
        addRequestParameter("listPriceString","9.00");
        addRequestParameter("listPriceCurrencyString","CAD");
        addRequestParameter("selSupplierId[0]","1000");
        getRequest().getSession().setAttribute("selItem",new Item());
        getRequest().getSession().setAttribute("selProduct",new Product());
        
        actionPerform();
        verifyNoActionErrors();
        

    }

}
