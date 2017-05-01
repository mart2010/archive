package ca.po.model.dao;

import java.util.List;

import net.sf.acegisecurity.UserDetails;
import net.sf.acegisecurity.providers.dao.AuthenticationDao;
import net.sf.acegisecurity.providers.dao.UsernameNotFoundException;

import org.springframework.dao.DataAccessException;

import ca.po.model.Lab;
import ca.po.model.User;

/**
 * @author MOUELLET
 *
 * Extends AuthenticationDao to be used by acegi security Authentication Manager
 */
public interface UserDAO extends AuthenticationDao {

    
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException; 
    
    List getUsers(boolean fetchLabs) throws DataAccessException;

    User getUser(Long id, boolean fetchLabs, boolean fetchRoles) throws DataAccessException;

    User getUserByUsername(String username, boolean fetchLabs, boolean fetchRoles) throws DataAccessException;

    /*
     * Assumes that Lab instance in User that are persisted in 
     * DB have valid id (i.e. persistent state), otherwise will throw Exception (duplicated name ) 
     */
    void saveUser(User user) throws DataAccessException;

    void deleteUser(User user) throws DataAccessException;
    void deleteUserByUsername(String username) throws DataAccessException;
    boolean hasUserOrdered(final Long userId) throws DataAccessException;

    List getLabs() throws DataAccessException;

    void saveLab(Lab newLab) throws DataAccessException;

    Lab getLabByName(String name) throws DataAccessException;

    Lab getLab(Long id) throws DataAccessException;
    
    void deleteLab(Lab lab) throws DataAccessException;
    
}