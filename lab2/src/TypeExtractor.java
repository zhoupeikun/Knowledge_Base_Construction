import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

import java.io.File;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Skeleton code for a type extractor.
 * @author peikun
 */
public class TypeExtractor {

    /**
     * Given as argument a Wikipedia file, the task is to run through all Wikipedia articles,
     * and to extract for each article the type (=class) of which the article
     * entity is an instance. For example, from a page starting with "Leicester is a city",
     * you should extract "city".
     * <p>
     * extract the longest possible type ("American rock-and roll star") consisting of adjectives,
     * nationalities, and nouns
     * if the type cannot reasonably be extracted ("Mathematics was invented in the 19th century"),
     * skip the article (do not output anything)
     * take only the first item of a conjunction ("and")
     * do not extract provenance ("from..", "in..", "by.."), but do extract complements
     * ("body of water")
     * do not extract too general words ("type of", "way", "form of"), but resolve like a
     * human ("A Medusa  is one of two forms of certain animals" -> "animals")
     * keep the plural
     * <p>
     * The output shall be printed to the screen in the form
     * entity TAB type NEWLINE
     * with one or zero lines per entity.
     */
    public static void main(String args[]) throws IOException {
        args = new String[]{"wikipedia-first.txt"};
        try (Parser parser = new Parser(new File(args[0]))) {
            while (parser.hasNext()) {
                Page nextPage = parser.next();
                String type = null;
                // Magic happens here
                String title = nextPage.title;
                String content = nextPage.content;


                //type = PatternSimple(content);

                type = PatternNormal(content, title);

                if (type != null)
                    System.out.println(nextPage.title + "\t" + type);
            }
        }
    }

    // I implemented two methods, one simple and one normal. For patternNormal, I considered several regex and try to optimized them.

    // Pattern Simple: A is B
    private static String PatternSimple(String s) {

        String type = null;
        String regexSimple = "([A-z][a-z]*) (is|was|are|were) ([a|an|the]) ([a-z]*)";
        Pattern patternSimple = Pattern.compile(regexSimple, Pattern.CASE_INSENSITIVE);
        Matcher matcher = patternSimple.matcher(s);
        while (matcher.find())
            type = matcher.group(4);


        return type;
    }


    // PatternNormal----Title is a ** of type
    private static String PatternNormal(String s, String title) {

        String type = null;
        String regexNormal = title + "(\\s+\\w+)*" + " (is|was|are|were) " + "(a|an|the) " + "(\\s+\\w+)*" + "(type of|form of|member of|sort of|way of|style of|kind of|part of|month of|design of|study of|name of)? "  + "(\\w+)";
        //String regexNormal2 = "(\\s+\\w+)*" + " (is|was|are|were) " + "(\\s+\\w+)*" + "(of)? " + "(\\w+)";
        //String regexNormal3 = "([A-z][a-z]*)" + " (is|was|are|were) " + "(a|an|the) " + "(\\s+\\w+)*" + "(type of|form of|member of|sort of|way of|style of|kind of|part of|month of|design of|study of|name of)? "  + "(\\w+)";
        Pattern patternNormal = Pattern.compile(regexNormal, Pattern.CASE_INSENSITIVE);
        Matcher matcher = patternNormal.matcher(s);
        while (matcher.find())
            type = matcher.group(6);
        return type;
    }




    //Firt, A is/are/was/were/ B, [A-Z][a-z]* (is|are|was|were) [a|an|the] ([a-z]*)
    //Second, split and
    //Third, split a/an/the


    // Split the sentences befor first AND
/*    public String splitAnd(String s){
        Parser parserAnd =
        return s;
    }

    public String splitProvenance(String s){
        Parser parserProvenance =
                return s;
    }*/

}