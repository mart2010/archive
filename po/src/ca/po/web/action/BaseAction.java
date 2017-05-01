package ca.po.web.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.acegisecurity.context.security.SecureContextUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.springframework.web.struts.MappingDispatchActionSupport;

import ca.po.model.User;
import ca.po.model.service.ItemManager;
import ca.po.model.service.PoManager;
import ca.po.model.service.SupplierManager;
import ca.po.model.service.UserManager;
import ca.po.util.converter.MoneyCADConverter;

/**
 * Implementation of Action that contains 
 * base methods for logging and conducting pre/post perform actions. 
 * This class is intended to be a base class for all Struts actions.
 *  
 */
public abstract class BaseAction extends MappingDispatchActionSupport {
    protected transient final Log log = LogFactory.getLog(getClass());

    private static final String SECURE = "secure";
    
    private static Long defaultLong = null;

    //convenient access to managers (threadsafe)
    private UserManager userManager;
    private ItemManager itemManager;
    private SupplierManager supplierManager; 
    private PoManager poManager;
    private MoneyCADConverter cadConverter;

    public MoneyCADConverter getCadConverter(){
        if (cadConverter==null) {
            cadConverter= (MoneyCADConverter) getWebApplicationContext().getBean("moneyCADConverter");
        }
        return cadConverter;
    }
    
    public UserManager getUserManager() {
        if (userManager== null){
            userManager= (UserManager) getWebApplicationContext().getBean("userManager");
        }
        return userManager;
    }

    public ItemManager getItemManager() {
        if (itemManager== null){
            itemManager= (ItemManager) getWebApplicationContext().getBean("itemManager");
        }
        return itemManager;
    }

    public SupplierManager getSupplierManager() {
        if (supplierManager== null){
            supplierManager= (SupplierManager) getWebApplicationContext().getBean("supplierManager");
        }
        return supplierManager;
    }

    public PoManager getPoManager() {
        if (poManager== null){
            poManager= (PoManager) getWebApplicationContext().getBean("poManager");
        }
        return poManager;
    }
    
    
    /*
     * Convenience method for getting an action form base on it's mapped scope.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param request The HTTP request we are processing
     * @return ActionForm the form from the specifies scope, or null if nothing
     *         found
     */
    protected ActionForm getActionForm(ActionMapping mapping,
                                       HttpServletRequest request) {
        ActionForm actionForm = null;

        // Remove the obsolete form bean
        if (mapping.getAttribute() != null) {
            if ("request".equals(mapping.getScope())) {
                actionForm =
                        (ActionForm) request.getAttribute(mapping.getAttribute());
            } else {
                HttpSession session = request.getSession();

                actionForm =
                        (ActionForm) session.getAttribute(mapping.getAttribute());
            }
        }
        return actionForm;
    }
    /*
     * Convenience method for removing the obsolete form bean.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param request The HTTP request we are processing
     */
    protected void removeFormBean(ActionMapping mapping,
                                  HttpServletRequest request) {
        // Remove the obsolete form bean
        if (mapping.getAttribute() != null) {
            if ("request".equals(mapping.getScope())) {
                request.removeAttribute(mapping.getAttribute());
            } else {
                HttpSession session = request.getSession();

                session.removeAttribute(mapping.getAttribute());
            }
        }
    }

    /*
     * Convenience method to update a formBean in it's scope
     *
     * @param mapping The ActionMapping used to select this instance
     * @param request The HTTP request we are processing
     * @param form    The ActionForm
     */
    protected void updateFormBean(ActionMapping mapping,
                                  HttpServletRequest request, ActionForm form) {
        // Remove the obsolete form bean
        if (mapping.getAttribute() != null) {
            if ("request".equals(mapping.getScope())) {
                request.setAttribute(mapping.getAttribute(), form);
            } else {
                HttpSession session = request.getSession();

                session.setAttribute(mapping.getAttribute(), form);
            }
        }
    }

    
    /*
     * Message (key) name of default locale to message key lookup.
     */
   // protected Map defaultKeyNameKeyMap = null;

    
    /*
     * Convenience method to initialize messages in a subclass. Useful to
     * preserve the Messages when doing multiple forwarding to actions (the
     * session scope also allows to preserve when Redirecting
     *  
     */
    public ActionMessages getMessages(HttpServletRequest request) {
        ActionMessages messages = null;
        HttpSession session = request.getSession();

        if (request.getAttribute(ActionMessages.GLOBAL_MESSAGE) != null) {
            messages = (ActionMessages) request
                    .getAttribute(ActionMessages.GLOBAL_MESSAGE);
            saveMessages(request, messages);
        } else if (session.getAttribute(ActionMessages.GLOBAL_MESSAGE) != null) {
            messages = (ActionMessages) session
                    .getAttribute(ActionMessages.GLOBAL_MESSAGE);
            saveMessages(request, messages);
            session.removeAttribute(ActionMessages.GLOBAL_MESSAGE);
        } else {
            messages = new ActionMessages();
        }

        return messages;
    }
    // --------------------------------------------------------- Public Methods
    // Don't use class variables in Action objects.  These are not session safe.


    /*
     * Convenience method to get current User
     */
    protected User getUser() {
        return (User) SecureContextUtils.getSecureContext().
        					getAuthentication().getPrincipal();
    }

    /*
     * Convenience method to get isAdmin
     */
    protected boolean userIsAdmin() {
        return  getUser().isAdmin();
        
    }
    
    /* 
     * Convenience method that returns global ActionForward based on 
     * "from" attribute in session, otherwise return null.  It always cleans-up
     * session after so that it is invalidated once consumed
     * 
     */
    protected ActionForward getNextActionForward(ActionMapping mapping,
            							  		HttpServletRequest request) {
        
        String toGlobalForward = (String) request.getSession().getAttribute("from");
        if (toGlobalForward!= null && mapping.findForward(toGlobalForward)!= null) {
            request.getSession().removeAttribute("from");
            return mapping.findForward(toGlobalForward);
        }
        return null;
    }
    
    /*
     * Convenience method that stored a global ActionForward
     */
    protected void putNextActionForward(ActionMapping mapping,
	  		HttpServletRequest request, String toGlobalForward) {

        if (toGlobalForward!= null && mapping.findForward(toGlobalForward)!= null) {
            request.getSession().setAttribute("from",toGlobalForward);
        }
        
    }
    
    /*
     * Useful to call before calling new Action from the convential way
     * (i.e. typically from Menu bar)
     */
    protected void cleanNextActionForward(HttpServletRequest request){
        request.getSession().removeAttribute("from");
    }
    
}