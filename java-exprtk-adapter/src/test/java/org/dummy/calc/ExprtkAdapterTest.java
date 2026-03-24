package org.dummy.calc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExprtkAdapterTest {

    private static final String EXPRESSION = "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2";
    private static final double EXPRESSION_EXPECTED = 19.988432890485228;
    private static final String NOT_AN_EXPRESSION = "abc";

    @Test
    void expressionTest() {
        double actual = JavaExprtkAdapter.calculate(EXPRESSION);
        Assertions.assertEquals(EXPRESSION_EXPECTED, actual, 0.00000000000000000000000000001);
    }

    @Test
    void notAnExpressionTest() {
        double actual = JavaExprtkAdapter.calculate(NOT_AN_EXPRESSION);
        Assertions.assertEquals(Double.NaN, actual, 0.00000000000000000000000000001);
    }

    @Test
    void nullExpressionTest() {
        double actual = JavaExprtkAdapter.calculate(null);
        Assertions.assertEquals(Double.NaN, actual, 0.00000000000000000000000000001);
    }

    @Test
    void blankExpressionTest() {
        double actual = JavaExprtkAdapter.calculate("");
        Assertions.assertEquals(Double.NaN, actual, 0.00000000000000000000000000001);
    }

    @Test
    void blank2ExpressionTest() {
        double actual = JavaExprtkAdapter.calculate("         \r  \n \t          ");
        Assertions.assertEquals(Double.NaN, actual, 0.00000000000000000000000000001);
    }
}
