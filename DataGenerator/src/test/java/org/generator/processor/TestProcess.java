package org.generator.processor;

import java.util.Map;

public class TestProcess {
    private static final String CONFIGURATION_FILE = "process/telefonica.json";

    public static void main(String[] args) throws Exception {
        Process process = new Process(CONFIGURATION_FILE, true);

        Map<String, Object> document;
        int count = 0;

        while (null != (document = process.nextDocument())){
            System.out.println(document);
            count++;
        }

        System.out.println(count + " documents generated");
        System.out.println("END!");
    }
}
