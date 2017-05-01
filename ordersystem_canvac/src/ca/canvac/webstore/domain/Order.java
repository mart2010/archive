package ca.canvac.webstore.domain;

import ca.canvac.webstore.Constants;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.math.BigDecimal;


public class Order implements Serializable {
    private String comments;
    private String fedex;
    private List lineItems = new ArrayList();
    private Date orderDate;

    /* Private Fields */

    private long orderId;

    private String orderWebStatus; //been cancelled or registered
    private String shipAddress1;
    private String shipAddress2;
    private String shipCity;
    private String shipCountry;
    private String shipPostalZip;
    private String shipProvState;
    private String shipToFirstName;
    private String shipToLastName;

    private boolean specialRequest = false;
    private BigDecimal totalPrice;
    private String username;

    public void addLineItem(CartItem cartItem) {
        LineItem lineItem = new LineItem(lineItems.size() + 1, cartItem);
        addLineItem(lineItem);
    }

    public void addLineItem(LineItem lineItem) {
        lineItems.add(lineItem);
    }

    public String getComments() {
        return comments;
    }


    public String getFedex() {
        return fedex;
    }

    public List getLineItems() {
        return lineItems;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public long getOrderId() {
        return orderId;
    }

    public String getOrderWebStatus() {
        return orderWebStatus;
    }

    public String getShipAddress1() {
        return shipAddress1;
    }

    public String getShipAddress2() {
        return shipAddress2;
    }

    public String getShipCity() {
        return shipCity;
    }

    public String getShipCountry() {
        return shipCountry;
    }

    public String getShipPostalZip() {
        return shipPostalZip;
    }

    public String getShipProvState() {
        return shipProvState;
    }

    public String getShipToFirstName() {
        return shipToFirstName;
    }

    public String getShipToLastName() {
        return shipToLastName;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public String getUsername() {
        return username;
    }


    /* Public Methods
     * Used by the Web layer to initialize the order based on a Cart
     * object plus an account object
    */

    public void initOrder(Account account, Cart cart) {
        //cannot init with null account
        if (account == null)
            throw new IllegalArgumentException("Cannot initialize Order with null account parameter");

        //cannot init with with null cart
        if (cart == null)
            throw new IllegalArgumentException("Cannot initialize Order with null cart parameter");

        //cannot init with empty cart
        if (cart.getNumberOfItems() == 0)
            throw new IllegalArgumentException("Cannot initialize Order with empty cart parameter");

        this.username = account.getUserId();
        this.orderDate = new Date();
        this.fedex = account.getFedex();
        this.totalPrice = cart.getSubTotal();
        this.shipToFirstName = account.getFirstName();
        this.shipToLastName = account.getLastName();
        this.shipAddress1 = account.getAddress1();
        this.shipAddress2 = account.getAddress2();
        this.shipCity = account.getCity();
        this.shipProvState = account.getProvState();
        this.shipPostalZip = account.getPostalZip();
        this.shipCountry = account.getCountry();
        //Oracle JDBC does not seem to accept Null for comments!
        this.comments = "";
        //reset the lineItems to reflect the actual state of the cart
        this.lineItems.clear();


        for (Iterator i = cart.getAllCartItems(); i.hasNext();) {
            CartItem cartItem = (CartItem) i.next();
            addLineItem(cartItem);
            if (cartItem.getItem().getName().equals(Constants.SPECIAL_REQUEST)) {
                this.specialRequest = true;
            }
        }

    }


    public boolean isSpecialRequest() {
        return specialRequest;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setFedex(String fedex) {
        this.fedex = fedex;
    }

    public void setLineItems(List lineItems) {
        this.lineItems = lineItems;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public void setOrderWebStatus(String orderWebStatus) {
        this.orderWebStatus = orderWebStatus;
    }

    public void setShipAddress1(String shipAddress1) {
        this.shipAddress1 = shipAddress1;
    }

    public void setShipAddress2(String shipAddress2) {
        this.shipAddress2 = shipAddress2;
    }

    public void setShipCity(String shipCity) {
        this.shipCity = shipCity;
    }

    public void setShipCountry(String shipCountry) {
        this.shipCountry = shipCountry;
    }

    public void setShipPostalZip(String shipPostalZip) {
        this.shipPostalZip = shipPostalZip;
    }

    public void setShipProvState(String shipProvState) {
        this.shipProvState = shipProvState;
    }

    public void setShipToFirstName(String shipFoFirstName) {
        this.shipToFirstName = shipFoFirstName;
    }

    public void setShipToLastName(String shipToLastName) {
        this.shipToLastName = shipToLastName;
    }

    public void setSpecialRequest(boolean specialRequest) {
        this.specialRequest = specialRequest;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String toString(){
        StringBuffer txt = new StringBuffer();
        txt.append("\n Order number= ").append(this.getOrderId());
        txt.append("\n Order submitted by ").append(this.getShipToFirstName())
        	.append(" ").append(this.getShipToLastName());
        txt.append("\n Delivery Address: ").append(this.getShipAddress1()).
        	append(" ").append(this.getShipAddress2());
        txt.append("\n City, Prov/State: ").append(this.getShipCity()).
        	append(" ").append(this.getShipProvState());
        txt.append("\n Country, Postal Code: ").append(this.getShipCountry()).
    		append(" ").append(this.getShipPostalZip());
        
        boolean specialReq = false;
        if (this.getLineItems() != null) {
	        for (Iterator lineItems = this.getLineItems().iterator(); lineItems.hasNext();) {
	            LineItem lineItem = (LineItem) lineItems.next();
	            txt.append(lineItem);
	
	            //flag a special request
	            if (Constants.SPECIAL_REQUEST.equals(lineItem.getItem().getName()))
	                specialReq = true;
	        }
        }
        if (specialReq) {
            txt.append("\n Special Request Description: \n").append(this.getComments());
        }

        return txt.toString();
       
    }
    
}
