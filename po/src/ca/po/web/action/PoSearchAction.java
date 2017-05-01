package ca.po.web.action;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.po.model.dao.PoSearchCriteria;
import ca.po.util.DateUtil;
import ca.po.web.form.SearchPoForm;



/**
 * 
 * @struts.action path="/admin/inputSearch" name="searchPoForm" scope="request"
 * 		validate="false" parameter="input"
 * @struts.action path="/admin/listSearch" name="searchPoForm" scope="request"
 * 		validate="true" input="/admin/inputSearch.html" parameter="list"
 * 
 * @struts.action-forward name="list" path="/WEB-INF/jsp/listPoByCriteria.jsp"
 *    
 */

public class PoSearchAction extends BaseAction {

    
    public ActionForward input(ActionMapping mapping,ActionForm form,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {

        populateLookups(request);
        return mapping.findForward("list");
    }
    
    
    private void populateLookups(HttpServletRequest request){
        List users = getUserManager().getUsers(false);
        request.setAttribute("users",users);
        List labs = getUserManager().getLabs();
        request.setAttribute("labs",labs);
        
    }
    
    public ActionForward list(ActionMapping mapping,ActionForm form,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {

        if (isCancelled(request)) {
            return mapping.findForward("home");
        }

        populateLookups(request);
        SearchPoForm searchForm = (SearchPoForm) form;
        //here I need to take care of pagination
        PoSearchCriteria criteria = new PoSearchCriteria();
        
        if (!searchForm.getUserId().equals("-1"))
            criteria.setUserId(new Long(searchForm.getUserId())); 
        if (!searchForm.getLabId().equals("-1"))
            criteria.setLabId(new Long(searchForm.getLabId())); 
        if (searchForm.getReceivedDateFrom().length()>1)
            criteria.setFromDate(DateUtil.convertDefaultStringToDate(searchForm.getReceivedDateFrom()));
        if (searchForm.getReceivedDateTo().length()>1)
            criteria.setToDate(DateUtil.convertDefaultStringToDate(searchForm.getReceivedDateTo()));
        
        List pos = getPoManager().getReceivedPOsByCriteria(criteria);
        if (pos==null)
            pos = new ArrayList();
        request.setAttribute("pos",pos);
        request.setAttribute("count",new Integer(pos.size()));
        
        return mapping.findForward("list");
    }

    
}
