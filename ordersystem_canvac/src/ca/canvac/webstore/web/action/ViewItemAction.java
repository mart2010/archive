package ca.canvac.webstore.web.action;

import ca.canvac.webstore.domain.SubCategory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.support.PagedListHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ViewItemAction extends BaseSpringAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String subCategoryId = request.getParameter("subCategoryId");
        if (subCategoryId != null) {
            PagedListHolder itemList = new PagedListHolder(webStore
                    .getItemListBySubCategory(subCategoryId));
            itemList.setPageSize(appConfig.getWebPageSize());
            SubCategory subCat = webStore.getSubCategory(subCategoryId);
            //I need to store to Session, because when user clicks
            // Next/Previous
            // the subCategory parameter is not set...
            request.getSession().setAttribute("ViewSubCategoryAction_itemList",
                    itemList);
            request.getSession().setAttribute(
                    "ViewSubCategoryAction_subCategory", subCat);
            request.setAttribute("itemList", itemList);
            request.setAttribute("subCategory", subCat);
        }
        //page scrolling...
        else {
            PagedListHolder itemList = (PagedListHolder) request.getSession()
                    .getAttribute("ViewSubCategoryAction_itemList");
            SubCategory subCategory = (SubCategory) request.getSession()
                    .getAttribute("ViewSubCategoryAction_subCategory");
            String page = request.getParameter("page");
            if ("next".equals(page)) {
                itemList.nextPage();
            } else if ("previous".equals(page)) {
                itemList.previousPage();
            }
            request.setAttribute("itemList", itemList);
            request.setAttribute("subCategory", subCategory);
        }
        return mapping.findForward("success");
    }

}