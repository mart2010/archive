package ca.canvac.webstore.dao;

import ca.canvac.webstore.domain.Category;
import ca.canvac.webstore.domain.SubCategory;
import ca.canvac.webstore.domain.Item;
import java.util.List;
import java.util.ListIterator;


public class CatSubCatItemDaoTest extends BaseDaoTest {

    CategoryDao catDao = null;
    SubCategoryDao subCatDao = null;
    ItemDao itemDao = null;


    protected void setUp() throws Exception {
        super.setUp();
        catDao = (CategoryDao) ctx.getBean("categoryDao");
        subCatDao = (SubCategoryDao) ctx.getBean("subCategoryDao");
        itemDao = (ItemDao) ctx.getBean("itemDao");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        catDao = null;
        subCatDao = null;
        itemDao = null;
    }


    public void testCatSubCat() {

        ListIterator iter=null;
        ListIterator iters=null;
        ListIterator iteri=null;
        Category cat = null;
        SubCategory scat = null;
        Item item = null;

        List catL = catDao.getCategoryList();
        for (iter=catL.listIterator(); iter.hasNext(); ){
            cat = (Category) iter.next();
            List scatL = subCatDao.getSubCategoryListByCategory(cat.getCategoryId());
            for (iters=scatL.listIterator();iters.hasNext(); ){
                scat = (SubCategory) iters.next();
                assertEquals(scat.getCategoryId(),cat.getCategoryId());
            }
            List itemL=itemDao.getItemListBySubCategory(scat.getSubCategoryId());
            for (iteri=itemL.listIterator(); iteri.hasNext();){
                item= (Item) iteri.next();
                assertEquals(item.getSubCategoryId(),scat.getSubCategoryId());
            }
        }
    }


}
