package org.dummy.calc;

/**
 * Managed code adapter.
 */
public final class JavaExprAdapterManaged {

    private JavaExprAdapterManaged() {
        //
    }

    /**
     * Calculate via EvalEx.
     *
     * @param expression expression
     * @return result
     */
    public static double calculateViaEvalEx(String expression) {
        if (expression == null || expression.isBlank()) {
            return Double.NaN;
        }
        try {
            return new com.udojava.evalex.Expression(expression).eval().doubleValue();
        } catch (Exception ex) {
            return Double.NaN;
        }
    }

    /**
     * Calculate via mXparser.
     *
     * @param expression expression
     * @return result
     */
    public static double calculateViamXparser(String expression) {
        if (expression == null || expression.isBlank()) {
            return Double.NaN;
        }
        try {
            return new org.mariuszgromada.math.mxparser.Expression(expression).calculate();
        } catch (Exception e) {
            return Double.NaN;
        }
    }
}
