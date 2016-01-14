package org.generator.utils.condition;

import org.generator.model.data.Condition;
import org.generator.model.data.Condition.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConditionUtils {
    public static boolean check (Map<String,Object> document, Condition condition) throws Exception {

        Type type = Type.fromString(condition.getCondition());

        switch (type){
            case EQUAL:
                return checkEqual(document, condition);

            default:
                throw new Exception("Condition \"" + type.toString() + "\" not implemented yet.");
        }
    }


    private static boolean checkEqual (Map<String,Object> document, Condition condition) throws Exception {
        List<Object> values = getValues(document, condition);
        Object firstObject = values.remove(0);
        if (null == firstObject){
            throw new Exception("Error while checking condition. \"" + condition.getSource().get(0)
                    + "\" not found");
        }
        if (values.isEmpty()){return false;}
        for (Object value : values){
            if (!firstObject.equals(value)){return false;}
        }
        return true;
    }

    private static List<Object> getValues  (Map<String, Object> document, Condition condition)
            throws Exception {
        List<Object> values = new ArrayList<>();
        for (String sourceField : condition.getSource()){values.add(document.get(sourceField));}
        if (null != condition.getValue()){values.add(condition.getValue());}
        return values;
    }
}
