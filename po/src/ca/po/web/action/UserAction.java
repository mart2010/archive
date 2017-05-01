package ca.po.web.action;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate.HibernateOptimisticLockingFailureException;

import ca.po.Constants;
import ca.po.model.Lab;
import ca.po.model.User;
import ca.po.model.type.Role;
import ca.po.web.form.UserForm;


/**
 *
 * @struts.action path="/admin/listUser" parameter="list"
 * @struts.action path="/editUser" name="userForm" scope="request"
 *  	validate="false" parameter="edit"
 * @struts.action path="/saveUser" name="userForm" scope="request"
 *  	validate="true" input="/editUser.html" parameter="save"
 * @struts.action path="/admin/deleteUser" parameter="delete"
 * 
 * @struts.action-forward name="list" path="/WEB-INF/jsp/listUser.jsp"
 * @struts.action-forward name="edit" path="/WEB-INF/jsp/editUser.jsp"
 * @struts.action-forward name="listAction" path="/admin/listUser.html"
 *    
 */

public class UserAction extends BaseAction {
    

    public ActionForward list(ActionMapping mapping,ActionForm form,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {
        
        //store usersList in request scope
        request.setAttribute("userList",getUserManager().getUsers(false) );
        
        //return a forward to list
        return mapping.findForward("list");
    }
    /*
     * This action is called for :
     * 1- existing user (request parameter 'username'
     * 2- new user (assume a request parameter 'username' = NEW_ENTITY_REQUEST_VALUE
     * 3- save has validation error (no request param) but needs selUser in session 
     * Store selUser in Session scope as well as available labs/roles in request-scope for drop-down 
     */
    public ActionForward edit(ActionMapping mapping,ActionForm form,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {

        UserForm userForm = (UserForm) form;
        //Prepare look-up for drop-downs
        request.setAttribute("labs",getUserManager().getLabs());
        //should replace it with a static reference in the JSP...
        request.setAttribute("roles",Role.ROLES_LIST);
        
        String username = request.getParameter("username");
        User selUser;
        //scenario 3.. to change how to intercept this scenario condition
        if (username == null) {
            //update the read-only labs/roles 
            if (!getUser().isAdmin()){
                selUser = (User) request.getSession().getAttribute("selUser");   
                refreshLabRoleForm(userForm,selUser);
            }
            return mapping.findForward("edit");
        }
        
        ActionMessages messages = getMessages(request);
        //new user to be added
        if (username.equals(Constants.NEW_ENTITY_REQUEST_VALUE)) {
            selUser = new User();
        } else {
            //existing User to be edited
            selUser = (User) getUserManager().getUserByUsername(username,true,true);
            if (selUser == null){
                messages.add(ActionMessages.GLOBAL_MESSAGE, 
                    	new ActionMessage("user.doesNotExist"));
            	saveMessages(request, messages);
            	return mapping.findForward("listAction");
            }
            
            //copy properties to the form bean
            BeanUtils.copyProperties(userForm,selUser);
            userForm.setConfPassword(selUser.getPassword());
            refreshLabRoleForm(userForm,selUser);
        }
        //store the selUser in Session
        request.getSession().setAttribute("selUser",selUser );        
        //return a forward to edit
        return mapping.findForward("edit");
    }

    private void refreshLabRoleForm(UserForm userForm, User selUser){
        Lab lab;
        String[] userLabs =  new String[selUser.getLabs().size()];
        int i = 0;
        for (Iterator iter = selUser.getLabs().iterator(); iter.hasNext();i++) {
            lab = (Lab) iter.next();
            userLabs[i] = lab.getName();
        }
        userForm.setSelLabs(userLabs);
        Role role;
        String[] userRoles = new String[selUser.getRoles().size()];
        i = 0;
        for (Iterator iter = selUser.getRoles().iterator(); iter.hasNext();i++) {
            role = (Role) iter.next();
            userRoles[i] = role.getName();
        }
        userForm.setSelRoles(userRoles);
        
        
    }
    
    //This assumes the User being saved is stored in Session ("selUser")
    public ActionForward save(ActionMapping mapping,ActionForm form,
            		HttpServletRequest request,HttpServletResponse response) throws Exception {
        
        //if Cancel, clean-up session and forward to /listUser (if Admin) or /home
        if (isCancelled(request)) {
            request.getSession().removeAttribute("selUser");
            if (getUser().isAdmin()) return mapping.findForward("listAction");
            else return mapping.findForward("home");
        }
        
        UserForm userForm = (UserForm) form;
        User userSelected = (User) request.getSession().getAttribute("selUser");

        //store the changed properties & set the labs/roles
        BeanUtils.copyProperties(userSelected,(UserForm)form);
        
        //Admin can also edit labs/roles, so need to update these
        if (userIsAdmin()) {
            //remove Labs/Roles first
            if (!userSelected.getRoles().isEmpty()) userSelected.getRoles().clear();
            if (!userSelected.getLabs().isEmpty()) userSelected.getLabs().clear();
            
            //get Persistent labs instance 
            if (userForm.getSelLabs() != null){
                String[] labs = userForm.getSelLabs(); 
                for (int i=0; i < labs.length; i++ ){
                    Lab lab = getUserManager().getLabByName(labs[i]);
                    if (lab == null) 
                        lab = new Lab(labs[i],null);
                    userSelected.addLab(lab);
                }
            }
            String[] roles = userForm.getSelRoles(); 
            for (int i=0; i < roles.length; i++ ){
                userSelected.addRole(Role.GET_ROLE(roles[i]) );
            }
            //check for potential a new LabName added in the form
            if ((userForm.getNewLabName() != null) && 
                    (userForm.getNewLabName().length()>0 )) {
                userSelected.addLab(new Lab(userForm.getNewLabName(),null));
            }
        }
        ActionMessages messages = getMessages(request);
        //save user
        try {
            getUserManager().saveUser(userSelected, getUser().getName());
            messages.add(ActionMessages.GLOBAL_MESSAGE, 
                	new ActionMessage("user.saveSuccess"));
        	saveMessages(request, messages);
        }
        catch (DataIntegrityViolationException ex) {
            messages.add(ActionMessages.GLOBAL_MESSAGE, 
                	new ActionMessage("user.integrityError"));
        	saveMessages(request, messages);
        }
        catch (HibernateOptimisticLockingFailureException ex){
            messages.add(ActionMessages.GLOBAL_MESSAGE, 
                	new ActionMessage("user.optimisticLockError"));
        	saveMessages(request, messages);
        }
        
        //clear Session
        request.getSession().removeAttribute("selUser");
        if (getUser().isAdmin()) return mapping.findForward("listAction");
        else return mapping.findForward("home");

    }

    public ActionForward delete(ActionMapping mapping,ActionForm form,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {

        String username = request.getParameter("username");
        ActionMessages messages = getMessages(request);
        //delete already deleted user returns Success message (ok no impact)
        try{
            int nb = getUserManager().deleteUserByUsername(username);
            if (nb==1){
                messages.add(ActionMessages.GLOBAL_MESSAGE, 
                    	new ActionMessage("user.deleteSuccess"));
            } else if (nb==0){
                messages.add(ActionMessages.GLOBAL_MESSAGE, 
                    	new ActionMessage("user.alreadyDeleted"));
            } else assert false;
            saveMessages(request, messages);
        }
        catch (IllegalArgumentException ex) {
            //delete user linked to PO
            messages.add(ActionMessages.GLOBAL_MESSAGE, 
                    	new ActionMessage("user.linkedPODeleteError"));
            saveMessages(request, messages);
        }

        return mapping.findForward("listAction");
        
    }


}
