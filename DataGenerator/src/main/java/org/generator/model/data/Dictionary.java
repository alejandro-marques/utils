package org.generator.model.data;

import org.generator.exception.LimitReachedException;

import java.util.*;

public class Dictionary {

    private final NavigableMap<Double, String> map = new TreeMap<>();
    private final Random random = new Random();

    private String name;
    private double total = 0;

    public Dictionary (String name){
        this.name = name;
    }

    public void add(Integer weight, String word) throws Exception {
        if (null == weight){weight = 1;}
        if (weight <= 0) {throw new Exception("Weight cannot be lower than 1");}
        total += weight;
        map.put(total, word);
    }

    public String getWord(int position) throws Exception {
        if (position >= 0 && position < map.size()) {
            int count = 0;
            for (Map.Entry<Double, String> entry : map.entrySet()) {
                if (count == position) return entry.getValue();
                count++;
            }
        }
        throw new Exception("Wrong position \"" + position + "\" for dictionary \"" + name + "\".");
    }

    public String getRandomWord() {
        double value = random.nextDouble() * total;
        return map.ceilingEntry(value).getValue();
    }

    public String getNextWord (String previousWord, boolean limit) throws Exception{
        if (previousWord.equals(map.lastEntry().getValue())){
            if (!limit){return map.firstEntry().getValue();}
            throw new LimitReachedException(previousWord,
                    "Last word in dictionary \"" + name + "\" already reached.");
        }

        Iterator<String> iterator = map.values().iterator();
        while (iterator.hasNext()){
            if (previousWord.equals(iterator.next())){
                return iterator.next();
            }
        }
        throw new Exception("No previous word \"" + previousWord +
                "\" was found in dictionary \"" + name + "\".");
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
