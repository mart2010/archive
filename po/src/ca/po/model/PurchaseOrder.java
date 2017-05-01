package ca.po.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import ca.po.model.type.Money;
import ca.po.model.type.PaymentType;
import ca.po.model.type.PoStatusCode;
import ca.po.util.converter.MoneyCADConverter;

/**
 * @author MOUELLET
 * @hibernate.class table="po_purchase_order"
 * 
 */
public class PurchaseOrder implements Serializable {

    private Long poId;
    private int version;
    
    private String orderFor;
    private String confirmNumber;
    //all price at PO level are converted into CAD
    //these are just persisted, so store as BigDecimal
    private BigDecimal revisedEffectiveCADTotal;
    private BigDecimal revisedEffectiveCADTax;

    //calculated from Lineitems
    private Money estCalculatedCADTotal;
    //value to persist in the DB
    private BigDecimal estCalculatedCADTotalPersisted;
    
    private Date openDate;
    private Date currentStatusChangeDate;
    private Date estimateReceptionDate;
    private PoStatusCode currentStatusChange;
    
    private String project;
    private PaymentType paymentType;
    private Lab lab;
    private User orderByUser;
    
    
    //to preserve the entry ordering
    private List poLineItems = new ArrayList();
    //cancelled the idea of keeping all historical status
    //private List poHistoricalStatusList = new ArrayList();
    
    private MoneyCADConverter moneyCADConverter;
    
    public PurchaseOrder(){
    }
    
    public PurchaseOrder(MoneyCADConverter conv){
        this.moneyCADConverter = conv;
    }
    
    public void setMoneyCADConverter(MoneyCADConverter moneyCADConverter) {
        this.moneyCADConverter = moneyCADConverter;
    }
    public MoneyCADConverter getMoneyCADConverter() {
        return moneyCADConverter;
    }

    //Add an Item (LineItem with default quantity=1)
    public boolean addItem(Item item, Supplier sup){
        //forbids the modification PO that has been Validated
        if ((this.currentStatusChange != null) &&
                	(this.currentStatusChange.ordinal >= PoStatusCode.VALIDATED.ordinal))
            throw new IllegalAccessError("Cannot add item to PO already Validated");
        
        for (ListIterator iter = poLineItems.listIterator() ; iter.hasNext() ;) {
            PoLineItem linetemp = (PoLineItem)iter.next();
            //increase quantity if same Item
            if (linetemp.getItem().equals(item)) {
                linetemp.incrementQuantity(1);
                return false;
            }
        }
        //new Item
        PoLineItem lineItem = new PoLineItem(item,1,sup);
        return this.poLineItems.add(lineItem);
    }

    
    //Default to first Supplier found
    public boolean addItem(Item itemFirstSupplier){
        assert ((itemFirstSupplier.getItemSupplies() != null) && 
        		(!itemFirstSupplier.getItemSupplies().isEmpty())) : "Item requires to have ItemSupplies";
        Supplier sup = ((ItemSupplied) itemFirstSupplier.getItemSupplies().iterator().next()).getSupplier(); 
        return this.addItem(itemFirstSupplier,sup);
    }

    //Remove an Item (no matter the quantity of the lineItem)
    public boolean removeItem(Item item){
        //forbids the modification PO that has been Validated
        if ((this.currentStatusChange != null) && 
                (this.currentStatusChange.ordinal >= PoStatusCode.VALIDATED.ordinal))
            throw new IllegalAccessError("Cannot remove item to PO already Validated");
        
        for (ListIterator iter = poLineItems.listIterator() ; iter.hasNext() ;) {
            PoLineItem linetemp = (PoLineItem)iter.next();
            //remove if find the same Item
            if (linetemp.getItem().equals(item)) {
                iter.remove();
                return true;
            }
        }
        return false;
    }
    
    public boolean isInitialized(){
        if (this.currentStatusChange == null) 
            return false;
        else return true;
    } 
    
    //convenient method indicating if po is still "proposed"    
    public boolean isEditableByUser() {
        if ((this.currentStatusChange == null) ||
                (this.currentStatusChange.equals(PoStatusCode.PROPOSED)))
            return true;
        return false;
    }

