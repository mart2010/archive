package ca.canvac.webstore.domain;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;


public class Item implements Serializable {
    private String availFlag;
    private String charAttribute1;
    private String charAttribute2;
    private String charAttribute3;
    private String charAttribute4;
    private String charAttribute5;
    private String charAttribute6;
    private String charAttribute7;
    private String charAttribute8;
    private String charAttribute9;
    private Date dateAttribute1;
    private Date dateAttribute2;

    /* Private Fields */

    private long itemId;
    private String name;
    private double numberAttribute1;
    private double numberAttribute2;
    private double numberAttribute3;
    private double numberAttribute4;
    private double numberAttribute5;
    private String status;

    private SubCategory subCategory;
    private String subCategoryId;
    private String unit;
    private BigDecimal unitPrice;


    public String getAvailFlag() {
        return availFlag;
    }

    public String getCharAttribute1() {
        return charAttribute1;
    }

    public String getCharAttribute2() {
        return charAttribute2;
    }

    public String getCharAttribute3() {
        return charAttribute3;
    }

    public String getCharAttribute4() {
        return charAttribute4;
    }

    public String getCharAttribute5() {
        return charAttribute5;
    }

    public String getCharAttribute6() {
        return charAttribute6;
    }

    public String getCharAttribute7() {
        return charAttribute7;
    }

    public String getCharAttribute8() {
        return charAttribute8;
    }

    public String getCharAttribute9() {
        return charAttribute9;
    }


    public Date getDateAttribute1() {
        return dateAttribute1;
    }

    public Date getDateAttribute2() {
        return dateAttribute2;
    }

    /* JavaBeans Properties */

    public long getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }


    public double getNumberAttribute1() {
        return numberAttribute1;
    }

    public double getNumberAttribute2() {
        return numberAttribute2;
    }

    public double getNumberAttribute3() {
        return numberAttribute3;
    }

    public double getNumberAttribute4() {
        return numberAttribute4;
    }

    public double getNumberAttribute5() {
        return numberAttribute5;
    }

    public String getStatus() {
        return status;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public String getUnit() {
        return unit;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setAvailFlag(String availabilityFlag) {
        this.availFlag = availabilityFlag;
    }

    public void setCharAttribute1(String charAttribute1) {
        this.charAttribute1 = charAttribute1;
    }

    public void setCharAttribute2(String charAttribute2) {
        this.charAttribute2 = charAttribute2;
    }

    public void setCharAttribute3(String charAttribute3) {
        this.charAttribute3 = charAttribute3;
    }

    public void setCharAttribute4(String charAttribute4) {
        this.charAttribute4 = charAttribute4;
    }

    public void setCharAttribute5(String charAttribute5) {
        this.charAttribute5 = charAttribute5;
    }

    public void setCharAttribute6(String charAttribute6) {
        this.charAttribute6 = charAttribute6;
    }

    public void setCharAttribute7(String charAttribute7) {
        this.charAttribute7 = charAttribute7;
    }

    public void setCharAttribute8(String charAttribute8) {
        this.charAttribute8 = charAttribute8;
    }

    public void setCharAttribute9(String charAttribute9) {
        this.charAttribute9 = charAttribute9;
    }

    public void setDateAttribute1(Date dateAttribute1) {
        this.dateAttribute1 = dateAttribute1;
    }

    public void setDateAttribute2(Date dateAttribute2) {
        this.dateAttribute2 = dateAttribute2;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumberAttribute1(double numberAttribute1) {
        this.numberAttribute1 = numberAttribute1;
    }

    public void setNumberAttribute2(double numberAttribute2) {
        this.numberAttribute2 = numberAttribute2;
    }

    public void setNumberAttribute3(double numberAttribute3) {
        this.numberAttribute3 = numberAttribute3;
    }

    public void setNumberAttribute4(double numberAttribute4) {
        this.numberAttribute4 = numberAttribute4;
    }

    public void setNumberAttribute5(double numberAttribute5) {
        this.numberAttribute5 = numberAttribute5;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }


    /* Public Methods */

    public String toString() {
        return 	"\n\n Item name= " + this.getName() + 
        		"\n Description=" + this.getCharAttribute1() +
        		", " + this.charAttribute3 + 
        		", " + this.getCharAttribute7();
    }
   

    
}
