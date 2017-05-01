package ca.canvac.webstore.web.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessages;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public abstract class BaseAction extends Action {

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

}