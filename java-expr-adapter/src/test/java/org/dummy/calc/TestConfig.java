package org.dummy.calc;

import java.util.StringJoiner;
import java.util.function.Function;

public class TestConfig {
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

    @Override
    public String toString() {
        return new StringJoiner(", ", TestConfig.class.getSimpleName() + "[", "]")
                .add("mayBeAdapteeName='" + mayBeAdapteeName + "'")
                .add("reference=" + reference)
                .add("expression='" + expression + "'")
                .add("expected=" + expected)
                .toString();
    }
}
