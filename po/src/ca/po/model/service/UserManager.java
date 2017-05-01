package ca.po.model.service;

import java.util.List;

import ca.po.model.Lab;
import ca.po.model.User;


public interface UserManager {

    List getUsers(boolean fetchLabs);
    
    User getUser(Long id, boolean fetchLabs, boolean fetchRoles);
    
    User getUserByUsername(String username, boolean fetchLabs, boolean fetchRoles);

    void saveUser(User newUser, String editor);

    int deleteUserByUsername(String username);
    
    List getLabs();

    void saveLab(Lab newLab);

    Lab getLabByName(String name);

    Lab getLab(Long id);
    
    void deleteLab(Lab lab);

    
}