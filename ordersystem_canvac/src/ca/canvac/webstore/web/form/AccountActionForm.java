package ca.canvac.webstore.web.form;

import ca.canvac.webstore.domain.Account;
import ca.canvac.webstore.Constants;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class AccountActionForm extends ValidatorForm {

    /* Constants */
    private static final ArrayList LANGUAGE_LIST = new ArrayList();

    /* Private Fields */
    private String userId;

    private String password;

    private String repeatedPassword;

    private List languages;

    private String validate;

    private String forwardAction;

    private Account account;

    /* Static Initializer */

    static {
        LANGUAGE_LIST.add(Constants.LANG_EN);
        LANGUAGE_LIST.add(Constants.LANG_FR);
    }

    /* Constructors */

    public AccountActionForm() {
        languages = LANGUAGE_LIST;

    }

    /* JavaBeans Properties */

    public String getForwardAction() {
        return forwardAction;
    }

    public void setForwardAction(String forwardAction) {
        this.forwardAction = forwardAction;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatedPassword() {
        return repeatedPassword;
    }

    public void setRepeatedPassword(String repeatedPassword) {
        this.repeatedPassword = repeatedPassword;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getValidate() {
        return validate;
    }

    public void setValidate(String validate) {
        this.validate = validate;
    }

    public List getLanguages() {
        return languages;
    }

    public void setLanguages(List languages) {
        this.languages = languages;
    }

    //this is called to extend the validate fct so that it can
    //support validation on two fields: password & confirmpasswd.
    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = super.validate(mapping, request);

        //the user tried to change his password
        if ((account.getPassword() != null && account.getPassword().length() > 0)
                || (repeatedPassword != null && repeatedPassword.length() > 0)) {
            if (!(account.getPassword().equals(repeatedPassword))) {
                errors.add("password", new ActionError(
                        "errors.password.nomatch"));
            }
        }
        return errors;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        setUserId(null);
        setPassword(null);
        setRepeatedPassword(null);
    }

}