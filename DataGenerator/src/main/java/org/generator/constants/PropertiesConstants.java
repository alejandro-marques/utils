package org.generator.constants;

/**
 * Created by alejandro on 5/11/15.
 */
public interface PropertiesConstants {

    // General
    String TYPE = "type";

    // Types
    String NUMERIC = "numeric";
    String TEXT = "text";
    String DATE = "date";


    // Subtypes
    String INTEGER = "integer";
    String DOUBLE = "double";
    String STRING = "string";
    String DICTIONARY = "dictionary";
    String RELATION = "relation";

    // Common
    String NONE = "none";
    String FIXED = "fixed";
    String RANDOM = "random";
    String UNIFORM = "uniform";
    String VALUE = "value";
    String POSITION = "position";
    String MIN = "min";
    String MAX = "max";
    String LIMIT = "limit";
    String WEIGHT = "weight";

    // Text properties
    String FILE = "file";
    String SEQUENTIAL = "sequential";
    String RELATED = "related";

    // Date properties
    String STEP = "step";
    String FORMAT = "format";

    // Numeric properties
    String GAUSSIAN = "gaussian";
    String MEAN = "mean";
    String VARIANCE = "variance";
}
