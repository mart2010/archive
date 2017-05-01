package ca.po.web.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

/**
 * This convenient bean form is used to help validate all
 * entity model class having name+desc fields.
 *
 * @struts.form name="nameDescForm"
 */
public class NameDescForm extends BaseForm {

    private String name;
    private String desc;
    
    public NameDescForm(){
    }
    
    public String getDesc() {
        return desc;
    }
    public void setDesc(String catDesc) {
        this.desc = catDesc;
    }
    public String getName() {
        return name;
    }
    /**
     * @struts.validator type="required"
     * @struts.validator type="minlength" 
     * @struts.validator-args arg0resource="obj.name"
     * 		arg1value="${beanNameMinLength}"
     * @struts.validator-var name="minlength" value="${beanNameMinLength}"
     */
    public void setName(String catName) {
        this.name = catName;
    }
    
    
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        this.name = null;
        this.desc = null;
    }
    
    

}
