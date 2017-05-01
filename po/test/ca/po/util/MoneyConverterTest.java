package ca.po.util;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import ca.po.model.type.Money;
import ca.po.util.converter.MoneyCADConverterImpl;

/**
 * @author MOUELLET
 * 
 */
public class MoneyConverterTest extends TestCase {

    private Map convTable = new HashMap(); 
    
    public MoneyConverterTest(String name){
        super(name);
    }
    
    public void setUp(){
        convTable.put("USD","0.8");
        convTable.put("EUR","0.5");
    }
    
    public void testConvertToCAD(){
        MoneyCADConverterImpl conv = new MoneyCADConverterImpl();
        conv.setConversionTable(convTable);
        
        Money usMoney = Money.getUSD(0.80d);
        Money eurMoney = new Money(new BigDecimal("0.50"), Currency.getInstance("EUR") );
        Money canMoney = Money.getCAD(1.00d);
        assertEquals(canMoney,conv.convertToCAD(canMoney));
        assertEquals(canMoney,conv.convertToCAD(usMoney));
        assertEquals(canMoney,conv.convertToCAD(eurMoney));        
        
        try {
            conv.convertToCAD(new Money(1.00d, Currency.getInstance("GBP")));
            fail("should have generated IllegalArgumentException");
        }
        catch (IllegalArgumentException ex) {
            assertNotNull(ex);
            assertEquals(ex.toString(),"java.lang.IllegalArgumentException: Currency GBP cannot be converted");
        }
        
        
    }
    
    
}
