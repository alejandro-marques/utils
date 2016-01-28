package org.generator.validator.transformation;

import org.generator.model.data.Transformation;

import java.util.List;

public class BasicTransformationValidator {

    public static void validateCombine (Transformation transformation, List<String> previousFields)
            throws Exception {
        String fieldName = TransformationValidator.checkField(transformation.getField());
        TransformationValidator.checkSourceFields(transformation.getSource(), previousFields, -1);
        previousFields.add(fieldName);
    }

    public static void validateClean (Transformation transformation, List<String> previousFields)
            throws Exception {
        TransformationValidator.checkSourceFields(transformation.getSource(), previousFields, -1);
    }
}
