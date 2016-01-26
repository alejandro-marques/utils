package org.generator.configuration;

import org.generator.model.data.Dictionary;

import java.util.HashMap;
import java.util.Map;

public class CommonData {

    private static boolean resourceConfiguration;
    private static String configurationPath;

    public static boolean isResourceConfiguration() {return resourceConfiguration;}
    public static void setResourceConfiguration(boolean resourceConfiguration) {
        CommonData.resourceConfiguration = resourceConfiguration;
    }

    public static String getConfigurationPath() {return configurationPath;}
    public static void setConfigurationPath(String configurationPath) {
        CommonData.configurationPath = configurationPath;
    }

    private static Map<String, Dictionary> dictionaries = new HashMap<>();
    private static Map<String, Map<String, Dictionary>> relations = new HashMap<>();
    private static Map<String, Map<String, Map<String, Object>>> translations = new HashMap<>();

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

    public static Map<String, Map<String, Map<String, Object>>> getTranslations (){
        return translations;
    }
    public static Map<String, Map<String, Object>> getTranslation (String name){
        return translations.get(name);
    }
    public static void addTranslation (String name, Map<String, Map<String, Object>> translation){
        translations.put(name, translation);
    }
}
