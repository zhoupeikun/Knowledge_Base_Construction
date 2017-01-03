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
 *
 */
public class Viterbi {

    /** HMM we'll use */
    protected HiddenMarkovModel model;

    /** Constructs the parser from a model file */
    public Viterbi(File modelFile) throws FileNotFoundException, IOException {
        model = new HiddenMarkovModel(modelFile);
    }

    /** Parses a sentence and returns the list of POS tags */
    public List<String> parse(String sentence) {
        List<String> words = Arrays.asList((". " + sentence.toLowerCase() + " .").split(" "));
        int numWords = words.size();
        List<String> tags = new ArrayList<String>(model.emissionProb.keySet());
        int numTags = tags.size();
        // Smart things happen here!
        return (null);
    }

    /**
     * Given (1) a Hidden Markov Model file and (2) a sentence (in quotes),
     * prints the sequence of POS tags
     */
    public static void main(String[] args) throws Exception {
        System.out.println(new Viterbi(new File(args[0])).parse(args[1]));
    }
}
