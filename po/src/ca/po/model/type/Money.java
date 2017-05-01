package ca.po.model.type;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import net.sf.hibernate.AssertionFailure;


/**
 * @author MOUELLET
 * Value object representing money.. based on M. Fowler
 * 
 */
public class Money {

    /**
     * Default local currency
     */
    static Currency LOCAL_CURRENCY = Currency.getInstance( Locale.getDefault() );
    
    /**
     * Convience function produces money in the 'local currency'.
     */
    public static Money local( double amount ){
        return new Money( amount, LOCAL_CURRENCY );     
    }

    /**
     * Convience function to return CAN money 
     */
    public static Money getCAD( double amount ){
        return new Money( amount, Currency.getInstance("CAD"));     
    }

    /**
     * Convience function to return USD money 
     */
    public static Money getUSD( double amount ){
        return new Money( amount, Currency.getInstance("USD"));     
    }

    /**
     * Convience function to return EUR money 
     */
    public static Money getEUR( double amount ){
        return new Money( amount, Currency.getInstance("EUR"));     
    }
    
    /**
     * Convience function to return GBP money 
     */
    public static Money getGBP( double amount ){
        return new Money( amount, Currency.getInstance("GBP"));     
    }
    
    
    static NumberFormat nf = NumberFormat.getInstance(); 
    static {
        if( nf instanceof DecimalFormat){
            DecimalFormat format = (DecimalFormat) nf;
            format.applyPattern("#,##0.00 ¤¤");
        }  
    }

    private static final int[] cents = new int[]{1, 10, 100, 1000 };    
    private int centFactor(){
         return cents[ currency.getDefaultFractionDigits() ];
    }   
    
    
    /**
     * Amount of money in currency. max amount:  $92,233,720,368,547,758.09
     * This holds the unit amount, e.g. 1.00$ = 100 (cents) 
     */
    private long longAmount;

    /**
     * Amount of money represented in BigDecimal e.g. 1.00$ = 1.00 (dol) 
     */
    private BigDecimal decimalAmount;
    
    
    /**
     * Currency amount of money is in.
     */
    private Currency currency;

    //Constructor made public for Hibernate
    public Money() {}
    
    /**
     * Creates a new money of the provided amount and currency.
     * <p>
     * Example: <code>new Money( 1.48, Currency.getInstance("USD") )</code>
     * </p>
     * @param amount Amount of Money.
     * @param currency Currency Money is to be measured in.
     */
    public Money( double amount, Currency currency ) {
        this.currency = currency;
        this.longAmount = Math.round( amount * centFactor() );
    }

    /**
     * Creates a new money of the provided amount and currency.
     * 
     * <p>
     * Example: <code>new Money( 12, Currency.getInstance("CAD") )</code>
     * </p>
     * 
     * @param amount Amount of Money.
     * @param currency Currency Money is to be measured in.
     */
    public Money( long amount, Currency currency ) {
        this.currency = currency;
        this.longAmount = amount * centFactor();
    }
    /**
     * Creates a new money of the provided amount and currency.
     * User defined rounding mode is default to BigDecimal.ROUND_HALF_UP
     */
    public Money( BigDecimal amount, Currency currency ){
        this.currency = currency;
        amount = amount.movePointRight( currency.getDefaultFractionDigits() );
        amount = amount.setScale( 0, BigDecimal.ROUND_HALF_UP );
        this.longAmount = amount.longValue();
    }

    /**
     * Creates a new money of the provided amount and currency.
     * User defined rounding mode is used - see java.math.BigDecimal rounding
     */
    public Money( BigDecimal amount, Currency currency, int roundingMode ){
        this.currency = currency;
        amount = amount.movePointRight( currency.getDefaultFractionDigits() );
        amount = amount.setScale( 0, roundingMode );
        this.longAmount = amount.longValue();
    }

        
    /**
     * @hibernate.property name="decimalAmount" 
     * @hibernate.column name="amt" sql-type="Decimal(10,2)"
     * @return BigDecimal 
     */
    public BigDecimal getDecimalAmount(){
        return BigDecimal.valueOf( longAmount, currency.getDefaultFractionDigits() );
    }

    //Setters defined for Hibernate to be able to instantiate Money based on decimalAmout and Currency
    public void setDecimalAmount(BigDecimal amount) {
        this.decimalAmount = amount;
        //TODO: Should use currency.getDefaultFractionDigits() instead of 2.. needs to make sure currency is set
        amount = amount.movePointRight( 2);
        amount = amount.setScale( 0, BigDecimal.ROUND_HALF_UP );
        this.longAmount = amount.longValue();
    }
    public void setCurrencyCode(String code){
        this.currency = Currency.getInstance(code);
    }
    
