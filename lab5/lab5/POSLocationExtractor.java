package lab5;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Empty skeleton for a location extractor with POS tags.
 *
 * @author Fabian M. Suchanek
 */
public class POSLocationExtractor {

    /**
     * Given a POS-tagged Wikipedia article, returns extract wherever possible
     * the location of a geographic entity. For example, from a page starting
     * with "Leicester/NNP is/VBZ a/DT city/NN in/IN the/DT Midlands/NNP", you
     * should extract "Midlands".
     * <p>
     * Try to extract exactly the region, country, or
     * city. Do not extract locations for non-geographical entities.
     * You can also skip articles by returning NULL.
     */
    public static String findLocation(Page nextPage) {
        String[] taggedText = nextPage.content.split(" ");
        List<String> title = Arrays.asList(nextPage.title.toLowerCase().split(" "));
        List<String> words = new ArrayList<String>();
        List<String> tags = new ArrayList<String>();

        String type = "";
        // judge wheather PREP is already found
        boolean prepFound = false;

        for (String t : taggedText) {
            if (t.split("/").length != 2)
                continue;
            words.add(t.substring(0, t.lastIndexOf("/")));
            tags.add(t.substring(t.lastIndexOf("/") + 1));
        }

        for (int i = 0; i < words.size(); i++) {
            // on by that from above below to of  | at
            if (!prepFound && tags.get(i).equals("IN") && !words.get(i).equals("on") && !words.get(i).equals("by")
                     && !words.get(i).contains("that") && !words.get(i).contains("from") && !words.get(i).equals("above")
                     && !words.get(i).contains("below") && !words.get(i).equals("to") && !words.get(i).equals("of")) {
                prepFound = true;
            }

            // the next NNP
            if (prepFound && tags.get(i).contains("NNP")) {
                for (int j = i; j < words.size(); j++) {
                    if (tags.get(j).contains("NNP")) {
                        type = type + (words.get(j) + " ");
                    } else {
                        break;
                    }
                }
                break;
            }
        }
        // split these unmatch object
        if (type.length() == 0)
            return null;
        return type;
    }

    /**
     * Given as arguments (1) a POS-tagged Wikipedia and (2) a target file,
     * writes "Title TAB location NEWLINE" to the target file
     */
    public static void main(String args[]) throws IOException {
        // Uncommment the following line for your convenience. Comment it out
        // again before submitting!
        // args = new String[] { "c:/fabian/data/wikipedia/wikipedia_pos.txt", "c:/fabian/data/my-results.txt" };
        // args = new String[]{"wikipedia-first-pos.txt", "my-results.txt"};
        try (Parser parser = new Parser(new File(args[0]))) {
            try (Writer result = new OutputStreamWriter(new FileOutputStream(args[1]), "UTF-8")) {
                while (parser.hasNext()) {
                    Page nextPage = parser.next();
                    String type = findLocation(nextPage);
                    if (type == null) continue;
                    result.write(nextPage.title + "\t" + type + "\n");
                }
            }
        }
    }

}