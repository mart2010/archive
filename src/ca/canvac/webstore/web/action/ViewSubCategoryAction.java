package ca.canvac.webstore.web.action;

import ca.canvac.webstore.domain.Category;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ViewSubCategoryAction extends BaseSpringAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String categoryId = request.getParameter("categoryId");
        if (categoryId != null) {
            Category category = webStore.getCategory(categoryId);
            List subCategoryList = webStore
                    .getSubCategoryListByCategory(categoryId);
            request.setAttribute("categoryName", category.getName());
            request.setAttribute("subCategoryList", subCategoryList);
        } else {
            throw new IllegalStateException("Cannot find sub category list");
        }

        return mapping.findForward("success");
    }

}