package org.generator.utils;

import org.generator.configuration.CommonData;
import org.generator.configuration.ProcessConfigurator;
import org.generator.model.configuration.ProcessInfo;
import org.generator.model.data.FieldValue;
import org.generator.model.data.Transformation;
import org.generator.utils.transformations.TransformationUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestTransformations {
    private static final String CONFIGURATION_FILE = "process/telefonica-paths.json";

    public static void main(String[] args) throws Exception {

        Map<String, FieldValue> document = new HashMap<>();
        document.put("journeyId", new FieldValue("ALTA #-#-# Canal online"));
        document.put("segment", new FieldValue("residencial"));
        document.put("product", new FieldValue("fijo"));
        document.put("age", new FieldValue("joven"));
        document.put("path", new FieldValue(1));
        document.put("touchpoint_order", new FieldValue(5));
        document.put("relatedDocuments", new FieldValue(6));
        document.put("touchpoint_functionality", new FieldValue("TRX , ALTA"));
        document.put("users", new FieldValue(461));

        ProcessConfigurator processConfigurator =
                new ProcessConfigurator(CONFIGURATION_FILE, true);
        System.out.println(FormatUtils.getObjectAsJson(CommonData.getRelations()));

        ProcessInfo processInfo = processConfigurator.getProcessInfo();

        List<Transformation> transformations = processInfo.getTransformations();

        System.out.println(FormatUtils.getObjectAsJson(document));

        for(Transformation transformation : transformations){
            TransformationUtils.transform(document, transformation);
        }

        System.out.println(FormatUtils.getObjectAsJson(document));
        System.out.println("END");
    }
}
