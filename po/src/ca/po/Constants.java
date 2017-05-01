package ca.po;


public class Constants {

    /**
     * File separator from System properties
     */
    public static final String FILE_SEP = System.getProperty("file.separator");

    public static final String LANG_EN = "English";
    public static final String LANG_FR = "Français";


    /**
     * User home from System properties
     */
    public static final String USER_HOME =
            System.getProperty("user.home") + FILE_SEP;


    /** Application scoped attributes for SSL Switching */
    public static final String HTTP_PORT = "httpPort";
    public static final String HTTPS_PORT = "httpsPort";

    /**
     * The session scope attribute under which the User object for the
     * currently logged in user is stored.
     */
    public static final String USER_KEY = "currentUser";

    /**
     * Flag for identifying new Entity (e.g. Item, Supplier..) creation 
     */
    public static final String NEW_ENTITY_REQUEST_VALUE = "-1";
    
    
    /** Application scoped attribute for authentication url */
    public static final String AUTH_URL = "authURL";

    /** The encryption algorithm key to be used for passwords */
    public static final String ENC_ALGORITHM = "algorithm";

    /** A flag to indicate if passwords should be encrypted */
    public static final String ENCRYPT_PASSWORD = "encryptPassword";

    /** The application scoped attribute for indicating a secure login */
    public static final String SECURE_LOGIN = "secureLogin";
    
}
