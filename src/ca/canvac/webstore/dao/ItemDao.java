package ca.canvac.webstore.dao;

import ca.canvac.webstore.domain.Item;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface ItemDao {

    Item getItem(long itemId) throws DataAccessException;

    List getItemListBySubCategory(String subCategoryId) throws DataAccessException;

    //using the Availability flag in the DB (-1: always av., 0: n.a. and >0:available)
    boolean isItemAvailable(long itemId) throws DataAccessException;

    List searchItemList(String keywords) throws DataAccessException;

}
