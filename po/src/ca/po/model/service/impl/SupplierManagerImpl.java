package ca.po.model.service.impl;

import java.util.List;

import ca.po.model.Supplier;
import ca.po.model.dao.SupplierDAO;
import ca.po.model.service.SupplierManager;


/**
 * @author MOUELLET
 *
 */
public class SupplierManagerImpl implements SupplierManager {

    private SupplierDAO supplierDAO;
    public void setSupplierDAO(SupplierDAO supDAO) {
        this.supplierDAO = supDAO;
    }

    
    public void saveSupplier(Supplier supplier, String username){
        if (supplier.getId() == null)
            supplier.setCreatedBy(username);
        
        supplierDAO.saveSupplier(supplier);
    }

    public List getSuppliers() {
        return supplierDAO.getSuppliers();
    }
    
    public Supplier getSupplier(Long id, boolean fetchItemSupplies) {
        return supplierDAO.getSupplier(id,fetchItemSupplies);
    }

    public int deleteSupplier(Long supId) {
        Supplier sup = supplierDAO.getSupplier(supId,true);
        if (sup == null)
            return 0;
        
        if (!sup.getItemSupplies().isEmpty())
            throw new IllegalArgumentException("Supplier cannot have items to be deleted");
        supplierDAO.deleteSupplier(sup);
        return 1;
    }

    
}
