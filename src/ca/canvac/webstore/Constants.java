package ca.canvac.webstore;


public class Constants {

    /**
     * File separator from System properties
     */
    public static final String FILE_SEP = System.getProperty("file.separator");
    public final static String LANG_EN = "English";

    public final static String LANG_FR = "Français";

    /**
     * To flag the special request order type *
     */
    public final static String SPECIAL_REQUEST = "Special Request";

    /**
     * User home from System properties
     */
    public static final String USER_HOME =
            System.getProperty("user.home") + FILE_SEP;
}
