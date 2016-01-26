package org.generator.validator.transformation;

import com.google.gson.reflect.TypeToken;
import org.generator.configuration.CommonData;
import org.generator.constants.PropertiesConstants;
import org.generator.model.data.Transformation;
import org.generator.model.data.Translation;
import org.generator.utils.FileUtils;
import org.generator.validator.Validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextTransformationValidator {

    public static void validateSplit (Transformation transformation, List<String> previousFields)
            throws Exception {
        String fieldName = TransformationValidator.checkField(transformation.getField());
        TransformationValidator.checkSourceFields(transformation.getSource(), previousFields, 1);
        previousFields.add(fieldName + "_[]");
    }

    public static void validateTranslate (Transformation transformation, List<String> previousFields)
            throws Exception {
        TransformationValidator.checkSourceFields(transformation.getSource(), previousFields, -1);
        String translation = (String) Validator.findAndCheckParameter(transformation.getParameters(),
                PropertiesConstants.TRANSLATION, Validator.ValueType.STRING, true);
        if (null == CommonData.getTranslation(translation)) {
            CommonData.addTranslation(translation, getTranslation(translation));
        }
    }


    private static Map<String, Map<String, Object>> getTranslation (String translationName)
            throws Exception {
        // If file is not a resource the configuration path is added
        String translationFile = (CommonData.isResourceConfiguration() ?
                "" : CommonData.getConfigurationPath()) + translationName;
        // The list of words for that dictionary is retrieved
        List<Translation> translationsInfo = FileUtils.getObjectFromJsonFile(translationFile,
                new TypeToken<List<Translation>>(){}.getType(), CommonData.isResourceConfiguration());

        Map<String, Map<String, Object>> translation = new HashMap<>();
        for (Translation translationInfo : translationsInfo) {
            translation.put(translationInfo.getValue(), translationInfo.getFields());
        }
        return translation;
    }
}
