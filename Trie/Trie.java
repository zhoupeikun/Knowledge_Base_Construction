package lab1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Skeleton for a Trie data structure.
 * 
 * @author Fabian Suchanek and Luis Galarraga.
 *
 */
public class Trie {

  /**
   * Adds a string to the trie.
   */
  public void add(String s) {
    throw new UnsupportedOperationException("The method Trie.add has not been implemented.");
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
    throw new UnsupportedOperationException("The method Trie.containedLength has not been implemented.");
  }

  /** Constructs a Trie from the lines of a file */
  public Trie(File file) throws IOException {
    try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"))) {
      String line;
      while ((line = in.readLine()) != null) {
        add(line);
      }
    }
  }

  /** Constructs an empty Trie */
  public Trie() {
  }

  /** returns a list of all strings in the trie. Do not create a field of the class that contains all strings of the trie!*/
  public List<String> allStrings() {
    throw new UnsupportedOperationException("The method Trie.allStrings has not been implemented.");
  }

  /** Use this to test your implementation. */
  public static void main(String[] args) throws IOException {
    // Hint: Remember that a Trie is a recursive data structure:
    // a trie has children that are again tries. You should
    // add the corresponding fields to the skeleton.
    // The methods add() and containedLength() are each no more than 15
    // lines of code!

    // Hint: You do not need to split the string into words.
    // Just proceed character by character, as in the lecture.

    Trie trie = new Trie();
    trie.add("New York City");
    trie.add("New York");
    System.out.println(trie.containedLength("I live in New York City center", 10) + " should be 13");
    System.out.println(trie.containedLength("I live in New York center", 10) + " should be 8");
    System.out.println(trie.containedLength("I live in Berlin center", 10) + " should be -1");
    System.out.println(trie.containedLength("I live in New Hampshire center", 10) + " should be -1");
    System.out.println(trie.containedLength("I live in New York center", 0) + " should be -1");
  }
}
