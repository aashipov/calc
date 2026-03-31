package org.dummy.calc;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class CalcVehicle extends VerticleBase {

    static final String HTTP_HOST = "0.0.0.0";
    static final int HTTP_PORT = 8080;

    private HttpServer buildServer() {
        HttpServerOptions hOpts = new HttpServerOptions()
                .setTcpFastOpen(true)
                .setTcpNoDelay(true)
                .setTcpQuickAck(true)
                .setHost(HTTP_HOST)
                .setPort(HTTP_PORT);
        HttpServer server = vertx.createHttpServer(hOpts);

        Router router = Router.router(vertx);
        Route route = router.route();
        route.handler(BodyHandler.create().setBodyLimit(4_096));
        route.handler(new CalcHandler());
        server.requestHandler(router);

        return server;
    }

    public CalcVehicle() {
        this.init(VertxInstanceHolder.getVertxInstance(), VertxInstanceHolder.getVertxInstance().getOrCreateContext());

    }

    @Override
    public Future<HttpServer> start() throws Exception {
        return buildServer().listen();
    }
}
