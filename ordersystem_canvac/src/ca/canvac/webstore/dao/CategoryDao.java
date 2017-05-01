package ca.canvac.webstore.dao;

import ca.canvac.webstore.domain.Category;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface CategoryDao {

    Category getCategory(String categoryId) throws DataAccessException;

    List getCategoryList() throws DataAccessException;

}
