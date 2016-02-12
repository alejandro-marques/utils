package org.generator.utils;

import com.google.gson.Gson;
import org.generator.launcher.LauncherConstants.Format;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class FormatUtils {

    private static final String CSV_SEPARATOR = ",";

    private static Gson gson = new Gson();
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");

    static {
        timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    // Generic
    public static String formatDocument (Map<String, Object> document, Format format)
            throws Exception {
        if (null == format){format = Format.JSON;}
        switch (format){
            case JSON:
                return getObjectAsJson(document);
            case CSV:
                return getDocumentAsCSV(document);
            default:
                throw new Exception("Not implemented format \"" + format.name + "\"");
        }
    }



    // Json
    public static <T> String getObjectAsJson (T object){
        return gson.toJson(object);
    }

    public static <T> T getJsonAsObject (String json, Type type){
        return (T) gson.fromJson(json, type);
    }

    // CSV
    public static String getDocumentAsCSV(Map<String, Object> document) {
        StringBuilder builder = new StringBuilder();
        String separator = "";
        for(Map.Entry<String, Object> entry : document.entrySet()){
            builder.append(separator);
            builder.append(entry.getValue());
            separator = CSV_SEPARATOR;
        }
        return builder.toString();
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
