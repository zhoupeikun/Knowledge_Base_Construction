import java.io.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Skeleton for a Trie data structure.
 *
 * @author Fabian Suchanek and Luis Galarraga.
 * @author peikun
 */
public class Trie {

    // Create a subTries with type of map<String, Trie>
    public Map<String, Trie> subTries = new HashMap<String, Trie>();
    public boolean finished;


    /**
     * Adds a string to the trie.
     */
    public void add(String s) {
        //throw new UnsupportedOperationException("The method Trie.add has not been implemented.");
        //allStrings().add(s);

/*        int strLenghth = s.length();*/

        // Given string is empty
/*        if(strLenghth == 0){
            return error;
        }*/

        // Check if there is a subTree
        // I need position to return character when use character type
        // When string, we can use subtring?????
        if (s.length() < 1) {

            finished = true;

        } else {

            String firstChar = s.substring(0, 1);

            if (subTries == null) {
                subTries = new HashMap<String, Trie>();
            }

            Trie trieFirstChar = subTries.get(firstChar);

            if (trieFirstChar == null) {
                trieFirstChar = new Trie();
                subTries.put(firstChar, trieFirstChar);
            }

            trieFirstChar.add(s.substring(1));
        }

    }

    /**
     * Given a string and a starting position (<var>startPos</var>), it returns
     * the length of the longest word in the trie that starts in the string at
     * the given position, or else -1. For example, if the trie contains words
     * "New York", and "New York City", containedLength(
     * "I live in New York City center", 10) returns 13, that is the length of
     * the longest word ("New York City") registered in the trie that starts at
     * position 10 of the string.
     */
    public int containedLength(String s, int startPos) {
        //throw new UnsupportedOperationException("The method Trie.containedLength has not been implemented.");

        if (s.length() <= startPos) {
            return -1;
        }

        // get the first character of startposition
        String firstChar = s.substring(startPos, startPos + 1);

        // get tries start with firtChar
        Trie subTrie = subTries.get(firstChar);

        if (subTrie == null) {
            if (finished)
                return 0;
            else
                return -1;
        }

        int length = subTrie.containedLength(s, startPos + 1);

        if (length == -1) {
            if (finished)
                return 0;
            else
                return -1;
        }

        return length + 1;
    }

    /**
     * Constructs a Trie from the lines of a file
     */
    public Trie(File file) throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"))) {
            String line;
            while ((line = in.readLine()) != null) {
                add(line);
            }
        }
    }

    /**
     * Constructs an empty Trie
     */
    public Trie() {
    }

    /**
     * returns a list of all strings in the trie. Do not create a field of the class that contains all strings of the trie!
     */
    public List<String> allStrings() {
        throw new UnsupportedOperationException("The method Trie.allStrings has not been implemented.");

    }

    /**
     * Use this to test your implementation.
     */
    public static void main(String[] args) throws IOException {
        // Hint: Remember that a Trie is a recursive data structure:
        // a trie has children that are again tries. You should
        // add the corresponding fields to the skeleton.
        // The methods add() and containedLength() are each no more than 15
        // lines of code!

        // Hint: You do not need to split the string into words.
        // Just proceed character by character, as in the lecture.

        Trie trie = new Trie();
        //trie.add("New York City");
        trie.add("New York");
        System.out.println(trie.containedLength("I live in New York City center", 10) + " should be 13");
        System.out.println(trie.containedLength("I live in New York center", 10) + " should be 8");
        System.out.println(trie.containedLength("I live in Berlin center", 10) + " should be -1");
        System.out.println(trie.containedLength("I live in New Hampshire center", 10) + " should be -1");
        System.out.println(trie.containedLength("I live in New York center", 0) + " should be -1");
    }
}
