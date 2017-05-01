package ca.po.model.service;

import java.util.List;

import ca.po.model.Supplier;


/**
 * @author MOUELLET
 *
 */
public interface SupplierManager {

    void saveSupplier(Supplier supplier, String username);

    List getSuppliers();

    Supplier getSupplier(Long id, boolean fetchItemSupplies);

    int deleteSupplier(Long supId);
   
   
}