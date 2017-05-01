package ca.po.model.dao.hibernate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.hibernate.Criteria;
import net.sf.hibernate.FetchMode;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.expression.Expression;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate.HibernateCallback;

import ca.po.model.Category;
import ca.po.model.Item;
import ca.po.model.ItemSupplied;
import ca.po.model.Product;
import ca.po.model.dao.ItemDAO;

/******************************************************************
 * 
 * This DAO deals with Category-Product-Item (the hierarchy does
 * not involve any cascade (cascade=none) in both direction
 * and ItemSupplied parent to child association are cascade save-update
 *
 * ****************************************************************/
public class ItemDAOHibernateImpl extends BaseDAOHibernate implements ItemDAO {

    public List getCategoriesOnly() throws DataAccessException {
        return getHibernateTemplate().loadAll(Category.class);
    }
    
    
    public List getCategories(boolean fetchProducts) throws DataAccessException {
        if (fetchProducts){
            List cats = (List) getHibernateTemplate().execute(
                    new HibernateCallback(){
                        public Object doInHibernate(Session session) throws HibernateException {
                            Query query = session.createQuery("from Category c left join fetch c.products");
                            return query.list();
                        }
                    });
            //need to remove duplicate Categories
            Set catsSet = new HashSet(cats);
            return new ArrayList(catsSet);
        } else {
            return getHibernateTemplate().loadAll(Category.class);
        }
    }
    
    public Category getCategory(final Long catId, boolean fetchProducts) throws DataAccessException {
        if (!fetchProducts){
            return (Category) getHibernateTemplate().get(Category.class,catId);
        } else {
            Category cat = (Category) getHibernateTemplate().execute(
                    new HibernateCallback() {
                        public Object doInHibernate(Session session) throws HibernateException {
                            Criteria criteria = session.createCriteria(Category.class);
                            criteria.add(Expression.eq("id", catId));
                            criteria.setFetchMode("products",FetchMode.EAGER);
                            return criteria.uniqueResult();
                        }
                    });
            return cat;
        }
        
    }
    
    /* With cascade "none" to Prod 
     * The user creating this new Cat/Prod/Item is passed as parameter
     * to store the createdBy username of the creator of new Cat/Prod/Item 
     * 
     */
    public void saveCategory(Category cat) throws DataAccessException {
         getHibernateTemplate().saveOrUpdate(cat); 
    }
    
    public List getProducts() throws DataAccessException {
        return getHibernateTemplate().loadAll(Product.class);
    }
    
    
    public List getProductsByCat(final Long catId, final boolean fetchCategory) throws DataAccessException {
        List prods = (List) getHibernateTemplate().execute(
                new HibernateCallback(){
                    public Object doInHibernate(Session session) throws HibernateException {
                        Criteria criteria = session.createCriteria(Product.class);
                        criteria.add(Expression.eq("category.id", catId));
                        if (fetchCategory){
                            criteria.setFetchMode("category",FetchMode.EAGER);
                        }
                        return criteria.list();
                    }
                });
        return prods;
    }
    
    
    public Product getProduct(final Long prodId, final boolean fetchItems, final boolean fetchCategory) throws DataAccessException {
        if ((!fetchCategory) && (!fetchItems)){
            return (Product) getHibernateTemplate().get(Product.class,prodId);
        }
        else {
            Product prod = (Product) getHibernateTemplate().execute(
                    new HibernateCallback() {
                        public Object doInHibernate(Session session) throws HibernateException {
                            Criteria criteria = session.createCriteria(Product.class);
                            criteria.add(Expression.eq("id", prodId));
                            if (fetchItems)
                                criteria.setFetchMode("items",FetchMode.EAGER);
                            if (fetchCategory)
                                criteria.setFetchMode("category",FetchMode.EAGER);
                            
                            return criteria.uniqueResult();
                        }
                    });
            return prod;
        }
        
    }
    
    
    /* With cascade "none" in both direction
     */
    public void saveProduct(Product prod) throws DataAccessException {
            getHibernateTemplate().saveOrUpdate(prod);   
    }
    
