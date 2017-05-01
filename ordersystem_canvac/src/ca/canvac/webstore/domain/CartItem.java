package ca.canvac.webstore.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public class CartItem implements Serializable {

    /* Private Fields */

    private Item item;
    private int quantity;


    public Item getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    /* Public methods */

    public BigDecimal getTotalPrice() {
        if (item != null) {
            return item.getUnitPrice().multiply(BigDecimal.valueOf(quantity));
        } else {
            return null;
        }
    }

    /*
    * this could have to be adjusted .... */

    public void incrementQuantity() {
        quantity++;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
