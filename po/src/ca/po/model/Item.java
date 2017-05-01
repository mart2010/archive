package ca.po.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import ca.po.model.type.Money;
/**
 * 2 diff items cannot have same name, itemNumber, manufacturer, distributor and format
 * Unique constraint should be enabled in DB
 * 
 * Item can be edited (updated not deleted..) only by Admin and the original creator
 * 
 * @hibernate.class table="po_item"
 * @hibernate.cache usage="nonstrict-read-write" 
 */
public class Item extends BaseEntity implements Serializable {
    
    private String name;
    private String desc;
    private String itemNumber;
    private String manufacturer;
    private String distributor;
    private String format;
    private Money listPrice;
    private int version;
    private Product product;
    private Set itemSupplies = new HashSet();
    private Date createdDate = new Date();
    private String createdBy;
    private Date updatedDate;
    private String updatedBy;

    
    public Item(){
    }
    public Item(String name, String desc, String itemNumber,
            String manufacturer, String distributor, String format,
            Money listPrice) {
        this.name = name;
        this.desc = desc;
        this.itemNumber = itemNumber;
        this.manufacturer = manufacturer;
        this.distributor = distributor;
        this.format = format;
        this.listPrice = listPrice;
    }
    	
    /*
     * Convenient methods that will either create new Supplier (supplier is transiant) 
     * or link to existing one (supplier is detached).  
     * For existing Item/supplier..will simply return "false"
     * 
     */
    public boolean addSupplier(Supplier supplier, String username) {
        return this.addSupplier(supplier,username,null,null,null);
    }

    public boolean addSupplier(Supplier supplier, String username, String bidInfo, String bidYear) {
        return this.addSupplier(supplier,username,bidInfo,bidYear,null);
    }
   
    public boolean addSupplier(Supplier supplier, String username, String bidInfo, String bidYear, String catalogNb) {
        if ((username== null) || username.equals("")) 
            throw new IllegalArgumentException("username is mandatory to assign createdBy in case of new Supplier");
        ItemSupplied itemSupplied = new ItemSupplied(this,supplier,username,bidInfo,bidYear,catalogNb);
        return (itemSupplies.add(itemSupplied));
    }
    
       
    
    //for now, only allow defining if Product is null (cannot re-assign..to be validated! )  
    // and no equivalent item already exist in Product
    public void defineProduct(Product prod){
        if (this.product == null) {
            if (prod.getItems().add(this)) {
                this.product = prod;
            } else throw new IllegalStateException("Item already existing inside Product");            
        } else 
            throw new IllegalArgumentException("Item has already a defined Product, cannot re-assign"); 
    }

    public boolean isBiddingAssociated(){
        for (Iterator iter = getItemSupplies().iterator();iter.hasNext();){
            ItemSupplied itemS = (ItemSupplied) iter.next();
            if ((itemS.getBidInfo() != null) && (itemS.getBidInfo().length()>1) ){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Two diff items can have same name (but different format, catalog, etc..)
     * @hibernate.property unique="false" not-null="true" length="100"
     */
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @hibernate.property length="100" column="description"
     */
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    /**
     * @hibernate.property length="50"
     */
    public String getItemNumber() {
        return itemNumber;
    }
    public void setItemNumber(String nb) {
        this.itemNumber = nb;
    }
    /**
     * @hibernate.property length="50"
     */
    public String getDistributor() {
        return distributor;
    }
    public void setDistributor(String distributor) {
        this.distributor = distributor;
    }
    /**
     * @hibernate.property length="50"
     */
    public String getFormat() {
        return format;
    }
    public void setFormat(String format) {
        this.format = format;
    }
    /**
     * @hibernate.property length="50"
     */
    public String getManufacturer() {
        return manufacturer;
    }
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    
    //Here I need an explicit association class 
    //since composite-element does not offer two-way navigation
    /**
     * @hibernate.set inverse="true" cascade="all-delete-orphan" lazy="true"
     * @hibernate.collection-key column="item_id"
     * @hibernate.collection-one-to-many class="ca.po.model.ItemSupplied"
     * @hibernate.collection-cache usage="nonstrict-read-write"
     */
    public Set getItemSupplies() {
        return this.itemSupplies;
    }
    private void setItemSupplies(Set itemSupplies) {
        this.itemSupplies = itemSupplies;
    }
    /**
     * @hibernate.many-to-one class="ca.po.model.Product" column="product_id"
     * 		cascade="none" not-null="true" foreign-key="fk_item_product"
     */
    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }
    /**
     * @hibernate.component class="ca.po.model.type.Money" prefix="listprice_"
     */
    public Money getListPrice() {
        return listPrice;
    }
    public void setListPrice(Money listPrice) {
        this.listPrice = listPrice;
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
     * @hibernate.property
     */
    public Date getCreatedDate() {
        return createdDate;
    }
    private void setCreatedDate(Date createdDateTime) {
        this.createdDate = createdDateTime;
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
    public String toString() {
        return new ToStringBuilder(this).append("name", this.name).append(
                "itemId", this.getId()).append("itemNumber", this.itemNumber).append("distributor", this.distributor).append(
                "format", this.format).append("originalCreator", this.createdBy).append(
                "manufacturer", this.manufacturer).toString();
    }

    /*
     * 
     * same item can have different supplier (hence suppliers not part of equals) 
     * Item natural key are : Name, Manufacturer, Distributor, format, itemNumber
     * 
     */
    public boolean equals(Object object) {
        if (!(object instanceof Item)) {
            return false;
        }
        Item rhs = (Item) object;
        return new EqualsBuilder().append(this.manufacturer, rhs.getManufacturer()).append(
                this.distributor,rhs.getDistributor()).append(
                this.name, rhs.getName()).append(this.format, rhs.getFormat()).append(
                this.itemNumber, rhs.getItemNumber()).isEquals();
    }
    public int hashCode() {
        return new HashCodeBuilder(-499040443, 1291716487).append(
                this.manufacturer).append(this.distributor).append(
                this.name).append(this.format).append(this.itemNumber).toHashCode();
    }
}
