package ca.canvac.webstore.domain.logic;

import ca.canvac.webstore.dao.*;
import ca.canvac.webstore.domain.*;
import java.util.List;

/**
 * WebStore primary business object.
 * It relies on a TransactionFactoryProxyBean,
 * as set in the applicationContext.xml in the war/WEB-INF directory.
 *
 * @author Martin Ouellet
 */
public class WebStoreImpl implements WebStoreFacade {

    private AccountDao accountDao;
    private CategoryDao categoryDao;
    private ItemDao itemDao;
    private OrderDao orderDao;
    private SendEmailConfirmation sendEmailOrder;
    private SubCategoryDao subCategoryDao;


    public Account getAccount(String userId) {
        return this.accountDao.getAccount(userId);
    }

    public Account getAccount(String userId, String password) {
        return this.accountDao.getAccount(userId, password);
    }

    public Category getCategory(String categoryId) {
        return this.categoryDao.getCategory(categoryId);
    }


    public List getCategoryList() {
        return this.categoryDao.getCategoryList();
    }

    public Item getItem(long itemId) {
        return this.itemDao.getItem(itemId);
    }


    public List getItemListBySubCategory(String subCategoryId) {
        return this.itemDao.getItemListBySubCategory(subCategoryId);
    }

    public Order getOrder(long orderId) {
        return this.orderDao.getOrder(orderId);
    }

    public List getOrdersByUserId(String userId) {
        return this.orderDao.getOrdersByUsername(userId);
    }

    public SubCategory getSubCategory(String subCategoryId) {
        return this.subCategoryDao.getSubCategory(subCategoryId);
    }


    public List getSubCategoryListByCategory(String categoryId) {
        return this.subCategoryDao.getSubCategoryListByCategory(categoryId);
    }

    public List getUsernameList() {
        return this.accountDao.getUsernameList();
    }

    //insert order and send confirmation
    public void insertOrder(Order order) {
        this.orderDao.insertOrder(order);
        this.sendEmailOrder.sendMail(order);
    }

    public boolean isItemAvailable(long itemId) {
        return this.itemDao.isItemAvailable(itemId);
    }

    public List searchItemList(String keywords) {
        return this.itemDao.searchItemList(keywords);
    }

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public void setCategoryDao(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public void setItemDao(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public void setSendEmailOrder(SendEmailConfirmation bean) {
        this.sendEmailOrder = bean;
    }

    public void setSubCategoryDao(SubCategoryDao subCategoryDao) {
        this.subCategoryDao = subCategoryDao;
    }


    public void updateAccount(Account account) {
        this.accountDao.updateAccount(account);
    }

}
