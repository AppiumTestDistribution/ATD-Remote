package com.hariharanweb.remoteappiummanager.controller;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import spark.Route;

import java.io.File;

public class AppiumController {
    AppiumDriverLocalService appiumDriverLocalService;
    public Route startAppium =(request, response) -> {
        startAppiumServer("/usr/local/lib/node_modules/appium/build/lib/main.js");
        return appiumDriverLocalService.getUrl().toString();
    };

    public Route startAppiumWithCustomPath =(request, response) -> {
        //http://127.0.0.1:4567/appium/start/?URL=/usr/local/lib/node_modules/appium/build/lib/main.js
        startAppiumServer(request.queryParamsValues("URL")[0]);
        return appiumDriverLocalService.getUrl().toString();
    };

    public Route stopAppium =(request, response) -> {
        appiumDriverLocalService.stop();
        if(!appiumDriverLocalService.isRunning()) {
            response.body("Server is stopped");
        }
        return response.body();
    };

    public Route isAppiumServerRunning =(request, response) ->{
        if(appiumDriverLocalService!=null && appiumDriverLocalService.isRunning())
            return "true";
        else return "false";


    };

    public Route getAppiumLogs=(request, response) -> {
        if(appiumDriverLocalService!=null)
            return appiumDriverLocalService.getStdOut();
        return response.body();
    };

    private AppiumDriverLocalService startAppiumServer(String path) {

        AppiumServiceBuilder builder =
                new AppiumServiceBuilder().withAppiumJS(new File(path))
                        .withArgument(GeneralServerFlag.LOG_LEVEL, "info")
                        .withIPAddress("127.0.0.1")
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
