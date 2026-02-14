package org.dummy.calc;

public class ExprtkAdapter {

    static {
        System.loadLibrary("java-exprtk-adapter");
    }

    public static native double calculate(String expression);
}
