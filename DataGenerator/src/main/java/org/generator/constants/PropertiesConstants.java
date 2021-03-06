package org.generator.constants;

/**
 * Created by alejandro on 5/11/15.
 */
public interface PropertiesConstants {

    // General
    String MODE = "mode";

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
    String TRANSLATION = "translation";

    // Common
    String NONE = "none";
    String FIXED = "fixed";
    String RANDOM = "random";
    String UNIFORM = "uniform";
    String VALUE = "value";
    String POSITION = "position";
    String MIN = "min";
    String MAX = "max";
    String MAX_VARIATIONS = "maxVariations";
    String LIMIT = "limit";
    String WEIGHT = "weight";
    String ORIGIN = "origin";

    // Text properties
    String SEQUENTIAL = "sequential";
    String PARAGRAPH = "paragraph";
    String LANGUAGE = "language";
    String MIN_WORDS = "minWords";
    String MAX_WORDS = "maxWords";
    String MIN_PARAGRAPHS = "minParagraphs";
    String MAX_PARAGRAPHS = "maxParagraphs";

    // Date properties
    String STEP = "step";
    String FORMAT = "format";

    // Numeric properties
    String GAUSSIAN = "gaussian";
    String MEAN = "mean";
    String VARIANCE = "variance";
}
