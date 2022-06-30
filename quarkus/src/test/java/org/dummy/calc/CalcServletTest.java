package org.dummy.calc;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeAll;

@QuarkusTest
public class CalcServletTest extends AppTestBase {
    @BeforeAll
    static void setUp() {
        org.mariuszgromada.math.mxparser.License.iConfirmNonCommercialUse("dummy");
    }
}
