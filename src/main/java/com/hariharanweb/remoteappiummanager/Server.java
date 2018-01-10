package com.hariharanweb.remoteappiummanager;

import com.hariharanweb.remoteappiummanager.controller.AppiumController;
import com.hariharanweb.remoteappiummanager.controller.DeviceController;
import com.hariharanweb.remoteappiummanager.transformers.JsonTransformer;
import spark.Spark;

import static spark.Spark.*;

public class Server {
    public static void main(String[] args) {
        DeviceController deviceController = new DeviceController();
        AppiumController appiumController = new AppiumController();

        get("/devices", deviceController.getDevices, new JsonTransformer());

        path("/device/:udid", () -> {
            get("", deviceController.getDevice, new JsonTransformer());
        });

        path("/appium", () -> {
            get("/start", appiumController.startAppium);
            get("/stop", appiumController.stopAppium);
        });
    }
}
