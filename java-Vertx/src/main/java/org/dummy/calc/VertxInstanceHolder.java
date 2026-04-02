package org.dummy.calc;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class VertxInstanceHolder {

    private static Vertx INSTANCE = null;

    public static synchronized Vertx getVertxInstance() {
        if (INSTANCE == null) {
            org.mariuszgromada.math.mxparser.License.iConfirmNonCommercialUse("dummy");
            VertxOptions vOpts = new VertxOptions()
                    .setPreferNativeTransport(true);
            INSTANCE = Vertx.vertx(vOpts);
        }
        return INSTANCE;
    }
}
