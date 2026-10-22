package org.dummy.calc;

/**
 * JNI adapter
 */
public final class JavaExprAdapterJni {

    private JavaExprAdapterJni() {
        //
    }

    /**
     * Calculate via exprtk (JNI).
     *
     * @param expression expression
     * @return result
     */
    static native double calculateViaExprtkJni(String expression);

    /**
     * Calculate via meval (JNI).
     *
     * @param expression expression
     * @return result
     */
    static native double calculateViaMevalJni(String expression);
}
