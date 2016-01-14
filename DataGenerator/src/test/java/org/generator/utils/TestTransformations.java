package org.generator.utils;

import com.google.gson.reflect.TypeToken;
import org.generator.model.configuration.ProcessConfiguration;
import org.generator.model.data.Transformation;
import org.generator.utils.transformations.TransformationUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestTransformations {
    private static final String CONFIGURATION_FILE = "process/basic.json";

    public static void main(String[] args) throws Exception {

        Map<String, Object> document = new HashMap<>();
        document.put("journey", "touchpoint A");
        document.put("segment", "autónomo");
        document.put("product", "fijo");
        document.put("age", "tercera edad");
        document.put("path", 1);
        document.put("position", 6);
        document.put("relatedDocuments", 6);
        document.put("touchpoint", "touchpoint 6");
        document.put("users", 461);

        ProcessConfiguration processConfiguration = FileUtils.getObjectFromJsonFile(CONFIGURATION_FILE,
                new TypeToken<ProcessConfiguration>(){}.getType(),
                true);

        List<Transformation> transformations = processConfiguration.getTransformations();

        System.out.println(FormatUtils.getObjectAsJson(document));

        for(Transformation transformation : transformations){
            TransformationUtils.transform(document, transformation);
        }

        System.out.println(FormatUtils.getObjectAsJson(document));
        System.out.println("END");
    }
}
