package ca.po.model;

import junit.framework.TestCase;

/**
 * @author MOUELLET
 *
 * TODO 
 */
public class ItemSupplierTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public ItemSupplierTest(String arg0) {
        super(arg0);
    }

    public void testCheckSupplier() {
        //supplier check
        Supplier sup1 = new Supplier("sup",null,null,null,"tester");
        Supplier sup2 = new Supplier("sup","sup desc","ttt","ttt","tester");
        assertEquals(sup1,sup2);

        //ItemSupplied check
        Item item = new Item("item",null,null,null,null,null,null);
        ItemSupplied itemSup1 = new ItemSupplied();
        itemSup1.setItem(item);
        itemSup1.setSupplier(sup1);
        ItemSupplied itemSup2 = new ItemSupplied();
        itemSup2.setItem(item);
        itemSup2.setSupplier(sup2);
        
        assertEquals(itemSup1,itemSup2);
        
        //Check addSupplier
        Item item2 = new Item();
        item2.setName("item2");
        
        assertTrue(item2.addSupplier(sup1,"tester"));
        assertFalse(item2.addSupplier(sup2,"tester"));
    }
    
    
    
    
}
