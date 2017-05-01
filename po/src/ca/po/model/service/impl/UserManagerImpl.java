package ca.po.model.service.impl;

import java.util.Date;
import java.util.List;

import ca.po.model.Lab;
import ca.po.model.User;
import ca.po.model.dao.UserDAO;
import ca.po.model.service.UserManager;

/**
 * @author MOUELLET
 *
 */

public class UserManagerImpl implements UserManager {

    private UserDAO userDAO;
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    
    public List getUsers(boolean fetchLabs) {
        return userDAO.getUsers(fetchLabs);
    }
     
    public User getUser(Long id, boolean fetchLabs, boolean fetchRoles) {
        return userDAO.getUser(id,fetchLabs,fetchRoles);
        
    }
     
    public User getUserByUsername(String username, boolean fetchLabs, boolean fetchRoles) {
        //by default fetch labs/roles
        return userDAO.getUserByUsername(username,fetchLabs,fetchRoles);
    }

     public void saveUser(User user, String editor) {
         if (user.isNew()) {
             user.setCreatedBy(editor);
             user.setUpdatedBy(editor);
             user.setUpdatedDate(user.getCreatedDate());
         } else {
             user.setUpdatedBy(editor);
             user.setUpdatedDate(new Date());
         }
         userDAO.saveUser(user);
     }
     
     
     public int deleteUserByUsername(String username) {
         User user = userDAO.getUserByUsername(username,false,false);
         
         if (user==null)
             return 0;
         
         if (userDAO.hasUserOrdered(user.getId()))
             throw new IllegalArgumentException("This user has already proposed some Order and cannot be deleted");
         
         userDAO.deleteUser(user);
         return 1;
     }

     public List getLabs() {
         return userDAO.getLabs();
     }
     
     public void saveLab(Lab newLab) {
         userDAO.saveLab(newLab);
     }
     
     public Lab getLabByName(String name) {
         return userDAO.getLabByName(name);
     }

     public Lab getLab(Long id) {
         return userDAO.getLab(id);
     }

     public void deleteLab(Lab lab) {
         userDAO.deleteLab(lab);
     }

    
}
