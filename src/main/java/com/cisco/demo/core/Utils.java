package com.cisco.demo.core;

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

        StringBuilder result = new StringBuilder("");

        //Get file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            File file = new File(classLoader.getResource(fileName).getFile());
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }
            scanner.close();
        } catch (FileNotFoundException e) {
        } catch (NullPointerException e) {
        }

        return result.toString();

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
