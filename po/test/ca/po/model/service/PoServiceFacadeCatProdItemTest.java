package ca.po.model.service;

import java.util.Iterator;
import java.util.List;

import ca.po.model.Category;
import ca.po.model.Item;
import ca.po.model.Lab;
import ca.po.model.Product;
import ca.po.model.Supplier;
import ca.po.model.User;
import ca.po.model.type.Money;
import ca.po.model.type.Role;


/**
 * @author MOUELLET
 * Attention: this needs to be changed according to the cascade="none" over 
 *	all cat-prod-item hierarchy....
 */

public class PoServiceFacadeCatProdItemTest extends PoBaseServiceFacadeTest {
    private User user1;
    /*
     * 
     * 
     */
    protected void setUp() throws Exception {
        super.setUp();
        itemManager = (ItemManager) ctx.getBean("itemManager");
        
        //create the dummy creator
        user1 = new User("user","user","user",Role.PO_USER,new Lab("lab",null),"cd@com.com", "pass");
        userManager.saveUser(user1,"me");
       
        //create two Categories 
        Category catA = new Category("catA",null);
        //should just create A
        itemManager.saveCategory(catA,user1.getName());

        //alternative way 
        Product prodB1 = new Product("prodB1",null);
        Category catB = new Category("catB","category B");
        prodB1.defineCategory(catB);
        //should not cascade to Cat B 
        itemManager.saveCategory(catB,user1.getName());
        itemManager.saveProduct(prodB1,user1.getName());

        
        //no cascade save-update the Prod/Cat
        Category catC = new Category("catC",null);
        Product prodC1 = new Product("prodC1",null);        
        Product prodC2 = new Product("prodC2",null);
        Item itemC11 = new Item("itemC11CAD",null,null,null,null,null,Money.getCAD(100.00));
        itemC11.defineProduct(prodC1);
        prodC1.defineCategory(catC);
        prodC2.defineCategory(catC);
        //should not cascade to ProdC1 and Cat C
        itemManager.saveCategory(catC,user1.getName());
        itemManager.saveProduct(prodC1,user1.getName());
        itemManager.saveItem(itemC11,prodC1,user1.getName());
       
        //C2 is not part of ItemC11 hierarchy
        itemManager.saveProduct(prodC2,user1.getName());

        Item itemD11 = new Item("itemD11USD",null,null,null,null,null,Money.getUSD(100.00));
        Product prodD1 = new Product("prodD1",null);
        Category catD = new Category("catD",null);
        catD.addProduct(prodD1);
        prodD1.addItem(itemD11);

        itemManager.saveCategory(catD,user1.getName());
        itemManager.saveProduct(prodD1,user1.getName());
        itemManager.saveItem(itemD11,prodD1,user1.getName());
        
    }

    protected void tearDown() throws Exception {
        super.tearDown(); 
        //clean-up, what was created in Setup
        userManager.deleteUser(user1);
        userManager.deleteLab(userManager.getLabByName("lab"));
        itemManager.deleteItem(itemManager.getItemByName("itemC11CAD"));
        itemManager.deleteItem(itemManager.getItemByName("itemD11USD"));
        itemManager.deleteProduct(itemManager.getProductByName("prodB1"));
        itemManager.deleteProduct(itemManager.getProductByName("prodC1"));
        itemManager.deleteProduct(itemManager.getProductByName("prodC2"));
        itemManager.deleteProduct(itemManager.getProductByName("prodD1"));
        itemManager.deleteCategory(itemManager.getCategoryByName("catA"));
        itemManager.deleteCategory(itemManager.getCategoryByName("catB"));
        itemManager.deleteCategory(itemManager.getCategoryByName("catC"));
        itemManager.deleteCategory(itemManager.getCategoryByName("catD"));
        //to check if this really affects the Hibernate Session bound object
        itemManager = null;  
    }
 
    public PoServiceFacadeCatProdItemTest(String arg0) {
        super(arg0);
    }

