package ca.canvac.webstore;


/**
 * Author: Martin
 * Date: Sep 15, 2004
 * Time: 11:14:03 AM
 * Comments: This Bean is used for external configuration and
 * loaded from the ApplicationContext Spring BeanFactory
 */
public class AppConfig {

    //default page size=10
    private int webPageSize = 10;

    public int getWebPageSize() {
        return webPageSize;
    }

    public void setWebPageSize(int webPageSize) {
        this.webPageSize = webPageSize;
    }


}
