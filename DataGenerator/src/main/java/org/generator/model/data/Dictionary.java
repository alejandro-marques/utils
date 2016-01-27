package org.generator.model.data;

import org.generator.exception.LimitReachedException;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class Dictionary {

    private final NavigableMap<Double, Word> weightsMap = new TreeMap<>();
    private final Random random = new Random();

    private String name;
    private double total = 0;
    private double maxWeight = 0;

    public Dictionary (String name){
        this.name = name;
    }

    public void add(Word word) throws Exception {
        Double weight = word.getWeight();
        if (null == weight){
            weight = 1.0;
            word.setWeight(weight);
        }
        if (weight <= 0) {throw new Exception("Weight cannot be lower than 1");}

        if (weight > maxWeight) {maxWeight = weight;}
        total += weight;
        weightsMap.put(total, word);
    }

    public Word getWord(int position) throws Exception {
        if (!weightsMap.isEmpty() && position >= 0 && position < weightsMap.size()) {
            int count = 0;
            for (Entry<Double, Word> entry : weightsMap.entrySet()) {
                if (count == position) return entry.getValue();
                count++;
            }
        }
        throw new Exception("Wrong position \"" + position + "\" for dictionary \"" + name + "\".");
    }

    public Word getRandomWord() {
        double value = random.nextDouble() * total;
        if (weightsMap.isEmpty()){return new Word(null, 1.0);}
        return weightsMap.ceilingEntry(value).getValue();
    }

    public Word getNextWord (String previousWord, boolean limit) throws Exception{
        if (weightsMap.isEmpty()){return new Word(null, 1.0);}
        if (null == previousWord || previousWord.equals(weightsMap.lastEntry().getValue().getValue())){
            if (!limit){return weightsMap.firstEntry().getValue();}
            throw new LimitReachedException(previousWord,
                    "Last word in dictionary \"" + name + "\" already reached.");
        }

        Iterator<Word> iterator = weightsMap.values().iterator();
        while (iterator.hasNext()){
            if (previousWord.equals(iterator.next().getValue())){
                return iterator.next();
            }
        }
        throw new Exception("No previous word \"" + previousWord +
                "\" was found in dictionary \"" + name + "\".");
    }

    public double getMaxWeight() {
        return maxWeight;
    }

    public double getMeanWeight() {
        return total / weightsMap.size();
    }

    @Override
    public String toString() {
        return weightsMap.toString();
    }
}
