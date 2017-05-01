package ca.po.model.service.impl;

import java.util.Date;
import java.util.List;

import ca.po.model.Category;
import ca.po.model.Item;
import ca.po.model.Product;
import ca.po.model.dao.ItemDAO;
import ca.po.model.service.ItemManager;


public class ItemManagerImpl implements ItemManager {
    
    private ItemDAO itemDAO;
    public void setItemDAO(ItemDAO itemDAO) {
        this.itemDAO = itemDAO;
    }    
    

    public List getCategoriesOnly() {
        return itemDAO.getCategoriesOnly();
    }
 
    public List getCategoriesWithProducts() {
        return itemDAO.getCategories(true);
    }
    
    public Category getCategory(Long id) {
        return itemDAO.getCategory(id,true);
    }
    
    
    public void saveCategory(Category cat, String creator) {
        if (cat.getId() == null) {
            cat.setCreatedBy(creator);
        }
            
        itemDAO.saveCategory(cat);
    }
    
    public List getProducts() {
        return itemDAO.getProducts();
    }
    
    public List getProductsByCat(Long catId){
        return itemDAO.getProductsByCat(catId,false);
        
    }
    
    public Product getProductOnly(Long id) {
        return itemDAO.getProduct(id,false,false);
    }
    
    public Product getProductWithItemAndCategory(Long id) {
        return itemDAO.getProduct(id,true,true);
    }
    
    public Product getProductWithItems(Long id) {
        return itemDAO.getProduct(id,true,false);
    }

    public Product getProductWithCategory(Long id) {
        return itemDAO.getProduct(id,false,true);
    }
    
    
    public void saveProduct(Product prod, Category cat, String creator) {
        //Assign username for new Product
        if (prod.isNew()) {
            if (prod.getCategory()==null) {
                assert cat!=null;
                prod.defineCategory(cat);
            }
            prod.setCreatedBy(creator);
    	}
        itemDAO.saveProduct(prod);
    }

    /*
     * To be used with items linked to Product (either new or existing)
     * Product argument is only mandatory when item has no link to Product 
     */
    public void saveItem(Item item, Product prod, String editor) {
        //new item
        if (item.isNew() ){
            if (item.getProduct()==null){
                assert (prod != null) : "This requires Product argument to be not null";
                prod = getProductWithItems(prod.getId());
                item.defineProduct(prod);
            }
            item.setCreatedBy(editor);
            item.setUpdatedBy(editor);
            item.setUpdatedDate(item.getCreatedDate());
        } else {  //Assign updatedDate and username for existing item
            item.setUpdatedBy(editor);
            item.setUpdatedDate(new Date());
        }
        itemDAO.saveItem(item);
    }
    
    //Returns 0 when category does not exist or 1 if it does
    public int deleteCategory(Long catId) {
        Category cat = itemDAO.getCategory(catId,true);
        if (cat == null)
            return 0;
        if (!cat.getProducts().isEmpty())
            throw new IllegalArgumentException("Category has to be empty to be deleted");
        itemDAO.deleteCategory(cat);
        return 1;
    }
    
    public int deleteProduct(Long prodId) {
        Product prod = itemDAO.getProduct(prodId,true,true);
        if (prod == null)
            return 0;
        if (!prod.getItems().isEmpty())
            throw new IllegalArgumentException("Product has to be empty to be deleted");
        //remove the product in category
        Category cat = prod.getCategory();
        cat.removeProduct(prod);
        
        itemDAO.deleteProduct(prod);
        return 1;
    }
    
    public int deleteItem(Long itemId) {
        Item item = itemDAO.getItem(itemId,true,false);
        if (item == null)
            return 0;
        
        if (itemDAO.isItemOrdered(item.getId())){
            throw new IllegalArgumentException("This item has been already ordered, so it cannot be deleted");
        }
        
        Product prod = item.getProduct();
        prod.removeItem(item);
        itemDAO.deleteItem(item);
        return 1;
    }

    public Item getItem(Long itemId, boolean fetchProduct, boolean fetchItemSupplies) {
        return itemDAO.getItem(itemId,fetchProduct,fetchItemSupplies);
    }
    
    
    public List getAllItems() {
        return itemDAO.getAllItemsOnly();
    }
    
    /*
     * Returns Items by ProdId (do not eager fetch Poduct)
     * 
     */
    public List getItemsByProduct(Long prodId) {
        return itemDAO.getItemsByProd(prodId,false);
    }
    
    public List getItemsByNameBeginWith(String nameBeginWith) {
        return itemDAO.getItemsByNameBeginWith(nameBeginWith);
    }


    public List getItemSuppliesByItems(Long[] itemIds, boolean fetchSupplier) {
        return itemDAO.getItemSupplies(itemIds,fetchSupplier);
    }


}
