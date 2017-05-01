package ca.po.web.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate.HibernateOptimisticLockingFailureException;

import ca.po.Constants;
import ca.po.model.Category;
import ca.po.model.Item;
import ca.po.model.ItemSupplied;
import ca.po.model.Product;
import ca.po.model.Supplier;
import ca.po.model.type.CurrencySupported;
import ca.po.model.type.Money;
import ca.po.web.form.ItemForm;
import ca.po.web.form.NameDescForm;

/**
 *
 * @struts.action path="/listItem" scope="request" parameter="list" 
 * @struts.action path="/editItem" name="itemForm" scope="request"
 * 		validate="false" parameter="edit"
 * @struts.action path="/saveItem" name="itemForm" scope="request"
 *  	validate="true" input="/WEB-INF/jsp/editItem.jsp" parameter="save"
 * @struts.action path="/deleteItem" parameter="delete"
 * @struts.action path="/newCat" name="nameDescForm" scope="request" 
 * 		validate="true" input="/listItem.html" parameter="newCat"
 * @struts.action path="/newProd" name="nameDescForm" scope="request" 
 * 		validate="true" input="/listItem.html" parameter="newProd"
 * @struts.action path="/deleteCat" parameter="deleteCat"
 * @struts.action path="/deleteProd" parameter="deleteProd"
 * 
 * @struts.action-forward name="list" path="/WEB-INF/jsp/listItem.jsp"
 * @struts.action-forward name="edit" path="/WEB-INF/jsp/editItem.jsp"
 * @struts.action-forward name="listAction" path="/listItem.html"   
 * @struts.action-forward name="listSupplierAction" path="/listSupplier.html"
 */

public class ItemAction extends BaseAction {

    /*
     * Used to obtain the current Category selected, possible choices:
     * 	1- existing category obtained from itemForm (user click a diff category)
     * 	2- existing category obtained by looking-up the session scope attribute
     * Will return "null" when two previous scenario fail (e.g. user first entrance 
     *  or category been removed from DB, needs default one)
     * 
     */
    private Category getSelCategory(HttpServletRequest request){

        String selCategoryId = request.getParameter("selCategoryId");
        Category selCategory = null;
        //catch any user click first
        if (selCategoryId != null) {
            selCategory = getItemManager().getCategory(new Long(selCategoryId) );
            //potentially the "selCategoryId" refers to a deleted category
            if (selCategory != null) {
                //invalidate selProduct
                request.getSession().removeAttribute("selProduct");
                return selCategory;
            }
        }

        //get category stored in Session that still exist in DB
        selCategory = (Category) request.getSession().getAttribute("selCategory");
        if ((selCategory != null) && (!selCategory.isNew()))
            return getItemManager().getCategory(selCategory.getId());

        return null;
    }
    
