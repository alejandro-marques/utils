package org.generator.model;

import com.google.gson.reflect.TypeToken;
import org.generator.model.data.Word;
import org.generator.utils.FileUtils;
import org.generator.utils.FormatUtils;

import java.util.List;

public class TestDictionary {
    private static final String CONFIGURATION_FILE = "data/telefonica/relations/" +
            "functionality-channel.json";

    public static void main(String[] args) throws Exception {

        List<Word> words = FileUtils.getObjectFromJsonFile(CONFIGURATION_FILE,
                new TypeToken<List<Word>>(){}.getType(),
                true);
        System.out.println(FormatUtils.getObjectAsJson(words));
        System.out.println("END");
    }
}
