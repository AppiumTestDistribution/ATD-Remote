package com.hariharanweb.helpers;

import java.io.IOException;
import java.net.Socket;

public class Helpers {

    public static boolean available(int port) throws IOException {
        Socket s = null;
        try {
            s = new Socket("localhost", port);
            return false;
        } catch (IOException e) {
            return true;
        } finally {
            if( s != null){
                try {
                    s.close();
                } catch (IOException e) {
                    throw new RuntimeException("You should handle this error." , e);
                }
            }
        }
    }
}