    public boolean isAssociatedWithBidding(){
        for (Iterator iter = getPoLineItems().iterator(); iter.hasNext();) {
            Item item = ((PoLineItem) iter.next()).getItem();
            if (item.isBiddingAssociated()) 
                return true;
        }
        return false;
        
    }
                                             
    
    /*
     * Must be called once at the purchase Order proposal
     */
    public void initialize(User user, Lab lab){
        if (this.currentStatusChange != null)   {
            throw new IllegalStateException("Cannot re-initialize a Purchase Order");
        }
        assert (lab != null) : "This method requires Not Null lab";

        //initialize the mandatory instance attribute
        this.lab = lab;
        this.orderByUser = user;

        this.currentStatusChange= PoStatusCode.PROPOSED;
        this.openDate = new Date();
        this.currentStatusChangeDate = openDate;
    }


    
    //to be called for each change of Status (after Proposed)
    public void changeStatus(PoStatusCode newStatus, User userAdmin){
        //throw exception if trying to set Status without Admin right 
        if (!userAdmin.isAdmin()) {
            throw new IllegalAccessError("Changing order status requires Admin role");
        }
        if (this.currentStatusChange== null){
            throw new IllegalAccessError("PO never been initialized, cannot change its status");            
        }
        //throw exception if trying to set Status backward or current
        if (newStatus.ordinal <= this.currentStatusChange.ordinal ) {
            throw new IllegalStateException("Cannot change order status to past or current status");
        }
        //throw exception if trying to set Status forward violating sequence
        if ( (newStatus.ordinal > (this.currentStatusChange.ordinal+1) ) && 
        		(!newStatus.equals(PoStatusCode.CANCELLED)) ){
            throw new IllegalStateException("Changing order status must respect sequence unless it is to be Cancelled");
        }

        //update the current attribute
        this.currentStatusChange = newStatus;
        this.currentStatusChangeDate = new Date();
    }
    /*
     * When user wants to cancel a PO which has at least been Proposed
     * User can only cancel their PO while Admin can cancel any PO
     */
    public void cancel(User user) {
        //throw exception if trying to cancell without Admin right 
        if ((!user.isAdmin()) && (!this.getOrderByUser().equals(user)))
            throw new IllegalAccessError("Cancelling order not owned by user requires Admin role ");

        if (this.currentStatusChange== null)
            throw new IllegalStateException("PO never been initialized, cannot cancel it");            
        
        //throw exception if trying to cancel already cancelled po
        if ( this.currentStatusChange.equals(PoStatusCode.CANCELLED))
            throw new IllegalStateException("Cannot cancel an already Cancelled Order");

        //update the current attribute
	    this.currentStatusChange = PoStatusCode.CANCELLED;
	    this.currentStatusChangeDate = new Date();
        
    }
    
    /**
	 * @hibernate.id generator-class="native" column="po_id" 
     */
    public Long getPoId() {
        return this.poId;
    }
    // only Hibernate should set ID
    private void setPoId(Long id) {
        this.poId = id;
    }

    /**
     * @hibernate.property length="30"
     */
    public String getConfirmNumber() {
        return confirmNumber;
    }
    public void setConfirmNumber(String confirmation) {
        this.confirmNumber = confirmation;
    }

    //lazy on-the-fly calculation avoiding multiple update points
    public Money getEstCalculatedCADTotal() {
        if (this.moneyCADConverter== null)
            throw new IllegalStateException("This method requires a 'MoneyCADConverter' implementation available");
        //intialize the CalculCAD amount
        estCalculatedCADTotal = Money.getCAD(0.00);
        for (ListIterator iter = poLineItems.listIterator() ; iter.hasNext() ;) {
            PoLineItem linetemp = (PoLineItem)iter.next();
            Money lineSubTotal = linetemp.getListPrice().multiply(linetemp.getQuantity());
            
            if (!lineSubTotal.getCurrency().equals(Currency.getInstance("CAD"))){
                //convert into CAD currency
                lineSubTotal =  moneyCADConverter.convertToCAD(lineSubTotal);
            }
            estCalculatedCADTotal= estCalculatedCADTotal.add(lineSubTotal);
        }
        return estCalculatedCADTotal;
    }
    /**
     * @hibernate.property
     * @hibernate.column name="estimated_CAD_tot"  sql-type="Decimal(10,2)"
     */
    public BigDecimal getEstCalculatedCADTotalPersisted() {
        /*
         * Only calculate if PO is still being edited for performance 
         * unless the persisted value is null (should not happen)
         */
        if ((this.getCurrentStatusChange().equals(PoStatusCode.PROPOSED)) ||
                (this.estCalculatedCADTotalPersisted==null))
            
            return this.getEstCalculatedCADTotal().getDecimalAmount();
        else 
            return this.estCalculatedCADTotalPersisted;
    }
    /*
     * Setter is used for performance reason for 'Validated' Pos (no lineitem editing) 
     */
    public void setEstCalculatedCADTotalPersisted(
            BigDecimal estCalculatedCADTotalPersisted) {
        this.estCalculatedCADTotalPersisted= estCalculatedCADTotalPersisted;
    }
    
