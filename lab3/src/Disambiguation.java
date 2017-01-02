

import java.io.File;
import java.io.IOException;

/**
 * Skeleton class to perform disambiguation
 *
 * @author Jonathan Lajus
 */
public class Disambiguation {

    /**
     * This program takes 3 command line arguments, namely the paths to:
     * - yagoLinks.tsv
     * - yagoLabels.tsv
     * - wikipedia-ambiguous.txt
     * in this order. You may also ignore the last argument at your will.
     * The program prints statements of the form:
     * <pageTitle>  TAB  <yagoEntity> NEWLINE
     * It is OK to skip articles.
     */
    public static void main(String[] args) throws IOException {
/*        if (args.length < 3) {
            System.err.println("usage: Disambiguation <yagoLinks> <yagoLabels> <wikiText>");
            return;
        }
        File dblinks = new File(args[0]);
        File dblabels = new File(args[1]);
        File wiki = new File(args[2]);*/

        File dblinks = new File("yagoLinks.tsv");
        File dblabels = new File("yagoLabels.tsv");
        File wiki = new File("wikipedia-ambiguous.txt");

        SimpleDatabase db = new SimpleDatabase(dblinks, dblabels);

        try (Parser parser = new Parser(wiki)) {
            while (parser.hasNext()) {
                Page nextPage = parser.next();
                String pageTitle = nextPage.title; // "Clinton_1"
                String pageContent = nextPage.content; // "Hillary Clinton was..."
                String pageLabel = nextPage.label(); // "Clinton"
                String correspondingYagoEntity = "<For_you_to_find>";
                /**
                 * TODO CODE HERE to disambiguate the entity.
                 */
                


                System.out.println(pageTitle + "\t" + correspondingYagoEntity);
            }
        }
    }
}
