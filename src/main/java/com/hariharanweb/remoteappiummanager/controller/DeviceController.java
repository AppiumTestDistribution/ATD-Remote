package com.hariharanweb.remoteappiummanager.controller;

import com.hariharanweb.helpers.Helpers;
import com.thoughtworks.android.AndroidManager;
import com.thoughtworks.device.Device;
import com.thoughtworks.device.DeviceManager;
import com.thoughtworks.device.SimulatorManager;
import com.thoughtworks.iOS.IOSManager;
import spark.Route;

import java.util.List;

public class DeviceController {

    private DeviceManager deviceManager;
    private SimulatorManager simulatorManager;
    private IOSManager iosManager;
    private AndroidManager androidManager;

    public DeviceController() {
        deviceManager = new DeviceManager();
        simulatorManager = new SimulatorManager();
        iosManager = new IOSManager();
        androidManager = new AndroidManager();
    }

    public Route getDevices = (request, response) -> {
        try {
            return deviceManager.getDevices();
        } catch (Exception e) {
            response.status(404);
            response.body(e.getMessage());
        }
        return response.body();
    };
    public Route getDevice = (request, response) -> {
        try {
            return deviceManager.getDevice(request.params(":udid"));
        } catch (Exception e) {
            response.status(404);
            response.body(e.getMessage());
        }
        return null;
    };

    public Route getBootedSims = ((request, response) -> {
        try {
            List<Device> devices = simulatorManager.getAllBootedSimulators("iOS");
            return devices;
        } catch (Exception e) {
            response.status(404);
            response.body(e.getMessage());
        }
        return null;
    });

    public Route getIOSDevices = ((request, response) -> {
        try {
            List<Device> devices = iosManager.getDevices();
            devices.addAll(simulatorManager.getAllBootedSimulators("iOS"));
            return devices;
        } catch (Exception e) {
            response.status(404);
            response.body(e.getMessage());
        }
        return null;
    });

    public Route getSimulator = (request, response) -> {
        List<Device> allSimulators = simulatorManager.getAllSimulators("iOS");
        String[] simulatorName = request.queryParamsValues("simulatorName");
        String[] simulatorOSVersion = request.queryParamsValues("simulatorOSVersion");
        if (simulatorName != null && simulatorOSVersion != null) {
            return simulatorManager.getDevice(simulatorName[0], simulatorOSVersion[0], "iOS");
        }
        return allSimulators;
    };

    public Route getIOSRealDevices = (request, response) -> {
        try {
            return iosManager.getDevices();
        } catch (Exception e) {
            response.status(404);
            response.body(e.getMessage());
        }
        return null;
    };

    public Route startWebkitProxy = (request, response) -> {
        try {
            String[] udid = request.queryParamsValues("udid");
            String[] port = request.queryParamsValues("port");
            String webkitRunner = "ios_webkit_debug_proxy -c " + udid[0] + ":" + port[0];
            Process p1 = Runtime.getRuntime().exec(webkitRunner);
            int pid = new Helpers().getPid(p1);
            return pid;
        } catch (Exception e) {
            response.status(404);
            response.body(e.getMessage());
        }
        return null;
    };

    public Route stopWebkitProxy = (request, response) -> {
        try {
            String[] processID = request.queryParamsValues("processID");
            if (Integer.parseInt(processID[0]) != -1) {
                String command = "kill -9 " + processID[0];
                Runtime.getRuntime().exec(command);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            response.status(404);
            response.body(e.getMessage());
        }
        return null;
    };

    public Route getAndroidDevices = (request, response) -> {
        try {
            return androidManager.getDevices();
        } catch (Exception e) {
            response.status(404);
            response.body(e.getMessage());
        }
        return null;
    };

    public Route startADBLog = (request, response) -> {
        String[] udid = request.queryParamsValues("udid");
        String[] fileName = request.queryParamsValues("fileName");
        return androidManager.startADBLog(udid[0], fileName[0]);
    };
    public Route stopADBLog = (request, response) -> {
        return androidManager.stopADBLog(request.params(":udid"));
    };
}
