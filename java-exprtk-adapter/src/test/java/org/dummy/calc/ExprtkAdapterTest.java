package org.dummy.calc;

import org.junit.Assert;
import org.junit.Test;

public class ExprtkAdapterTest {
    private static final String EXPRESSION = "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2";
    private static final double EXPRESSION_EXPECTED = 19.988432890485228;
    private static final String NOT_AN_EXPRESSION = "abc";

    @Test
    public void expressionTest() {
        double actual = ExprtkAdapter.calculate(EXPRESSION);
        Assert.assertEquals(EXPRESSION_EXPECTED, actual, 0.00000000000000000000000000001);
    }

    @Test
    public void notAnExpressionTest() {
        double actual = ExprtkAdapter.calculate(NOT_AN_EXPRESSION);
        Assert.assertEquals(Double.NaN, actual, 0.00000000000000000000000000001);
    }
}
