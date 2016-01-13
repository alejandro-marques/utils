package org.generator.configuration;

import org.generator.model.data.Dictionary;
import org.generator.utils.FormatUtils;

import java.util.Map;

public class TestProcessConfigurator {
    private static final String CONFIGURATION_FILE = "process/basic.json";

    public static void main(String[] args) throws Exception {

        ProcessConfigurator processConfigurator =
                new ProcessConfigurator(CONFIGURATION_FILE, true);

        System.out.println(FormatUtils.getObjectAsJson(processConfigurator.getProcessConfiguration()));
        for (Map.Entry<String, Dictionary> dictionary : CommonData.getDictionaries().entrySet()) {
            System.out.println(dictionary.getKey() + ": " + FormatUtils.getObjectAsJson(dictionary.getValue()));
        }
        System.out.println("END");
    }
}
