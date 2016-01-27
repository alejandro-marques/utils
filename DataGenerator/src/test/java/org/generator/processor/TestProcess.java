package org.generator.processor;

import java.util.Map;

public class TestProcess {
    private static final String CONFIGURATION_FILE = "process/telefonica-reap.json";

    public static void main(String[] args) throws Exception {
        Processor processor = new Processor(CONFIGURATION_FILE, true);

        Map<String, Object> document;
        int count = 0;

        while (null != (document = processor.nextDocument())){
            System.out.println(document);
            count++;
        }

        System.out.println(count + " documents generated");
        System.out.println("END!");
    }
}
