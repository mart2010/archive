package ca.po.model.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import ca.po.model.Supplier;


/**
 * @author MOUELLET
 *
 */
public interface SupplierDAO {

    void saveSupplier(Supplier supplier) throws DataAccessException;

    List getSuppliers() throws DataAccessException;

    Supplier getSupplier(Long id, boolean fetchItemSupplies) throws DataAccessException;

    void deleteSupplier(Supplier sup) throws DataAccessException;
    
    
}