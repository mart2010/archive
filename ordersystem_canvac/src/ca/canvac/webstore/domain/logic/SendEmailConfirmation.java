package ca.canvac.webstore.domain.logic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;


/*
 * This bean is used to configure the email sender in WebStoreImpl
 * It is instanciated with Spring context file, all instance variable except mailMessage
 * are invariant (value set during injection). 
 * It validates its state with init() method:
 * Scenario 1: ServiceActive is set to false - no state validation
 * Scenario 2: ServiceActive is set to true - validation must occur so that,
 * the Bean instanciation fails and Web app does not start-up
 * Scenario 3: no ServiceActive is set, by default this will be the same as Scenario 1
 */

public class SendEmailConfirmation {

    private final Log logger = LogFactory.getLog(getClass());
    //attributes implemented via a Spring Bean for external configuration
    private MailSender mailSender;
    //By Default Mail Service is Not Active
    private boolean mailServiceActive = false;
    private String staticMessageText;
    private String[] mailCC;
    private String[] mailTo;
    private String mailFrom;
    private String subject;
   
    
    public SendEmailConfirmation() {
    }

    public boolean getMailServiceActive() {
        return mailServiceActive;
    }


    //called by Bean factory
    public void init() {
        if (this.mailServiceActive) {
            validateState();
        }
    }

    //it is now generalised to accept Object with toString() used as message  
    public boolean sendMail(Object obj) {
        if (this.mailServiceActive) {
            //Local mailMessage is used to avoid Synchronization
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setCc(this.mailCC);
            mailMessage.setFrom(this.mailFrom);
            mailMessage.setTo(this.mailTo);
            mailMessage.setSubject(this.subject);
            //add Order details info with Order.toString() 
            mailMessage.setText(staticMessageText + "\n" + obj);
            try {
                this.mailSender.send(mailMessage);
                return true;
            } catch (MailException ex) {
                // just log it and go on
                logger.warn("An exception occured when trying to send email", ex);
                return false;
            }
        }
        return false;
    }
    
    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    //boolean value in XML config must be = 1 or true
    public void setMailServiceActive(boolean mailServiceActive) {
        this.mailServiceActive = mailServiceActive;
    }

    public void setMailCC(String[] mailCC) {
        	this.mailCC = mailCC;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }
    
    public void setMailTo(String[] mailTo) {
        this.mailTo = mailTo;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public void setStaticMessageText(String txt) {
        this.staticMessageText = txt;
    }

    private void validateState() {
        if (this.mailSender == null)
            throw new IllegalStateException("mailSender is required");
        if ((this.mailTo == null) || this.mailTo.length == 0)
            throw new IllegalStateException("mailTo is required");
        if ((this.mailFrom == null) || this.mailFrom.equals(""))
            throw new IllegalStateException("mailFrom is required");
    }

}
