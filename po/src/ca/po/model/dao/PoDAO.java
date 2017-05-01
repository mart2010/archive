package ca.po.model.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import ca.po.model.PurchaseOrder;
import ca.po.model.type.PoStatusCode;


public interface PoDAO {

    PurchaseOrder getPO(Long id, boolean fetchLineItems) throws DataAccessException;
    
    PurchaseOrder getPOWithItemsSupplied(Long id) throws DataAccessException;
    
    void savePO(PurchaseOrder po) throws DataAccessException;

    public List getPOsByUserId(Long userId, boolean fetchLineItems) throws DataAccessException; 
 
    public List getPOsByStatus(PoStatusCode code, boolean fetchLineItems) throws DataAccessException ;

    public List getPOSByCriteria(PoSearchCriteria criteria, PoStatusCode code) throws DataAccessException ;
    
}