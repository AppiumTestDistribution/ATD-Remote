package com.hariharanweb.remoteappiummanager.controller;

import com.google.gson.JsonObject;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import spark.Route;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;

public class AppiumController {
    AppiumDriverLocalService appiumDriverLocalService;
    public Route startAppium = (request, response) -> {
        String appiumPath = "/usr/local/lib/node_modules/appium/build/lib/main.js";

        String[] urlParameter = request.queryParamsValues("URL");
        if (urlParameter != null && urlParameter[0] != null) {
            appiumPath = urlParameter[0];
        }
        startAppiumServer(appiumPath);
        URL url = appiumDriverLocalService.getUrl();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("port", url.getPort());
        return jsonObject;
    };

    public Route stopAppium = (request, response) -> {
        appiumDriverLocalService.stop();
        if (!appiumDriverLocalService.isRunning()) {
            response.body("Server is stopped");
        }
        return response.body();
    };

    public Route isAppiumServerRunning = (request, response) -> {
        boolean isAppiumRunning = false;
        if (appiumDriverLocalService != null && appiumDriverLocalService.isRunning())
            isAppiumRunning = true;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", isAppiumRunning);
        jsonObject.addProperty("port", appiumDriverLocalService.getUrl().getPort());
        return jsonObject;
    };

    public Route getAppiumLogs = (request, response) -> {
        if (appiumDriverLocalService != null && appiumDriverLocalService.isRunning())
            return appiumDriverLocalService.getStdOut();
        return response.body();
    };

    private AppiumDriverLocalService startAppiumServer(String path) throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("google.com", 80));
        String ipAddress = socket.getLocalAddress().toString()
                .replace("/","");
        AppiumServiceBuilder builder =
                new AppiumServiceBuilder().withAppiumJS(new File(path))
                        .withArgument(GeneralServerFlag.LOG_LEVEL, "info")
                        .withIPAddress(ipAddress)
                        .usingAnyFreePort();
        appiumDriverLocalService = AppiumDriverLocalService.buildService(builder);
        appiumDriverLocalService.start();
        System.out.println(
                "**************************************************************************\n");
        System.out.println("Appium Server Started at......"
                + appiumDriverLocalService.getUrl().getPort());
        System.out.println(
                "**************************************************************************\n");
        return appiumDriverLocalService;
    }
}
