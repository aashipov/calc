package org.dummy.calc;

import java.util.function.Function;
import java.util.stream.Stream;

import static org.dummy.calc.JavaExprAdapter.EVALEX;
import static org.dummy.calc.JavaExprAdapter.EXPRTK;
import static org.dummy.calc.JavaExprAdapter.MEVAL;
import static org.dummy.calc.JavaExprAdapter.MXPARSER;
import org.dummy.calc.JavaExprAdapterTest.TestConfig.TestConfigProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

class JavaExprAdapterTest {

    private static final String SIMPLE_EXPRESSION = "2 + 2";
    private static final Double SIMPLE_EXPRESSION_RESULT = 4D;
    private static final String COMPLEX_EXPRESSION = "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2";
    private static final double COMPLEX_EXPRESSION_RESULT_EXPRTK = 19.988432890485228D;
    private static final double COMPLEX_EXPRESSION_RESULT_MXPARSER = 19.98843289048526D;
    private static final double COMPLEX_EXPRESSION_RESULT_EVALEX = 19.9884D;
    private static final String NOT_AN_EXPRESSION = "abc";

    static class TestConfig {

        String mayBeAdapteeName;
        Function<String, Double> reference;
        String expression;
        Double expected;

        public TestConfig(String mayBeAdapteeName, Function<String, Double> reference, String expression, Double expected) {
            this.mayBeAdapteeName = mayBeAdapteeName;
            this.reference = reference;
            this.expression = expression;
            this.expected = expected;
        }

        static class TestConfigProvider implements ArgumentsProvider {

            @Override
            public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
                return Stream.of(Arguments.of(new TestConfig(EXPRTK, JavaExprAdapter::calculateViaExprtk, SIMPLE_EXPRESSION, SIMPLE_EXPRESSION_RESULT)),
                        Arguments.of(new TestConfig(EXPRTK, JavaExprAdapter::calculateViaExprtk, COMPLEX_EXPRESSION, COMPLEX_EXPRESSION_RESULT_EXPRTK)),
                        Arguments.of(new TestConfig(EXPRTK, JavaExprAdapter::calculateViaExprtk, NOT_AN_EXPRESSION, Double.NaN)),
                        Arguments.of(new TestConfig(EXPRTK, JavaExprAdapter::calculateViaExprtk, null, Double.NaN)),
                        Arguments.of(new TestConfig(EXPRTK, JavaExprAdapter::calculateViaExprtk, "", Double.NaN)),
                        Arguments.of(new TestConfig(EXPRTK, JavaExprAdapter::calculateViaExprtk, " \n\r\t ", Double.NaN)),
                        Arguments.of(new TestConfig(MEVAL, JavaExprAdapter::calculateViaMeval, SIMPLE_EXPRESSION, SIMPLE_EXPRESSION_RESULT)),
                        Arguments.of(new TestConfig(MEVAL, JavaExprAdapter::calculateViaMeval, COMPLEX_EXPRESSION, COMPLEX_EXPRESSION_RESULT_EXPRTK)),
                        Arguments.of(new TestConfig(MEVAL, JavaExprAdapter::calculateViaMeval, NOT_AN_EXPRESSION, Double.NaN)),
                        Arguments.of(new TestConfig(MEVAL, JavaExprAdapter::calculateViaMeval, null, Double.NaN)),
                        Arguments.of(new TestConfig(MEVAL, JavaExprAdapter::calculateViaMeval, "", Double.NaN)),
                        Arguments.of(new TestConfig(MEVAL, JavaExprAdapter::calculateViaMeval, " \n\r\t ", Double.NaN)),
                        Arguments.of(new TestConfig(MXPARSER, JavaExprAdapter::calculateViamXparser, COMPLEX_EXPRESSION, COMPLEX_EXPRESSION_RESULT_MXPARSER)),
                        Arguments.of(new TestConfig(MXPARSER, JavaExprAdapter::calculateViamXparser, NOT_AN_EXPRESSION, Double.NaN)),
                        Arguments.of(new TestConfig(MXPARSER, JavaExprAdapter::calculateViamXparser, null, Double.NaN)),
                        Arguments.of(new TestConfig(MXPARSER, JavaExprAdapter::calculateViamXparser, "", Double.NaN)),
                        Arguments.of(new TestConfig(MXPARSER, JavaExprAdapter::calculateViamXparser, " \n\r\t ", Double.NaN)),
                        Arguments.of(new TestConfig(EVALEX, JavaExprAdapter::calculateViaEvalEx, COMPLEX_EXPRESSION, COMPLEX_EXPRESSION_RESULT_EVALEX)),
                        Arguments.of(new TestConfig(EVALEX, JavaExprAdapter::calculateViaEvalEx, NOT_AN_EXPRESSION, Double.NaN)),
                        Arguments.of(new TestConfig(EVALEX, JavaExprAdapter::calculateViaEvalEx, null, Double.NaN)),
                        Arguments.of(new TestConfig(EVALEX, JavaExprAdapter::calculateViaEvalEx, "", Double.NaN)),
                        Arguments.of(new TestConfig(EVALEX, JavaExprAdapter::calculateViaEvalEx, " \n\r\t ", Double.NaN))
                );
            }
        }
    }

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
