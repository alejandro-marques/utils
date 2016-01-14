package org.generator.utils.transformations;

import org.generator.model.data.Transformation;

import java.util.HashMap;
import java.util.Map;

public class BasicTransformation {

    public static void combine(Map<String, Object> document, Transformation transformation)
            throws Exception {
        Map<String, Object> combined = new HashMap<>();
        for (String sourceField : transformation.getSource()){
            Object value = document.remove(sourceField);
            combined.put(sourceField, value);
        }
        document.put(transformation.getField(), combined);
    }
}
