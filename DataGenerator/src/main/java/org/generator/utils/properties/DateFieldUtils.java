package org.generator.utils.properties;

import org.generator.constants.PropertiesConstants;
import org.generator.model.data.Field;
import org.generator.model.data.Property;
import org.generator.model.data.Property.Value;
import org.generator.utils.RandomUtils;

import java.text.SimpleDateFormat;
import java.util.*;

public class DateFieldUtils {

    public static final Map<String, Integer> stepConverter = new LinkedHashMap<String, Integer>(){
        {
            put("YEAR", Calendar.YEAR);
            put("MONTH", Calendar.MONTH);
            put("DAY", Calendar.DATE);
            put("HOUR", Calendar.HOUR);
            put("MINUTE", Calendar.MINUTE);
            put("SECOND", Calendar.SECOND);
            put("MILLISECOND", Calendar.MILLISECOND);
        }
    };


    public static Object getDatePropertyValue (Field field, Object previousValue, boolean stringFormat) throws Exception {
        Date value = null;
        Map<String, String> parameters = field.getValue();
        SimpleDateFormat dateFormat = new SimpleDateFormat(parameters.get(PropertiesConstants.FORMAT));

        if (null != previousValue){
            if (stringFormat && previousValue instanceof String){
                try{value = dateFormat.parse((String) previousValue);}
                catch (Exception exception){
                    throw new Exception (previousValue + " is not a valid Date (Reason: " + exception + ")");
                }
            }
            else if (previousValue instanceof Date){value = (Date) previousValue;}
            else {throw new Exception (previousValue.toString() + " is not a valid Date");}
            parameters = field.getVariation();
        }

        value = getValue(parameters, value);
        return stringFormat? dateFormat.format(value) : value;
    }

    private static Date getValue (Map<String, String> parameters, Date previousValue) throws Exception {
        String typeString = parameters.get(PropertiesConstants.TYPE);
        Value valueType = Property.getEnum(Value.class, typeString);
        Calendar value = new GregorianCalendar();
        if (null != previousValue) {value.setTime(previousValue);}

        switch (valueType){
            case FIXED:
                if (null == previousValue){value = getFixedValue(parameters);}
                else {addFixedVariation(parameters, value);}
                break;

            case UNIFORM:
                if (null == previousValue){value = getUniformValue(parameters);}
                else {addUniformVariation(parameters, value);}
                break;

            default:
                throw new Exception("String type \"" + typeString + "\" not implemented yet.");
        }

        return value.getTime();
    }

    private static Calendar getFixedValue (Map<String, String> parameters) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat(parameters.get(PropertiesConstants.FORMAT));
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(dateFormat.parse(parameters.get(PropertiesConstants.VALUE)));

        return calendar;
    }

    private static Calendar getUniformValue (Map<String, String> parameters) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat(parameters.get(PropertiesConstants.FORMAT));

        Calendar maxCalendar = new GregorianCalendar();
        maxCalendar.setTime(dateFormat.parse(parameters.get(PropertiesConstants.MAX)));
        maxCalendar.add(getMinimumStepFromFormat(dateFormat), 1);

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(RandomUtils.getRandomDate(
                dateFormat.parse(parameters.get(PropertiesConstants.MIN)),
                maxCalendar.getTime(),
                dateFormat));

        return calendar;
    }

    private static void addFixedVariation(Map<String, String> parameters, Calendar previousValue) throws Exception {
        Integer step = stepConverter.get(parameters.get(PropertiesConstants.STEP));
        Integer value = Integer.parseInt(parameters.get(PropertiesConstants.VALUE));

        previousValue.add(step, value);
    }

    private static void addUniformVariation(Map<String, String> parameters, Calendar previousValue) throws Exception {
        Integer step = stepConverter.get(parameters.get(PropertiesConstants.STEP));
        Integer min = Integer.parseInt(parameters.get(PropertiesConstants.MIN));
        Integer max = Integer.parseInt(parameters.get(PropertiesConstants.MAX));

        previousValue.add(step, RandomUtils.getRandomInteger(min, max));
    }

    private static int getMinimumStepFromFormat(SimpleDateFormat format) throws Exception {
        Calendar calendar = new GregorianCalendar();
        Calendar truncatedCalendar = new GregorianCalendar();
        truncatedCalendar.setTime(format.parse(format.format(calendar.getTime())));
        Integer lastTruncatedStep = null;

        for (Map.Entry<String,Integer> entry : stepConverter.entrySet()){
            int step = entry.getValue();
            if (calendar.get(step) != truncatedCalendar.get(step)){
                calendar.add(step, 1);
                truncatedCalendar.setTime(format.parse(format.format(calendar.getTime())));
                if (calendar.get(step) != truncatedCalendar.get(step)){
                    return lastTruncatedStep != null? lastTruncatedStep : step;
                }
            }
            lastTruncatedStep = step;
        }
        return Calendar.MILLISECOND;
    }
}