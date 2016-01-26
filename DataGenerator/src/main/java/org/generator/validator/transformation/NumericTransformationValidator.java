package org.generator.validator.transformation;

import org.generator.model.configuration.FieldValueDefinition;
import org.generator.model.configuration.FieldValueDefinition.Type;
import org.generator.model.configuration.FieldValueInfo;
import org.generator.model.data.Transformation;
import org.generator.validator.FieldInfoValidator;

import java.util.List;

public class NumericTransformationValidator {

    public static void validateNumber (Transformation transformation, List<String> previousFields)
            throws Exception {
        String fieldName = TransformationValidator.checkField(transformation.getField());
        FieldValueInfo value = transformation.getValue();
        TransformationValidator.checkSourceFields(transformation.getSource(), previousFields, -1);
        if (null != value){
            FieldInfoValidator.checkFieldValueInfo(value, previousFields);
            if (Type.NUMERIC != FieldValueDefinition.getEnum(Type.class, value.getType())){
                throw new Exception("Value for numeric transformation must be a valid number");
            }
        }

        previousFields.add(fieldName);
    }

    public static void validateRound(Transformation transformation, List<String> previousFields)
            throws Exception {
        TransformationValidator.checkSourceFields(transformation.getSource(), previousFields, -1);
    }
}
