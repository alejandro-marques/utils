package org.generator.utils;

import com.google.gson.reflect.TypeToken;
import org.generator.configuration.CommonData;
import org.generator.configuration.ProcessConfigurator;
import org.generator.model.configuration.ProcessConfiguration;
import org.generator.model.data.Transformation;
import org.generator.utils.transformations.TransformationUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestTransformations {
    private static final String CONFIGURATION_FILE = "process/telefonica.json";

    public static void main(String[] args) throws Exception {

        Map<String, Object> document = new HashMap<>();
        document.put("journeyId", "ALTA #-#-# Canal online");
        document.put("segment", "residencial");
        document.put("product", "fijo");
        document.put("age", "joven");
        document.put("path", 1);
        document.put("touchpoint_order", 5);
        document.put("relatedDocuments", 6);
        document.put("touchpoint_functionality", "TRX , ALTA");
        document.put("users", 461);

        ProcessConfigurator processConfigurator =
                new ProcessConfigurator(CONFIGURATION_FILE, true);
        System.out.println(FormatUtils.getObjectAsJson(CommonData.getRelations()));

        ProcessConfiguration processConfiguration = processConfigurator.getProcessConfiguration();

        List<Transformation> transformations = processConfiguration.getTransformations();

        System.out.println(FormatUtils.getObjectAsJson(document));

        for(Transformation transformation : transformations){
            TransformationUtils.transform(document, transformation);
        }

        System.out.println(FormatUtils.getObjectAsJson(document));
        System.out.println("END");
    }
}
