package org.generator.model.configuration;

import com.google.gson.reflect.TypeToken;
import org.generator.utils.FileUtils;
import org.generator.utils.FormatUtils;

public class TestProcessModel {
    private static final String CONFIGURATION_FILE = "process/test.json";

    public static void main(String[] args) throws Exception {

        ProcessConfiguration processConfiguration = FileUtils.getObjectFromJsonFile(CONFIGURATION_FILE,
                new TypeToken<ProcessConfiguration>(){}.getType(),
                true);
        System.out.println(FormatUtils.getObjectAsJson(processConfiguration));
        System.out.println("END");
    }
}
