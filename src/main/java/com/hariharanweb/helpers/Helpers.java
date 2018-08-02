package com.hariharanweb.helpers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Helpers {

    public static boolean isPortAvailable(int port) throws IOException {
        Socket s = null;
        try {
            s = new Socket("localhost", port);
            return false;
        } catch (IOException e) {
            return true;
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (IOException e) {
                    throw new RuntimeException("You should handle this error.", e);
                }
            }
        }
    }


    /* * Generates Random ports
    * Used during starting appium server
    */
    public static int getAvailablePort() throws IOException {
        ServerSocket socket = new ServerSocket(0);
        socket.setReuseAddress(true);
        int port = socket.getLocalPort();
        socket.close();
        return port;
    }

    public static String excuteProcess(String command) throws IOException {
        Process process = Runtime.getRuntime().exec(command);
        // get std output
        BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";
        String allLine = "";
        int i = 1;
        while ((line = r.readLine()) != null) {
            allLine = allLine + "" + line + "\n";
            if (line.contains("Console LogLevel: debug") && line.contains("Complete")) {
                break;
            }
            i++;
        }
        return allLine;
    }

    public static String getHostMachineIpAddress() throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("google.com", 80));
        return socket.getLocalAddress().toString()
                .replace("/", "");
    }

    public int getPid(Process process) {

        try {
            Class<?> cProcessImpl = process.getClass();
            Field fPid = cProcessImpl.getDeclaredField("pid");
            if (!fPid.isAccessible()) {
                fPid.setAccessible(true);
            }
            return fPid.getInt(process);
        } catch (Exception e) {
            return -1;
        }
    }

    public JsonObject parseJson( String jsonLine) {
        JsonElement jelement = new JsonParser().parse(jsonLine);
        return jelement.getAsJsonObject();
    }

    public GeneralServerFlag getServerArgument ( String capability ) {

        switch (capability) {
            case "SESSION_OVERRIDE":
                return ServerArgs.SESSION_OVERRIDE.getArgument();

            case "PRE_LAUNCH":
                return ServerArgs.PRE_LAUNCH.getArgument();

            case "LOG_LEVEL":
                return ServerArgs.LOG_LEVEL.getArgument();

            case "RELAXED_SECURITY":
                return ServerArgs.RELAXED_SECURITY.getArgument();

            case "CALLBACK_ADDRESS":
                return ServerArgs.CALLBACK_ADDRESS.getArgument();

            case "CALLBACK_PORT":
                return ServerArgs.CALLBACK_PORT.getArgument();

            case "LOG_TIMESTAMP":
                return ServerArgs.LOG_TIMESTAMP.getArgument();

            case "LOCAL_TIMEZONE":
                return ServerArgs.LOCAL_TIMEZONE.getArgument();

            case "LOG_NO_COLORS":
                return ServerArgs.LOG_NO_COLORS.getArgument();

            case "WEB_HOOK":
                return ServerArgs.WEB_HOOK.getArgument();

            case "CONFIGURATION_FILE":
                return ServerArgs.CONFIGURATION_FILE.getArgument();

            case "ROBOT_ADDRESS":
                return ServerArgs.ROBOT_ADDRESS.getArgument();

            case "ROBOT_PORT":
                return ServerArgs.ROBOT_PORT.getArgument();

            case "SHOW_CONFIG":
                return ServerArgs.SHOW_CONFIG.getArgument();

            case "NO_PERMS_CHECKS":
                return ServerArgs.NO_PERMS_CHECKS.getArgument();

            case "STRICT_CAPS":
                return ServerArgs.STRICT_CAPS.getArgument();

            case "TEMP_DIRECTORY":
                return ServerArgs.TEMP_DIRECTORY.getArgument();

            case "DEBUG_LOG_SPACING":
                return ServerArgs.DEBUG_LOG_SPACING.getArgument();

            case "ASYNC_TRACE":
                return ServerArgs.ASYNC_TRACE.getArgument();

            case "ENABLE_HEAP_DUMP":
                return ServerArgs.ENABLE_HEAP_DUMP.getArgument();

            default:
                return null;
        }
    }
}
