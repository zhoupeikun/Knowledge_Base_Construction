package lab4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Skeleton for a Viterbi POS tagger.
 *
 * @author Fabian M. Suchanek
 * @author peikun
 */
public class Viterbi {

    /**
     * HMM we'll use
     */
    protected HiddenMarkovModel model;

    /**
     * Constructs the parser from a model file
     */
    public Viterbi(File modelFile) throws FileNotFoundException, IOException {
        model = new HiddenMarkovModel(modelFile);
    }

    /**
     * Parses a sentence and returns the list of POS tags
     */
    public List<String> parse(String sentence) {
        List<String> words = Arrays.asList((". " + sentence.toLowerCase() + " .").split(" "));
        int numWords = words.size();
        List<String> tags = new ArrayList<String>(model.emissionProb.keySet());
        int numTags = tags.size();
        // Smart things happen here!
        double probability[][] = new double[numTags + 1][numWords + 1];
        int previous[][] = new int[numTags + 1][numTags + 1];
        for (int i = 0; i < numWords; i++) {
            for (int j = 0; j < numTags; j++) {
                double maxVal = 0;
                int maxIndex = -1;
                for (int k = 0; k < numTags; k++) {

                    double temp;
                    double emissionProb;
                    double transitionProb;

                    // tags.get(j) - tag -> words.get(i) - word
                    if (model.emissionProb.get(tags.get(j)).get(words.get(i)) == null) {
                        // set as 10e-20
                        emissionProb = 10e-20;
                    } else {
                        emissionProb = model.emissionProb.get(tags.get(j)).get(words.get(i));
                    }

                    // tags.get(k) - tag  -> tags.get(j) - tagNext
                    if (model.transitionProb.get(tags.get(k)).get(tags.get(j)) == null) {
                        transitionProb = 0;
                    } else {
                        transitionProb = model.transitionProb.get(tags.get(k)).get(tags.get(j));
                    }

                    if (i == 0) {
                        temp = emissionProb * transitionProb;
                    } else {
                        temp = probability[k][i - 1] * emissionProb * transitionProb;
                    }

                    if (maxVal < temp) {
                        maxVal = temp;
                        maxIndex = k;
                    }
                }
                probability[j][i] = maxVal;
                // the best processor for tag[i] [j]
                previous[j][i] = maxIndex;
            }
        }

        int lastIndex = -1;
        double maxProbability = -1;
        for (int i = 0; i < numTags; i++) {
            // update maxProbability when crruent probability is greater
            if (probability[i][numWords - 1] > maxProbability) {
                lastIndex = i;
                maxProbability = probability[i][numWords - 1];
            }
        }

        List<String> tmpTags = new ArrayList<String>();
        List<String> posTags = new ArrayList<String>();

        for (int i = numWords - 1; i >= 0; i--) {
            tmpTags.add(tags.get(lastIndex));
            lastIndex = previous[lastIndex][i];
        }
        for (int i = 0; i < numWords; i++) {
            posTags.add(tmpTags.get(tmpTags.size() - i - 1));
        }
        return posTags;

    }

    /**
     * Given (1) a Hidden Markov Model file and (2) a sentence (in quotes),
     * prints the sequence of POS tags
     */
    public static void main(String[] args) throws Exception {
        // args[0] = "wikipedia-first-pos.txt";
        // args[1] = "Elvis is the best";
        // args[1] = "Elvis sings best";
        System.out.println(new Viterbi(new File(args[0])).parse(args[1]));
        // System.out.println(new Viterbi(new File("result.txt")).parse("Elvis is the best"));

    }
}
