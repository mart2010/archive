package ca.po.model.dao.hibernate;


import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.Criteria;
import net.sf.hibernate.FetchMode;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.expression.Expression;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate.HibernateCallback;

import ca.po.model.PoLineItem;
import ca.po.model.PurchaseOrder;
import ca.po.model.dao.PoDAO;
import ca.po.model.dao.PoSearchCriteria;
import ca.po.model.dao.hibernate.usertype.PoStatusCodeUserType;
import ca.po.model.type.PoStatusCode;


/****************************************************************
 * 
 * The following deals with Purchase Order, creation, track history
 * 
 ****************************************************************/

public class PoDAOHibernateImpl extends BaseDAOHibernate implements PoDAO {

    
    /*
     * lab and orderBy are always fetched by default (many to one, with lazy=false on the one)
     *  
     */
    public PurchaseOrder getPO(final Long id, final boolean fetchLineItems) throws DataAccessException {

        PurchaseOrder po = (PurchaseOrder) getHibernateTemplate().execute(
                new HibernateCallback() {
                    public Object doInHibernate(Session session) throws HibernateException {
                        Criteria criteria = session.createCriteria(PurchaseOrder.class);
                        criteria.add(Expression.eq("id", id));
                        if (fetchLineItems)
                            criteria.setFetchMode("poLineItems",FetchMode.EAGER);
       
                        return criteria.uniqueResult();
                    }
                });
        
        return po;
        
    }

    /* To construct the complete po object graph (with lineitem, item, itemSuplied)
     * I need to force a lazy intialization which triggers a second DB access
     * Performance warning if po has many lineitems...
     */
    public PurchaseOrder getPOWithItemsSupplied(final Long id) throws DataAccessException {

        PurchaseOrder po = (PurchaseOrder) getHibernateTemplate().execute(
                new HibernateCallback() {
                    public Object doInHibernate(Session session) throws HibernateException {
                        Criteria criteria = session.createCriteria(PurchaseOrder.class);
                        criteria.add(Expression.eq("id", id));
                        criteria.setFetchMode("poLineItems",FetchMode.EAGER);
                        PurchaseOrder p = (PurchaseOrder) criteria.uniqueResult();
                        
                        //go fetch the itemSupplies (and supplier by extension)
                        for (Iterator iter = p.getPoLineItems().iterator(); iter.hasNext();){
                            PoLineItem lineItem = (PoLineItem) iter.next();
                            lineItem.getItem().getItemSupplies().size();
                        }
                        return p;
                    }
                });
        
        return po;


    }

    
    
    public void savePO(PurchaseOrder po) throws DataAccessException {
        getHibernateTemplate().saveOrUpdate(po);
         
    }

    public List getPOsByUserId(Long userId, boolean fetchLineItems) throws DataAccessException  {
        String q;
        if (fetchLineItems){
            q = "from PurchaseOrder p left join fetch p.poLineItems where p.orderByUser.id=?";
        } else {
            q = "from PurchaseOrder p where p.orderByUser.id=?";
        }
        
        List pos = getHibernateTemplate().find(q, userId);
        return pos;
    }
    
    /*
     * Careful with eager fetching LineItems, since each POs will have lineItems
     */
    public List getPOsByStatus(final PoStatusCode code, final boolean fetchLineItems) throws DataAccessException  {
        List pos = (List) getHibernateTemplate().execute(
                new HibernateCallback() {
                    public Object doInHibernate(Session session) throws HibernateException {
	                  Query q;
	                  if (!fetchLineItems) {
	                      q = session.createQuery("from PurchaseOrder p where p.currentStatusChange=:code");
	                  }	else {
	                      q = session.createQuery("from PurchaseOrder p left join fetch p.poLineItems where p.currentStatusChange=:code");
	                  }
	                  q.setParameter("code",code,Hibernate.custom(PoStatusCodeUserType.class));
	                  
                	  return q.list();
	                }
	            });
        
        return pos;
    }
    
    
    public List getPOSByCriteria(final PoSearchCriteria searchCriteria, final PoStatusCode code) throws DataAccessException {
        
        List pos = (List) getHibernateTemplate().execute(
                new HibernateCallback() {
                    public Object doInHibernate(Session session) throws HibernateException {

                        Criteria poCriteria = session.createCriteria(PurchaseOrder.class);
                        poCriteria.add(Expression.eq("currentStatusChange",code));
                        
                        if (searchCriteria.getUserId()!= null)
                            poCriteria.add(Expression.eq("orderByUser.id", searchCriteria.getUserId()));
                        if (searchCriteria.getLabId() != null)
                            poCriteria.add(Expression.eq("lab.id",searchCriteria.getLabId()));
                        if ((searchCriteria.getFromDate()!= null) && (searchCriteria.getToDate() != null)){
                            poCriteria.add(Expression.between("currentStatusChangeDate",searchCriteria.getFromDate(),searchCriteria.getToDate()));
                        } else if (searchCriteria.getFromDate()!= null) {
                            poCriteria.add(Expression.gt("currentStatusChangeDate",searchCriteria.getFromDate()));
                        } else if (searchCriteria.getToDate() != null) {
                            poCriteria.add(Expression.lt("currentStatusChangeDate",searchCriteria.getToDate()));
                        }
                        //check paging, pageBegin is mandatory in this case
                        if (searchCriteria.getPageBegin()!=null){
                            poCriteria.setFirstResult(searchCriteria.getPageBegin().intValue());
                            poCriteria.setMaxResults(searchCriteria.getPageSize().intValue());
                        }
                            
                        return poCriteria.list();
                    }
                });
        
        return pos;
        
    }

    


}
