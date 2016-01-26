package org.generator.utils;

import java.util.Map;
import java.util.Map.Entry;

public class CommonUtils {

    public static String getStringFromMapValues(Map<String, Object> map, String separator) throws Exception {
        StringBuilder builder = new StringBuilder();
        String prefix = "";
        for(Entry<String, Object> entry : map.entrySet()){
            builder.append(prefix).append(entry.getValue());
            prefix = separator;
        }

        return builder.toString();
    }
}
