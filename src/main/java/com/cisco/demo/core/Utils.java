package com.cisco.demo.core;

import java.io.File;
import java.io.FileNotFoundException;
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

}
