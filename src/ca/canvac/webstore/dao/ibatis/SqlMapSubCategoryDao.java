package ca.canvac.webstore.dao.ibatis;

import ca.canvac.webstore.dao.SubCategoryDao;
import ca.canvac.webstore.domain.SubCategory;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import java.util.List;

public class SqlMapSubCategoryDao extends SqlMapClientDaoSupport implements SubCategoryDao {

    public SubCategory getSubCategory(String subCategoryId) throws DataAccessException {
        return (SubCategory) getSqlMapClientTemplate().queryForObject("getSubCategory", subCategoryId);
    }

    public List getSubCategoryListByCategory(String categoryId) throws DataAccessException {
        return getSqlMapClientTemplate().queryForList("getSubCategoryListByCategory", categoryId);
    }

}
