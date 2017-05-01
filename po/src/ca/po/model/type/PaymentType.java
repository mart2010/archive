package ca.po.model.type;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * @author MOUELLET
 *  Typesafe enumeration class for PaymentType
 * 
 */
public class PaymentType implements Serializable{
  
    public static final PaymentType PO = new PaymentType("PO");
    public static final PaymentType INTERNET = new PaymentType("Internet");
    public static final PaymentType CARTE_ACHAT = new PaymentType("Carte-Achat");
    public static final PaymentType DA = new PaymentType("Dem. d'Approv.");
    
    public static final Map PAYMENT_TYPES = new HashMap();
    static {
        PAYMENT_TYPES.put(PO.toString(),PO);
        PAYMENT_TYPES.put(INTERNET.toString(),INTERNET);
        PAYMENT_TYPES.put(CARTE_ACHAT.toString(),CARTE_ACHAT);
        PAYMENT_TYPES.put(DA.toString(),DA);
    }

    public static PaymentType GET_PAYMENT_TYPE(String name){
        return (PaymentType) PAYMENT_TYPES.get(name);
    }
    
    private String name;
       
    private PaymentType(String name){
        this.name = name;
    }

    Object readResolve() {
        return GET_PAYMENT_TYPE(name);
    }
    
    public String toString(){
        return this.name;
    }
    
    public String getName(){
        return name;
    }
}
