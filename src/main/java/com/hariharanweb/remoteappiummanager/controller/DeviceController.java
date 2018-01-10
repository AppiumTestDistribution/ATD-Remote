package com.hariharanweb.remoteappiummanager.controller;

import com.thoughtworks.device.DeviceManager;
import spark.Route;

public class DeviceController {

    private DeviceManager deviceManager;

    public DeviceController(){
        deviceManager = new DeviceManager();
    }

    public Route getDevices = (request, response) -> deviceManager.getDeviceProperties();
    public Route getDevice = (request, response) -> {
        try{
            return deviceManager.getDeviceProperties(request.params(":udid"));
        }catch (Exception e){
            response.status(404);
            response.body(e.getMessage());
        }
        return null;
    };
}
