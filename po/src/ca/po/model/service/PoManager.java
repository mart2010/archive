package ca.po.model.service;

import java.util.Collection;
import java.util.List;

import ca.po.model.PurchaseOrder;
import ca.po.model.User;
import ca.po.model.dao.PoSearchCriteria;
import ca.po.model.type.PoStatusCode;


public interface PoManager {

    PurchaseOrder getPO(Long id, boolean fetchLineItems);
    PurchaseOrder getPOWithItemsSupplied(Long id);

    
    List getPOsByUser(User user, boolean fetchLineItems);
    
    List getPOsByStatusCode(PoStatusCode code, boolean fetchLineItems);
    List getReceivedPOsByCriteria(PoSearchCriteria criteria);
    
    void saveProposedPO(PurchaseOrder po, User user, Long labId);
    void saveValidatedPOs(Collection purchaseOrders, User admin);

    void saveFollowUpPO(PurchaseOrder po, PoStatusCode newStatus, User admin);
    void saveCancelledPO(PurchaseOrder po, User user);

    
    
    
    
    
}