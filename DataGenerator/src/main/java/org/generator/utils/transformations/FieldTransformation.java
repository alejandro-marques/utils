package org.generator.utils.transformations;

import org.generator.model.configuration.FieldValueInfo;
import org.generator.model.data.FieldValue;
import org.generator.model.data.Transformation;
import org.generator.utils.values.ValueUtils;

import java.util.Map;

public class FieldTransformation {

    public static void createField(Map<String, FieldValue> document, Transformation transformation)
            throws Exception {
        FieldValueInfo operand = transformation.getValue();
        String fieldName = transformation.getField();

        document.put(fieldName, ValueUtils.getValue(operand, 0, null, document));
    }

    public static void removeField(Map<String, FieldValue> document, Transformation transformation)
            throws Exception {
        for (String sourceField : transformation.getSource()){
            document.remove(sourceField);
        }
    }

    public static void copyField  (Map<String, FieldValue> document, Transformation transformation)
            throws Exception {
        duplicateField(document, transformation, false);
    }

    public static void renameField  (Map<String, FieldValue> document, Transformation transformation)
            throws Exception {
        duplicateField(document, transformation, true);
    }

    public static void duplicateField (Map<String, FieldValue> document, Transformation transformation,
            boolean deleteOriginal) throws Exception {
        String fieldName = transformation.getField();
        String sourceField = transformation.getSource().get(0);
        document.put(fieldName, deleteOriginal? document.remove(sourceField) : document.get(sourceField));
    }
}
