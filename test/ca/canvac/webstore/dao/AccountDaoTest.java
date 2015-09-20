package ca.canvac.webstore.dao;

import ca.canvac.webstore.domain.Account;
import org.springframework.dao.DataAccessException;
import org.springframework.beans.BeanUtils;



public class AccountDaoTest extends BaseDaoTest {

    AccountDao accDao = null;
    Account acc = null;


    protected void setUp() throws Exception {
        super.setUp();
        accDao = (AccountDao) ctx.getBean("accountDao");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        accDao = null;
    }

    private static final String EX = "Exception";
    private static final String AC = "Account";
    private static final String NL = "null";

    private void checkAcc(String username, String passwd, String expected, boolean withPasswd) {
        Account a = null;
        try {
            if (withPasswd) { a = accDao.getAccount(username,passwd); }
            else  { a = accDao.getAccount(username); }

            if (NL.equals(expected)) { assertNull(a); }
            else if (EX.equals(expected)) { fail("Error: Expected DataAccessException to be thrown"); }
            else if (AC.equals(expected)) {
                assertNotNull(a);
                assertEquals(a.getUserId(),username);
            }
        }
        catch (DataAccessException e) {
            if (EX.equals(expected)) assertTrue(true);
            else {
                fail("Error: DataAccessExpetion was thrown but not expected");
            }
        }
        catch (Exception e) {
            fail("Only DataAccessException should be thrown");
        }

    }

    private void checkAcc(String username, String expected) {
        checkAcc(username,"",expected,false);
    }


    public void testGetAccount() {
        //test various cases with only username
        checkAcc("",NL);
        checkAcc("!badjoker!",NL);
        checkAcc("con000001",AC);

        //test various cases with username,password
        checkAcc("","",NL,true);
        checkAcc("!badjoker!","",NL,true);
        checkAcc("!badjoker!","!badjoker!",NL,true);
        checkAcc("con000006","",NL,true);

        //test a good case
        String accU = (String) accDao.getUsernameList().get(300);
        Account acc = accDao.getAccount(accU);
        assertEquals(accU,acc.getUserId());
        //will only work when passwd=username
        checkAcc(acc.getUserId(),acc.getUserId(),AC,true);

    }
    //Note that this will change the HSQL database content..
    //but will saved back the original data
    public void testUpdateAccout() throws Exception {
        String accU = (String) accDao.getUsernameList().get(200);
        Account oriAcc = accDao.getAccount(accU);
        Account targetAcc = new Account();
        BeanUtils.copyProperties(oriAcc,targetAcc);

        //modify some attr
        targetAcc.setAddress1("add");
        targetAcc.setCity("111");
        accDao.updateAccount(targetAcc);

        //check the changed attr
        assertEquals(targetAcc.getAddress1(),
                accDao.getAccount(oriAcc.getUserId()).getAddress1());
        assertEquals(targetAcc.getCity(),
                accDao.getAccount(oriAcc.getUserId()).getCity());

        //check a not-null field
        targetAcc.setFirstName(null);
        try {
            accDao.updateAccount(targetAcc);
            fail("Error: exception expected");
        } catch (DataAccessException ex) {
            assertTrue(true);
        } catch (Exception ex) {
            fail("unexpected Exception was thrown: " + ex );
        }

        //change back to the original account
        accDao.updateAccount(oriAcc);
        assertEquals(accDao.getAccount(oriAcc.getUserId()).getAddress1(),
                oriAcc.getAddress1());
        assertEquals(accDao.getAccount(oriAcc.getUserId()).getCity(),
                oriAcc.getCity());

    }

}
