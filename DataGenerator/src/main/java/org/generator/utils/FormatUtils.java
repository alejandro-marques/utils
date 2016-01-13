package org.generator.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by alejandro on 20/10/15.
 */
public class FormatUtils {

    private static Gson gson = new Gson();

    public static <T> String getObjectAsJson (T object){
        return gson.toJson(object);
    }

    public static <T> T getJsonAsObject (String json, Type type){
        return (T) gson.fromJson(json, type);
    }
}
