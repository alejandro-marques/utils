package org.generator.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class FormatUtils {

    private static Gson gson = new Gson();
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");

    static {
        timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    // Json
    public static <T> String getObjectAsJson (T object){
        return gson.toJson(object);
    }

    public static <T> T getJsonAsObject (String json, Type type){
        return (T) gson.fromJson(json, type);
    }


    // Numbers
    public static Double formatDouble (Double number, int decimals){
        if (null == number){return null;}
        return Double.parseDouble(String.format(new Locale("en_US"), "%." + decimals + "f", number));
    }

    // Dates and time
    public static String getTimeFromMillis(long time){
        Date date = new Date(time);
        return timeFormat.format(date);
    }
}
