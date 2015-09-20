package ca.canvac.webstore.dao;

import ca.canvac.webstore.domain.SubCategory;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface SubCategoryDao {

    SubCategory getSubCategory(String subCategoryId) throws DataAccessException;

    List getSubCategoryListByCategory(String categoryId) throws DataAccessException;

}
