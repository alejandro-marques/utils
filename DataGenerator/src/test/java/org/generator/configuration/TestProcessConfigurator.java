package org.generator.configuration;

import org.generator.model.data.Dictionary;
import org.generator.utils.FormatUtils;

import java.util.Map;

public class TestProcessConfigurator {
    private static final String CONFIGURATION_FILE = "process/telefonica.json";

    public static void main(String[] args) throws Exception {

        ProcessConfigurator processConfigurator =
                new ProcessConfigurator(CONFIGURATION_FILE, true);

        System.out.println(FormatUtils.getObjectAsJson(processConfigurator.getProcessConfiguration()));
        System.out.println("\nDictionaries:");
        for (Map.Entry<String, Dictionary> dictionary : CommonData.getDictionaries().entrySet()) {
            System.out.println("\t" + dictionary.getKey() + ": " + FormatUtils.getObjectAsJson(dictionary.getValue()));
        }
        System.out.println("\nRelations:");
        for (Map.Entry<String, Map<String, Dictionary>> relation : CommonData.getRelations().entrySet()) {
            System.out.println("\t" + relation.getKey() + ": ");
            for (Map.Entry<String, Dictionary> dictionary : relation.getValue().entrySet()){
                System.out.println("\t\t" + dictionary.getKey() + ": " + FormatUtils.getObjectAsJson(dictionary.getValue()));
            }
        }
        System.out.println("END");
    }
}
