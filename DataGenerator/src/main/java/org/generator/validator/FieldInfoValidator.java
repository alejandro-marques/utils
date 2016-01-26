package org.generator.validator;

import org.generator.constants.PropertiesConstants;
import org.generator.model.configuration.FieldInfo;

import org.generator.model.configuration.FieldValueDefinition;
import org.generator.model.configuration.FieldValueDefinition.Type;
import org.generator.model.configuration.FieldValueDefinition.Subtype;
import org.generator.model.configuration.FieldValueInfo;
import org.generator.validator.Validator.ValueType;
import org.generator.validator.value.NumericValueInfoValidator;
import org.generator.validator.value.TextValueInfoValidator;

import java.util.List;
import java.util.Map;

public class FieldInfoValidator {

    public static void validate (FieldInfo fieldInfo, List<String> previousFields) throws Exception {
        String fieldName = checkFieldName(fieldInfo.getName());
        fieldInfo.setName(fieldName);
        try {checkFieldValueInfo(fieldInfo.getValue(), previousFields);}
        catch (Exception exception){
            throw new Exception ( "-> " + exception.getMessage());
        }
    }

    public static String checkFieldName (String name) throws Exception {
        if (null == name){throw new Exception("Received field without name.");}
        // Name is normalized
        name = name.trim();
        // Name is checked
        if (name.startsWith("_")){
            throw new Exception("Wrong field name for \"" + name + "\". Leading underscores are not " +
                "allowed for field names as they are used for auxiliary fields.");
        }
        if (name.matches(".*_[0-9,\\[\\]]+")){
            throw new Exception("Wrong field name for \"" + name + "\". Trailing underscores with " +
                "numbers or empty brackets are not allowed for field names as they are used for " +
                "transformation fields.");
        }
        return name;
    }

    public static void checkFieldValueInfo (FieldValueInfo fieldValueInfo,
            List<String> previousFields) throws Exception {
        Type type = checkType(fieldValueInfo.getType());
        Subtype subtype = checkSubtype(fieldValueInfo.getSubtype(), type);

        try {
            checkFieldOptions(fieldValueInfo.getOptions());
            checkFieldValueInfoParameters(type, subtype, fieldValueInfo, previousFields);
        }
        catch (Exception exception){
            throw new Exception ("value (" + type + ": " + subtype + ") -> "
                    + exception.getMessage());
        }
    }

    private static void checkFieldValueInfoParameters (Type type, Subtype subtype,
            FieldValueInfo fieldValueInfo, List<String> previousFields)
            throws Exception {
        switch (type){
            case NUMERIC:
                NumericValueInfoValidator.validate(subtype, fieldValueInfo);
                break;
            case TEXT:
                TextValueInfoValidator.validate(subtype, fieldValueInfo, previousFields);
                break;
            case DATE:
                break;
        }
    }

    private static Type checkType (String typeName) throws Exception {
        if (null == typeName){
            throw new Exception("Missing value mandatory property \"type\"");
        }

        Type type = FieldValueDefinition.getEnum(FieldValueDefinition.Type.class, typeName);
        if (null == type) {
            throw new Exception("Not supported value type \"" + typeName + "\"");
        }
        return type;
    }

    private static Subtype checkSubtype (String subtypeName, Type type) throws Exception {
        if (null == subtypeName){
            throw new Exception("Missing value mandatory property \"subtype\"");
        }

        Subtype subtype = FieldValueDefinition.getEnum(Subtype.class, subtypeName);
        if (null == subtype) {
            throw new Exception("Not supported value subtype \"" + subtypeName + "\"");
        }
        if (!type.isValidSubtype(subtype)){
            throw new Exception("Not supported \"" + type + "\" subtype \"" + subtypeName + "\"");
        }
        return subtype;
    }

    private static void checkFieldOptions(Map<String, String> options)
            throws Exception {
        if (options != null && !options.isEmpty()){
            Validator.findAndCheckParameter(options, PropertiesConstants.WEIGHT, ValueType.BOOLEAN, false);
        }
    }
}
