package com.hariharanweb.remoteappiummanager.controller;

import com.google.gson.JsonObject;
import com.hariharanweb.helpers.Helpers;
import spark.Route;

/**
 * Created by bsneha on 17/01/18.
 */
public class MachineController {

    public Route getXCodeVersion = (request, response) -> {
        try {
            String commandOutput = Helpers.excuteProcess("xcodebuild -version");
            String[] results = commandOutput.split("\n");
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("xcodeVersion", results[0].replace("Xcode ",""));
            jsonObject.addProperty("buildVersion", results[1].replace("Build version ",""));
            return jsonObject;
        } catch (Exception e) {
            response.status(500);
            response.body(e.getMessage());
            return response.body();
        }

    };

    public Route getAvailablePort = (request,response ) -> Helpers.getAvailablePort();
}
