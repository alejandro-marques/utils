package org.generator.model.configuration;

import com.google.gson.reflect.TypeToken;
import org.generator.utils.FileUtils;
import org.generator.utils.FormatUtils;

public class TestProcessModel {
    private static final String CONFIGURATION_FILE = "test/test-paths.json";

    public static void main(String[] args) throws Exception {

        ProcessInfo processInfo = FileUtils.getObjectFromJsonFile(CONFIGURATION_FILE,
                new TypeToken<ProcessInfo>(){}.getType(),
                true);
        System.out.println(FormatUtils.getObjectAsJson(processInfo));
        System.out.println("END");
    }
}
