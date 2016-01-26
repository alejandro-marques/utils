package org.generator.validator.value;

import org.generator.model.configuration.FieldValueDefinition;
import org.generator.model.configuration.FieldValueDefinition.Mode;
import org.generator.model.configuration.FieldValueDefinition.Subtype;

public class FieldValueInfoValidator {

    protected static Mode checkMode (String modeName, Subtype subtype, boolean isVariation)
            throws Exception {
        Mode mode = FieldValueDefinition.getEnum(Mode.class, modeName);
        if (null == mode){
            throw new Exception("Not supported " + (isVariation? "variation":"initial") + " value "
                    + "mode \"" + modeName + "\"");
        }
        if ((!isVariation && !subtype.isValidInitialMode(mode)) ||
                (isVariation && !subtype.isValidVariationMode(mode))){
            throw new Exception("Not supported \"" + subtype + "\" subtype " +
                    (isVariation? "variation":"initial") + " value mode \"" + modeName + "\"");
        }
        return mode;
    }
}
