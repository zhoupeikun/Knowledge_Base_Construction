import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

/**
 * Skeleton for a Hidden Markov Model
 *
 * @author Fabian M. Suchanek
 *
 */
public class Test {

  /**
   * Stores transition probabilities of the form ADJ -> { NN -> 0.99, VBZ ->
   * 0.01 }. Should sum to 1 for each tag.
   */
  protected Map<String, Map<String, Double>> transitionProb = new TreeMap<String, Map<String, Double>>();

  /**
   * Stores emission probabilities of the form PN -> { "Elvis" -> 0.8,
   * "Priscilla" -> 0.2 }. Should sum to 1 for each tag.
   */
  protected Map<String, Map<String, Double>> emissionProb = new TreeMap<String, Map<String, Double>>();

  /** Retrieves the emission probability for this tag and this word */
  public double getEmissionProbability(String tag, String word) {
    if (!emissionProb.containsKey(tag)) return (0);
    if (!emissionProb.get(tag).containsKey(word)) return (0);
    return (emissionProb.get(tag).get(word));
  }

  /** Retrieves the transition probability for these tags */
  public double getTransitionProbability(String fromTag, String toTag) {
    if (!transitionProb.containsKey(fromTag)) return (0);
    if (!transitionProb.get(fromTag).containsKey(toTag)) return (0);
    return (transitionProb.get(fromTag).get(toTag));
  }

  /**
   * Constructs a Hidden Markov Model from a tagged Wikipedia corpus, i.e,
   * fills the fields transitionProb and emissionProb. Lowercase all words.
   */
  public Test(String wikipediaCorpus) throws IOException {
    try (Parser parser = new Parser(new File(wikipediaCorpus))) {
      int num = 0;
      while (parser.hasNext()) {
        Page nextPage = parser.next();
        num ++;
        nextPage.content += "./. ";
        String[] wordsWithTags = nextPage.content.split(" ");
        for (int i = 0; i < wordsWithTags.length; i++) {
          if (wordsWithTags[i].split("/").length != 2)
            continue;
          String word = wordsWithTags[i].split("/")[0].toLowerCase();
          String tag = wordsWithTags[i].split("/")[1];
          emissionProb.putIfAbsent(tag, new TreeMap<String, Double>() {{put(word, (double)0);}});
          emissionProb.get(tag).put(word, emissionProb.get(tag).getOrDefault(word, (double)0) + 1);
          if (i < wordsWithTags.length-1) {
            if (wordsWithTags[i+1].split("/").length != 2)
              continue;
            String tag2 = wordsWithTags[i+1].split("/")[1];
            transitionProb.putIfAbsent(tag, new TreeMap<String, Double>(){{put(tag2, (double)0);}});
            transitionProb.get(tag).put(tag2, transitionProb.get(tag).getOrDefault(tag2, (double)0) +1);
          }
        }
      }
      // calculate the average
      for (String key : transitionProb.keySet()) {
        double sum = 0.0d;
        for (double n : transitionProb.get(key).values()) {
          sum += n;
        }
        for (String k : transitionProb.get(key).keySet()) {
          transitionProb.get(key).put(k, transitionProb.get(key).get(k)/sum);
        }
      }
      for (String key : emissionProb.keySet()) {
        double sum = 0.0d;
        for (double n : emissionProb.get(key).values()) {
          sum += n;
        }
        for (String k : emissionProb.get(key).keySet()) {
          emissionProb.get(key).put(k, emissionProb.get(key).get(k)/sum);
        }
      }
    }
  }

  /** Saves this model to a file */
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
  public Test(File model) throws FileNotFoundException, IOException {
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
    HiddenMarkovModel model = new HiddenMarkovModel("wikipedia-first-pos.txt");
    // model.saveTo(new File(args[1]));
    model.saveTo(new File("result.txt"));
  }

}
