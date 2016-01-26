package org.generator.validator.condition;

import org.generator.model.data.Condition;
import org.generator.model.data.Condition.Type;
import org.generator.validator.transformation.TransformationValidator;

import java.util.List;

public class ConditionValidator {
    public static Condition validate (Condition condition, List<String> previousValues)
            throws Exception {
        Type type = checkConditionType(condition.getCondition());

        switch (type){
            case EQUAL:
                return checkEqualsCondition(condition, previousValues);

            default:
                throw new Exception("Condition \"" + type.toString() + "\" not implemented yet.");
        }
    }

    private static Type checkConditionType (String conditionTypeName) throws Exception {
        if (null == conditionTypeName){
            throw new Exception("Missing value mandatory property \"condition\"");
        }

        Type conditionType = Type.fromString(conditionTypeName);
        if (null == conditionType){
            throw new Exception("Not supported \"" + conditionTypeName + "\" condition.");
        }
        return conditionType;
    }

    private static Condition checkEqualsCondition (Condition condition, List<String> previousValues)
            throws Exception {

        String value = condition.getValue();
        List<String> sourceFields = condition.getSource();

        if (null == value || (null != sourceFields && !sourceFields.isEmpty())){
            TransformationValidator.checkSourceFields(sourceFields, previousValues, -1);
        }

        return condition;
    }
}
