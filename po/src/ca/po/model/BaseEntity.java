package ca.po.model;

import java.io.Serializable;

/**
 * @author Martin
 *  This is the root for all Entity requiring an Id
 *  Also used to enforce overriding of toString, equals and hashCode
 * 
 */
public abstract class BaseEntity implements Serializable {
    //corresponds to the surrogate key
    private Long id;

    /**
	 * @hibernate.id generator-class="native" column="id" 
     */
    public Long getId() {
        return this.id;
    }

    /* only Hibernate should set ID, but cannot set to Private, 
     * since subclasses need visibility on this Setter
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    public boolean isNew(){
        return (this.id== null);
    }
    
    public abstract String toString();
    public abstract boolean equals(Object o);
    public abstract int hashCode();

}


