package ca.canvac.webstore.dao.ibatis;

import ca.canvac.webstore.dao.CategoryDao;
import ca.canvac.webstore.domain.Category;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import java.util.List;

public class SqlMapCategoryDao extends SqlMapClientDaoSupport implements CategoryDao {

    public Category getCategory(String categoryId) throws DataAccessException {
        //this was modified to use the int class encapsulation Integer
        return (Category) getSqlMapClientTemplate().queryForObject("getCategory", categoryId);
    }

    public List getCategoryList() throws DataAccessException {
        return getSqlMapClientTemplate().queryForList("getCategoryList", null);
    }

}
