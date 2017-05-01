package ca.canvac.webstore.web.action;

import ca.canvac.webstore.domain.logic.WebStoreFacade;
import ca.canvac.webstore.AppConfig;

/*
 * User: OUELLM Date: Nov 2, 2004 Time: 12:21:11 PM Comments: this class is
 * Spring Enabled to allow subclass to use the Bean injected for accsing the
 * Business facade
 */
public class BaseSpringAction extends BaseAction {

    protected WebStoreFacade webStore = null;

    public void setWebStore(WebStoreFacade webStore) {
        this.webStore = webStore;
    }

    protected AppConfig appConfig = null;

    public void setAppConfig(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

}