    /**
     * @hibernate.property name="currencyCode" type="string"   
     * @hibernate.column name="currency" length="3"
     * @return String
     */
    public String getCurrencyCode(){
        return this.currency.getCurrencyCode();
    }
    
    
    public long getLongAmount() {
        return longAmount;
    }

    public Currency getCurrency(){
        return this.currency;
    }

    /**
     * Simple addition ensuring matched Currency.
     * 
     * @param other
     * @return Money
     */
    public Money add( Money other ){
        assertSameCurrencyAs( other );
        return newMoney( longAmount + other.longAmount );
    }
    /**
     * Simple addition ensuring matched Currency.
     * 
     * @param other
     * @return Money
     */
    public Money subtract( Money other ){
        assertSameCurrencyAs( other );
        return newMoney( longAmount - other.longAmount );
    }
    
    /**
     * Comparison of Money objects used by Comparable interface.
     * This method allows Money to be sorted.
     * 
     * @param other
     * @return Boolean int -1 if less than, 1 if greater than and 0 if equal to other
     * @throws ClassCastException if other is not a Money
     * @throws AssertionFailure if other Money is not of the same Currency
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo( Object other ) {
        return compareTo( (Money) other );
    }
    /**
     * Comparison of Money objects.
     * 
     * @param other
     * @return Boolean int -1 if less than, 1 if greater than and 0 if equal to other
     * @throws AssertionFailure if other Money is not of the same Currency
     * @see java.lang.Comparable#compareTo(Object)
     */
    public int compareTo( Money other ){
        assertSameCurrencyAs( other );
        if( longAmount < other.longAmount) return -1;
        else if (longAmount == other.longAmount) return 0;
        else return 1;
    }        
    /**
     * Convience implementation of greater than function.
     * 
     * @param other
     * @return Boolean True if money is greater than other
     * @see #compareTo(type.Money)
     */
    public boolean greaterThan( Money other ){
        return (compareTo(other) > 0);
    }
    /** Convience comparison functions 
     * 
     * @param other
     * @return Boolean True if money is less than other
     * @see #compareTo(Money)
     */
    public boolean lessThan( Money other ){
        return (compareTo(other) < 0);
    }
    /**
     * Money multiplication with default rounding mode.
     * 
     * Equivilent of <code>multiply( amount, BigDecimal.ROUND_HALF_EVEN )</code>
     * @param amount Multiplicator
     * @return Money
     */
    public Money multiply( double amount ) {
        return multiply( new BigDecimal(amount) );
    }
    /**
     * Money multiplication with Long 
     * no need for rounding mode, since Long values are integer
     * createdby: Martin Ouellet
     */
    public Money multiply( long value ) {
        Money money = new Money();
        long multipliedValue = value * longAmount;
        money.longAmount = multipliedValue;
        money.currency = this.currency;
        return money;
    }
 
    
    /**
     * Money multiplication with default rounding mode.
     * 
     * Equivilent of <code>multiply( amount, BigDecimal.ROUND_HALF_EVEN )</code>
     * @param amount Multiplicator
     * @return Money
     */
    public Money multiply( BigDecimal amount ){        
        return multiply( amount, BigDecimal.ROUND_HALF_EVEN );
    }

    /**
     * Money multiplication with user specified rounding mode.
     * 
     * @param amount Multiplicator
     * @param roundingMode Rounding mode as specified by BigDecimal
     * @return Money
     * @see java.math.BigDecimal#ROUND_UP
     * @see java.math.BigDecimal#ROUND_DOWN
     * @see java.math.BigDecimal#ROUND_CEILING
     * @see java.math.BigDecimal#ROUND_FLOOR
     * @see java.math.BigDecimal#ROUND_HALF_UP
     * @see java.math.BigDecimal#ROUND_HALF_DOWN
     * @see java.math.BigDecimal#ROUND_HALF_EVEN
     * @see java.math.BigDecimal#ROUND_UNNECESSARY
     */
    public Money multiply( BigDecimal amount, int roundingMode ){
        return new Money( getDecimalAmount().multiply(amount), currency, roundingMode );
    }
    /**
     * Allocates money "evenly' into n amounts.
     * Modified from the original to handle negative Monetary values.
     */
    public Money[] allocate( int n ){
        Money[] results = new Money[n];        
        Money lowResult = newMoney( longAmount / n );
        Money highResult = newMoney( lowResult.longAmount + (longAmount>=0?1:-1) );

        int remainder = Math.abs( (int) longAmount % n );
        for( int i = 0; i < remainder; i++) results[i] = highResult;                
        for( int i = remainder; i < n; i++) results[i] = lowResult;

        return results;
    }
    /**
     * Allocates money according to provided ratios.
     * Modified from the origaionl to handle negative Monetary values.
     */
    public Money[] allocate( long[] ratios ){
        long total = 0;
        for( int i=0; i<ratios.length; i++) total += ratios[i];
        long remainder = longAmount;
        
        Money[] results = new Money[ ratios.length];
        for( int i=0; i < results.length; i++){
            results[i] = newMoney( longAmount*ratios[i]/total );
            remainder -= results[i].longAmount;
        }
        if( remainder > 0 ){
            for( int i=0; i<remainder; i++){
                results[i].longAmount++;
            }
        }
        if( remainder < 0 ){
            for( int i=0; i>remainder; i--){
                results[i].longAmount--;
            }
        }        
        return results;
    }
    
