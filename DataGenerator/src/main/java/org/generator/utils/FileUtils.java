package org.generator.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alejandro on 19/10/15.
 */
public class FileUtils {

    public static List<String> getListFromFile (String fileName, boolean innerFile) throws Exception {
        List<String> list = new ArrayList<>();

        InputStreamReader input;
        try {
            input = innerFile?
                    new InputStreamReader(FileUtils.class.getResourceAsStream("/" + fileName), "UTF-8") :
                    new FileReader(fileName);
        }
        catch (Exception exception) {throw new Exception("File \"" + fileName + "\" not found.");}

        if (input != null){
            BufferedReader reader = new BufferedReader(input);
            String line=null;
            while((line=reader.readLine()) != null){
                list.add(line.trim());
            }
        }

        return list;
    }

    public static <T> T getObjectFromJsonFile (String fileName, Type type, boolean inner) throws Exception{
        StringBuilder builder = new StringBuilder();
        for (String line : getListFromFile(fileName, inner)){
            builder.append(line.trim());
        }
        try {
            return FormatUtils.getJsonAsObject(builder.toString(), type);
        }
        catch (Exception exception){
            throw new Exception("File \"" + fileName + "\" does not have the expected format for " + type);
        }
    }
}
