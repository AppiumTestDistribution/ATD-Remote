package com.hariharanweb.remoteappiummanager.controller;

import com.thoughtworks.android.AndroidManager;
import com.thoughtworks.device.DeviceManager;
import com.thoughtworks.device.SimulatorManager;
import com.thoughtworks.iOS.IOSManager;
import spark.Route;

public class DeviceController {

    private DeviceManager deviceManager;
    private SimulatorManager simulatorManager;
    private IOSManager iosManager;
    private AndroidManager androidManager;

    public DeviceController(){
        deviceManager = new DeviceManager();
        simulatorManager = new SimulatorManager();
        iosManager = new IOSManager();
        androidManager = new AndroidManager();
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

    public Route getSimulators = (request, response) -> {
        try{
            return simulatorManager.getAllSimulators("iOS");
        }catch (Exception e){
            response.status(404);
            response.body(e.getMessage());
        }
        return null;
    };

    public Route getIOSDevices = (request, response) -> {
        try{
            return iosManager.getDevices();
        }catch (Exception e){
            response.status(404);
            response.body(e.getMessage());
        }
        return null;
    };

    public Route getAndroidDevices = (request, response) -> {
        try{
            return androidManager.getDevices();
        }catch (Exception e){
            response.status(404);
            response.body(e.getMessage());
        }
        return null;
    };
}
