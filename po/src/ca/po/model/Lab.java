package ca.po.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author MOUELLET
 *  This represents the lab(s) a user is associated with 
 *  and by extension which lab the purchaseOrder is done for.  
 * 
 * @hibernate.class table="po_lab" mutable="false"
 * @hibernate.cache usage="read-only"
 */
public class Lab extends BaseNamedEntity implements Serializable {
  
    public Lab() {
    }
    public Lab(String name, String desc){
        super(name,desc);
    }
    
    public String toString(){
        return this.getName();
    }
    
    public boolean equals(Object object) {
        if (!(object instanceof Lab)) {
            return false;
        }
        Lab rhs = (Lab) object;
        return new EqualsBuilder().append(this.getName(), rhs.getName()).isEquals();
    }
    public int hashCode() {
        return new HashCodeBuilder(-1583149161, -910556983).append(this.getName()).toHashCode();
    }


}
