package ca.po.web.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import ca.po.util.DateUtil;

/**
 *
 * @struts.form name="searchPoForm"
 */
public class SearchPoForm extends BaseForm {

    private String userId;
    private String labId;
    private String receivedDateFrom;
    private String receivedDateTo;
    
    public SearchPoForm(){
    }
    
    public String getLabId() {
        return labId;
    }
    public void setLabId(String labId) {
        this.labId = labId;
    }
    public String getReceivedDateFrom() {
        return receivedDateFrom;
    }
    /**
     * Now done in validate() method to avoid hardcoding the datePattern
     * struts.validator type="date"
     * struts.validator-args arg0resource="jspSearch.fromDate"
     * struts.validator-var name="datePattern" value="MM/dd/yyyy"
     */
    public void setReceivedDateFrom(String receivedAfter) {
        this.receivedDateFrom = receivedAfter;
    }
    public String getReceivedDateTo() {
        return receivedDateTo;
    }
    /**
     * struts.validator type="date"
     * struts.validator-args arg0resource="jspSearch.toDate"
     * struts.validator-var name="datePattern" value="dd/MM/yyyy"
     */
    public void setReceivedDateTo(String receivedBefore) {
        this.receivedDateTo = receivedBefore;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
    }


    public ActionErrors validate(ActionMapping mapping, 
            HttpServletRequest request) {
        // Perform validator framework validations
        ActionErrors errors = super.validate(mapping, request);
        
        if ((getReceivedDateFrom().length() > 0) &&
                (DateUtil.convertDefaultStringToDate
                    (getReceivedDateFrom()) == null))  {
            errors.add("receivedDateFrom", new ActionMessage("errors.invalidDate"));
        }
        
        
        if ((getReceivedDateTo().length() > 0) &&      
             (DateUtil.convertDefaultStringToDate
                 (getReceivedDateTo()) == null)) {
            errors.add("receivedDateTo", new ActionMessage("errors.invalidDate"));
        }
        
        return errors;
        
    }
    

}
