package com.brain.servicepassengeruser.internalcommon.util;

import java.math.BigDecimal;

public class BigDecimalUtils {

    /**
     * 加法
     * @param v1
     * @param v2
     * @return
     */
    public static double add(double v1,double v2){
        BigDecimal p1 = BigDecimal.valueOf(v1);
        BigDecimal p2 = BigDecimal.valueOf(v2);
        return p1.add(p2).setScale(2,BigDecimal.ROUND_UP).doubleValue();
    }

    /**
     * 减法
     * @param v1
     * @param v2
     * @return
     */
    public static double substract(double v1,double v2){
        BigDecimal p1 = BigDecimal.valueOf(v1);
        BigDecimal p2 = BigDecimal.valueOf(v2);
        return p1.subtract(p2).setScale(2,BigDecimal.ROUND_UP).doubleValue();
    }

    public static double multiply(double v1,double v2){
        BigDecimal p1 = BigDecimal.valueOf(v1);
        BigDecimal p2 = BigDecimal.valueOf(v2);
        return p1.multiply(p2).setScale(2,BigDecimal.ROUND_UP).doubleValue();
    }

    public static double divide(double v1,double v2){
        if(v2<=0){
            throw new IllegalArgumentException();
        }
        BigDecimal p1 = BigDecimal.valueOf(v1);
        BigDecimal p2 = BigDecimal.valueOf(v2);
        return p1.divide(p2).setScale(2,BigDecimal.ROUND_UP).doubleValue();
    }
}
