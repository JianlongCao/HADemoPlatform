package com.cisco.demo.core;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;
import java.util.Scanner;

public class Utils {

    private static Utils utils = null;

    public static Utils Instance() {
        if(utils == null) {
            utils = new Utils();
        }
        return utils;
    }

    private Utils() {

    }

    public String getFile(String fileName) {

        //Get file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();
        String content = "";
        InputStream inputStream = this.getClass().getResourceAsStream("/" + fileName);
        try {
            content = IOUtils.toString(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;
    }

    public String getIp() throws Exception {
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            String ip = in.readLine();
            return ip;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    System.out.println("Failed to get host IP");
                }
            }
        }
    }

    public static boolean checkNotNull(String arg) {
        if(arg == null || arg.trim().equals("")){
            return false;
        }
        return true;
    }

}
