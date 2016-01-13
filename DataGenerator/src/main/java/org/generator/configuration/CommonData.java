package org.generator.configuration;

import org.generator.model.data.Dictionary;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alejandro on 12/01/16.
 */
public class CommonData {

    private static Map<String, Dictionary> dictionaries = new HashMap<>();

    public static Map<String, Dictionary> getDictionaries (){return dictionaries;}
    public static Dictionary getDictionary (String name){
        return dictionaries.get(name);
    }
    public static void addDictionary (String name, Dictionary dictionary){
        dictionaries.put(name, dictionary);
    }
}
