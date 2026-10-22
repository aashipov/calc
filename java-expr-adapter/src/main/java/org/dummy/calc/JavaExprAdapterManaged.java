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
        try {
            return new org.mariuszgromada.math.mxparser.Expression(expression).calculate();
        } catch (Exception e) {
            return Double.NaN;
        }
    }
}