    /*
     * Used to obtain the current Product selected, possible choices:
     * 	1- existing product obtained from itemForm (user click a diff product)
     * 	2- existing product obtained by looking-up the session scope attribute
     * Will return "null" when two previous scenario fail (e.g. user first entrance 
     *  or product been removed from DB, needs default one)
     */
    private Product getSelProduct(HttpServletRequest request){
        
        String selProductId = request.getParameter("selProductId");
        Product selProduct = null;
        //catch any user click first
        if (selProductId != null) {
            return getItemManager().getProductOnly(new Long(selProductId) );
        }
        //get product stored in Session that still exist in DB
        selProduct = (Product) request.getSession().getAttribute("selProduct");
        if ((selProduct != null) && (!selProduct.isNew()))
             return getItemManager().getProductOnly(selProduct.getId());
        
        return null;
    }
    
    
    /*
     * Set categories, products and items in Request scope and 
     * selCategory and selProduct in Session (to keep current selection)
     * 
     * Use selCatId and selProdId set as request parameter to update 
     * current selection, note that these are mutually exclusive (see JSP view) 
     */
    public ActionForward list(ActionMapping mapping,ActionForm form,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {
        
        //if Cancel, clean-up session and forward to /home
        //we keep selCategory and selProduct active in Session (for user convenience)
        if (isCancelled(request)) {
            //this should link to global forward: home
            return mapping.findForward("home");
        }

        //store Categories in request scope
        List categories = getItemManager().getCategoriesWithProducts();      
        
        //if none categories exist yet, skip all the rest..        
        if ((categories == null) || (categories.isEmpty())) {
            List emptyList = new ArrayList();
            request.setAttribute("categories", emptyList);
            request.setAttribute("products", emptyList);
            request.setAttribute("items", emptyList);
            request.getSession().setAttribute("selCategory",new Category());
            request.getSession().setAttribute("selProduct",new Product());
            return mapping.findForward("list");
        }
        
        request.setAttribute("categories",categories );
        Category selCategory = getSelCategory(request);
        //Current Selection was neither obtained from Request nor Session 
        //typically occur for first Action call (or category been removed)
        if (selCategory == null) {
            selCategory = (Category) categories.get(0);
        } 
        //update current Category selection in Session
        request.getSession().setAttribute("selCategory",selCategory);
        
        //if none products exist yet for category, skip the rest..
        if (selCategory.getProducts() == null || (selCategory.getProducts().isEmpty())) {
            List emptyList = new ArrayList();
            request.setAttribute("products", emptyList);
            request.setAttribute("items", emptyList);
            request.getSession().setAttribute("selProduct",new Product());    
            return mapping.findForward("list");
        }
        
        //store Products as List in request scope 
        ArrayList products = new ArrayList(selCategory.getProducts());
        request.setAttribute("products", products);
        
        Product selProduct = getSelProduct(request);
        /*
         * get default selProduct whenever:
         *  1- no selProduct stored yet in session or also when, 
         *  2- user click on select Category which made selProduct to be invalidated
         */
        if (selProduct == null )
            selProduct = (Product) products.get(0);

        //update current Product selection in Session
        request.getSession().setAttribute("selProduct",selProduct);

        //Get Items children of selProduct
        List items = getItemManager().getItemsByProduct(selProduct.getId());
        request.setAttribute("items",items );

        //return a forward to list
        return mapping.findForward("list");
    }
    
    /*
     * This is entered via:
     * 1- User selected an existing item from the list (request param itemId)
     * 2- User created a new item (request param itemId=-1)
     * 3- User has created a new Supplier and got re-forwarded here (like 3)
     *  
     */
    public ActionForward edit(ActionMapping mapping,ActionForm form,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {
        
        //Prepare look-up for drop-downs
        if (request.getSession().getAttribute("currencies") == null)
            request.getSession().setAttribute("currencies",CurrencySupported.CURRENCIES_TEXT);
        if (request.getSession().getAttribute("suppliers") == null)
            request.getSession().setAttribute("suppliers",getSupplierManager().getSuppliers());
        
        ItemForm itemForm = (ItemForm) form;
        Item item = null;
        String itemId = request.getParameter("itemId");

        /*
         * scenario 3 
         */
        if (itemId == null) {
            item = (Item) request.getSession().getAttribute("selItem");
            assert (item != null);
            refreshForm(itemForm,item);
            return mapping.findForward("edit");
        }

        /*
         * scenario 1 and 2: itemId is found in Request Param (either new Item or existing)
         */
        if (itemId.equals(Constants.NEW_ENTITY_REQUEST_VALUE)) {
            item = new Item();
        }  
        else {
            //Existing Item to be edited (product is fetched as well)
            item = getItemManager().getItem(new Long(itemId),true,true);
        }
        
        //for existing item, update form
        if (!item.isNew()) {
            refreshForm(itemForm,item);
        }
        
        request.getSession().setAttribute("selItem",item);
        //return a forward to edit
        return mapping.findForward("edit");

   }

    private void refreshForm(ItemForm itemForm, Item item) throws Exception {
        //copy properties to the form bean
        BeanUtils.copyProperties(itemForm,item);
        //need to convert ListPrice to ListPriceString, check for better way?
        itemForm.setListPriceString(
                item.getListPrice().getDecimalAmount().toString());
        itemForm.setListPriceCurrencyString(
                item.getListPrice().getCurrencyCode());
        //copy the itemSupplies
        List tempList = new ArrayList(item.getItemSupplies());
        for (int i=0; i<tempList.size(); i++){
            itemForm.setItemSupplier(i,(ItemSupplied) tempList.get(i));
        }
        
    }
    
    private void cleanSessionAfterSave(ActionMapping mapping, HttpServletRequest request){
        request.getSession().removeAttribute("selItem");
        request.getSession().removeAttribute("currencies");
        request.getSession().removeAttribute("suppliers");
        removeFormBean(mapping, request);
    }

    
    /*
     * Assumes the Item being saved is stored in Session scope ("selItem")
     * and also assumes that it is associated with a valid persisted Product (see edit)
     * N.B. for new Item, it also requires "selProduct" in Session scope to link to new Item
     */
    public ActionForward save(ActionMapping mapping,ActionForm form,
            		HttpServletRequest request,HttpServletResponse response) throws Exception {
        
        //if Cancel, clean-up session and forward to /listAction
        if (isCancelled(request)) {
            cleanSessionAfterSave(mapping,request);
            //avoid the list action to interpret this Cancel action
            request.removeAttribute(Globals.CANCEL_KEY);
            return mapping.findForward("listAction");
        }

        ItemForm itemForm = (ItemForm) form;
        Item itemSelected = (Item) request.getSession().getAttribute("selItem");
        if (itemSelected== null) {
            throw new IllegalAccessException("Cannot proceed with a non-valid Item");
        }
        
        //store the changed properties
        BeanUtils.copyProperties(itemSelected,itemForm);
        //adjust for the ListPriceString and ListPriceCurrencyString
        Money money = new Money(
                new BigDecimal(itemForm.getListPriceString()),
                Currency.getInstance(itemForm.getListPriceCurrencyString()));
        itemSelected.setListPrice(money);
        
       
        //clean and get the updated ItemSupplied info
        if (itemSelected.getItemSupplies()!= null)
                itemSelected.getItemSupplies().clear();

        for (int i=0; i < itemForm.getItemSuppliers().size(); i++ ) {
            if (! itemForm.getItemSupplier(i).
                    getSupplier().getId().equals(new Long(Constants.NEW_ENTITY_REQUEST_VALUE))){
                Supplier supplier = getSupplierManager().
                		getSupplier(itemForm.getItemSupplier(i).
                                getSupplier().getId(),false);
                String bidinfo = itemForm.getItemSupplier(i).getBidInfo();
                String bidyear = itemForm.getItemSupplier(i).getBidYear();
                String catalogNb = itemForm.getItemSupplier(i).getCatalogNumber();
                
                itemSelected.addSupplier(supplier,getUser().getName(),bidinfo,bidyear,catalogNb);
            }
        }

        //if user click goToSupplierList reforward appropriately
        if (request.getParameter("goToListSupplier").length()!=0) {
            //force a refresh of suppliers
            request.getSession().removeAttribute("suppliers");
            putNextActionForward(mapping,request,"editItemAction");
            return mapping.findForward("listSupplierAction");
        }

        ActionMessages messages = getMessages(request);
        //check which product to link to (only used for new item)
        Product selProd = (Product) request.getSession().getAttribute("selProduct");
        assert selProd!=null;
        //save item and clean session..        
        try {
            getItemManager().saveItem(itemSelected,selProd,getUser().getName());
            messages.add(ActionMessages.GLOBAL_MESSAGE, 
                	new ActionMessage("item.saveSuccess"));
            saveMessages(request, messages);
        } 
        //the item is not unique (Exception thrown by defineProduct method)
        catch (IllegalStateException ex) {
            messages.add(ActionMessages.GLOBAL_MESSAGE, 
                	new ActionMessage("item.duplicateError"));
        	saveMessages(request, messages);
        	return mapping.findForward("edit");
        }
        catch (DataIntegrityViolationException ex){
            messages.add(ActionMessages.GLOBAL_MESSAGE, 
                	new ActionMessage("item.integrityError"));
        	saveMessages(request, messages);
        } 
        catch (HibernateOptimisticLockingFailureException ex){
            messages.add(ActionMessages.GLOBAL_MESSAGE, 
                	new ActionMessage("item.optimisticLockError"));
        	saveMessages(request, messages);
        }
        cleanSessionAfterSave(mapping,request);
        return mapping.findForward("listAction");
    }

    //assumes itemId request param
    public ActionForward delete(ActionMapping mapping,ActionForm form,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {

        ActionMessages messages = getMessages(request);
        String itemId = request.getParameter("itemId");
        assert ((itemId != null) && (itemId.length()>0));

        try {
            int nb = getItemManager().deleteItem(new Long(itemId));
            if (nb==1){
                messages.add(ActionMessages.GLOBAL_MESSAGE, 
                    	new ActionMessage("item.deleteSuccess"));
            } else if (nb==0){
                messages.add(ActionMessages.GLOBAL_MESSAGE, 
                    	new ActionMessage("item.alreadyDeleted"));
            } else assert false;
            saveMessages(request, messages);
        }
        catch (IllegalArgumentException ex) {
            //delete an item linked to PO
            messages.add(ActionMessages.GLOBAL_MESSAGE, 
                    	new ActionMessage("item.NotEmptyDeleteError"));
            saveMessages(request, messages);
            return mapping.findForward("editItemAction");
        }
        
        request.getSession().removeAttribute("selItem");
        return mapping.findForward("listAction");
        
    }
    
    
    public ActionForward newCat(ActionMapping mapping,ActionForm form,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {
        
        NameDescForm nameDescForm = (NameDescForm) form;
        ActionMessages messages = getMessages(request);
        
        Category newCat = new Category(nameDescForm.getName(),nameDescForm.getDesc());
        //should maybe catch any excpetion ... such as duplicated name in DB...
        try {
            getItemManager().saveCategory(newCat,getUser().getName());
            messages.add(ActionMessages.GLOBAL_MESSAGE, 
                	new ActionMessage("newCat.success"));
            saveMessages(request, messages);
        }
        catch (DataIntegrityViolationException ex) {
            messages.add(ActionMessages.GLOBAL_MESSAGE, 
                	new ActionMessage("cat.integrityError"));
        	saveMessages(request, messages);
        }
        
        //clean-up the form
        nameDescForm.reset(mapping,request);
        return mapping.findForward("listAction");
    }
    
    /*
     * Assumes "selCategory" in session scope to link to new product created
     */
    public ActionForward newProd(ActionMapping mapping,ActionForm form,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {
        
        NameDescForm nameDescForm = (NameDescForm) form;
        ActionMessages messages = getMessages(request);
        
        Category selCat = (Category) request.getSession().getAttribute("selCategory");
        //selCategory.. e.g. user press cancel and went back to save?
        if (selCat==null) {
            messages.add(ActionMessages.GLOBAL_MESSAGE, 
                	new ActionMessage("newProd.noCat"));
            saveMessages(request, messages);
            return mapping.findForward("listAction");
        }

        Product newProd = new Product(nameDescForm.getName(),nameDescForm.getDesc());
        try {
            getItemManager().saveProduct(newProd,selCat,getUser().getName());    
            messages.add(ActionMessages.GLOBAL_MESSAGE, 
                	new ActionMessage("newProd.success"));
            saveMessages(request, messages);
        }
        //the product is not unique (Exception thrown by defineCategory method)
        catch (IllegalStateException ex) {
            messages.add(ActionMessages.GLOBAL_MESSAGE, 
                	new ActionMessage("prod.duplicateError"));
        	saveMessages(request, messages);
        	return mapping.findForward("edit");
        }
        catch (DataIntegrityViolationException ex) {
            messages.add(ActionMessages.GLOBAL_MESSAGE, 
                	new ActionMessage("prod.integrityError"));
        	saveMessages(request, messages);
        }

        //clean-up the form
        nameDescForm.reset(mapping,request);
        return mapping.findForward("listAction");
    }
    
    
    public ActionForward deleteCat(ActionMapping mapping,ActionForm form,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {
        
        ActionMessages messages = getMessages(request);
        String catId = request.getParameter("catId");
        assert catId!=null;

        //catch the non-empty category DB error
        try {
            int nb = getItemManager().deleteCategory(new Long(catId));
            //reset current selection in session
            request.getSession().setAttribute("selCategory",new Category());
            request.getSession().setAttribute("selProduct",new Product());
            if (nb==1) {
            	messages.add(ActionMessages.GLOBAL_MESSAGE, 
                	new ActionMessage("cat.deleteSuccess"));
            } else if (nb==0){
                messages.add(ActionMessages.GLOBAL_MESSAGE, 
                    	new ActionMessage("cat.alreadyDeleted"));
            } else assert false;
            saveMessages(request, messages);
        } 
        catch (IllegalArgumentException ex) {
            //cat not empty
            messages.add(ActionMessages.GLOBAL_MESSAGE, 
                    	new ActionMessage("cat.NotEmptyDeleteError"));
            saveMessages(request, messages);
        }
        
        return mapping.findForward("listAction");
    }
    
    
    public ActionForward deleteProd(ActionMapping mapping,ActionForm form,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {
        
        ActionMessages messages = getMessages(request);
        String prodId = request.getParameter("prodId");
        assert prodId!=null;

        try {
            int nb = getItemManager().deleteProduct(new Long(prodId));
            //reset current selection in session
            request.getSession().setAttribute("selProduct",new Product());
            if (nb==1) {
                messages.add(ActionMessages.GLOBAL_MESSAGE, 
                    	new ActionMessage("prod.deleteSuccess"));
            } else if (nb==0){
                messages.add(ActionMessages.GLOBAL_MESSAGE, 
                    	new ActionMessage("prod.alreadyDeleted"));
            } else assert false;
            saveMessages(request, messages);
        } 
        catch (IllegalArgumentException ex) {
            //product not empty
            messages.add(ActionMessages.GLOBAL_MESSAGE, 
                    	new ActionMessage("prod.NotEmptyDeleteError"));
            saveMessages(request, messages);
        }
        
        return mapping.findForward("listAction");
    }
    

    
}
