package org.dummy.calc;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.ThreadingModel;
import io.vertx.core.impl.cpu.CpuCoreSensor;

public class App {

    private static final int CAPACITY = Math.max(2, CpuCoreSensor.availableProcessors());

    // An instance per worker pool thread
    static Future<String> launch() {
        DeploymentOptions options = new DeploymentOptions()
                .setThreadingModel(ThreadingModel.WORKER)
                .setWorkerPoolName("calc-worker")
                .setWorkerPoolSize(CAPACITY)
                .setInstances(CAPACITY);
        return VertxInstanceHolder.getVertxInstance().deployVerticle(CalcVehicle.class, options);
    }

    public static void main(String[] args) {
        launch();
    }
}
