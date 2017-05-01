package ca.po.model.dao.hibernate;

import java.util.List;

import net.sf.acegisecurity.UserDetails;
import net.sf.acegisecurity.providers.dao.UsernameNotFoundException;
import net.sf.hibernate.Criteria;
import net.sf.hibernate.FetchMode;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.expression.Expression;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate.HibernateCallback;

import ca.po.model.Lab;
import ca.po.model.User;
import ca.po.model.dao.UserDAO;


/**
 *  The following deals with User manipulation Lab/Role automatically persisted (cascade save-update)
 *  
 */
public class UserDAOHibernateImpl extends BaseDAOHibernate	implements UserDAO {

    
    /*
     * AuthenticationDAO for acegi Authentication Manager
     * 
     */
    public UserDetails loadUserByUsername(String username) 
    				throws UsernameNotFoundException, DataAccessException {
        //here we need both labs/roles
        User user = this.getUserByUsername(username,true,true);
        if (user == null)
            throw new UsernameNotFoundException("Username not found!");
        
        //Authorities (Roles) should be fetched
        return (UserDetails) user;
        
    }

    
    public List getUsers(final boolean fetchLabs) throws DataAccessException {
        List users = (List) getHibernateTemplate().execute(
                new HibernateCallback(){
                        public Object doInHibernate(Session session) throws HibernateException {
                            String q;
                            if (!fetchLabs) q = "from User u ";
                            else q = "from User u left join fetch u.labs";
                            
                            Query query = session.createQuery(q);
                            return query.list();
                        }
                    });
        return users;
    }

    //restrict to only one eager fetch for one-to-many relationship (so only roles are set with FetchMode)
    public User getUser(final Long id, final boolean fetchLabs, final boolean fetchRoles) throws DataAccessException {
        User user = (User) getHibernateTemplate().execute(
                new HibernateCallback(){
                    public Object doInHibernate(Session session) throws HibernateException {
                        Criteria criteria = session.createCriteria(User.class);
                        criteria.add(Expression.eq("id", id));
                        if (fetchRoles){
                            criteria.setFetchMode("roles",FetchMode.EAGER);    
                        }
                        User u = (User) criteria.uniqueResult();
                        //force to lazy initialize the labs, this will force a second DB access
                        if (fetchLabs){
                            u.getLabs().size();
                        }
                        return u;
                    }
                });

        return user;
    }
    
    //restrict to only one eager fetch for one-to-many relationship (so only roles are set with FetchMode)
    public User getUserByUsername(final String username, final boolean fetchLabs, final boolean fetchRoles) throws DataAccessException {
        
        User user = (User) getHibernateTemplate().execute(
                new HibernateCallback(){
                    public Object doInHibernate(Session session) throws HibernateException {
                        Criteria criteria = session.createCriteria(User.class);
                        criteria.add(Expression.eq("name", username));
                        if (fetchRoles){
                            criteria.setFetchMode("roles",FetchMode.EAGER);    
                        }
                        User u = (User) criteria.uniqueResult();
                        //force to lazy initialize the labs, this will force a second DB access
                        if (fetchLabs){
                            u.getLabs().size();
                        }
                        return u;
                    }
                });

        return user;
    }
    
    /*
     * Assumes that Lab instance in User that are persisted in 
     * DB have valid id (i.e. persistent state), otherwise will throw Exception (duplicated name ) 
     */
    public void saveUser(User user) throws DataAccessException {
         getHibernateTemplate().saveOrUpdate(user);
    }
    //does not cascade delete lab
    public void deleteUser(User user) throws DataAccessException {
        getHibernateTemplate().delete(user);
    }
    public void deleteUserByUsername(String username) throws DataAccessException {
        getHibernateTemplate().delete("from User u where u.name=?",username,Hibernate.STRING);
    }

    public List getLabs() throws DataAccessException {
        return getHibernateTemplate().loadAll(Lab.class);
    }
    public void saveLab(Lab newLab) throws DataAccessException {
        getHibernateTemplate().saveOrUpdate(newLab);
    }
    public Lab getLabByName(String name) throws DataAccessException {
        List labs = getHibernateTemplate().find(
                "from Lab l where l.name=?", name);
        if (labs.size() == 0) return null;
        else {
            return (Lab) labs.get(0);
        }
    }
    public void deleteLab(Lab lab) throws DataAccessException {
        getHibernateTemplate().delete(lab);
    }

    public Lab getLab(Long id) throws DataAccessException {
        return (Lab) getHibernateTemplate().get(Lab.class,id);
    }

    //check whether this user has ordered PO
    public boolean hasUserOrdered(final Long userId) throws DataAccessException {
        
        Integer count = (Integer) getHibernateTemplate().execute(
                new HibernateCallback() {
                    public Object doInHibernate(Session session) throws HibernateException {
                        return session.createQuery(
                                "select count(*) " +
                                "from PurchaseOrder po join po.orderByUser user " +
                                "where po.orderByUser.id=?")
                        		  .setLong(0,userId.longValue())
                        		  .uniqueResult();
                    
                    }
                });
        return (count.intValue() == 0 ? false : true);       
    }

    

}
