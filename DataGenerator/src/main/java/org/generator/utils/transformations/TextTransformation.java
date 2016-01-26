package org.generator.utils.transformations;

import org.generator.configuration.CommonData;
import org.generator.constants.PropertiesConstants;
import org.generator.constants.TransformationConstants;
import org.generator.model.data.FieldValue;
import org.generator.model.data.Transformation;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TextTransformation {

    public static void split(Map<String, FieldValue> document, Transformation transformation)
            throws Exception {
        String field = transformation.getField();
        Map<String, String> parameters = transformation.getParameters();
        boolean trim = Boolean.parseBoolean(parameters.get(TransformationConstants.TRIM));
        String character = parameters.get(TransformationConstants.CHARACTER);

        List<String> source = transformation.getSource();
        for (String fieldName : source){
            String sourceValue = (String) document.remove(fieldName).getValue();

            if (null != sourceValue){
                String[] values = sourceValue.split(character);
                for (int i = 0; i < values.length; i++) {
                    document.put(field + "_" + i, new FieldValue(trim? values[i].trim() : values[i]));
                }
            }
            else {document.put(field, null);}
        }
    }

    public static void translate(Map<String, FieldValue> document, Transformation transformation)
            throws Exception {
        List<String> sources = transformation.getSource();
        String translationName = transformation.getParameters().get(PropertiesConstants.TRANSLATION);
        Map<String, Map<String, Object>> translation = CommonData.getTranslation(translationName);

        for (String source : sources) {
            Object value = document.get(source);
            Map<String, Object> fields = translation.get(value.toString());
            if (null != fields){
                for (Entry<String, Object> field : fields.entrySet()){
                    document.put(field.getKey(), new FieldValue(field.getValue()));
                }
            }
        }
    }
}
