package ca.po.util.converter;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

import ca.po.model.type.Money;

/**
 * @author MOUELLET
 *	Simple class that implements MoneyCADConverter interface.
 *  It is used as a default CAD currency converter with currency
 *  exchange rate defined as Bean Properties in Spring ctx file.
 */
public class MoneyCADConverterImpl implements MoneyCADConverter {
    
    //Will hold collection like ("USD","0.8"), ("GBP","0.5"), etc..
    //set in the config xml file
    private Map conversionTable;
    
    
    public Money convertToCAD(Money fromMoney){
        //do nothing if already in CAD
        if (fromMoney.getCurrencyCode().equals("CAD")) return fromMoney;
        
        if (conversionTable.get(fromMoney.getCurrencyCode()) == null)
            throw new IllegalArgumentException("Currency " + fromMoney.getCurrencyCode() + " cannot be converted");
        //found a rate in the table        
        BigDecimal rate =  new BigDecimal((String) conversionTable.get(fromMoney.getCurrencyCode()));
        BigDecimal convValue = fromMoney.getDecimalAmount();
        convValue = convValue.divide(rate,BigDecimal.ROUND_HALF_UP);
        return new Money(convValue,Currency.getInstance("CAD"));
    }

    
    
    public Map getConversionTable() {
        return conversionTable;
    }
    public void setConversionTable(Map conversionTable) {
        this.conversionTable = conversionTable;
    }
}
