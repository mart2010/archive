package ca.po.model.type;

import java.util.List;

import junit.framework.TestCase;

public class PoStatusCodeTest extends TestCase {

    public PoStatusCodeTest(String arg0) {
        super(arg0);
    }

    public void testPoStatusCode() {
        PoStatusCode Valid = PoStatusCode.VALIDATED;
        assertEquals(PoStatusCode.CODES().size(),5);
        List fromOrder = (List) PoStatusCode.RANGECODES(Valid);
        assertEquals(fromOrder.size(),3);
        assertEquals(fromOrder.get(0),PoStatusCode.ORDERED);
        System.out.println(PoStatusCode.RANGECODES(PoStatusCode.PROPOSED));
        System.out.println(PoStatusCode.RANGECODES(PoStatusCode.CANCELLED));
        System.out.println(PoStatusCode.CODES());
    }
    
}
