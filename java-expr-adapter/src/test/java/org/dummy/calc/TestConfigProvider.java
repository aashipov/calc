package org.dummy.calc;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

import static org.dummy.calc.JavaExprAdapter.*;
import static org.dummy.calc.JavaExprAdapter.EVALEX;
import static org.dummy.calc.JavaExprAdapter.MXPARSER;

public class TestConfigProvider implements ArgumentsProvider {

    private static final String SIMPLE_EXPRESSION = "2 + 2";
    private static final Double SIMPLE_EXPRESSION_RESULT = 4D;
    private static final String COMPLEX_EXPRESSION = "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2";
    private static final double COMPLEX_EXPRESSION_RESULT_EXPRTK = 19.988432890485228D;
    private static final double COMPLEX_EXPRESSION_RESULT_MXPARSER = 19.98843289048526D;
    private static final double COMPLEX_EXPRESSION_RESULT_EVALEX = 19.9884D;
    private static final String NOT_AN_EXPRESSION = "abc";

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
                Arguments.of(new TestConfig(MXPARSER, JavaExprAdapterManaged::calculateViamXparser, COMPLEX_EXPRESSION, COMPLEX_EXPRESSION_RESULT_MXPARSER)),
                Arguments.of(new TestConfig(MXPARSER, JavaExprAdapterManaged::calculateViamXparser, NOT_AN_EXPRESSION, Double.NaN)),
                Arguments.of(new TestConfig(MXPARSER, JavaExprAdapterManaged::calculateViamXparser, null, Double.NaN)),
                Arguments.of(new TestConfig(MXPARSER, JavaExprAdapterManaged::calculateViamXparser, "", Double.NaN)),
                Arguments.of(new TestConfig(MXPARSER, JavaExprAdapterManaged::calculateViamXparser, " \n\r\t ", Double.NaN)),
                Arguments.of(new TestConfig(EVALEX, JavaExprAdapterManaged::calculateViaEvalEx, COMPLEX_EXPRESSION, COMPLEX_EXPRESSION_RESULT_EVALEX)),
                Arguments.of(new TestConfig(EVALEX, JavaExprAdapterManaged::calculateViaEvalEx, NOT_AN_EXPRESSION, Double.NaN)),
                Arguments.of(new TestConfig(EVALEX, JavaExprAdapterManaged::calculateViaEvalEx, null, Double.NaN)),
                Arguments.of(new TestConfig(EVALEX, JavaExprAdapterManaged::calculateViaEvalEx, "", Double.NaN)),
                Arguments.of(new TestConfig(EVALEX, JavaExprAdapterManaged::calculateViaEvalEx, " \n\r\t ", Double.NaN)),
                Arguments.of(new TestConfig(null, JavaExprAdapterManaged::calculateViaEvalEx, COMPLEX_EXPRESSION, COMPLEX_EXPRESSION_RESULT_EVALEX)),
                Arguments.of(new TestConfig("", JavaExprAdapterManaged::calculateViaEvalEx, COMPLEX_EXPRESSION, COMPLEX_EXPRESSION_RESULT_EVALEX)),
                Arguments.of(new TestConfig(" \n\r\t ", JavaExprAdapterManaged::calculateViaEvalEx, COMPLEX_EXPRESSION, COMPLEX_EXPRESSION_RESULT_EVALEX))
        );
    }
}