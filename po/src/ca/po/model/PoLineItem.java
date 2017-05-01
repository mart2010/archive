package ca.po.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import ca.po.model.type.Money;
/**
 * @author MOUELLET
 * 
 */
public class PoLineItem implements Serializable {

    private Item item;
    private int quantity;
    //inherited from Item.listPrice, but potentially modifable by user
    private Money listPrice;
    private Supplier supplier;
    
    
    public PoLineItem(){
    }

    public PoLineItem(Item item, int qty){
        this.item = item;
        this.quantity = qty;
        this.listPrice = item.getListPrice(); 
    }

    public PoLineItem(Item item, int qty, Supplier sup){
        this.item = item;
        this.quantity = qty;
        this.supplier = sup;
        this.listPrice = item.getListPrice(); 
    }

    
    /**
     * @hibernate.many-to-one class="ca.po.model.Item" column="item_id"
     * 		not-null="true" outer-join="true" foreign-key="fk_lineitem_item"
     */
    public Item getItem() {
        return item;
    }
    public void setItem(Item item) {
        this.item = item;
    }
    
    /**
     * @hibernate.property not-null="true"
     */
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int qty) {
        this.quantity = qty;
    }
    public void incrementQuantity(int qty){
        this.setQuantity( this.quantity + qty);
    }
    /**
     * @hibernate.component class="ca.po.model.type.Money" prefix="listprice_"
     */
    public Money getListPrice() {
        return listPrice;
    }
    public void setListPrice(Money unitPrice) {
        this.listPrice = unitPrice;
    }

    /**
     * @hibernate.many-to-one class="ca.po.model.Supplier" column="supplier_id"
     * 		cascade="none" not-null="true" outer-join="true" foreign-key="fk_lineitem_supplier"
     */    
    public Supplier getSupplier() {
        return supplier;
    }
    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    
    
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append("Item", this.item.getName())
                .append("Quantity", this.quantity).toString();
    }
    public boolean equals(Object object) {
        if (!(object instanceof PoLineItem)) {
            return false;
        }
        PoLineItem rhs = (PoLineItem) object;
        return new EqualsBuilder()
        		.append(this.item, rhs.item).isEquals();
    }
    public int hashCode() {
        return new HashCodeBuilder(1543523745, -1870682267)
        		.append(this.item).toHashCode();
    }

}

