package org.generator.configuration;

import org.generator.model.data.Dictionary;

import java.util.HashMap;
import java.util.Map;

public class CommonData {

    private static Map<String, Dictionary> dictionaries = new HashMap<>();
    private static Map<String, Map<String, Dictionary>> relations = new HashMap<>();

    public static Map<String, Dictionary> getDictionaries (){return dictionaries;}
    public static Dictionary getDictionary (String name){
        return dictionaries.get(name);
    }
    public static void addDictionary (String name, Dictionary dictionary){
        dictionaries.put(name, dictionary);
    }

    public static Map<String, Map<String, Dictionary>> getRelations (){return relations;}
    public static Map<String, Dictionary> getRelation (String name){
        return relations.get(name);
    }
    public static void addRelation (String name, Map<String, Dictionary> relation){
        relations.put(name, relation);
    }
}
