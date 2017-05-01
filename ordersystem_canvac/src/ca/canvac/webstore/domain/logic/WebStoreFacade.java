package ca.canvac.webstore.domain.logic;

import ca.canvac.webstore.domain.*;
import java.util.List;

/**
 * WebStore primary business interface.
 *
 * @author Martin Ouellet
 */
public interface WebStoreFacade {

    Account getAccount(String userId);

    Account getAccount(String userId, String password);

    Category getCategory(String categoryId);

    List getCategoryList();

    Item getItem(long itemId);

    List getItemListBySubCategory(String subCategoryId);

    Order getOrder(long orderId);

    List getOrdersByUserId(String userId);

    SubCategory getSubCategory(String subCategoryId);

    List getSubCategoryListByCategory(String categoryId);

    List getUsernameList();


    void insertOrder(Order order);

    boolean isItemAvailable(long itemId);

    List searchItemList(String keywords);

    void updateAccount(Account account);

}
