package org.generator.validator;

import java.util.List;
import java.util.Map;

public class Validator {

    public enum ValueType {
        INTEGER ("integer"),
        DOUBLE ("double"),
        STRING ("string"),
        BOOLEAN ("boolean");

        String name;

        ValueType (String name){this.name = name;}

        @Override
        public String toString() {return name;}
    }

    public static Object findAndCheckParameter (Map<String,String> parameters, String name,
            ValueType type, boolean isMandatory) throws Exception {
        if (parameters == null){throw new Exception ("Null parameters received");}
        return checkParameter(name, parameters.get(name), type, isMandatory);
    }

    public static Object checkParameter (String name, String value, ValueType type,
            boolean isMandatory) throws Exception {
        if (null != value){
            try {
                switch (type){
                    case INTEGER:
                        return Integer.parseInt(value);
                    case DOUBLE:
                        return Double.parseDouble(value);
                    case STRING:
                        return value;
                    case BOOLEAN:
                        return Boolean.parseBoolean(value);
                }
            }
            catch (Exception exception){
                throw new Exception("Invalid " + type.toString() + " value \"" + value +
                        "\" for parameter \"" + name + "\"");
            }
        }
        else if (isMandatory){
            throw new Exception("Missing mandatory parameter \"" + name + "\"");
        }
        return null;
    }

    public static List<String> checkParameterList (String name, List<String> list, ValueType type,
            boolean isMandatory) throws Exception {
        if (null == list || list.isEmpty() && isMandatory){
            throw new Exception("Missing mandatory parameter \"" + name + "\"");
        }
        for (String value : list){checkParameter(name, value, type, false);}
        return list;
    }

    public static void isLesserThan (String property, Integer value, int limit, boolean isNullValid)
            throws Exception {
        if (!isNullValid && null == value){
            throw new Exception ("Mandatory property \"" + property + "\" must be defined");
        }
        if (value != null && value < limit){
            throw new Exception ("\"" + property + "\" cannot be lesser than \"" + limit + "\".");
        }
    }
}
