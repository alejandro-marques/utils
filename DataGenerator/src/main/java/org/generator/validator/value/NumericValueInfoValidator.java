package org.generator.validator.value;

import org.generator.constants.PropertiesConstants;
import org.generator.model.configuration.FieldValueDefinition.Mode;
import org.generator.model.configuration.FieldValueDefinition.Subtype;
import org.generator.model.configuration.FieldValueInfo;
import org.generator.validator.Validator;
import org.generator.validator.Validator.ValueType;

import java.util.Map;

public class NumericValueInfoValidator extends FieldValueInfoValidator {

    public static void validate (Subtype subtype, FieldValueInfo fieldValueInfo) throws Exception {
        // Numeric parameters found in value configuration must be of the same type as the generated value. For
        // example, when generating an integer, min and max limits must be integers too. Therefore the default
        // value type for these fields is saved so every numeric parameter can be checked with the corresponding type
        ValueType parameterType = Subtype.INTEGER == subtype? ValueType.INTEGER : ValueType.DOUBLE;

        // General value parameters are checked
        validateOptions(fieldValueInfo.getOptions(), parameterType);

        // Initial parameters are mandatory, if not present an exception is thrown
        Map<String, String> initialParameters = fieldValueInfo.getInitial();
        if (null == initialParameters || initialParameters.isEmpty()) {
            throw new Exception("Missing mandatory initial value parameters");
        }
        validateValueParameters(subtype, initialParameters, parameterType, false);

        // Variation parameters are optional, therefore they are only checked if present
        Map<String, String> variationParameters = fieldValueInfo.getVariation();
        if (null != variationParameters && !variationParameters.isEmpty()) {
            validateValueParameters(subtype, variationParameters, parameterType, true);
        }
    }

    private static void validateOptions(Map<String, String> options, ValueType parameterType)
            throws Exception {
        if (options != null && !options.isEmpty()){
            Validator.findAndCheckParameter(options, PropertiesConstants.LIMIT, ValueType.BOOLEAN, false);
            Validator.findAndCheckParameter(options, PropertiesConstants.MAX, parameterType, false);
            Validator.findAndCheckParameter(options, PropertiesConstants.MIN, parameterType, false);
        }
    }

    private static void validateValueParameters(Subtype subtype, Map<String, String> parameters,
            ValueType parameterType, boolean isVariation) throws Exception {
        try {
            String modeName = (String) Validator.findAndCheckParameter(parameters,
                    PropertiesConstants.MODE, ValueType.STRING, true);
            Mode mode = checkMode(modeName, subtype, isVariation);

            switch (mode) {
                case FIXED:
                    validateFixed(parameters, parameterType);
                    break;
                case UNIFORM:
                    validateUniform(parameters, parameterType);
                    break;
                case GAUSSIAN:
                    validateGaussian(parameters, parameterType);
                    break;
            }
        }
        catch (Exception exception){
            throw new Exception ((isVariation? "variation" : "initial") + " -> "
                    + exception.getMessage());
        }
    }

    private static void validateFixed(Map<String, String> parameters, ValueType parameterType)
            throws Exception {
        Validator.findAndCheckParameter(parameters, PropertiesConstants.VALUE, parameterType, true);
    }

    private static void validateUniform(Map<String, String> parameters, ValueType parameterType)
            throws Exception {
        Validator.findAndCheckParameter(parameters, PropertiesConstants.MIN, parameterType, true);
        Validator.findAndCheckParameter(parameters, PropertiesConstants.MAX, parameterType, true);
    }

    private static void validateGaussian(Map<String, String> parameters, ValueType parameterType)
            throws Exception {
        Validator.findAndCheckParameter(parameters, PropertiesConstants.MIN, parameterType, true);
        Validator.findAndCheckParameter(parameters, PropertiesConstants.MAX, parameterType, true);
        Validator.findAndCheckParameter(parameters, PropertiesConstants.MEAN, parameterType, true);
        Validator.findAndCheckParameter(parameters, PropertiesConstants.VARIANCE, parameterType, true);
    }
}
