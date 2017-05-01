package ca.po.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @hibernate.class table="po_item_supplied"
 * @hibernate.cache usage="nonstrict-read-write"
 */
public class ItemSupplied implements Serializable {

    private Long itemSuppliedID;
    private String bidInfo;
    private String bidYear;
    private String catalogNumber;
    private Date createdDate = new Date();
    private Item item;
    private Supplier supplier;

    
    public ItemSupplied(){
    }
    
    public ItemSupplied(Item item, Supplier supplier, String createdByUsername){
        this(item, supplier, createdByUsername,null,null,null);
    }

    public ItemSupplied(Item item, Supplier supplier,String createdByUsername, 
            	String bidInfo, String bidYear, String catalogNb){
        this.setItem(item);
        this.setSupplier(supplier);
        //store the current user, will not be retained if 
        //Supplier already exist (check the addSupplier() in Item)
        this.getSupplier().setCreatedBy(createdByUsername);
        this.setBidInfo(bidInfo);
        this.setBidYear(bidYear);
        this.setCatalogNumber(catalogNb);
    }

    
    //  convenient methods to set bidding information
    public void defineBiddingInfo(String bidInfo, String bidYear){
        this.setBidInfo(bidInfo);
        this.setBidYear(bidYear);
    }

    /**
	 * @hibernate.id generator-class="native" column="item_supplied_id" 
     */    
    public Long getItemSuppliedID() {
        return itemSuppliedID;
    }
    public void setItemSuppliedID(Long itemSuppliedID) {
        this.itemSuppliedID = itemSuppliedID;
    }

    /**
     * @hibernate.property length="30"
     */
    public String getCatalogNumber() {
        return catalogNumber;
    }
    public void setCatalogNumber(String catalogNumber) {
        this.catalogNumber = catalogNumber;
    }
    /**
     * @hibernate.property length="100"
     */
    public String getBidInfo() {
        return bidInfo;
    }
    public void setBidInfo(String bidInfo) {
        this.bidInfo = bidInfo;
    }
    /**
     * @hibernate.property length="4"
     */
    public String getBidYear() {
        return bidYear;
    }
    public void setBidYear(String bidYear) {
        this.bidYear = bidYear;
    }
   
    /**
     * @hibernate.many-to-one class="ca.po.model.Supplier" column="supplier_id" outer-join="true"
     * 		cascade="save-update" not-null="true"  foreign-key="fk_itemsupplied_supplier"
     */
    public Supplier getSupplier() {
        return supplier;
    }
    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
    /**
     * @hibernate.many-to-one class="ca.po.model.Item" column="item_id"
     * 		cascade="none" not-null="true"  foreign-key="fk_itemsupplied_item"
     */
    public Item getItem() {
        return item;
    }
    public void setItem(Item item) {
        this.item = item;
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
    
    public String toString() {
        return new ToStringBuilder(this).append("bidYear", this.bidYear)
                .append("bidInfo", this.bidInfo).append("supplier",
                        this.supplier).append("item", this.item).toString();
    }
    public boolean equals(Object object) {
        if (!(object instanceof ItemSupplied)) {
            return false;
        }
        ItemSupplied rhs = (ItemSupplied) object;
        return new EqualsBuilder().append(
                this.item, rhs.getItem()).append(
                this.supplier, rhs.getSupplier()).isEquals();
    }
    public int hashCode() {
        return new HashCodeBuilder(-1626631623, 1844256251).append(
                this.item).append(
                this.supplier).toHashCode();
    }
    
    
}
