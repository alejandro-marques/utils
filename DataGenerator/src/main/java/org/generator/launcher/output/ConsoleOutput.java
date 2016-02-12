package org.generator.launcher.output;

import org.generator.utils.FormatUtils;
import org.generator.launcher.LauncherConstants.Format;

import java.util.Map;

public class ConsoleOutput implements Output {
    @Override
    public void write(Map<String, Object> document, Format format) throws Exception {
        System.out.println(FormatUtils.formatDocument(document, format));
    }

    @Override
    public void flush() throws Exception {
    }

    @Override
    public void close() throws Exception {

    }
}
