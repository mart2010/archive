package ca.po.model.dao.hibernate;

import java.util.List;

import net.sf.hibernate.Criteria;
import net.sf.hibernate.FetchMode;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.expression.Expression;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate.HibernateCallback;

import ca.po.model.Supplier;
import ca.po.model.dao.SupplierDAO;


public class SupplierDAOHibernateImpl extends BaseDAOHibernate implements SupplierDAO {
    
    /****************************************************************
     * 
     * The following deals with Supplier, Project
     * 
     ****************************************************************/
    public void saveSupplier(Supplier supplier) throws DataAccessException {
        getHibernateTemplate().saveOrUpdate(supplier);
    }
    public List getSuppliers() throws DataAccessException {
        return getHibernateTemplate().loadAll(Supplier.class);
    }
    public Supplier getSupplier(final Long id, boolean fetchItemSupplies) throws DataAccessException {
        if (!fetchItemSupplies)
            return (Supplier) getHibernateTemplate().get(Supplier.class,id);

        Supplier sup = (Supplier) getHibernateTemplate().execute(
                new HibernateCallback() {
                    public Object doInHibernate(Session session) throws HibernateException {
                        Criteria criteria = session.createCriteria(Supplier.class);
                        criteria.add(Expression.eq("id", id));
                        criteria.setFetchMode("itemSupplies",FetchMode.EAGER);
                        return criteria.uniqueResult();
                    }
                });
        return sup;
    }
        

    public void deleteSupplier(Supplier sup) throws DataAccessException {
        getHibernateTemplate().delete(sup);
    }


}
