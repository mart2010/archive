package ca.po.model.type;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * @author MOUELLET
 *  Typesafe enumeration class for Purchase Order Status Code
 *  Purchase order has to follow this seqence:
 *  "Proposed", "Validated", "Ordered", "Received". 
 *  Note that code="Cancelled" can be also set at any point in the sequence
 * (except after "Received" or before "Proposed")
 * 
 * 
 */
public class PoStatusCode implements Serializable {
  
    private final String code;

    
    public static final PoStatusCode PROPOSED = new PoStatusCode("Proposed");
    public static final PoStatusCode VALIDATED = new PoStatusCode("Validated");
    public static final PoStatusCode ORDERED = new PoStatusCode("Ordered"); 
    public static final PoStatusCode RECEIVED = new PoStatusCode("Received");
    public static final PoStatusCode CANCELLED = new PoStatusCode("Cancelled");
    //will preserve the insertion order
    private static final Map MAP_INSTANCES = new LinkedHashMap();
    private static final List LIST_CODES;
    private static final List LIST_FOR_VALIDATED;
    private static final List LIST_FOR_ORDERED;
    
    
    static {
        MAP_INSTANCES.put(PROPOSED.toString(),PROPOSED);
        MAP_INSTANCES.put(VALIDATED.toString(),VALIDATED);
        MAP_INSTANCES.put(ORDERED.toString(),ORDERED);
        MAP_INSTANCES.put(RECEIVED.toString(),RECEIVED);
        MAP_INSTANCES.put(CANCELLED.toString(),CANCELLED);
        LIST_CODES = new ArrayList(MAP_INSTANCES.values());
        LIST_FOR_VALIDATED = new ArrayList(MAP_INSTANCES.values());
        LIST_FOR_VALIDATED.remove(PoStatusCode.PROPOSED);
        LIST_FOR_VALIDATED.remove(PoStatusCode.RECEIVED);
        LIST_FOR_ORDERED = new ArrayList(MAP_INSTANCES.values());
        LIST_FOR_ORDERED.remove(PoStatusCode.PROPOSED);
        LIST_FOR_ORDERED.remove(PoStatusCode.VALIDATED);
    }
    
    public static final List CODES() {
        return LIST_CODES;
    }

    /*
     * Method that returns all Codes subsequent to "fromStatus"
     */
    public static final List RANGECODES(PoStatusCode fromStatus){
        return ( LIST_CODES.subList(fromStatus.ordinal+1, LIST_CODES.size()));
    }
    
    /*
     * Method that returns all Codes relevant for Validated Type PO
     */
    public static final List CODES_FOR_VALIDATED(PoStatusCode fromStatus){
        return ( LIST_CODES.subList(fromStatus.ordinal+1, LIST_CODES.size()));
    }
    
    public static List getLIST_FOR_ORDERED() {
        return LIST_FOR_ORDERED;
    }
    public static List getLIST_FOR_VALIDATED() {
        return LIST_FOR_VALIDATED;
    }
    
    //  Ordinal of next StatusInfo to be created
    private static int nextOrdinal = 0;
    
    private PoStatusCode(String code){
        this.code = code;
    }
    // Assign an ordinal to this StatusCode
    public final int ordinal = nextOrdinal++;
    
    
    /*
     * This method, which is invoked automatically by the serialization system, 
     * prevents duplicate constants from coexisting as a result of deserialization
     */
    Object readResolve() throws ObjectStreamException  {
        return getInstance(code);
    }

    public static PoStatusCode getInstance(String nameCode) {
        return (PoStatusCode) MAP_INSTANCES.get(nameCode);
    }
    
    public String toString(){
        return this.code;
    }

    public String getCode(){
        return this.code;
    }
    
    
}
