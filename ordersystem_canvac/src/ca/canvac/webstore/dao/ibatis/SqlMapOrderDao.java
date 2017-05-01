package ca.canvac.webstore.dao.ibatis;

import ca.canvac.webstore.dao.OrderDao;
import ca.canvac.webstore.domain.LineItem;
import ca.canvac.webstore.domain.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import java.util.List;

public class SqlMapOrderDao extends SqlMapClientDaoSupport implements OrderDao {

    private SqlMapSequenceDao sequenceDao;

    public Order getOrder(long orderId) throws DataAccessException {
        Order order = (Order) getSqlMapClientTemplate().queryForObject("getOrder", new Long(orderId));
        if (order != null) {
            order.setLineItems(getSqlMapClientTemplate().queryForList("getLineItemsByOrderId", new Long(order.getOrderId())));
        }
        return order;
    }

    public List getOrdersByUsername(String username) throws DataAccessException {
        return getSqlMapClientTemplate().queryForList("getOrdersByUsername", username);
    }

    //insert the order and sets Order_ID PK from DB
    public void insertOrder(Order order) throws DataAccessException {
        order.setOrderId(this.sequenceDao.getNextId("ordernum"));
        getSqlMapClientTemplate().update("insertOrder", order);
        for (int i = 0; i < order.getLineItems().size(); i++) {
            LineItem lineItem = (LineItem) order.getLineItems().get(i);
            lineItem.setOrderId(order.getOrderId());
            getSqlMapClientTemplate().update("insertLineItem", lineItem);
        }
    }

    //this method is not part of the public API of the Interface
    //for now, only used in unit testing and returns the number of rows deleted
    public long removeOrder(long orderId) throws DataAccessException {
        return getSqlMapClientTemplate().delete("deleteOrder", new Long(orderId));

    }

    public void setSequenceDao(SqlMapSequenceDao sequenceDao) {
        this.sequenceDao = sequenceDao;
    }


}
