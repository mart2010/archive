package ca.po.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author MOUELLET
 * TODO 
 * 
 * @hibernate.class table="po_supplier"
 * @hibernate.cache usage="nonstrict-read-write"
 */
public class Supplier extends BaseNamedEntity implements Serializable {
    
    private Date createdDate = new Date();
    private String createdBy;
    private String salesRepName;
    private String salesRepContact;
    private Set itemSupplies = new HashSet();
    
    

    public Supplier() {
    }

    public Supplier(String name, String desc, String salesRepName, 
            String salesRepContact, String username){
        super(name,desc);
        this.salesRepName = salesRepName;
        this.salesRepContact = salesRepContact;
        this.createdBy = username;
    }
    
    
    /**
     * @hibernate.property
     */
    public Date getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Date date) {
        this.createdDate = date;
    }
    /**
     * @hibernate.property length="50"
     */
    public String getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    /**
     * @hibernate.property column="salesrep_contact" length="100"
     */
    public String getSalesRepContact() {
        return salesRepContact;
    }
    public void setSalesRepContact(String salesRepContact) {
        this.salesRepContact = salesRepContact;
    }
    /**
     * @hibernate.property column="salesrep_name" length="50"
     */
    public String getSalesRepName() {
        return salesRepName;
    }
    public void setSalesRepName(String salesRepName) {
        this.salesRepName = salesRepName;
    }
    /**
     * We create Item and assign Supplier, not the other way around (cascade="none")
     * @hibernate.set inverse="true" cascade="none" lazy="true"
     * @hibernate.collection-key column="supplier_id"
     * @hibernate.collection-one-to-many class="ca.po.model.ItemSupplied"
     *  
     */
    public Set getItemSupplies() {
        return itemSupplies;
    }
    private void setItemSupplies(Set items) {
        this.itemSupplies = items;
    }

  
    public boolean equals(Object object) {
        if (!(object instanceof Supplier)){
            return false;
        }
        Supplier rhs = (Supplier) object;
        return new EqualsBuilder().append(this.getName(), rhs.getName()).isEquals();
    }
    public int hashCode() {
        return new HashCodeBuilder(-1583149161, -910556983).append(this.getName()).toHashCode();
    }

    
    
}
