package org.generator.utils.transformations;

import org.generator.constants.TransformationConstants;
import org.generator.model.data.Transformation;

import java.util.List;
import java.util.Map;

public class TextTransformation {

    public static void split(Map<String, Object> document, Transformation transformation)
            throws Exception {
        String field = transformation.getField();
        Map<String, String> parameters = transformation.getParameters();
        boolean trim = Boolean.parseBoolean(parameters.get(TransformationConstants.TRIM));
        String character = parameters.get(TransformationConstants.CHARACTER);

        List<String> source = transformation.getSource();
        for (String fieldName : source){
            String sourceValue = (String) document.remove(fieldName);

            if (null != sourceValue){
                String[] values = sourceValue.split(character);
                for (int i = 0; i < values.length; i++) {
                    document.put(field + "_" + i, trim? values[i].trim() : values[i]);
                }
            }
            else {document.put(field, null);}
        }
    }
}
