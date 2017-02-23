package lab4;

import sun.jvm.hotspot.utilities.soql.MapScriptObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.TreeMap;

/**
 * Skeleton for a Hidden Markov Model
 *
 * @author Fabian M. Suchanek
 * @author peikun
 */
public class HiddenMarkovModel {

    /**
     * Stores transition probabilities of the form ADJ -> { NN -> 0.99, VBZ ->
     * 0.01 }. Should sum to 1 for each tag.
     * Probability of ADJ followed by a NN/VBZ
     */
    protected Map<String, Map<String, Double>> transitionProb = new TreeMap<String, Map<String, Double>>();

    /**
     * Stores emission probabilities of the form PN -> { "Elvis" -> 0.8,
     * "Priscilla" -> 0.2 }. Should sum to 1 for each tag.
     * Probability of PN is Elvis/Priscilla
     */
    protected Map<String, Map<String, Double>> emissionProb = new TreeMap<String, Map<String, Double>>();

    /**
     * Retrieves the emission probability for this tag and this word
     * Map<tag, Map<word, probability>>
     */
    public double getEmissionProbability(String tag, String word) {
        if (!emissionProb.containsKey(tag)) return (0);
        if (!emissionProb.get(tag).containsKey(word)) return (0);
        return (emissionProb.get(tag).get(word));
    }

    /**
     * Retrieves the transition probability for these tags
     * Map<fromTag, Map<toTag, probability>>
     */
    public double getTransitionProbability(String fromTag, String toTag) {
        if (!transitionProb.containsKey(fromTag)) return (0);
        if (!transitionProb.get(fromTag).containsKey(toTag)) return (0);
        return (transitionProb.get(fromTag).get(toTag));
    }

    /**
     * Constructs a Hidden Markov Model from a tagged Wikipedia corpus, i.e,
     * fills the fields transitionProb and emissionProb. Lowercase all words.
     */
    public HiddenMarkovModel(String wikipediaCorpus) throws IOException {
        try (Parser parser = new Parser(new File(wikipediaCorpus))) {
            while (parser.hasNext()) {
                Page nextPage = parser.next();
                String[] wordsWithTags = nextPage.content.split(" ");
                // Magic goes here
                // Map<String, Double> emissionMap = new TreeMap<>();
                // Map<String, Double> transitionMap = new TreeMap<>();
                for (int i = 0; i < wordsWithTags.length; i++) {
                    // Split word and tag
                    int index = wordsWithTags[i].lastIndexOf("/");
                    String word = wordsWithTags[i].substring(0, index).toLowerCase();
                    String tag = wordsWithTags[i].substring(index + 1);
                    //String word = wordsWithTags[i].split("/")[0].toLowerCase();
                    //String tag = wordsWithTags[i].split("/")[1];

                    // Store word and tag in map and count number of each tag
                    //Map<String, Double> emissionMap = new TreeMap<>();
 /*                   if (emissionProb.constainsKey(tag)){
                        if (emissionProb.get(tag).containsKey(word)){
                            emissionProb.get(tag).put(word, emissionProb.get(tag).get(word) + 1);
                        } else {
                            Map<String, Double> emissionMap = new TreeMap<>();
                            emissionMap.put(word, (double) 1);
                            emissionProb.put(tag, emissionMap);
                        }
                    } else {
                        Map<String, Double> emissionMap2 = new TreeMap<>();
                        emissionMap2.put(word, (double) 1);
                        emissionProb.put(tag, emissionMap2);
                    }*/

                    if (! emissionProb.containsKey(tag)) {
                        Map<String, Double> emissionMap = new TreeMap<>();
                        emissionMap.put(word, (double) 1);
                        emissionProb.put(tag, emissionMap);
                    }

                    if (! emissionProb.get(tag).containsKey(word)) {
                        emissionProb.get(tag).put(word, (double) 1);
                    } else {
                        emissionProb.get(tag).put(word, emissionProb.get(tag).get(word) + 1);
                    }


                    // store tag and tagNext in map and count number of each tagNext
                    if (i < wordsWithTags.length - 1) {

                        //String tagNext = wordsWithTags[i+1].split("/")[1];
                        int index2 = wordsWithTags[i + 1].lastIndexOf("/");
                        String tagNext = wordsWithTags[i + 1].substring(index2 + 1);
                        //Map<String, Double> transitionMap = new TreeMap<>();
/*                        if (transitionProb.get(tag) != null) {
                            if (transitionProb.get(tag).get(tagNext) != null){
                                transitionProb.get(tag).put(tagNext, transitionProb.get(tag).get(tagNext) + 1);
                            } else {
                                Map<String, Double> transitionMap = new TreeMap<>();
                                transitionMap.put(tagNext, (double) 1);
                                transitionProb.put(tag, transitionMap);
                            }
                        }  else {
                            Map<String, Double> transitionMap2 = new TreeMap<>();
                            transitionMap2.put(tagNext, (double) 1);
                            transitionProb.put(tag, transitionMap2);
                        }*/
                        if (! transitionProb.containsKey(tag)) {
                            Map<String, Double> transitionMap2 = new TreeMap<>();
                            transitionMap2.put(tagNext, (double) 1);
                            transitionProb.put(tag, transitionMap2);
                        }

                        if (! transitionProb.get(tag).containsKey(tagNext)) {
                            transitionProb.get(tag).put(tagNext, (double) 1);
                        } else {
                            transitionProb.get(tag).put(tagNext, transitionProb.get(tag).get(tagNext) + 1);
                        }


                    }
                }

                // compute the probability of emission
                // computerProbability(emissionProb);
                for (String tagKey: emissionProb.keySet()) {
                    double sum = 0.0d;
                    for (double count: emissionProb.get(tagKey).values()){
                        sum = sum + count;
                    }
                    for (String wordKey: emissionProb.get(tagKey).keySet()) {
                        emissionProb.get(tagKey).put(wordKey, emissionProb.get(tagKey).get(wordKey)/sum);
                        // emissionProb.get(tagKey).put(wordKey, sum);
                    }
                }

                // compute the probability of transition from
                // computerProbability(transitionProb);

                for (String tagKey: transitionProb.keySet()) {
                    double sum = 0.0d;
                    for (double count:transitionProb.get(tagKey).values()){
                        sum = sum + count;
                    }
                    for (String tagKey2: transitionProb.get(tagKey).keySet()) {
                        transitionProb.get(tagKey).put(tagKey2, transitionProb.get(tagKey).get(tagKey2)/sum);
                        // transitionProb.get(tagKey).put(tagKey2, sum);
                    }
                }
            }
        }
    }

