package org.dummy.calc;

import io.helidon.microprofile.testing.AddConfig;
import io.helidon.microprofile.testing.junit5.HelidonTest;

@HelidonTest
@AddConfig(key = "server.port", value = "8080")
public class AppTest extends AppTestBase {

}
