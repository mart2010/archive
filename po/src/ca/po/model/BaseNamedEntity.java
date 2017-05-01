package ca.po.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author Martin
 *
 * Add the name, desc properties nearly 
 * Provides common ToString, equals, and hashCode for all
 * namedEntity having property Name used as Business Key
 * 
 */
public abstract class BaseNamedEntity extends BaseEntity {
    //natural key not used as PK, but used in equals, hashcode...
    private String name;
    private String desc;

    public BaseNamedEntity(){
    }
    //convenient constructor to use by subclass
    public BaseNamedEntity(String name, String desc){
        this.name = name;
        this.desc = desc;
    }
    /**
     * @hibernate.property unique="true" not-null="true" length="50"
     */
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @hibernate.property length="100" 
     * column="description" 
     */
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }

    //Common methods used for NamedEntity
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
        		.append("name", this.getName()).toString();
    }
   
    
}
