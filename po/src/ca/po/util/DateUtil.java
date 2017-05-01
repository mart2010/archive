package ca.po.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * Date Utility Class
 * This is used to convert Strings to Dates and Timestamps
 *
 */
public class DateUtil {

    public final static String PATTERN_DEFAULT = "dd/MM/yyyy";
    
    public final static String PATTERN_MM_DD_YYYY = "MM/dd/yyyy";
    public final static String PATTERN_DD_MM_YYYY = "dd/MM/yyyy";
    public final static String PATTERN_DD_MM_YY = "dd/MM/yy";
    
    public final static String timePattern = "HH:mm";



    
    /**
     * This method generates a string representation of a date/time
     * in the format specified on input
     *
     * Return null when parsing returned error
     * 
     */
    public static final Date convertStringToDate(String aMask, String strDate) {
        
        SimpleDateFormat df = new SimpleDateFormat(aMask);
        //enforce strict adherence to the format 
        df.setLenient(false);
        Date returnedDate;
        try {
            returnedDate = df.parse(strDate);
            return returnedDate;
        } catch (ParseException ex){
            return null;
        } catch (Exception ex) {
            return null;
        }
         
    }

    
    public static final Date convertDefaultStringToDate(String strDate) {
      return convertStringToDate(PATTERN_DEFAULT,strDate);
    }
    

    /**
     * This method generates a string representation of a date's date/time
     * in the format you specify on input
     *
     * @param aMask the date pattern the string is in
     * @param aDate a date object
     * @return a formatted string representation of the date
     * 
     */
    public static final String convertDateToString(String aMask, Date aDate) {
        SimpleDateFormat df = new SimpleDateFormat(aMask);
        
        return df.format(aDate);
        
    }

    
    public static final String convertDateToDefaultString(Date aDate) {
        return convertDateToString(PATTERN_DEFAULT, aDate);
    }

    
    
}
