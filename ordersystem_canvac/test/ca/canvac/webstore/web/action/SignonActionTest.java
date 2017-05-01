package ca.canvac.webstore.web.action;

import servletunit.struts.MockStrutsTestCase;


public class SignonActionTest extends MockStrutsTestCase {

    public SignonActionTest(String testName){
        super(testName);
    }

    public void testFrmParameter(){

        setRequestPathInfo("/shop/signon");
        addRequestParameter("userId","-1");
        addRequestParameter("password","");
        actionPerform();
        verifyForward("failureSignOn");

        clearRequestParameters();
        addRequestParameter("userId","con000005");
        addRequestParameter("password","con000005");
        actionPerform();
        verifyForward("success");


    }



}
