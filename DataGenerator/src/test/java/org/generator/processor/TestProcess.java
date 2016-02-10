package org.generator.processor;

import org.generator.utils.FormatUtils;

import java.util.Map;

public class TestProcess {
    private static final String CONFIGURATION_FILE = "stratio/stratio-temporal.json";
    //private static final String CONFIGURATION_FILE = "mapfre/configuration/stratio-sales.json";

    public static void main(String[] args) throws Exception {
        Processor processor = new Processor(CONFIGURATION_FILE, true);

        Map<String, Object> document;
        int count = 0;

        while (null != (document = processor.nextDocument())){
            System.out.println(FormatUtils.getObjectAsJson(document));
            count++;
        }

        System.out.println(count + " documents generated");
        System.out.println("END!");
    }
}
