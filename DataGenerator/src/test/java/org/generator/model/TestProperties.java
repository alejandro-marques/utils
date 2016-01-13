package org.generator.model;

import org.generator.model.data.Property;
import org.generator.model.data.Property.Type;
import org.generator.model.data.Property.Subtype;
import org.generator.model.data.Property.Value;

public class TestProperties {

    public static void main(String[] args) {
        String type = "text";
        String subtype = "dictionary";
        String value = "fixed";

        try{checkProperty(type, subtype, value);}
        catch (Exception exception){
            System.out.println("Error: " + exception.getMessage());
        }
    }

    private static void checkProperty (String typeName, String subtypeName, String valueName)
            throws Exception {
        Type type = Property.getEnum(Type.class, typeName);
        if (null == type){throw new Exception("Type \"" + typeName + "\" not supported");}

        switch (type){
            case TEXT:
                checkString(subtypeName, valueName);
                break;
            case NUMERIC:
                checkNumeric(subtypeName, valueName);
                break;
            case DATE:
                checkDate(subtypeName, valueName);
                break;
        }
    }

    private static void checkString(String subtypeName, String valueName) throws Exception {
        Subtype subtype = checkSubtype(Type.TEXT, subtypeName, valueName);

        switch (subtype){
            case DICTIONARY:
                break;
            case STRING:
                break;
            default:
                throw new Exception("Subtype \"" + subtype.toString() + "\" not implemented.");
        }
    }

    private static void checkNumeric(String subtypeName, String valueName) throws Exception {
        Subtype subtype = checkSubtype(Type.NUMERIC, subtypeName, valueName);

    }

    private static void checkDate(String subtypeName, String valueName) throws Exception {
        Subtype subtype = checkSubtype(Type.DATE, subtypeName, valueName);

    }

    private static Subtype checkSubtype(Type type, String subtypeName, String valueName)
            throws Exception {
        Subtype subtype = Property.getEnum(Subtype.class, subtypeName);
        if (null == subtype){throw new Exception("Subtype \"" + subtypeName + "\" not supported");}
        if (!type.isValidSubtype(subtype)){
            throw new Exception("Subtype \"" + subtypeName + "\" not supported " +
                    "for type " + type.toString());
        }
        return subtype;
    }

    private static Value checkValue(Type type, Subtype subtype, String valueName, boolean variation)
            throws Exception {
        Value value = Property.getEnum(Value.class, valueName);
        if (null == value){
            throw new Exception((variation?"Variation":"Value") + " \"" + valueName + "\" not supported");
        }
        if (!variation && !subtype.isValidValue(value) || variation && !subtype.isValidVariation(value)){
            throw new Exception((variation?"Variation":"Value") + " \"" + valueName + "\" not supported " +
                    "for subtype " + subtype.toString() + " and type " + type.toString() );
        }
        return value;
    }
}
