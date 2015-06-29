package com.cisco.demo.generaladapter;

import com.cisco.demo.excutionplatform.ExcutionUnit;
import com.cisco.demo.excutionplatform.ExcutionUnitBulder;

public class ColorSwitch extends Device {

    private int hue=0, sat=0, bri=0;


    public ColorSwitch(String addr, String radio, String type) {
        super(addr, radio, type);
    }

    public ColorSwitch(String addr, String radio, String type, int hue, int sat, int bri){
        super(addr, radio, type);
        this.hue = hue;
        this.sat = sat;
        this.bri = bri;
    }

    public int getHue() {
        return hue;
    }

    public int getSat() {
        return sat;
    }

    public int getBri() {
        return bri;
    }

    public void setColor(int hue, int sat, int brightness) {
        ExcutionUnit excutionUnit = new ExcutionUnitBulder(this).builder();
        excutionUnit.color(hue, sat, brightness);
    }

//
//    public void setColor(float[] HSVcolor)
//    {
//        if(HSVcolor.length == 3)
//        {
//            for(float hsvcolor:HSVcolor)
//            {
//                if(hsvcolor <0 || hsvcolor >1)
//                    return;
//            }
//            Color c = Color.getHSBColor(HSVcolor[0], HSVcolor[1], HSVcolor[2]);
//            System.out.println("RGB is " + c.getRed() + c.getGreen() + c.getBlue());
//            java.util.List<Float> colorxy = getRGBtoXY(c);
//            short x = (short)(colorxy.get(0)*255);
//            short y = (short)(colorxy.get(1)*255);
//            System.out.println("Set color xy is " + x + " " + y);
//            setColor(x, y);
//        }
//    }
//
//    public static java.util.List<Float> getRGBtoXY(Color c) {
//        // For the hue bulb the corners of the triangle are:
//        // -Red: 0.675, 0.322
//        // -Green: 0.4091, 0.518
//        // -Blue: 0.167, 0.04
//        double[] normalizedToOne = new double[3];
//        float cred, cgreen, cblue;
//        cred = c.getRed();
//        cgreen = c.getGreen();
//        cblue = c.getBlue();
//        normalizedToOne[0] = (cred / 255);
//        normalizedToOne[1] = (cgreen / 255);
//        normalizedToOne[2] = (cblue / 255);
//        float red, green, blue;
//
//        // Make red more vivid
//        if (normalizedToOne[0] > 0.04045) {
//            red = (float) Math.pow(
//                    (normalizedToOne[0] + 0.055) / (1.0 + 0.055), 2.4);
//        } else {
//            red = (float) (normalizedToOne[0] / 12.92);
//        }
//
//        // Make green more vivid
//        if (normalizedToOne[1] > 0.04045) {
//            green = (float) Math.pow((normalizedToOne[1] + 0.055)
//                    / (1.0 + 0.055), 2.4);
//        } else {
//            green = (float) (normalizedToOne[1] / 12.92);
//        }
//
//        // Make blue more vivid
//        if (normalizedToOne[2] > 0.04045) {
//            blue = (float) Math.pow((normalizedToOne[2] + 0.055)
//                    / (1.0 + 0.055), 2.4);
//        } else {
//            blue = (float) (normalizedToOne[2] / 12.92);
//        }
//
//        float X = (float) (red * 0.649926 + green * 0.103455 + blue * 0.197109);
//        float Y = (float) (red * 0.234327 + green * 0.743075 + blue * 0.022598);
//        float Z = (float) (red * 0.0000000 + green * 0.053077 + blue * 1.035763);
//
//        float x = X / (X + Y + Z);
//        float y = Y / (X + Y + Z);
//
//        java.util.List<Float> xyAsList = new ArrayList<Float>();
//        xyAsList.add(x);
//        xyAsList.add(y);
//        return xyAsList;
//    }
//    //{ "actuators":[ {"type":"light","state":"enabled", "measurement":[{"value":{"level":0}}]}] }
//    //{ "actuators"[ {"type":"light","state":"enabled", "measurement":[{"value":{"hue":200,"saturation":200},
//    // "unit":"HSL256"}]}] }
//    public void setColor(short hue, short sat, short brightness) {
//        this.hue = hue;
//        this.sat = sat;
//        JSONArray messure_array = new JSONArray();
//        JSONObject messure_value = new JSONObject();
//        JSONObject color_value = new JSONObject();
//        color_value.put("hue", new Short(hue));
//        color_value.put("saturation", new Short(sat));
//        messure_value.put("value", color_value);
//        messure_array.put(messure_value);
//
//        JSONObject actuators_value = new JSONObject();
//        actuators_value.put("type", new String(getParent().getType()));
//        actuators_value.put("state", new String("enabled"));
//        actuators_value.put("measurement", messure_array);
//
//        JSONArray actuators_array = new JSONArray();
//        actuators_array.put(actuators_value);
//
//        JSONObject actuators_root = new JSONObject();
//        actuators_root.put("actuators",actuators_array);
//
//        System.out.println(actuators_root);
//
//        HttpCmd httpCmd = new HttpCmd("put", consts1.SERVERIP + "/devices/" + getParent().getRadio()+ "/" + getParent
//                ().getIEEE(), actuators_root.toString());
//        HTTPHelper.Instance().addCmd(httpCmd);
////
////        HttpClient client = HttpClientBuilder.create().build();
////        HttpPut httpput = new HttpPut(
////                consts1.SERVERIP + "/devices/" + getParent().getRadio()+ "/" + getParent().getIEEE());
////        try {
////            StringEntity input = new StringEntity(actuators_root.toString());
////            input.setContentType("application/json");
////            httpput.setEntity(input);
////            client.execute(httpput);
////
////        } catch (UnsupportedEncodingException e) {
////            e.printStackTrace();
////        } catch (ClientProtocolException e) {
////            e.printStackTrace();
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//    }
}
