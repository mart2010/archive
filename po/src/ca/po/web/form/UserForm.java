package ca.po.web.form;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import net.sf.acegisecurity.context.security.SecureContextUtils;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import ca.po.model.User;


/**
 * @struts.form name="userForm"
 * 
 */
public class UserForm extends BaseForm implements Serializable {

    
    //name is the actual username    
    private String name;
    private String password;
    private String confPassword;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String newLabName;
    
    private String[] selLabs;
    private String[] selRoles;
    
    public String getName() {
        return name;
    }
    /**
     * @struts.validator type="required"
     * @struts.validator-args arg0resource="user.username"
     * @struts.validator type="minlength" arg1value="${var:minlength}"
     * @struts.validator-var name="minlength" value="${beanNameMinLength}"
     * 
     */
    public void setName(String username) {
        this.name = username;
    }
    public String getPassword() {
        return password;
    }
    /**
     * @struts.validator type="required" 
     * @struts.validator-args arg0resource="user.password"
     */
    public void setPassword(String password) {
        this.password = password;
    }
    public String getConfPassword() {
        return confPassword;
    }
    /**
     * @struts.validator type="required" 
     * @struts.validator-args arg0resource="user.confPassword"
     * @struts.validator type="validwhen" msgkey="user.errorPassmatch"
     * @struts.validator-var name="test" value="(*this* == password)"
     * 
     */
    public void setConfPassword(String confirmPassword) {
        this.confPassword = confirmPassword;
    }
    public String getEmail() {
        return email;
    }
    /**
     * @struts.validator type="required"
     * @struts.validator-args arg0resource="user.email"
     * @struts.validator type="email"
     */
    public void setEmail(String email) {
        this.email = email;
    }
    public String getFirstName() {
        return firstName;
    }
    /**
     * @struts.validator type="required" 
     * @struts.validator-args arg0resource="user.firstName"
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    /**
     * @struts.validator type="required" 
     * @struts.validator-args arg0resource="user.lastName"
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String[] getSelLabs() {
        return selLabs;
    }
    /**
     * Now done in Validate()
     * struts.validator type="required" msgkey="user.errorLabs"
     */
    public void setSelLabs(String[] selLabs) {
        this.selLabs = selLabs;
    }
    public String[] getSelRoles() {
        return selRoles;
    }
    /**
     * Now done in Validate()
     * struts.validator type="required" msgkey="user.errorRoles"
     */
    public void setSelRoles(String[] selRoles) {
        this.selRoles = selRoles;
    }
    public String getNewLabName() {
        return newLabName;
    }
    /**
     *  @struts.validator type="minlength" 
     *  @struts.validator-args arg0resource="lab.name"
     * 		arg1value="${beanNameMinLength}"
     *  @struts.validator-var name="minlength" value="${beanNameMinLength2}"
     */
    public void setNewLabName(String newLabName) {
        this.newLabName = newLabName;
    }
    
    //validate Labs/Roles are populated but only for admin user
    public ActionErrors validate(ActionMapping mapping, 
            HttpServletRequest request) {

        ActionErrors errors = super.validate(mapping, request);
        
        if (((User) SecureContextUtils.getSecureContext().
        					getAuthentication().getPrincipal()).isAdmin()) {
	        if ((this.selLabs==null || this.selLabs.length==0) && 
	                (this.newLabName==null || this.newLabName.length()==0) )
	            errors.add("selLabs", new ActionMessage("user.errorLabs"));
	
	        if ((this.selRoles == null) || (this.selRoles.length==0))
	            errors.add("selRoles", new ActionMessage("user.errorRoles"));
        }
        
        return errors;
        
    }

    
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        
    }
    
}
