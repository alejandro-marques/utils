package org.generator.model.data;

import org.generator.exception.LimitReachedException;

import java.util.*;

public class Dictionary {

    private final NavigableMap<Double, String> cumulativeWeightsMap = new TreeMap<>();
    private final Map<String, Double> weightsMap = new HashMap<>();
    private final Random random = new Random();

    private String name;
    private double total = 0;
    private double maxWeight = 0;

    public Dictionary (String name){
        this.name = name;
    }

    public void add(Double weight, String word) throws Exception {
        if (null == weight){weight = 1.0;}
        if (weight <= 0) {throw new Exception("Weight cannot be lower than 1");}
        if (weight > maxWeight) {maxWeight = weight;}
        total += weight;
        cumulativeWeightsMap.put(total, word);
        weightsMap.put(word, weight);
    }

    public String getWord(int position) throws Exception {
        if (position >= 0 && position < cumulativeWeightsMap.size()) {
            int count = 0;
            for (Map.Entry<Double, String> entry : cumulativeWeightsMap.entrySet()) {
                if (count == position) return entry.getValue();
                count++;
            }
        }
        throw new Exception("Wrong position \"" + position + "\" for dictionary \"" + name + "\".");
    }

    public Double getWeight (String wordName){
        if (null != wordName && null != weightsMap.get(wordName)){
            return weightsMap.get(wordName)/maxWeight;
        }
        return null;
    }

    public String getRandomWord() {
        double value = random.nextDouble() * total;
        return cumulativeWeightsMap.ceilingEntry(value).getValue();
    }

    public String getNextWord (String previousWord, boolean limit) throws Exception{
        if (previousWord.equals(cumulativeWeightsMap.lastEntry().getValue())){
            if (!limit){return cumulativeWeightsMap.firstEntry().getValue();}
            throw new LimitReachedException(previousWord,
                    "Last word in dictionary \"" + name + "\" already reached.");
        }

        Iterator<String> iterator = cumulativeWeightsMap.values().iterator();
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
        return cumulativeWeightsMap.toString();
    }
}
