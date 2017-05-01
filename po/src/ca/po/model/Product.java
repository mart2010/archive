package ca.po.model;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *  @author MOUELLET
 *   
 *  @hibernate.class table="po_product" lazy="true"
 *  @hibernate.cache usage="nonstrict-read-write"
 */
public class Product extends BaseNamedEntity implements Serializable {
    
    private Category category;
    private Set items = new LinkedHashSet();
    private Date createdDate = new Date();
    private String createdBy;
    
    public Product(){
    }
    public Product(String name, String desc){
        super(name, desc);
    }
    
    //for now, only allow defining if Category is null (cannot re-assign)  
    // and no equivalent item already exist in Product
    public void defineCategory(Category category){
        if (this.category == null) {
            if (category.getProducts().add(this)) {
            	this.category = category;
            } else throw new IllegalStateException("Product already existing inside Category");        
        } else {
            //decide what to do for re-allocating category?
            throw new IllegalArgumentException("Product is already Categorized, cannot re-define the Category");
        }
    }

    public void removeItem(Item item){
        if (this.equals(item.getProduct())){
            if (this.items.remove(item)){
                item.setProduct(null);
            }
            else throw new IllegalStateException("Item does not exist inside this Product"); 
        } else //item not linked to this Product
            throw new IllegalArgumentException("Cannot delete an Item that is not linked to this Product");
    }

    
    
    //for now, only add Item with no Product already defined
    public void addItem(Item item){
        if (item.getProduct()== null){
            if (this.items.add(item)){
                item.setProduct(this);
            }
            else throw new IllegalStateException("Adding item already exisintg inside Product"); 
        } else 
            throw new IllegalArgumentException("Item already linked to existing Product, cannot re-assign this");
	}
    
    /**
     * @hibernate.set inverse="true" cascade="none" order-by="name asc" lazy="true"
     * @hibernate.collection-key column="product_id"
     * @hibernate.collection-one-to-many class="ca.po.model.Item"
     * @hibernate.collection-cache usage="nonstrict-read-write"
     */
    public Set getItems() {
        return this.items;
    }
    public void setItems(Set items) {
        this.items = items;
    }
    
    /**
     * @hibernate.many-to-one class="ca.po.model.Category" column="category_id"
     * 		cascade="none" not-null="true"  foreign-key="fk_product_category"
     */
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
 
    /**
     * Store the username
     * @hibernate.property length="50"
     */
    public String getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(String createdByUser) {
        this.createdBy = createdByUser;
    }
    
    /**
     * @hibernate.property
     */
    public Date getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Date createdDateTime) {
        this.createdDate = createdDateTime;
    }

    public boolean equals(Object object) {
        if (!(object instanceof Product)) {
            return false;
        }
        Product rhs = (Product) object;
        return new EqualsBuilder().append(this.getName(), rhs.getName()).isEquals();
    }
    public int hashCode() {
        return new HashCodeBuilder(-1583149161, -910556983).append(this.getName()).toHashCode();
    }
    
}
