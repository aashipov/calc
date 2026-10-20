package org.dummy.calc;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Native library adapter.
 */
public class JavaExprAdapter {

    private static final Logger LOG = Logger.getLogger(JavaExprAdapter.class.getSimpleName());
    private static final String JNI = "JNI";
    private static final String EXPRTK_LIBRARY_NAME = "libc-exprtk-adapter.so";
    private static final String EXPRTK_LIBRARY_FUNCTION_NAME = "calculate_via_exprtk";
    private static final String MEVAL_LIBRARY_NAME = "libc-meval-adapter.so";
    private static final String MEVAL_LIBRARY_FUNCTION_NAME = "calculate_via_meval";
    private static final String ADAPTER_NAME = "java-expr-adapter";
    private static final Linker LINKER = Linker.nativeLinker();
    private static final FunctionDescriptor FUNCTION_DESCRIPTOR = FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE, ValueLayout.ADDRESS);
    public static final String EXPRTK = "exprtk";
    public static final String MEVAL = "meval";

    /**
     * Environment variable, native library harness name, either JNI or FFM.
     */
    private static final String SHARED_LIBRARY_HARNESS_ENV_VAR_NAME = "SHARED_LIBRARY_HARNESS";

    static {
        System.loadLibrary(ADAPTER_NAME);
        LOG.log(Level.FINE, "Shared library harness: {0}", getSharedLibraryHarnessName());
    }

    static String getSharedLibraryHarnessName() {
        return (System.getenv(SHARED_LIBRARY_HARNESS_ENV_VAR_NAME) == null || System.getenv(SHARED_LIBRARY_HARNESS_ENV_VAR_NAME).isBlank()) ? JNI : System.getenv(SHARED_LIBRARY_HARNESS_ENV_VAR_NAME);
    }

    static boolean isJniHarness() {
        return JNI.equalsIgnoreCase(getSharedLibraryHarnessName());
    }

    /**
     * Calculate via exprtk (FFM).
     *
     * @param expression expression
     * @return result
     */
    static double calculateViaExprtkFfm(String expression) {
        Arena arena = Arena.ofAuto();
        SymbolLookup lookup = SymbolLookup.libraryLookup(EXPRTK_LIBRARY_NAME, arena);
        Optional<MemorySegment> memorySegmentOptional = lookup.find(EXPRTK_LIBRARY_FUNCTION_NAME);
        if (memorySegmentOptional.isEmpty()) {
            throw new IllegalStateException(EXPRTK_LIBRARY_NAME + " or its function " + EXPRTK_LIBRARY_FUNCTION_NAME + " not found");
        }
        MemorySegment funcArg = arena.allocateFrom(expression);
        try {
            return (double) LINKER.downcallHandle(memorySegmentOptional.get(), FUNCTION_DESCRIPTOR).invoke(funcArg);
        } catch (Throwable ex) {
            return Double.NaN;
        }
    }

    /**
     * Calculate via exprtk (JNI).
     *
     * @param expression expression
     * @return result
     */
    static native double calculateViaExprtkJni(String expression);

    /**
     * Calculate via exprtk.
     *
     * @param expression expression
     * @return result
     */
    public static double calculateViaExprtk(String expression) {
        if (expression == null) {
            return Double.NaN;
        }
        return isJniHarness() ? calculateViaExprtkJni(expression) : calculateViaExprtkFfm(expression);
    }

    /**
     * Calculate via meval (FFM).
     *
     * @param expression expression
     * @return result
     */
    static double calculateViaMevalFfm(String expression) {
        Arena arena = Arena.ofAuto();
        SymbolLookup lookup = SymbolLookup.libraryLookup(MEVAL_LIBRARY_NAME, arena);
        Optional<MemorySegment> memorySegmentOptional = lookup.find(MEVAL_LIBRARY_FUNCTION_NAME);
        if (memorySegmentOptional.isEmpty()) {
            throw new IllegalStateException(MEVAL_LIBRARY_NAME + " or its function " + MEVAL_LIBRARY_FUNCTION_NAME + " not found");
        }
        MemorySegment funcArg = arena.allocateFrom(expression);
        try {
            return (double) LINKER.downcallHandle(memorySegmentOptional.get(), FUNCTION_DESCRIPTOR).invoke(funcArg);
        } catch (Throwable ex) {
            return Double.NaN;
        }
    }

    /**
     * Calculate via meval (JNI).
     *
     * @param expression expression
     * @return result
     */
    static native double calculateViaMevalJni(String expression);

    /**
     * Calculate via meval.
     *
     * @param expression expression
     * @return result
     */
    public static double calculateViaMeval(String expression) {
        if (expression == null) {
            return Double.NaN;
        }
        return isJniHarness() ? calculateViaMevalJni(expression) : calculateViaMevalFfm(expression);
    }
}
