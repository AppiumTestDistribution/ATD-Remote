package com.hariharanweb.remoteappiummanager.controller;

import com.google.gson.JsonObject;
import com.hariharanweb.helpers.Helpers;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import spark.Route;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class AppiumController {
    AppiumDriverLocalService appiumDriverLocalService;
    public Route startAppium = (request, response) -> {
        String appiumPath = null;
        String port = null;

        String[] urlParameter = request.queryParamsValues("URL");
        String[] userPort = request.queryParamsValues("PORT");
        if (urlParameter != null && urlParameter[0] != null) {
            appiumPath = urlParameter[0];
        }
        if (userPort != null && userPort[0] != null) {
            port = userPort[0];
        }
        startAppiumServer(appiumPath,port);
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

    private AppiumDriverLocalService startAppiumServer(String path, String port) throws IOException {
        String ipAddress = Helpers.getHostMachineIpAddress();
        AppiumServiceBuilder builder =
                new AppiumServiceBuilder()
                        .withArgument(GeneralServerFlag.LOG_LEVEL, "info")
                        .withIPAddress(ipAddress);

        if (path != null && !path.isEmpty()) {
            builder.withAppiumJS(new File(path));
        }
        if (port !=null && !port.isEmpty()) {
            builder.usingPort(Integer.parseInt(port));
        } else {
            builder.usingAnyFreePort();
        }
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
