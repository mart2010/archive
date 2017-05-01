package ca.po.model.type;

import junit.framework.TestCase;

/**
 * @author MOUELLET
 *
 */
public class MoneyTest extends TestCase {
    
    public MoneyTest(String name){
        super(name);
    }
    
    public void testMoneyMult() throws Exception {
        Money mon1 = Money.getCAD(20.01d);
        Money mon2 = mon1.multiply(10l);
        assertEquals(mon2, Money.getCAD(200.10d));
        System.out.println(mon2);
        
    }
    

}
