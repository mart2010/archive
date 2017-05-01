package ca.canvac.webstore.web.action;

import java.util.Date;
import ca.canvac.webstore.dao.OrderDao;
import ca.canvac.webstore.dao.ibatis.SqlMapOrderDao;
import ca.canvac.webstore.web.form.OrderActionForm;


public class NewOrderActionTest extends ActionBaseTest {

    public NewOrderActionTest(String testName){
        super(testName);
    }

    private void setReqParam(String fedex, String fname, String lname, String addr,
                             String city, String prov, String country, String postal ) {
        addRequestParameter("order.fedex",fedex);
        addRequestParameter("order.shipToFirstName",fname);
        addRequestParameter("order.shipToLastName",lname);
        addRequestParameter("order.shipAddress1",addr);
        addRequestParameter("order.shipCity",city);
        addRequestParameter("order.shipProvState",prov);
        addRequestParameter("order.shipCountry",country);
        addRequestParameter("order.shipPostalZip",postal);
    }
    
    
    public void testNotOkFrmParameter(){
        //Try to acces newOrder with no OrderFrm, Validator should generate errors
        setRequestPathInfo("/shop/newOrder");
        actionPerform();
        verifyActionErrors(new String[] { "errors.required" ,
                                          "errors.required",
                                          "errors.required",
                                          "errors.required",
                                          "errors.required",
                                          "errors.required",
                                          "errors.required",
                                          "errors.required" } );
        verifyForwardPath("/WEB-INF/jsp/NewOrderForm.jsp");
        clearRequestParameters();

        //Test a few error case
        setReqParam("777777777","","","","","","","");
        actionPerform();
        verifyActionErrors(new String[] { "errors.required",
                                          "errors.required",
                                          "errors.required",
                                          "errors.required",
                                          "errors.required",
                                          "errors.required",
                                          "errors.required" } );
        clearRequestParameters();

        setReqParam("11","ff","ll","aa","cc","pp","cc","pp");
        actionPerform();
        verifyActionErrors(new String[] { "errors.invalidfedex" });

    }


    public void testOkFrmParam () {

        setRequestPathInfo("/shop/newOrder");
        setReqParam("8888-8888-8","fname","lname","address1","city1","prov1","country1","postal1");
        actionPerform();

        //no AcctForm in Session should be caught by SecureAction
        verifyForward("global-signon");

        //set a dummy AccForm to avoid "global-signon"
        getSession().setAttribute("accountForm",NewOrderFormActionTest.getAccountFrm("11",""));

        OrderActionForm orderFrm = (OrderActionForm) getActionForm();
        orderFrm.getOrder().setUsername("con000006");
        orderFrm.getOrder().setOrderDate(new Date());

        //Order in
        actionPerform();
        verifyForward("success");

        //Cleaning up this order
        long id = orderFrm.getOrder().getOrderId();
        OrderDao dao = (OrderDao) ctx.getBean("orderDao");
        ((SqlMapOrderDao) dao).removeOrder(id);
    }



}
