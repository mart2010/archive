package ca.po.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.acegisecurity.context.security.SecureContextUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * 
 * @struts.action path="/login" parameter="login"
 * @struts.action path="/logout" parameter="logout"
 *   
 * @struts.action-forward name="login" path="/public/login.jsp"
 *    
 */

public class LogAction extends BaseAction {

    
    public ActionForward login(ActionMapping mapping,ActionForm form,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {

        ActionMessages messages = getMessages(request);
        //user already authenticated
        if (SecureContextUtils.getSecureContext().getAuthentication().isAuthenticated()) {
            messages.add(ActionMessages.GLOBAL_MESSAGE, 
                	new ActionMessage("msg.alreadylogin"));
            saveMessages(request.getSession(), messages);

            String referer = request.getHeader("Referer");
            if (referer != null) {
                response.sendRedirect(response.encodeRedirectURL(referer));
                return null;
            } 
            return mapping.findForward("home");
        }
       
        return mapping.findForward("login");
        
    }
 
    public ActionForward logout(ActionMapping mapping,ActionForm form,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {

        if (SecureContextUtils.getSecureContext().getAuthentication().isAuthenticated()) {
	        ActionMessages messages = getMessages(request);
	        messages.add(ActionMessages.GLOBAL_MESSAGE, 
	            	new ActionMessage("msg.logout"));
	        saveMessages(request, messages);
	        SecureContextUtils.getSecureContext().setAuthentication(null);
	        request.getSession().invalidate();
             
            return mapping.findForward("login");
        }
        return mapping.findForward("login"); 
    }    
}