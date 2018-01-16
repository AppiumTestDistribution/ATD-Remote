package com.hariharanweb.remoteappiummanager;

import com.hariharanweb.remoteappiummanager.controller.AppiumController;
import com.hariharanweb.remoteappiummanager.controller.DeviceController;
import com.hariharanweb.remoteappiummanager.transformers.JsonTransformer;

import java.util.logging.Logger;

import static spark.Spark.*;

public class Server {
    public static void main(String[] args) {
        final Logger LOGGER =
                Logger.getLogger(Server.class.getName());
        if (System.getProperty("port") != null) {
            port(Integer.parseInt(System.getProperty("port")));
            LOGGER.info("Started Server on port" + System.getProperty("port"));
        }
        DeviceController deviceController = new DeviceController();
        AppiumController appiumController = new AppiumController();

        get("/", (req, res) -> "Server is Running!!!");
        get("/devices", deviceController.getDevices, new JsonTransformer());

        path("/device", () -> {
            get("/:udid", deviceController.getDevice, new JsonTransformer());
            path("/ios", () -> {
                get("/simulators", deviceController.getSimulators, new JsonTransformer());
                get("/realDevice", deviceController.getIOSDevices, new JsonTransformer());
            });
            path("/android", () -> {
                get("", deviceController.getAndroidDevices, new JsonTransformer());
            });
        });

        path("/appium", () -> {
            get("/start", appiumController.startAppium, new JsonTransformer());
            get("/stop", appiumController.stopAppium, new JsonTransformer());
            get("/isRunning", appiumController.isAppiumServerRunning, new JsonTransformer());
            get("/logs", appiumController.getAppiumLogs);
        });

        after((request, response) -> {
            response.header("Content-Type", "application/json");
        });
    }
}
