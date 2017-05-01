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
 *  @hibernate.class table="po_category" lazy="true"
 *  @hibernate.cache usage="nonstrict-read-write"
 */
public class Category extends BaseNamedEntity implements Serializable {
    
    private Date createdDate = new Date();
    //to keep alphabetical order
    private Set products = new LinkedHashSet();
    private String createdBy;
    
    public Category(){
    }
    public Category(String name, String desc){
        super(name, desc);
    }
    
    //only add Product with no Category already defined
    public void addProduct(Product prod){
        if (prod.getCategory()== null){
            if (products.add(prod)){
                //use the setter and not define to avoid the two-way checks
                prod.setCategory(this);
            }
            else throw new IllegalStateException("Product already existing inside Category"); 
        } else //category in product already defined..for now forbids re-categorization
            throw new IllegalArgumentException("Product is already Categorized, cannot re-define the Category");
    }

    public void removeProduct(Product prod){
        if (this.equals(prod.getCategory())){
            if (this.products.remove(prod)){
                prod.setCategory(null);
            }
            else throw new IllegalStateException("Product does not exist inside this Category"); 
        } else //product not linked to this category
            throw new IllegalArgumentException("Cannot delete a Product that is not linked to this category");
    }
    
    /**
     * 
     * @hibernate.set inverse="true" cascade="none" order-by="name asc" lazy="true"
     * @hibernate.collection-key column="category_id"
     * @hibernate.collection-one-to-many class="ca.po.model.Product"
     * @hibernate.collection-cache usage="nonstrict-read-write"
     */
    public Set getProducts() {
        return this.products;
    }
    public void setProducts(Set items) {
        this.products = items;
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
    private void setCreatedDate(Date createdDateTime) {
        this.createdDate = createdDateTime;
    }
    
    public boolean equals(Object object) {
        if (!(object instanceof Category)) {
            return false;
        }
        Category rhs = (Category) object;
        return new EqualsBuilder().append(this.getName(), rhs.getName()).isEquals();
    }
    public int hashCode() {
        return new HashCodeBuilder(-1583149161, -910556983).append(this.getName()).toHashCode();
    }

    
}
