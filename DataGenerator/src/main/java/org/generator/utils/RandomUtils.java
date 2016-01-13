package org.generator.utils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by alejandro on 19/10/15.
 */
public class RandomUtils {

    private static Random random = new Random();

    /*
        NUMBER UTILS
     */
    public static int getRandomInteger (int min, int max) throws Exception{
        if (min == max) {return max;}
        long difference = (long) max - (long) min;
        if (difference <= 0){
            throw new ArithmeticException("Maximum value must be greater than minimum value");
        }
        if (difference > Integer.MAX_VALUE){
            throw new ArithmeticException("Difference between minimum value and maximum value " +
                    "exceeds the maximum allowed value for an integer.");
        }
        return min + random.nextInt((int) difference + 1);
    }

    public static double getRandomDouble (int min, int max) throws Exception{
        if (min == max) {return max;}
        return getRandomInteger(min, max - 1) + random.nextDouble();
    }

    public static double getRandomGaussianDouble (double mean, double variance, double lowerLimit, double upperLimit)
            throws ArithmeticException{
        if (upperLimit <= lowerLimit){
            throw new ArithmeticException("Upper limit should be bigger than lower limit");
        }
        if (mean < lowerLimit || mean > upperLimit){
            throw new ArithmeticException("Mean value should be inside the limits");
        }
        if (variance > (upperLimit - lowerLimit)){
            throw new ArithmeticException("Variance should be smaller than the limits distance");
        }

        double value;
        while (true){
            value = getRandomGaussian(mean, variance);
            if (value >= lowerLimit && value <= upperLimit){return value;}
        }
    }

    public static double getRandomGaussian (double mean, double variance) throws ArithmeticException {
        if (variance < 0){throw new ArithmeticException("Variance must be non-negative.");}
        return mean + variance * random.nextGaussian();
    }

    public static int getRandomGaussianInteger (int mean, int variance, int lowerLimit, int upperLimit)
            throws ArithmeticException{
        return (int) getRandomGaussianDouble(mean, variance, lowerLimit, upperLimit);
    }

    /*
        DATE UTILS
     */
    public static Date getRandomDate (Date beginDate, Date endDate, SimpleDateFormat format) throws Exception {
        String date = format.format(getRandomDate(beginDate, endDate));
        return format.parse(date);
    }

    /*
        DATE UTILS
     */
    public static Date getRandomDate (Date beginDate, Date endDate) throws Exception {
        long beginTime = beginDate.getTime();
        if (beginTime > endDate.getTime()){
            throw new Exception("End date must be more recent than begin date");
        }
        long diffTime = endDate.getTime() - beginTime;
        return new Date(beginTime + (long)(new Random().nextDouble() * diffTime));
    }

    /*
        LIST UTILS
     */

    public static <T> T getRandomValueFromList (List<T> list){
        if (null == list || list.isEmpty()){return  null;}
        return list.get(random.nextInt(list.size()));
    }



    /*
        MAP UTILS
     */
    public static <T,Y> Y getRandomValueFromMap (Map<T,Y> map){
        return map.get(getRandomKeyFromMap(map));
    }

    public static <T,Y> T getRandomKeyFromMap (Map<T,Y> map){
        List<T> keys = new ArrayList<T>(map.keySet());
        return keys.get(random.nextInt(keys.size()));
    }
}