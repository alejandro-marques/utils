package org.generator.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class FormatUtils {

    private static Gson gson = new Gson();

    public static <T> String getObjectAsJson (T object){
        return gson.toJson(object);
    }

    public static <T> T getJsonAsObject (String json, Type type){
        return (T) gson.fromJson(json, type);
    }


    public static Double formatDouble (Double number, int decimals){
        if (null == number){return null;}
        return Double.parseDouble(String.format(new Locale("en_US"), "%." + decimals + "f", number));
    }
}
