package org.generator.utils.transformations;

import org.generator.model.data.Condition;
import org.generator.model.data.FieldValue;
import org.generator.model.data.Transformation;
import org.generator.model.data.Transformation.Operation;
import org.generator.utils.condition.ConditionUtils;

import java.util.List;
import java.util.Map;

public class TransformationUtils {

    public static void transform (Map<String,FieldValue> document, Transformation transformation) throws Exception {
        if (null == document){return;}

        List<Condition> conditions = transformation.getConditions();
        if (conditions != null) {
            for (Condition condition : conditions) {
                if (!ConditionUtils.check(document, condition)) {return;}
            }
        }

        Operation operation = Operation.fromString(transformation.getOperation());
        if (null == operation){throw new Exception("Type \"" + transformation.getOperation() + "\" not supported.");}

        switch (operation){

            case ADD:
                FieldTransformation.addField(document, transformation);
                break;

            case REMOVE:
                FieldTransformation.removeField(document, transformation);
                break;

            case COPY:
                FieldTransformation.copyField(document, transformation);
                break;

            case RENAME:
                FieldTransformation.renameField(document, transformation);
                break;


            case COMBINE:
                BasicTransformation.combine(document, transformation);
                break;


            case MULTIPLY:
            case DIVIDE:
            case SUM:
            case SUBTRACT:
                NumericTransformation.operate(document, transformation);
                break;

            case ROUND:
                NumericTransformation.round(document, transformation);
                break;


            case SPLIT:
                TextTransformation.split(document, transformation);
                break;

            case TRANSLATE:
                TextTransformation.translate(document, transformation);
                break;


            default:
                throw new Exception("Type \"" + transformation.getOperation() + "\" not implemented yet.");
        }
    }
}
