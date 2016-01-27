package org.generator.validator.transformation;

import org.generator.model.data.Transformation;
import org.generator.validator.FieldInfoValidator;

import java.util.List;

public class FieldTransformationValidator {

    public static void validateCreateField(Transformation transformation, List<String> previousFields)
            throws Exception {
        String fieldName = TransformationValidator.checkField(transformation.getField());
        FieldInfoValidator.checkFieldValueInfo(transformation.getValue(), previousFields);
        previousFields.add(fieldName);
    }

    public static void validateRemoveField (Transformation transformation,
            List<String> previousFields) throws Exception {
        TransformationValidator.checkSourceFields(transformation.getSource(), previousFields, -1);
    }


    public static void validateCopyField (Transformation transformation,
            List<String> previousFields) throws Exception {
        validateDuplicateField(transformation, previousFields, false);
    }


    public static void validateRenameField (Transformation transformation,
            List<String> previousFields) throws Exception {
        validateDuplicateField(transformation, previousFields, true);
    }

    public static void validateDuplicateField (Transformation transformation,
            List<String> previousFields, boolean removeOriginal) throws Exception {
        String fieldName = TransformationValidator.checkField(transformation.getField());
        List<String> sourceFields = TransformationValidator.checkSourceFields(
                transformation.getSource(), previousFields, 1);
        previousFields.add(fieldName);
        if (removeOriginal){previousFields.remove(sourceFields.get(0));}
    }
}
