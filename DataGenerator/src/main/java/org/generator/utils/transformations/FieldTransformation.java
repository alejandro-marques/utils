package org.generator.utils.transformations;

import org.generator.model.data.Field;
import org.generator.model.data.Transformation;
import org.generator.utils.properties.FieldUtils;

import java.util.List;
import java.util.Map;

public class FieldTransformation {

    public static void addField(Map<String, Object> document, Transformation transformation)
            throws Exception {
        Field operand = transformation.getValue();
        document.put(transformation.getField(), FieldUtils.getFieldValue(operand, null));
    }

    public static void removeField(Map<String, Object> document, Transformation transformation)
            throws Exception {
        for (String sourceField : transformation.getSource()){
            document.remove(sourceField);
        }
    }

    public static void copyField  (Map<String, Object> document, Transformation transformation)
            throws Exception {
        duplicateField(document, transformation, false);
    }

    public static void renameField  (Map<String, Object> document, Transformation transformation)
            throws Exception {
        duplicateField(document, transformation, true);
    }

    public static void duplicateField (Map<String, Object> document, Transformation transformation,
            boolean deleteOriginal) throws Exception {
        String suffix = "";
        int count = 1;

        String fieldName = transformation.getField();
        List<String> sources = transformation.getSource();
        for (String source : sources){
            document.put(fieldName + suffix, deleteOriginal?
                    document.remove(source) : document.get(source));
            suffix = "" + count++;
        }
    }
}