    public void testGetItemsByCatProd(){
        Long catCId = ((Category) itemManager.getCategoryByName("catC")).getId();
        List items = itemManager.getItemsByProdCat(null,catCId);
        assertEquals(1,items.size());
        Long prodC1Id = ((Product) itemManager.getProductByName("prodC1")).getId();
        items = itemManager.getItemsByProdCat(prodC1Id,catCId);
        assertEquals(1,items.size());
        Long prodC2Id = ((Product) itemManager.getProductByName("prodC2")).getId();
        items = itemManager.getItemsByProdCat(prodC2Id,catCId);
        assertEquals(0,items.size() );
        items = itemManager.getItemsByProdCat(null,null);
        List itemsAll = itemManager.getItems();
        assertEquals(items.size(),itemsAll.size());
        
    }
    
  
    public void testSimple(){
        User user2 = new User("user2","user2","user2",Role.PO_USER,new Lab("lab2",null),"cd@com.com","pass");
         userManager.saveUser(user2,"me");
        Item item = itemManager.getItemByName("itemC11CAD");
        item.setManufacturer("no");
        //does not throw IllagalAccessException anymore 
        itemManager.saveItem(item,null,user2.getName());

        user2.addRole(Role.PO_ADMIN);
         userManager.saveUser(user2,"me");
        itemManager.saveItem(item,null,user2.getName());
        assertEquals(itemManager.getItemByName("itemC11CAD").getManufacturer(),"no");
        //clean-up
        userManager.deleteUser(user2);
        userManager.deleteLab(userManager.getLabByName("lab2"));
    }

    
    public void testUpdate(){
        Item itemC11 = itemManager.getItemByName("itemC11CAD");
        itemC11.setDesc("Item desc");
        itemC11.getProduct().setDesc("Prod desc");
        itemC11.getProduct().getCategory().setDesc("Cat desc");
        itemManager.saveItem(itemC11,null,user1.getName());
        Category catC = itemManager.getCategoryByName("catC");
        //illustrate no more cascade Update
        assertNull(catC.getDesc());
        Iterator iter = catC.getProducts().iterator();  
        //should be ordered by Name
        Product prodC1 = (Product) iter.next();
        assertNull(prodC1.getDesc());
        iter = prodC1.getItems().iterator();
        //should be ordered by Name
        Item item = (Item) iter.next();
        assertEquals(item,itemC11);
        assertEquals(item.getId(),itemC11.getId());
    }
    
    public void testItemSupplier(){
       //check the cascade to Supplier
        Item itemSup = new Item("itemSup",null,null,null,null,null,Money.getCAD(100.00));
        itemSup.addSupplier(new Supplier("Sup",null,null,null,user1.getName()),user1.getName());
        Product prodSup = new Product("prodSup",null);
        Category catSup = new Category("catSup",null);
        prodSup.setCategory(catSup);
        itemSup.setProduct(prodSup);
        itemManager.saveCategory(catSup,user1.getName());
        itemManager.saveProduct(prodSup,user1.getName());
        itemManager.saveItem(itemSup,prodSup,user1.getName());
        assertTrue(itemSup.getItemSupplies().size()>0);
        
        //add another new supplier
        Supplier sup2 = new Supplier("Sup2",null,"salesRep2","salescontact2",null);
        itemSup.addSupplier(sup2,user1.getName());
        itemManager.saveItem(itemSup,null,user1.getName());
        assertEquals(supplierManager.getSuppliers().size(),2);
        //inserting the same should not impact
        Supplier sup = new Supplier("Sup",null,null,null,null);
        assertFalse(itemSup.addSupplier(sup,"blab"));
        
        //try removing the item.. but should keep the added Suppliers
        itemManager.deleteItem(itemSup);
        assertEquals(supplierManager.getSuppliers().size(),2);
        //clean-up
        itemManager.deleteProduct(prodSup);
        itemManager.deleteCategory(prodSup.getCategory());
        supplierManager.deleteSupplier(sup2);
        supplierManager.deleteSupplier(sup);        
    }
    
}
