package ca.po.util.converter;

import ca.po.model.type.Money;

/**
 * Simple Interface used in the Spring ctx, allowing 
 * plugging any implementater easily from the Spring ctx config file
 *
 */
public interface MoneyCADConverter {
    Money convertToCAD(Money fromMoney);
}