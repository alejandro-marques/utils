package org.generator.launcher.output;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.generator.launcher.LauncherConstants;
import org.generator.launcher.LauncherConstants.Format;
import org.generator.launcher.LauncherConstants.OutputType;
import org.generator.launcher.LauncherUtils;
import org.generator.utils.FormatUtils;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.util.Map;

public class HdfsOutput implements Output {

    private FileSystem hdfs;
    private BufferedWriter bufferedWriter;


    public HdfsOutput (Map<String, String> parameters) throws Exception {
        LauncherUtils.validateParameters(parameters, OutputType.HDFS);
        setupFileSystem(parameters.get(LauncherConstants.HOST_PARAMETER),
                Integer.parseInt(parameters.get(LauncherConstants.PORT_PARAMETER)),
                parameters.get(LauncherConstants.PATH_PARAMETER),
                parameters.get(LauncherConstants.FILE_PARAMETER));
    }

    @Override
    public void write(Map<String, Object> document, Format format) throws Exception {
        bufferedWriter.write(FormatUtils.formatDocument(document, format));
    }

    @Override
    public void flush() throws Exception {
    }

    @Override
    public void close() throws Exception {
        bufferedWriter.close();
        hdfs.close();
    }

    private void setupFileSystem(String host, int port, String path, String fileName)
            throws Exception {
        System.setProperty("HADOOP_USER_NAME", "hadoop");

        String url = "hdfs://" + host + ":" + port;
        Configuration configuration = new Configuration();
        hdfs = FileSystem.get(new URI(url), configuration);
        Path file = new Path(url + "/" + path + "/" + fileName);

        if (hdfs.exists(file)) {hdfs.delete(file, true);}
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(hdfs.create(file), "UTF-8"));
    }
}