    /**
     * Money representation (based on currency).
     * <ul>
     * <li><code>AMOUNT CODE</code>
     * <li><code>1.00 USD</code> One dollar us
     * <li><code>1.00 CAD</code> One dollar canadian
     * <li><code>1 JPY</code> One yen
     * </ul>
     * You may wish to format the amount according to your locale using
     * the following code fragment:
     * <code><pre>
     * Currency currency = money.currency();
     * double amount = money.amount().doubleValue();
     * 
     * NumberFormat nf=NumberFormat.getCurrencyInstance();
     * nf.setCurrency( currency );
     * nf.setMinimumFractionDigits( currency.getDefaultFractionDigits() );
     * nf.setMaximumFractionDigits( currency.getDefaultFractionDigits() );
     *
     * System.out.println( nf.format( amount ) );
     * </pre></code>
     * This implemenation only really returns the expected result for the
     * local currency.  For non local currencies the Currency CODE is used
     * the symbol as per the Currency.getSymbol() specification - and the
     * resulting string cannot be reparsed back into a amount.
     * <ul>
     * <li><code>$1.00</code> or <code>-$1.00</code> - USD printed in us local
     * <li><code>CAD1.00</code> or <code>-CAD1.00</code> - CAD printed in us local
     * </ul>
     * MoneyUtilites supports a more pleasing, locale independent,
     * printing/parsing representation.
     */
    public String toString(){
        nf.setCurrency( currency );
        nf.setMinimumFractionDigits( currency.getDefaultFractionDigits() );
        nf.setMaximumFractionDigits( currency.getDefaultFractionDigits() );
        
        return nf.format( getDecimalAmount().doubleValue() );
    }
    
    /**
     * Returns Money object represented by provided string.
     * <ul>
     * <li><code>NUMBER CODE</code>
     * </ul>
     * 
     * @param str Representation of money in the form <code>AMOUNT CODE</code>
     * @return Money
     */
    static public Money valueOf( String str ) throws java.text.ParseException{
        Currency currency;
        Number number;        
        currency = Currency.getInstance( str.substring( str.length() - 3 ) );

        nf.setCurrency( currency );
        nf.setMinimumFractionDigits( currency.getDefaultFractionDigits() );
        nf.setMaximumFractionDigits( currency.getDefaultFractionDigits() );
                                
        number = nf.parse( str );
        return new Money( number.doubleValue(), currency );
    }

    /**
     *  this uses J2SE1.4's assert keyword.
     * As expected this code will do nothing if asserts are turned off.
     * 
     */
    private void assertSameCurrencyAs( Money arg){
        assert (arg != null) ;
        assert (currency.equals( arg.currency)) : "Currency must be the same!";
    }
    /** Used to return Money in the same currency as this one. */
    private Money newMoney(long amount){
        Money money = new Money();
        money.currency = this.currency;
        money.longAmount = amount;
       
        return money;
    }
    /**
     * Money instances must have same currency and amount to be equal.
     * 
     * @param other
     * @return boolean
     * @see java.lang.Object#equals(Object)
     */
    public boolean equals( Object other ) {
        return (other instanceof Money) && equals( (Money) other);        
    }
    
    /**
     * Money instances must have same currency and amount to be equal.
     * 
     * @param other
     * @return boolean
     * @see java.lang.Object#equals(Object)
     */
    public boolean equals( Money other ) {
        return currency.equals( other.currency) && (longAmount == other.longAmount );
    }
    /**
     * Hash value based on amount.
     * 
     * Possibly should include currancy information in order to be consistent
     * with equals implementation.
     * 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return (int) (longAmount ^ (longAmount >>> 32 ) );
    }
    
    
}