    /* With cascade "none" to Prod
     */
    public void saveItem(Item item) throws DataAccessException {
        getHibernateTemplate().saveOrUpdate(item);
    }
    
    //with no cascade delete to Prod. 
    public void deleteCategory(Category cat) throws DataAccessException {
        getHibernateTemplate().delete(cat);
    }
    //with no cascade delete to Item.  
    public void deleteProduct(Product prod) throws DataAccessException {
        getHibernateTemplate().delete(prod);
    }
    //No cascade delete to PO... i.e. 
    //no item can be removed if it is linked to existing PO
    public void deleteItem(Item item) throws DataAccessException {
        getHibernateTemplate().delete(item);
    }

    public Item getItem(final Long itemId, final boolean fetchProduct, final boolean fetchItemSupplies) throws DataAccessException {
        if ((!fetchItemSupplies) && (!fetchProduct)) {
            return (Item) getHibernateTemplate().get(Item.class,itemId);
        } else {
            Item item = (Item) getHibernateTemplate().execute(
                    new HibernateCallback() {
                        public Object doInHibernate(Session session) throws HibernateException {
                            Criteria criteria = session.createCriteria(Item.class);
                            criteria.add(Expression.eq("id", itemId));
                            if (fetchProduct)
                                criteria.setFetchMode("product",FetchMode.EAGER);
                            if (fetchItemSupplies)
                                criteria.setFetchMode("itemSupplies",FetchMode.EAGER);
                            return criteria.uniqueResult();
                        }
                    });
            return item;
            
        }
            
    }
    
   
    public List getAllItemsOnly() throws DataAccessException {
        return getHibernateTemplate().loadAll(Item.class);
    }
    /*
     * Returns Items by ProdId 
     * 
     */
    public List getItemsByProd(final Long prodId, final boolean fetchProduct) throws DataAccessException {
        List items = (List) getHibernateTemplate().execute(
                new HibernateCallback(){
                    public Object doInHibernate(Session session) throws HibernateException {
                        Criteria criteria = session.createCriteria(Item.class);
                        criteria.add(Expression.eq("product.id", prodId));
                        if (fetchProduct){
                            criteria.setFetchMode("product",FetchMode.EAGER);
                        }
                        return criteria.list();
                    }
                });
        return items;
   
    }

    
    public List getItemsByNameBeginWith(String nameBeginWith) throws DataAccessException {
        List items = getHibernateTemplate().find(
                "from Item i where i.name like ?%", nameBeginWith);
        if (items.size() == 0) return null;
        else return  items;
    }

    
    
   public boolean isItemOrdered(final Long itemId) throws DataAccessException {
       
       Integer count = (Integer) getHibernateTemplate().execute(
               new HibernateCallback() {
                   public Object doInHibernate(Session session) throws HibernateException {
                       return session.createQuery(
                               "select count(*) " +
                               "from PurchaseOrder po join po.poLineItems l join l.item item " +
                               "where item.id=?")
                       		  .setLong(0,itemId.longValue())
                       		  .uniqueResult();
                   
                   }
               });
       return (count.intValue() == 0 ? false : true);       
   }

   //all itemSupplies relatyed to multiple itemIds
   public List getItemSupplies(final Long[] itemIds, final boolean fetchSupplier) throws DataAccessException {
       
       List itemSupplies = (List) getHibernateTemplate().execute(
               new HibernateCallback(){
                   public Object doInHibernate(Session session) throws HibernateException {
                       Criteria criteria = session.createCriteria(ItemSupplied.class);
                       criteria.add(Expression.in("item.id", itemIds));
                       if (fetchSupplier){
                           criteria.setFetchMode("supplier",FetchMode.EAGER);
                       }
                       return criteria.list();
                   }
               });
       return itemSupplies;
   }

   
}
