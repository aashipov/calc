package org.dummy.calc;

import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.context.annotation.Property;
import jakarta.inject.Inject;

@MicronautTest
@Property(name = "micronaut.server.port", value = "8080")
class CalcTest extends AppTestBase {

    @Inject
    EmbeddedApplication<?> application;

}
