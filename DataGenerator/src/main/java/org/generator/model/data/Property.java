package org.generator.model.data;

import org.generator.constants.PropertiesConstants;

import java.util.EnumSet;

public class Property {

    public enum Type {
        TEXT (PropertiesConstants.TEXT, EnumSet.of(Subtype.STRING, Subtype.DICTIONARY)),
        NUMERIC (PropertiesConstants.NUMERIC, EnumSet.of(Subtype.INTEGER, Subtype.DOUBLE)),
        DATE (PropertiesConstants.DATE, null);

        private String name;
        private EnumSet<Subtype> subtypes;

        Type (String name, EnumSet<Subtype> subtypes){
            this.name = name;
            this.subtypes = subtypes;
        }

        public boolean isValidSubtype(Subtype subtype){
            return subtypes.contains(subtype);
        }

        @Override
        public String toString() {return name;}
    }

    public enum Subtype {
        DICTIONARY (PropertiesConstants.DICTIONARY,
                EnumSet.of(Value.FIXED, Value.RANDOM),
                EnumSet.of(Value.NONE, Value.SEQUENTIAL)),
        STRING (PropertiesConstants.STRING,
                EnumSet.of(Value.FIXED, Value.RANDOM),
                EnumSet.of(Value.NONE)),
        INTEGER (PropertiesConstants.INTEGER,
                EnumSet.of(Value.FIXED, Value.UNIFORM, Value.GAUSSIAN),
                EnumSet.of(Value.NONE, Value.FIXED, Value.UNIFORM, Value.GAUSSIAN)),
        DOUBLE(PropertiesConstants.DOUBLE,
                EnumSet.of(Value.FIXED, Value.UNIFORM, Value.GAUSSIAN),
                EnumSet.of(Value.NONE, Value.FIXED, Value.UNIFORM, Value.GAUSSIAN));

        private String name;
        private EnumSet<Value> values;
        private EnumSet<Value> variations;

        Subtype (String name, EnumSet<Value> values, EnumSet<Value> variations){
            this.name = name;
            this.values = values;
            this.variations = variations;
        }

        public boolean isValidValue(Value value){
            return values.contains(value);
        }

        public boolean isValidVariation(Value variation){
            return variations.contains(variation);
        }

        @Override
        public String toString() {return name;}
    }

    public enum Value {
        NONE (PropertiesConstants.NONE),
        FIXED (PropertiesConstants.FIXED),
        RANDOM (PropertiesConstants.RANDOM),
        UNIFORM (PropertiesConstants.UNIFORM),
        GAUSSIAN(PropertiesConstants.GAUSSIAN),
        SEQUENTIAL(PropertiesConstants.SEQUENTIAL);

        private String name;

        Value (String name){
            this.name = name;
        }

        @Override
        public String toString() {return name;}
    }


    public static <E extends Enum<E>> E getEnum (Class<E> clazz, String name){
        for(E enumeration : EnumSet.allOf(clazz)){
            if(enumeration.toString().equalsIgnoreCase(name)){
                return enumeration;
            }
        }
        return null;
    }
}