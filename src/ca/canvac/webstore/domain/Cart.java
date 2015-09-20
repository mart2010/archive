package ca.canvac.webstore.domain;

import org.springframework.beans.support.PagedListHolder;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.math.BigDecimal;

public class Cart implements Serializable {

    private final PagedListHolder itemList = new PagedListHolder();

    /* Private Fields */
    private final Map itemMap = Collections.synchronizedMap(new HashMap());

    /* JavaBeans Properties */

    public Cart() {
        this.itemList.setPageSize(25);
    }

    public void addItem(Item item) {
        CartItem cartItem = (CartItem) itemMap.get(new Long(item.getItemId()));
        //it is a new item that is added to cart
        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setItem(item);
            cartItem.setQuantity(1);
            itemMap.put(new Long(item.getItemId()), cartItem);
            itemList.getSource().add(cartItem);
        } else {
            //check the rule for quantity for tetr/peptide...
            cartItem.incrementQuantity();
        }
    }

    /* Public Methods */

    public boolean containsItemId(long itemId) {
        return itemMap.containsKey(new Long(itemId));
    }
    /* See if feasible to adapt so that gloabl WebPageSize could be use here as well
      //used to change the default value.. must be called by Action which returns
      //cart to the client
      public void setWebPageSize(int webPageSize) {
          this.itemList.setPageSize(webPageSize);
      }
     */


    public Iterator getAllCartItems() {
        return itemList.getSource().iterator();
    }

    public PagedListHolder getCartItemList() {
        return itemList;
    }

    public int getNumberOfItems() {
        return itemList.getSource().size();
    }

    public BigDecimal getSubTotal() {
        BigDecimal subTotal = new BigDecimal("0.00");
        for (Iterator items = getAllCartItems(); items.hasNext();) {
            CartItem cartItem = (CartItem) items.next();
            Item item = cartItem.getItem();
            BigDecimal listPrice = item.getUnitPrice();
            int quantity = cartItem.getQuantity();
            subTotal = subTotal.add(listPrice.multiply(BigDecimal.valueOf(quantity)));
        }
        return subTotal;
    }

    public void incrementQuantityByItemId(long itemId) {
        CartItem cartItem = (CartItem) itemMap.get(new Long(itemId));
        cartItem.incrementQuantity();
    }


    public Item removeItemById(long itemId) {
        CartItem cartItem = (CartItem) itemMap.remove(new Long(itemId));
        if (cartItem == null) {
            return null;
        } else {
            itemList.getSource().remove(cartItem);
            return cartItem.getItem();
        }
    }

    public void setQuantityByItemId(long itemId, int quantity) {
        CartItem cartItem = (CartItem) itemMap.get(new Long(itemId));
        cartItem.setQuantity(quantity);
    }

}
