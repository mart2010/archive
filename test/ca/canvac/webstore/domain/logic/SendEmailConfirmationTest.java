package ca.canvac.webstore.domain.logic;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import junit.framework.TestCase;
import ca.canvac.webstore.domain.Order;

public class SendEmailConfirmationTest extends TestCase {


    public SendEmailConfirmationTest(String name) {
        super(name);
    }

    public void testSendOrderInit(){
        SendEmailConfirmation sendBean = new SendEmailConfirmation();
        sendBean.setMailSender(new JavaMailSenderImpl());
        sendBean.setMailTo(new String[] {"bla"});
        sendBean.setMailCC(new String[] {"bla"});
        sendBean.setMailFrom(null);
        //missing "From" attribute
        try {
            sendBean.init();
            //the MailService is not active, so send should be ignored silently
            sendBean.sendMail(new Order());
            sendBean.setMailServiceActive(true);
            sendBean.init();
            fail("should have generated IllegalStateException");
        } catch (IllegalStateException ex) {
            assertNotNull(ex);
        }
        sendBean.setMailFrom("bla");
        sendBean.init();
        assertTrue(sendBean.getMailServiceActive());
    }

    
    
    
}