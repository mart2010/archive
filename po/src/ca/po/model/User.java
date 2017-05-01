package ca.po.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.sf.acegisecurity.GrantedAuthority;
import net.sf.acegisecurity.UserDetails;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import ca.po.model.type.Role;

/**
 * @author MOUELLET
 *  
 * @hibernate.class table="po_user"
 *  
 **/
public class User extends BaseNamedEntity implements Serializable, UserDetails {

    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private int version;
    //all users are set by default to 'true' 
    private boolean enabled = true;
    private Set roles= new HashSet();
    private Set labs= new HashSet();
    private Date createdDate = new Date();
    private String createdBy;
    private Date updatedDate;
    private String updatedBy;
    

    public User() {
    }
    //Convenient method with single role/lab 
    public User(String username, String fname, String lname, 
            Role role, Lab lab, String email, String password) {
       super(username,null);
       this.firstName = fname;
       this.lastName = lname;
       this.password = password;
       this.email = email;
       if (role != null) this.addRole(role);
       if (lab != null) this.addLab(lab);
    }

    
    public boolean isAdmin(){
        return (this.roles.contains(Role.PO_ADMIN));
    }
    
    public boolean addRole(Role role){
        return this.roles.add(role);
    }
    public boolean addLab(Lab lab){
        return this.labs.add(lab);
    }
    
    
    /**
     * @hibernate.property not-null="true" length="30"
     */
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * @hibernate.property not-null="true" length="80"
     */
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * @hibernate.property not-null="true" length="50"
     */
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    /**
     * @hibernate.property not-null="true" length="50"
     */
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    /**
     * @hibernate.property length="50"
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    /**
     * @hibernate.version column="version" 
     */
    public int getVersion() {
        return version;
    }
    public void setVersion(int version) {
        this.version = version;
    }
    /**
     * @hibernate.property
     */
    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    /**
     * @hibernate.set table="po_user_role" lazy="true"
     * @hibernate.collection-key column="user_id" 
     * @hibernate.collection-element column="role"
     * 		type="ca.po.model.dao.hibernate.usertype.RoleUserType"
     *  
     */
    public Set getRoles() {
        return this.roles;
    }
    public void setRoles(Set roles) {
        this.roles = roles;
    }
    /**
     * @hibernate.set table="po_user_lab" 
     * 			cascade="save-update" lazy="true"
     * @hibernate.collection-key column="user_id" 
     * @hibernate.collection-many-to-many class="ca.po.model.Lab"
     *          column="lab_id"
     */
    public Set getLabs() {
        return this.labs;
    }
    public void setLabs(Set labs) {
        this.labs = labs;
    }
    
    /**
     * @hibernate.property
     */
    public Date getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Date lastUpdatedDatetime) {
        this.createdDate = lastUpdatedDatetime;
    }
    /**
     * Store the username of the creator
     * @hibernate.property length="50"
     */
    public String getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(String createdByUser) {
        this.createdBy = createdByUser;
    }
    /**
     * Store the username of the updater
     * @hibernate.property length="50"
     */
    public String getUpdatedBy() {
        return updatedBy;
    }
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
    /**
     * @hibernate.property
     */
    public Date getUpdatedDate() {
        return updatedDate;
    }
    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
    
    /*
     * Next are Acegi specifics (requires username, password and Authority 
     * are always not null). This will be respected through DB constraint
     */
     public GrantedAuthority[] getAuthorities() {
        
        GrantedAuthority[] grantedAuthority = new GrantedAuthority[this.roles.size()];
        int i = 0;
        
        for(Iterator iter = this.roles.iterator(); iter.hasNext(); i++) {
            final Role role = (Role) iter.next();
            grantedAuthority[i] = new GrantedAuthority() {
                public String getAuthority() {
                    return role.getName();
                }
            };
        }
        return grantedAuthority;
    }
    
    public String getUsername() {
        return this.getName();
    }

    public boolean isAccountNonExpired() {
        //not supported in the web app
        return true;
    }
    public boolean isAccountNonLocked() {
        //not supported in the web app
        return true;
    }
    public boolean isCredentialsNonExpired() {
        //not supported in the web app
        return true;
    }

    
    public String toString() {
        return new ToStringBuilder(this).append("lastName", this.lastName)
        								.append("firstName", this.firstName)
        								.append("username", this.getName())
        								.append("email", this.email)
        								.append("phoneNumber", this.phoneNumber)
        								.append("email", this.email)
        								.toString();
    }

    public boolean equals(Object object) {
        if (!(object instanceof User)) {
            return false;
        }
        User rhs = (User) object;
        return new EqualsBuilder().append(this.getName(), rhs.getName()).isEquals();
    }
    public int hashCode() {
        return new HashCodeBuilder(-1583149161, -910556983).append(this.getName()).toHashCode();
    }
    
   
}
