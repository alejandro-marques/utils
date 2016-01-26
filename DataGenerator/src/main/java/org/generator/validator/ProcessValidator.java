package org.generator.validator;

import org.generator.constants.FieldConstants;
import org.generator.model.configuration.GeneratorInfo;
import org.generator.model.configuration.ProcessInfo;
import org.generator.model.configuration.FieldInfo;
import org.generator.model.data.Transformation;
import org.generator.validator.transformation.TransformationValidator;

import java.util.*;

public class ProcessValidator {

    private static List<String> previousFields = new ArrayList<>();

    public static void validate (ProcessInfo processInfo)
            throws Exception {
        try {
            // Checks if configuration exists
            if (null == processInfo) {
                throw new Exception("No process configuration found.");
            }
            validateGenerator(processInfo);
            checkFields(processInfo);
            validateModel(processInfo);
            validateTransformations(processInfo.getTransformations());
        }
        catch (Exception exception){
            throw new Exception("Error while validating process configuration " +
                    "(Reason: " + exception.getMessage() + ").");
        }
    }

    private static void validateGenerator (ProcessInfo processInfo)
            throws Exception {
        GeneratorInfo generatorInfo = processInfo.getGenerator();

        if (null == generatorInfo){
            throw new Exception ("Generator configuration must be defined");
        }

        if (generatorInfo.isAuxiliaryFields()){
            previousFields.add(FieldConstants.ID_FIELD);
            previousFields.add(FieldConstants.WEIGHT_FIELD);
            previousFields.add(FieldConstants.RELATED_DOCUMENTS_FIELD);
        }

        try {
            Validator.isLesserThan("maxDocuments", generatorInfo.getMaxDocuments(), -1, false);

            Integer maxRelatedDocuments = generatorInfo.getMaxRelatedDocuments();
            Integer minRelatedDocuments = generatorInfo.getMinRelatedDocuments();
            if (null != minRelatedDocuments){
                if (null == maxRelatedDocuments) {
                    throw new Exception("If \"minRelatedDocuments\" is defined " +
                            "\"maxRelatedDocuments\" must be defined too");
                }
                if (minRelatedDocuments > maxRelatedDocuments){
                    throw new Exception("\"maxRelatedDocuments\" value must be greater than " +
                            "\"minRelatedDocuments\" one");
                }
            }

            Validator.isLesserThan("maxRelatedDocuments", generatorInfo.getMaxRelatedDocuments(), 1, true);
            Validator.isLesserThan("minRelatedDocuments", generatorInfo.getMinRelatedDocuments(), 1, true);

            if (null == minRelatedDocuments){generatorInfo.setMinRelatedDocuments(1);}
            if (null == maxRelatedDocuments){generatorInfo.setMaxRelatedDocuments(1);}
            try {
                List<FieldInfo> idFields = generatorInfo.getIdFields();
                // Id fields are ordered
                idFields = orderFields(idFields);
                generatorInfo.setIdFields(idFields);

                // And then validated
                validateFields(idFields);
            }
            catch (Exception exception){throw new Exception("idFields -> " + exception.getMessage());}
        }
        catch (Exception exception){throw new Exception("generator -> " + exception.getMessage());}
    }

    private static void validateModel(ProcessInfo processInfo)
            throws Exception {
        List<FieldInfo> model = processInfo.getModel();
        // Fields are ordered
        model = orderFields(model);
        processInfo.setModel(model);

        // And then validated
        try {validateFields(model);}
        catch (Exception exception){
            throw new Exception("model " + exception.getMessage());
        }
    }

    private static void validateTransformations(List<Transformation> transformations)
            throws Exception {
        if (null != transformations && !transformations.isEmpty()){
            for (Transformation transformation : transformations){
                try {TransformationValidator.validate(transformation, previousFields);}
                catch (Exception exception){
                    throw new Exception("transformations [" + transformation + "] -> " +
                            exception.getMessage());
                }
            }
        }
    }

    private static void validateFields(List<FieldInfo> fieldsInfo)
            throws Exception {

        if (null != fieldsInfo && !fieldsInfo.isEmpty()){
            for (FieldInfo fieldInfo : fieldsInfo){
                try {
                    FieldInfoValidator.validate(fieldInfo, previousFields);
                    previousFields.add(fieldInfo.getName());
                }
                catch (Exception exception){
                    throw new Exception("[" + fieldInfo.getName() + "] " + exception.getMessage());
                }
            }
        }
    }

    private static void checkFields (ProcessInfo configuration) throws Exception {
        List<FieldInfo> idFields = configuration.getGenerator().getIdFields();
        List<FieldInfo> modelFields = configuration.getModel();
        if ((null == idFields || idFields.isEmpty()) &&
                (null == modelFields || modelFields.isEmpty())) {
            throw new Exception("At least one field must be defined for either id or model");
        }
    }

    private static List<FieldInfo> orderFields(List<FieldInfo> fields){
        if (null == fields){return null;}
        List<FieldInfo> orderedFields = new ArrayList<>();
        List<FieldInfo> unorderedFields = new ArrayList<>();
        SortedMap<Integer, FieldInfo> orderedFieldsMap = new TreeMap<>();

        for(FieldInfo fieldInfo : fields) {
            Integer order = fieldInfo.getOrder();
            if (null == order) { unorderedFields.add(fieldInfo);}
            else {orderedFieldsMap.put(order, fieldInfo);}
        }

        for (Map.Entry<Integer, FieldInfo> entry : orderedFieldsMap.entrySet()) {
            orderedFields.add(entry.getValue());
        }
        orderedFields.addAll(unorderedFields);
        return orderedFields;
    }
}
