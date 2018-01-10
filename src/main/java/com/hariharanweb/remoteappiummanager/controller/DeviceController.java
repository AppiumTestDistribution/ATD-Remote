package com.hariharanweb.remoteappiummanager.controller;

import com.thoughtworks.device.Device;
import com.thoughtworks.device.DeviceManager;
import spark.Route;

import java.util.List;

public class DeviceController {
    public Route getDevices = (request, response) -> {
        DeviceManager deviceManager = new DeviceManager();
        List<Device> deviceProperties = deviceManager.getDeviceProperties();

        return deviceProperties;
    };
}
