package ca.po.model.dao.hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;

/**
 * @author Martin
 *
 * Parent Class for DAOHibernateImpl providing
 * convenient common methods for getting/saving object 
 * from the DB
 */
public abstract class BaseDAOHibernate extends HibernateDaoSupport {
    protected transient final Log log = LogFactory.getLog(getClass());

    //assumes the Object has an Id of type Long
    public Object getObject(Long objId, Object obj) {
        try {
            obj = getHibernateTemplate().get(obj.getClass(), objId);
        } catch (Exception e) {
            throw new ObjectRetrievalFailureException(obj.getClass(), objId);
        }
        return obj;
    }
    
    public Object saveObject(Object obj) {
        getHibernateTemplate().saveOrUpdateCopy(obj);
        return obj;
    }

    public void removeObject(Object obj) {
        if (obj != null){
           getHibernateTemplate().delete(obj);
        }
    }
    
    
}
