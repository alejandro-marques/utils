package org.generator.launcher.output;

import org.generator.utils.FormatUtils;

import java.util.Map;

public class ConsoleOutput implements Output {
    @Override
    public void write(Map<String, Object> document) throws Exception {
        System.out.println(FormatUtils.getObjectAsJson(document));
    }

    @Override
    public void flush() throws Exception {
    }
}
