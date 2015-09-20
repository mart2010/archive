package ca.canvac.webstore.dao.ibatis;

import ca.canvac.webstore.dao.ItemDao;
import ca.canvac.webstore.domain.Item;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class SqlMapItemDao extends SqlMapClientDaoSupport implements ItemDao {


    /* Inner Classes */

    public static class ListSearch {

        private List keywordList = new ArrayList();

        public ListSearch(String keywords) {
            StringTokenizer splitter = new StringTokenizer(keywords, " ", false);
            while (splitter.hasMoreTokens()) {
                this.keywordList.add("%" + splitter.nextToken() + "%");
            }
        }

        public List getKeywordList() {
            return keywordList;
        }
    }

    public Item getItem(long itemId) throws DataAccessException {
        Item item = (Item) getSqlMapClientTemplate().queryForObject("getItem", new Long(itemId));
        return item;
    }

    public List getItemListBySubCategory(String subCategoryId) throws DataAccessException {
        return getSqlMapClientTemplate().queryForList("getItemListBySubCategory", subCategoryId);
    }


    //Consider that it is always available when flag is different from 0
    // in case where it was not set (i.e. =null)
    public boolean isItemAvailable(long itemId) throws DataAccessException {
        Integer i = (Integer) getSqlMapClientTemplate().queryForObject("getAvailability", new Long(itemId));
        return (i == null || i.intValue() != 0);
    }

    public List searchItemList(String keywords) throws DataAccessException {
        Object parameterObject = new ListSearch(keywords);
        return getSqlMapClientTemplate().queryForList("searchItemList", parameterObject);
    }


}
