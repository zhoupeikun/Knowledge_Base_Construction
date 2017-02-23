package lab5;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Empty skeleton for a type extractor with POS tags.
 *
 * @author Fabian M. Suchanek
 * @author peikun
 */
public class POSTypeExtractor {

    /**
     * Given a POS-tagged Wikipedia article, returns the type (=class) of which
     * the article entity is an instance. For example, from a page starting with
     * "Leicester/NNP is/VBZ a/DT city/NN in/IN the/DT Midlands/NNP", you should
     * extract "city".
     * <p>
     * - extract the longest possible type ("American rock star")
     * consisting of adjectives and nouns, including nationalities
     * - do not extract provenance or specifics ("from...", "in...", "by...")
     * - do not extract too general words ("type of", "way", "form of"), but resolve like
     * a human ("A Medusa is one of two forms of certain animals" -> "animals")
     * - keep the plural
     * - do not restrict the output to hard-coded types
     * - in case of uncertainty, skip the article by returning NULL
     */
    public static String findType(Page nextPage) {
        String[] taggedText = nextPage.content.split(" ");
        List<String> title = Arrays.asList(nextPage.title.toLowerCase().split(" "));
        List<String> words = new ArrayList<>();
        List<String> tags = new ArrayList<>();
        String type = "";

        for (String s : title) {
            if (s.split("/").length != 2)
                continue;
            words.add(s.substring(0, s.lastIndexOf("/")));
            tags.add(s.substring(s.lastIndexOf("/") + 1));
        }

        for (int i = 0; i < words.size(); i++) {
            if (tags.get(i).contains("VBD") || tags.get(i).contains("VBZ")) {
                for (int j = i; j < words.size(); j++) {
                    if (tags.get(j).contains("NN") && words.get(j).contains("type") && words.get(j).contains("form")
                            && words.get(j).contains("way") && words.get(j).contains("sort") && words.get(j).contains("kind")
                            && words.get(j).contains("field")) {
                        type = type + (words.get(j) + " ");
                    }
                    if (j-1 > 0 && tags.get(j-1).contains("JJ")){
                        type = (words.get(j-1) + " " + type);
                    }
                }
            } else {
                break;
            }
        }
        return type;
    }

    /**
     * Given as arguments (1) a POS-tagged Wikipedia and (2) a target file, writes "Title
     * TAB class NEWLINE" to the target file
     */
    public static void main(String args[]) throws IOException {
        // Uncommment the following line for your convenience. Comment it out
        // again before submitting!
        // args = new String[] { "c:/fabian/data/wikipedia/wikipedia_pos.txt", "c:/fabian/data/my-output.txt" };
        // args = new String[]{"wikipedia-first-pos.txt", "my-output.txt"};
        try (Parser parser = new Parser(new File(args[0]))) {
            try (Writer result = new OutputStreamWriter(new FileOutputStream(args[1]), "UTF-8")) {
                while (parser.hasNext()) {
                    Page nextPage = parser.next();
                    String type = findType(nextPage);
                    if (type == null) continue;
                    result.write(nextPage.title + "\t" + type + "\n");
                }
            }
        }
    }

}