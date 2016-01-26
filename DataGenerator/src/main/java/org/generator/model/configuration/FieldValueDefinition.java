package org.generator.model.configuration;

import org.generator.constants.PropertiesConstants;

import java.util.EnumSet;

public class FieldValueDefinition {

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
                EnumSet.of(Mode.FIXED, Mode.RANDOM),
                EnumSet.of(Mode.NONE, Mode.FIXED, Mode.RANDOM, Mode.SEQUENTIAL)),
        STRING (PropertiesConstants.STRING,
                EnumSet.of(Mode.FIXED, Mode.RANDOM),
                EnumSet.of(Mode.NONE)),
        INTEGER (PropertiesConstants.INTEGER,
                EnumSet.of(Mode.FIXED, Mode.UNIFORM, Mode.GAUSSIAN),
                EnumSet.of(Mode.NONE, Mode.FIXED, Mode.UNIFORM, Mode.GAUSSIAN)),
        DOUBLE(PropertiesConstants.DOUBLE,
                EnumSet.of(Mode.FIXED, Mode.UNIFORM, Mode.GAUSSIAN),
                EnumSet.of(Mode.NONE, Mode.FIXED, Mode.UNIFORM, Mode.GAUSSIAN));

        private String name;
        private EnumSet<Mode> values;
        private EnumSet<Mode> variations;

        Subtype (String name, EnumSet<Mode> values, EnumSet<Mode> variations){
            this.name = name;
            this.values = values;
            this.variations = variations;
        }

        public boolean isValidInitialMode(Mode value){
            return values.contains(value);
        }

        public boolean isValidVariationMode(Mode variation){
            return variations.contains(variation);
        }

        @Override
        public String toString() {return name;}
    }

    public enum Mode {
        NONE (PropertiesConstants.NONE),
        FIXED (PropertiesConstants.FIXED),
        RANDOM (PropertiesConstants.RANDOM),
        UNIFORM (PropertiesConstants.UNIFORM),
        GAUSSIAN(PropertiesConstants.GAUSSIAN),
        SEQUENTIAL(PropertiesConstants.SEQUENTIAL),
        RELATED(PropertiesConstants.RELATED);

        private String name;

        Mode(String name){
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