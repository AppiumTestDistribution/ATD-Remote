package com.hariharanweb.remoteappiummanager.controller;

import spark.Route;

public class AppiumController {
    public Route startAppium =(request, response) -> {
        //some command to start appium comes here and return if success or failure
        return "Starting Appium server";
    };

    public Route stopAppium =(request, response) -> {
        //some command to stop appium comes here and return if success or failure
        return "Stopping Appium server";
    };
}
