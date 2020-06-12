package com.chqx.waterview;


import java.math.BigDecimal;

public class Utils {

    public static String getBigDecimal(int source, int p) {
        BigDecimal b1 = new BigDecimal(Double.toString(source));
        BigDecimal b2 = new BigDecimal(Double.toString(p));
        BigDecimal b3 = b1.divide(b2);//此处举例使用乘法（注意：必须使用一个新的BigDecimal来获得运算后的值）
        Double b4 = b3.doubleValue();
        return b4 + "";
    }
}