    /**
     * @hibernate.property 
     * @hibernate.column name="effective_CAD_tot" sql-type="Decimal(10,2)"
     */
    public BigDecimal getRevisedEffectiveCADTotal() {
        return revisedEffectiveCADTotal;
    }
    public void setRevisedEffectiveCADTotal(BigDecimal revisedPrice) {
        this.revisedEffectiveCADTotal = revisedPrice;
    }
    /**
     * @hibernate.property
     * @hibernate.column name="effective_CAD_tax" sql-type="Decimal(10,2)"
     */
    public BigDecimal getRevisedEffectiveCADTax() {
        return revisedEffectiveCADTax;
    }
    public void setRevisedEffectiveCADTax(BigDecimal taxProv) {
        this.revisedEffectiveCADTax = taxProv;
    }
    /**
     * @hibernate.property length="30"
     */
    public String getOrderFor() {
        return orderFor;
    }
    public void setOrderFor(String orderFor) {
        this.orderFor = orderFor;
    }

    /**
     * This is customized in the file : hibernate-generator-params-PurchaseOrder.xml 
     * hibernate.list table="po_lineitem" lazy="true" 
     * hibernate.collection-key column="po_id"
     * hibernate.collection-index column="linenum"
     * hibernate.collection-composite-element class="ca.po.model.PoLineItem"
     */
    public List getPoLineItems() {
        return poLineItems;
    }
    private void setPoLineItems(List poLineItems) {
        this.poLineItems = poLineItems;
    }

    /**
     * @hibernate.many-to-one class="ca.po.model.Lab" column="lab_id"
     * 		cascade="none" not-null="true" outer-join="false" foreign-key="fk_purchase_order_lab" 
     */
    public Lab getLab() {
        return lab;
    }
    public void setLab(Lab lab) {
        this.lab = lab;
    }
    /**
     * @hibernate.many-to-one class="ca.po.model.User" column="user_id"
     * 		cascade="none" not-null="true" outer-join="false" foreign-key="fk_purchase_order_user"
     */
    public User getOrderByUser() {
        return orderByUser;
    }
    public void setOrderByUser(User user) {
        this.orderByUser = user;
    }
    /**
     * @hibernate.property length="100"
     */
    public String getProject() {
        return project;
    }
    public void setProject(String project) {
        this.project = project;
    }
    /**
     * @hibernate.property column="payment_type" length="50"
     * 		type="ca.po.model.dao.hibernate.usertype.PaymentTypeUserType"
     */
    public PaymentType getPaymentType() {
        return paymentType;
    }
    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }
    /**
     * @hibernate.property column="current_status" length="30"
     * 		type="ca.po.model.dao.hibernate.usertype.PoStatusCodeUserType"
     */
    public PoStatusCode getCurrentStatusChange() {
        return currentStatusChange;
    }
    public void setCurrentStatusChange(PoStatusCode lastStatusCode) {
        this.currentStatusChange = lastStatusCode;
    }
    /**
     * @hibernate.property column="current_status_date"
     */
    public Date getCurrentStatusChangeDate() {
        return currentStatusChangeDate;
    }
    public void setCurrentStatusChangeDate(Date date) {
        this.currentStatusChangeDate = date;
    }
    /**
     * @hibernate.property
     */
    public Date getOpenDate() {
        return openDate;
    }
    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }
    /**
     * @hibernate.property column="est_reception_date"
     */
    public Date getEstimateReceptionDate() {
        return estimateReceptionDate;
    }
    public void setEstimateReceptionDate(Date estimateReceptionDate) {
        this.estimateReceptionDate = estimateReceptionDate;
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

    
    public String toString() {
        return new ToStringBuilder(this).append("po_id", this.getPoId())
                .append("orderBy", this.getOrderByUser().getName()).append("Lab",this.lab.getName())
                .append("Status", this.currentStatusChange).toString();
    }
    public boolean equals(Object object) {
        if (!(object instanceof PurchaseOrder)) {
            return false;
        }
        PurchaseOrder rhs = (PurchaseOrder) object;
        return new EqualsBuilder().append(
                this.orderByUser, rhs.getOrderByUser()).append(this.confirmNumber, rhs.getConfirmNumber())
                .append(this.openDate, rhs.getOpenDate()).isEquals();
    }
    public int hashCode() {
        return new HashCodeBuilder(-1626631623, 1844256251)
        		.append(this.orderByUser).append(this.confirmNumber)
                .append(this.openDate).toHashCode();
    }
    

}
