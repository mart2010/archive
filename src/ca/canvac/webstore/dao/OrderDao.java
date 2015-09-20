package ca.canvac.webstore.dao;

import ca.canvac.webstore.domain.Order;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface OrderDao {

    Order getOrder(long orderId) throws DataAccessException;

    List getOrdersByUsername(String username) throws DataAccessException;

    void insertOrder(Order order) throws DataAccessException;

}
