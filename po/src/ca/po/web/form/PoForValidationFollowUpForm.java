package ca.po.web.form;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import ca.po.util.DateUtil;

/**
 * This bean form is used for validating POs
 * as well as doing the follow-up on POs
 *
 * @struts.form name="poForValidationFollowUpForm"
 */
public class PoForValidationFollowUpForm extends BaseForm {

   
    private String[] selPoIds = new String[] {};

    private Map payTypes = new HashMap();
    private Map confirmNbs = new HashMap();
    private Map statusCodes = new HashMap();
    private Map revisedCADTotals = new HashMap();
    private Map revisedCADTaxes = new HashMap();
    private Map estimateReceptionDates = new HashMap();
    
    
    public PoForValidationFollowUpForm(){
    }
    
    public String[] getSelPoIds() {
        return selPoIds;
    }
    /**
     * This does not work, so validation occurs within action code instead
     * struts.validator type="required" msgkey="pos.validationListEmpty"
     */
    public void setSelPoIds(String[] selPoIds) {
        this.selPoIds = selPoIds;
    }
    
    public String getPayType(String poId){
        return (String) this.payTypes.get(poId);
    }
    public void setPayType(String poId, String typeId){
        this.payTypes.put(poId,typeId);
    }
    public String getConfirmNb(String poId){
        return (String) this.confirmNbs.get(poId);
    }
    public void setConfirmNb(String poId, String nb){
        this.confirmNbs.put(poId,nb);
    }
    public void setStatusCode(String poId,String code){
        this.statusCodes.put(poId,code);
    }
    public String getStatusCode(String poId){
        return (String) this.statusCodes.get(poId);
    }
    public String getRevisedCADTotal(String poId){
        return (String) this.revisedCADTotals.get(poId);
    }
    public void setRevisedCADTotal(String poId, String total){
        this.revisedCADTotals.put(poId,total);
    }
    public String getRevisedCADTax(String poId){
        return (String) this.revisedCADTaxes.get(poId);
    }
    public void setRevisedCADTax(String poId, String total){
        this.revisedCADTaxes.put(poId,total);
    }
    public String getEstimateReceptionDate(String poId) {
        return (String) this.estimateReceptionDates.get(poId);
    }
    public void setEstimateReceptionDate(String poId, String date) {
        this.estimateReceptionDates.put(poId,date);
    }

    
    
    public ActionErrors validate(ActionMapping mapping, 
            HttpServletRequest request) {
        // Perform validator framework validations
        ActionErrors errors = super.validate(mapping, request);
        
        boolean foundError = false;
        for (int i = 0; i < this.selPoIds.length; i++){
            if (foundError == true) break; 

            try {
                if (((getRevisedCADTotal(selPoIds[i]).length()>0) &&
	                (Float.parseFloat(this.getRevisedCADTotal(selPoIds[i])) < 0)) || 
	               ((getRevisedCADTax(selPoIds[i]).length()>0) &&
	                (Float.parseFloat(this.getRevisedCADTax(selPoIds[i])) < 0))) {
	                	errors.add("effPrice", new ActionMessage("pos.revEff"));
	                    foundError = true;
	             }
	        } catch (NumberFormatException ex) {
	 	        errors.add("effPrice", new ActionMessage("pos.revEff"));
	 	        foundError = true;
	 	    } 
	        
            if (getEstimateReceptionDate(selPoIds[i]).length() > 0){
                if (DateUtil.convertDefaultStringToDate
                        (getEstimateReceptionDate(selPoIds[i])) == null) {
    	 	        errors.add("estDate", new ActionMessage("pos.estDate"));
    	 	        foundError = true;
                }
            }
	 	    
        }

        return errors;
        
    }

    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
    }
    
    

}
