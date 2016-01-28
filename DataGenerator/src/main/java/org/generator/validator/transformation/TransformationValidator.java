package org.generator.validator.transformation;

import org.generator.constants.TransformationConstants;
import org.generator.model.data.Condition;
import org.generator.model.data.Transformation;
import org.generator.model.data.Transformation.Operation;
import org.generator.validator.FieldInfoValidator;
import org.generator.validator.Validator;
import org.generator.validator.Validator.ValueType;
import org.generator.validator.condition.ConditionValidator;

import java.util.List;

public class TransformationValidator {

    public static void validate (Transformation transformation, List<String> previousFields)
            throws Exception {
        Operation operation = checkOperation(transformation.getOperation());
        checkConditions(transformation.getConditions(), previousFields);


        switch (operation){

            case CREATE:
                FieldTransformationValidator.validateCreateField(transformation, previousFields);
                break;

            case REMOVE:
                FieldTransformationValidator.validateRemoveField(transformation, previousFields);
                break;

            case COPY:
                FieldTransformationValidator.validateCopyField(transformation, previousFields);
                break;

            case RENAME:
                FieldTransformationValidator.validateRenameField(transformation, previousFields);
                break;


            case COMBINE:
                BasicTransformationValidator.validateCombine(transformation, previousFields);
                break;

            case CLEAN:
                BasicTransformationValidator.validateClean(transformation, previousFields);
                break;


            case MULTIPLY:
            case DIVIDE:
            case SUM:
            case SUBTRACT:
                NumericTransformationValidator.validateNumber(transformation, previousFields);
                break;

            case ROUND:
                NumericTransformationValidator.validateRound(transformation, previousFields);
                break;


            case SPLIT:
                TextTransformationValidator.validateSplit(transformation, previousFields);
                break;

            case TRANSLATE:
                TextTransformationValidator.validateTranslate(transformation, previousFields);
                break;


            default:
                throw new Exception("Type \"" + transformation.getOperation() + "\" not implemented yet.");
        }
    }

    private static Operation checkOperation (String operationName) throws Exception {
        if (null == operationName){
            throw new Exception("Missing value mandatory property \"operation\"");
        }

        Operation operation = Operation.fromString(operationName);
        if (null == operation){
            throw new Exception("Not supported \"" + operationName + "\" operation.");
        }
        return operation;
    }

    private static List<Condition> checkConditions (List<Condition> conditions,
            List<String> previousFields) throws Exception{
        if (null == conditions) {return null;}
        for (Condition condition : conditions){
            try{ConditionValidator.validate(condition, previousFields);}
            catch (Exception exception){
                throw new Exception("condition -> " + exception.getMessage());
            }
        }
        return conditions;
    }

    public static String checkField(String fieldName) throws Exception {
        String field = (String) Validator.checkParameter(TransformationConstants.FIELD,
                fieldName, ValueType.STRING, true);
        FieldInfoValidator.checkFieldName(field);
        return field;
    }

    public static List<String> checkSourceFields (List<String> sourceFields,
            List<String> previousFields, int maxItems) throws Exception {
        sourceFields = Validator.checkParameterList(TransformationConstants.SOURCE,
                sourceFields, ValueType.STRING, true);
        if (-1 != maxItems && maxItems < sourceFields.size()){
            throw new Exception("Only up to \"" + maxItems + "\" values are allowed for \"source\"");
        }
        for (String field : sourceFields){
            if (field != null && field.matches(".*_[0-9]+")){
                field = field.replaceAll("_[0-9]+", "_[]");
            }
            if (!previousFields.contains(field)){
                throw new Exception("Field \"" + field + "\" not found. Check name or field " +
                        "generation order");
            }
        }
        return sourceFields;
    }
}
