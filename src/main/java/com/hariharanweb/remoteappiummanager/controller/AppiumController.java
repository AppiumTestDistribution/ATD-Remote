package com.hariharanweb.remoteappiummanager.controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.hariharanweb.helpers.Helpers;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import spark.Route;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

public class AppiumController {
    AppiumDriverLocalService appiumDriverLocalService;
    String serverCaps;
    Helpers helper = new Helpers();

    public Route startAppium = (request, response) -> {
        String appiumPath = null;
        String port = null;

        JsonObject obj = helper.parseJson(request.body());

        JsonElement serverPath = obj.get("APPIUM_PATH");
        JsonElement userPort = obj.get("PORT");
        serverCaps = obj.get("SERVER_CAPS").getAsString();

        if (!serverPath.equals(JsonNull.INSTANCE)) {
            appiumPath = serverPath.getAsString();
        }

        if (!userPort.equals(JsonNull.INSTANCE) ) {
            port = userPort.getAsString();
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

        if(serverCaps != null) {
            addUserServerCaps(builder);
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

    private void addUserServerCaps (AppiumServiceBuilder builder){

        Set<Map.Entry<String, JsonElement>> entries = helper.parseJson(serverCaps).getAsJsonObject().entrySet();

        if(!entries.isEmpty()) {

            for (Map.Entry<String, JsonElement> entry : entries) {

                GeneralServerFlag serverArgument = helper.getServerArgument(entry.getKey());

                if(serverArgument != null && !entry.getValue().toString().equals("null")){
                    builder.withArgument(serverArgument, entry.getValue().getAsString());
                }
                else if(serverArgument !=null && entry.getValue().toString().equals("null")){
                    builder.withArgument(serverArgument);
                }
            }
        }
    }
}
