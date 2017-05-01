package ca.po.web.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import ca.po.model.ItemSupplied;
import ca.po.model.Supplier;
import ca.po.model.type.CurrencySupported;


/**
 * @struts.form name="itemForm"
 */
public class ItemForm extends BaseForm implements Serializable {

    //this should be externalized in configuration...
    public final static int NB_SUPPLIERS_BY_ITEM = 3;
    
    //to hold available currency codes as String
    private List currencies;
    private String name;
    private String desc;
    private String itemNumber;
    private String manufacturer;
    private String distributor;
    private String format;

    //Define as String, so BeanUtil does not choke on non-numeric data
    //and number are validated correctly
    private String listPriceString;
    private String listPriceCurrencyString;
    
    
    //for now, use 3 potential Suppliers by Item
    private List itemSuppliers = new ArrayList();

    
    public ItemForm(){
        //set currencies for Drop-down
        currencies=CurrencySupported.CURRENCIES_TEXT;
    }
    
    public String getName() {
        return name;
    }
    /**
     * @struts.validator type="required"
     * @struts.validator-args arg0resource="item.name"
     */
    public void setName(String name) {
        this.name = name;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getDistributor() {
        return distributor;
    }
    /**
     * struts.validator type="required"
     * struts.validator-args arg0resource="item.distributor"
     */
    public void setDistributor(String distributor) {
        this.distributor = distributor;
    }
    public String getFormat() {
        return format;
    }
    /**
     * struts.validator type="required"
     * struts.validator-args arg0resource="item.format"
     */
    public void setFormat(String format) {
        this.format = format;
    }
    public String getItemNumber() {
        return itemNumber;
    }
    /**
     * struts.validator type="required"
     * struts.validator-args arg0resource="item.number"
     */
    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }
    public String getListPriceString() {
        return listPriceString;
    }
    /**
     * @struts.validator type="required"
     * @struts.validator-args arg0resource="item.listPrice"
     * @struts.validator type="mask"
     * @struts.validator-var name="mask" value="${currency}"
     */
    public void setListPriceString(String listPriceString) {
        this.listPriceString = listPriceString;
    }
    public String getListPriceCurrencyString() {
        return listPriceCurrencyString;
    }
    public void setListPriceCurrencyString(String listPriceCurrencyString) {
        this.listPriceCurrencyString = listPriceCurrencyString;
    }
    public String getManufacturer() {
        return manufacturer;
    }
    /**
     * struts.validator type="required"
     * struts.validator-args arg0resource="item.manufacturer"
     */
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    public List getCurrencies() {
        return currencies;
    }
    public List getItemSuppliers() {
        return itemSuppliers;
    }
    public void setItemSuppliers(List itemSuppliers) {
        this.itemSuppliers = itemSuppliers;
    }

    public ItemSupplied getItemSupplier(int index){
        return (ItemSupplied) this.itemSuppliers.get(index);
    }
    public void setItemSupplier(int index, ItemSupplied value){
        this.itemSuppliers.set(index,value);
    }
    
   
    
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        this.itemSuppliers.clear();
        //reset the List of supplier
        for (int i=0; i < NB_SUPPLIERS_BY_ITEM; i++ ) {
            ItemSupplied itemSupplied = new ItemSupplied();
            Supplier supplier = new Supplier();
            itemSupplied.setSupplier(supplier);
            this.itemSuppliers.add(i,itemSupplied);
        }
            
    }

    public ActionErrors validate(ActionMapping mapping, 
            HttpServletRequest request) {
        // Perform validator framework validations
        ActionErrors errors = super.validate(mapping, request);

        
        //Check at least one SupplierId is selected
        int i;
        for (i=0; i < this.itemSuppliers.size(); i++ ){
            if (!((ItemSupplied) this.itemSuppliers.get(i)).
                    	getSupplier().getId().equals(new Long("-1"))) break;
        }
        
        if (i==this.itemSuppliers.size())
        	errors.add("suppliers", new ActionMessage("jspItem.oneSupplier"));        
        
        return errors;
    }

    
}
