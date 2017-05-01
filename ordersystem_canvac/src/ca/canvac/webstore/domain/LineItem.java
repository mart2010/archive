package ca.canvac.webstore.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public class LineItem implements Serializable {
    private Item item;
    private long itemId;
    private String itemName;
    private int lineNumber;

    /* Private Fields */

    private long orderId;
    private BigDecimal price;
    private int quantity;

    /* Constructors */

    public LineItem() {
    }

    public LineItem(int lineNumber, CartItem cartItem) {
        this.lineNumber = lineNumber;
        this.quantity = cartItem.getQuantity();
        this.itemId = cartItem.getItem().getItemId();
        this.price = cartItem.getTotalPrice();
        this.itemName = cartItem.getItem().getName();
        this.item = cartItem.getItem();
    }

    public Item getItem() {
        return item;
    }

    public long getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    /* JavaBeans Properties */

    public long getOrderId() {
        return orderId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String toString(){
        return (this.getItem() +  "\n Item quantity= " + this.getQuantity());
    }

}
