package ca.po;


/**
 * 
 * This Bean is used for external configuration and
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
