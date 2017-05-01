package ca.po.model.service;

import java.util.List;

import ca.po.model.Category;
import ca.po.model.Item;
import ca.po.model.Product;


/**
 * @author MOUELLET
 *
 */
public interface ItemManager {


    List getCategoriesOnly();
    
    List getCategoriesWithProducts();
    
    Category getCategory(Long id);     

    void saveCategory(Category cat, String username);

    List getProducts();
    
    List getProductsByCat(Long catId);

    Product getProductOnly(Long id);

    Product getProductWithItemAndCategory(Long id);
    Product getProductWithItems(Long id);
    Product getProductWithCategory(Long id);
    
    void saveProduct(Product prod, Category cat, String username);

    void saveItem(Item item, Product prod, String username);

    int deleteCategory(Long catId);

    int deleteProduct(Long prodId);

    int deleteItem(Long itemId);

    List getAllItems();

    List getItemsByProduct(Long prodId);
    
    List getItemsByNameBeginWith(String nameBeginWith);

    List getItemSuppliesByItems(Long[] itemId, boolean fetchSupplier);
    
    Item getItem(Long itemId, boolean fetchProduct, boolean fecthItemSupplies ); 
    
    
}