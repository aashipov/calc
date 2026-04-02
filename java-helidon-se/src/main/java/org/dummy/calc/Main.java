package org.dummy.calc;

import io.helidon.logging.common.LogConfig;
import io.helidon.webserver.WebServer;

public class Main {

    static final String HTTP_HOST = "0.0.0.0";
    static final int HTTP_PORT = 8080;

    private Main() {
        //
    }

    static WebServer buildWebServer() {
        return WebServer.builder()
                .host(HTTP_HOST)
                .port(HTTP_PORT)
                .routing(routing -> routing.register("/", new CalcService()))
                .build();
    }

    public static void main(String[] args) {
        LogConfig.configureRuntime();
        buildWebServer().start();
    }
}
