package org.dummy.calc;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Expression adapter facade.
 */
public final class JavaExprAdapter {

    private static final Logger LOG = Logger.getLogger(JavaExprAdapter.class.getSimpleName());
    private static final String JNI = "JNI";
    private static final String ADAPTER_NAME = "java-expr-adapter-jni";
    public static final String EXPRTK = "exprtk";
    public static final String MEVAL = "meval";
    public static final String MXPARSER = "mxparser";
    public static final String EVALEX = "evalex";
    private static final Pattern MXPARSER_PATTERN = Pattern.compile(MXPARSER);
    private static final Pattern EXPRTK_PATTERN = Pattern.compile(EXPRTK);
    private static final Pattern MEVAL_PATTERN = Pattern.compile(MEVAL);

    /**
     * Environment variable, native library harness name, either JNI or FFM.
     */
    private static final String SHARED_LIBRARY_HARNESS_ENV_VAR_NAME = "SHARED_LIBRARY_HARNESS";

    static {
        System.loadLibrary(ADAPTER_NAME);
        LOG.log(Level.FINE, "Shared library harness: {0}", getSharedLibraryHarnessName());
        org.mariuszgromada.math.mxparser.License.iConfirmNonCommercialUse("dummy");
    }

    static String getSharedLibraryHarnessName() {
        return (System.getenv(SHARED_LIBRARY_HARNESS_ENV_VAR_NAME) == null || System.getenv(SHARED_LIBRARY_HARNESS_ENV_VAR_NAME).isBlank()) ? JNI : System.getenv(SHARED_LIBRARY_HARNESS_ENV_VAR_NAME);
    }

    static boolean isJniHarness() {
        return JNI.equalsIgnoreCase(getSharedLibraryHarnessName());
    }

    /**
     * Calculate via exprtk.
     *
     * @param expression expression
     * @return result
     */
    public static double calculateViaExprtk(String expression) {
        if (expression == null || expression.isBlank()) {
            return Double.NaN;
        }
        return isJniHarness() ? JavaExprAdapterJni.calculateViaExprtkJni(expression) : JavaExprAdapterFfm.calculateViaExprtkFfm(expression);
    }

    /**
     * Calculate via meval.
     *
     * @param expression expression
     * @return result
     */
    public static double calculateViaMeval(String expression) {
        if (expression == null || expression.isBlank()) {
            return Double.NaN;
        }
        return isJniHarness() ? JavaExprAdapterJni.calculateViaMevalJni(expression) : JavaExprAdapterFfm.calculateViaMevalFfm(expression);
    }

    /**
     * Calculate entrypoint.
     *
     * @param expression expression
     * @param mayBeAdapteeName a string containing
     * {@link JavaExprAdapter#EXPRTK} {@link JavaExprAdapter#MEVAL} {@link JavaExprAdapter#MXPARSER},
     * null or blank
     * @return result
     */
    public static double calculate(String expression, String mayBeAdapteeName) {
        if (expression == null || expression.isBlank()) {
            return Double.NaN;
        }
        if (mayBeAdapteeName == null || mayBeAdapteeName.isBlank()) {
            return JavaExprAdapterManaged.calculateViaEvalEx(expression);
        }
        String mayBeAdapteeNameLowercase = mayBeAdapteeName.toLowerCase();
        double result;
        if (MXPARSER_PATTERN.matcher(mayBeAdapteeNameLowercase).find()) {
            result = JavaExprAdapterManaged.calculateViamXparser(expression);
        } else if (EXPRTK_PATTERN.matcher(mayBeAdapteeNameLowercase).find()) {
            result = calculateViaExprtk(expression);
        } else if (MEVAL_PATTERN.matcher(mayBeAdapteeNameLowercase).find()) {
            result = calculateViaMeval(expression);
        } else {
            result = JavaExprAdapterManaged.calculateViaEvalEx(expression);
        }
        return result;
    }

    private JavaExprAdapter() {
        //
    }
}