    public void computerProbability(Map<String, Map<String, Double>> treeMap) {
        double sum = 0.0d;
        for (String outerKey : treeMap.keySet()) {
            for (double count : treeMap.get(outerKey).values()) {
                sum = sum + count;
            }
            for (String innerKey : treeMap.get(outerKey).keySet()) {
                treeMap.get(outerKey).put(innerKey, treeMap.get(outerKey).get(innerKey) / sum);
            }
        }
    }

    /**
     * Saves this model to a file
     */
    public void saveTo(File model) throws IOException {
        try (Writer out = new FileWriter(model)) {
            for (String fromTag : transitionProb.keySet()) {
                Map<String, Double> map = transitionProb.get(fromTag);
                for (String toTag : map.keySet()) {
                    out.write("T\t" + fromTag + "\t" + toTag + "\t" + map.get(toTag) + "\n");
                }
            }
            for (String tag : emissionProb.keySet()) {
                Map<String, Double> map = emissionProb.get(tag);
                for (String word : map.keySet()) {
                    out.write("E\t" + tag + "\t" + word + "\t" + map.get(word) + "\n");
                }
            }
        }
    }

    /**
     * Constructs a Hidden Markov Model from a previously stored model file.
     */
    public HiddenMarkovModel(File model) throws FileNotFoundException, IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(model))) {
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                String[] split = line.split("\t");
                if (split[0].equals("T")) {
                    Map<String, Double> map = transitionProb.get(split[1]);
                    if (map == null) transitionProb.put(split[1], map = new TreeMap<>());
                    map.put(split[2], Double.parseDouble(split[3]));
                } else if (split[0].equals("E")) {
                    Map<String, Double> map = emissionProb.get(split[1]);
                    if (map == null) emissionProb.put(split[1], map = new TreeMap<>());
                    map.put(split[2], Double.parseDouble(split[3]));
                }
            }
        }
    }

    /**
     * Given (1) a POS-tagged Wikipedia corpus and (2) a target model file,
     * constructs the model and stores it in the target model file.
     */
    public static void main(String[] args) throws IOException {
        HiddenMarkovModel model = new HiddenMarkovModel(args[0]);
        // HiddenMarkovModel model = new HiddenMarkovModel("wikipedia-first-pos.txt");
        model.saveTo(new File(args[1]));
        // model.saveTo(new File("result.txt"));
    }

}
