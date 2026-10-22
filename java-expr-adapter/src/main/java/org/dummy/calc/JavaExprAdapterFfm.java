package org.dummy.calc;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.util.Optional;

/**
 * FFM adapter.
 */
public final class JavaExprAdapterFfm {

    private static final String EXPRTK_LIBRARY_NAME = "libc-exprtk-adapter.so";
    private static final String EXPRTK_LIBRARY_FUNCTION_NAME = "calculate_via_exprtk";
    private static final String MEVAL_LIBRARY_NAME = "libc-meval-adapter.so";
    private static final String MEVAL_LIBRARY_FUNCTION_NAME = "calculate_via_meval";
    private static final Linker LINKER = Linker.nativeLinker();
    private static final FunctionDescriptor FUNCTION_DESCRIPTOR = FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE, ValueLayout.ADDRESS);

    private JavaExprAdapterFfm() {
        //
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
}
