package ca.po.model.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import ca.po.model.Category;
import ca.po.model.Item;
import ca.po.model.Product;


/**
 * @author MOUELLET
 *
 */
public interface ItemDAO {

    public List getCategoriesOnly() throws DataAccessException;
    
    List getCategories(boolean fetchProducts) throws DataAccessException;

    Category getCategory(Long id, boolean fetchProducts) throws DataAccessException;     

    void saveCategory(Category cat) throws DataAccessException;

    List getProducts() throws DataAccessException;
    
    List getProductsByCat(Long catId, boolean fetchCategory) throws DataAccessException;
    
    Product getProduct(Long id, boolean fetchItems, boolean fetchCategory) throws DataAccessException;
    
    void saveProduct(Product prod) throws DataAccessException;

    void saveItem(Item item) throws DataAccessException;

    void deleteCategory(Category cat) throws DataAccessException;

    void deleteProduct(Product prod) throws DataAccessException;

    void deleteItem(Item item) throws DataAccessException;

    List getItemSupplies(Long[] itemIds, boolean fetchSupplier) throws DataAccessException;
    
    List getAllItemsOnly() throws DataAccessException;

    List getItemsByProd(Long prodId, boolean fetchProduct) throws DataAccessException;
    
    List getItemsByNameBeginWith(String nameBeginWith) throws DataAccessException;

    Item getItem(Long itemId, boolean fetchProduct, boolean fetchItemSupplies) throws DataAccessException; 
    
    boolean isItemOrdered(Long itemId) throws DataAccessException;
    
}
