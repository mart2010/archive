package ca.po.model.type;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

/**
 * @author MOUELLET
 *
 * Convenient Enumerated class holding all Currency type 
 * supported by the Web app
 */
public class CurrencySupported {

    private final Currency currency;
    
    public static final CurrencySupported CAD = 
        new CurrencySupported(Currency.getInstance("CAD"));
    public static final CurrencySupported USD = 
        new CurrencySupported(Currency.getInstance("USD"));
    public static final CurrencySupported EUR = 
        new CurrencySupported(Currency.getInstance("EUR"));
    public static final CurrencySupported GBP = 
        new CurrencySupported(Currency.getInstance("GBP"));
    
    public static final List CURRENCIES = new ArrayList();
    static {
        CURRENCIES.add(CurrencySupported.CAD);
        CURRENCIES.add(CurrencySupported.USD);
        CURRENCIES.add(CurrencySupported.EUR);
        CURRENCIES.add(CurrencySupported.GBP);
    }
    
    public static final List CURRENCIES_TEXT = new ArrayList();
    static {
        CURRENCIES_TEXT.add(CurrencySupported.CAD.toString());
        CURRENCIES_TEXT.add(CurrencySupported.USD.toString());
        CURRENCIES_TEXT.add(CurrencySupported.EUR.toString());
        CURRENCIES_TEXT.add(CurrencySupported.GBP.toString());
    }
    
    
    private CurrencySupported(Currency cur){
        this.currency = cur;
    }

    public String toString(){
        return this.currency.getCurrencyCode();
    }

}
