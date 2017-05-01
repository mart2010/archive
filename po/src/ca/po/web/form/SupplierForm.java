package ca.po.web.form;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

/**
 * @struts.form name="supplierForm" 
 */
public class SupplierForm extends BaseForm implements Serializable {

    private String name;
    private String desc;
    private String salesRepName;
    private String salesRepContact;
 
    
    public SupplierForm(){
    }

    public String getName() {
        return name;
    }
    /**
     * @struts.validator type="required"
     * @struts.validator type="minlength" 
     * @struts.validator-args arg0resource="supplier.name"
     * 		arg1value="${beanNameMinLength}"
     * @struts.validator-var name="minlength" value="${beanNameMinLength2}"
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
    public String getSalesRepContact() {
        return salesRepContact;
    }
    public void setSalesRepContact(String salesRepContact) {
        this.salesRepContact = salesRepContact;
    }
    public String getSalesRepName() {
        return salesRepName;
    }
    public void setSalesRepName(String salesRepName) {
        this.salesRepName = salesRepName;
    }

    
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
    }

    
    
}
