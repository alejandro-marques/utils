package org.generator.utils;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static List<String> getListFromFile (String fileName, boolean innerFile) throws Exception {
        List<String> list = new ArrayList<>();

        InputStreamReader input;
        try {
            input = innerFile?
                    new InputStreamReader(FileUtils.class.getResourceAsStream(
                            fileName.startsWith("/")? "":"/" + fileName), "UTF-8") :
                    new FileReader(fileName);
        }
        catch (Exception exception) {throw new Exception("File \"" + fileName + "\" not found.");}

        BufferedReader reader = new BufferedReader(input);
        String line;
        while((line = reader.readLine()) != null){
            list.add(line.trim());
        }

        return list;
    }


    public static List<String> getListFromFolder (String folderName, boolean innerFile, String extension)
            throws Exception {
        List<String> list = new ArrayList<>();

        for (String fileName : getFolderFiles(folderName, innerFile)){
            if (fileName.endsWith("." + extension)){
       		    list.addAll(getListFromFile(fileName, innerFile));
            }
        }

        return list;
    }


    public static <T> T getObjectFromJsonFile (String fileName, Type type, boolean inner) throws Exception{
        StringBuilder builder = new StringBuilder();
        for (String line : getListFromFile(fileName, inner)){
            builder.append(line.trim());
        }
        try {return FormatUtils.getJsonAsObject(builder.toString(), type);}
        catch (Exception exception){
            throw new Exception("File \"" + fileName + "\" does not have the expected format" +
                    " [" + exception.getMessage() + "]");
        }
    }


    private static List<String> getFolderFiles (String folderName, boolean innerFile) throws Exception{
        List<String> files = new ArrayList<>();

        if (!innerFile){
            File folder = new File(folderName);
            if (!folder.exists()){throw new Exception("Folder \"" + folderName + "\" not found.");}
            if (!folder.isDirectory()){throw new Exception("File \"" + folderName + "\" is not a valid directory.");}

            for (File file : folder.listFiles()){
                if (file.isFile()){files.add(file.getPath());}
            }

            return files;
        }
        else {
            ClassLoader loader = FileUtils.class.getClassLoader();
            InputStream inputStream = loader.getResourceAsStream(folderName);
            if (null == inputStream){throw new Exception("Folder \"" + folderName + "\" not found.");}
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            try {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    String resource = folderName + "/" + line;
                    checkIsValidResource(resource);
                    files.add(resource);
                }
                bufferedReader.close();
            }
            catch (Exception exception){throw new Exception("File \"" + folderName + "\" is not a valid directory.");}
        }

        return files;
    }

    private static void checkIsValidResource (String resource) throws Exception{
        try {if (null == FileUtils.class.getResourceAsStream(resource.startsWith("/")? "":"/" + resource)){
            throw new Exception();}
        }
        catch (Exception exception) {throw new Exception("Resource \"" + resource + "\" not found.");}
    }
}
