package org.dummy.calc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

class JavaExprAdapterTest {

    @ParameterizedTest
    @ArgumentsSource(TestConfigProvider.class)
    void testVias(TestConfig tc) {
        Double actual = tc.reference.apply(tc.expression);
        Assertions.assertEquals(tc.expected, actual, 0.00000000000000000000000000001, tc.mayBeAdapteeName);
    }

    @ParameterizedTest
    @ArgumentsSource(TestConfigProvider.class)
    void testCalculate(TestConfig tc) {
        Double actual = JavaExprAdapter.calculate(tc.expression, tc.mayBeAdapteeName);
        Assertions.assertEquals(tc.expected, actual, 0.00000000000000000000000000001, tc.mayBeAdapteeName);
    }
}
