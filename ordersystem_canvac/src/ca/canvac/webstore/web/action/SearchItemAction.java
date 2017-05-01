package ca.canvac.webstore.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.*;
import org.springframework.beans.support.PagedListHolder;

public class SearchItemAction extends BaseSpringAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        if (request.getParameter("search") != null) {
            String keyword = request.getParameter("keyword");
            ActionMessages messages = getMessages(request);
            if (keyword == null || keyword.length() == 0) {
                messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                        "search.noKey"));
                saveMessages(request, messages);
                return mapping.findForward("failure");
            } else {
                PagedListHolder itemList = new PagedListHolder(webStore
                        .searchItemList(keyword.toLowerCase()));
                itemList.setPageSize(appConfig.getWebPageSize());
                request.getSession().setAttribute("SearchItemAction_itemList",
                        itemList);
                request.setAttribute("itemList", itemList);
                return mapping.findForward("success");
            }
            //scrolling through pages
        } else {
            String page = request.getParameter("page");
            PagedListHolder itemList = (PagedListHolder) request.getSession()
                    .getAttribute("SearchItemAction_itemList");
            if ("next".equals(page)) {
                itemList.nextPage();
            } else if ("previous".equals(page)) {
                itemList.previousPage();
            }
            request.setAttribute("itemList", itemList);
            return mapping.findForward("success");
        }
    }

}