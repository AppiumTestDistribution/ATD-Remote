package com.hariharanweb.remoteappiummanager.controller;

import com.thoughtworks.device.DeviceManager;
import spark.Route;

public class DeviceController {

    private DeviceManager deviceManager;

    public DeviceController(){
        deviceManager = new DeviceManager();
    }

    public Route getDevices = (request, response) -> {
        try {
             return deviceManager.getDevices();
        } catch (Exception e){
            response.status(404);
            response.body(e.getMessage());
        }
        return response.body();
    };
    public Route getDevice = (request, response) -> {
        try{
            return deviceManager.getDevice(request.params(":udid"));
        }catch (Exception e){
            response.status(404);
            response.body(e.getMessage());
        }
        return null;
    };
}
