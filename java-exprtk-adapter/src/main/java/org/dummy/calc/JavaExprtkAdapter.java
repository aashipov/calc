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

public class JavaExprtkAdapter {

    private static final String JNI = "JNI";
    private static final String SHARED_LIBRARY_NAME = "libc-exprtk-adapter.so";
    private static final String SHARED_LIBRARY_FUNCTION_NAME = "calculate";
    private static final String ADAPTER_NAME = "java-exprtk-adapter";
    private static final Logger LOG = Logger.getLogger(JavaExprtkAdapter.class.getSimpleName());
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
     * Via FFM.
     *
     * @param expression expression
     * @return result
     */
    static double calculateFfm(String expression) {
        FunctionDescriptor functionDescriptor = FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE, ValueLayout.ADDRESS);
        Linker linker = Linker.nativeLinker();
        Arena arena = Arena.ofAuto();
        SymbolLookup lookup = SymbolLookup.libraryLookup(SHARED_LIBRARY_NAME, arena);
        Optional<MemorySegment> memorySegmentOptional = lookup.find(SHARED_LIBRARY_FUNCTION_NAME);
        if (memorySegmentOptional.isEmpty()) {
            throw new IllegalStateException(SHARED_LIBRARY_NAME + " or its function " + SHARED_LIBRARY_FUNCTION_NAME + " not found");
        }
        MemorySegment funcArg = arena.allocateFrom(expression);
        try {
            return (double) linker.downcallHandle(memorySegmentOptional.get(), functionDescriptor).invoke(funcArg);
        } catch (Throwable ex) {
            return Double.NaN;
        }
    }

    /**
     * Via JNI.
     *
     * @param expression expression
     * @return result
     */
    static native double calculateJni(String expression);

    /**
     * Calculate.
     *
     * @param expression expression
     * @return result
     */
    public static double calculate(String expression) {
        if (expression == null) {
            return Double.NaN;
        }
        return isJniHarness() ? calculateJni(expression) : calculateFfm(expression);
    }
}
