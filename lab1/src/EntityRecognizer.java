import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


/**
 * Skeleton for an entity recognizer based on a trie.
 *
 * @author Fabian M. Suchanek
 * @author peikun
 */
public class EntityRecognizer {

    /**
     * The task is to modify the class so that it takes as arguments (1) the
     * Wikipedia corpus and (2) a file with a list of entities, and so that it
     * outputs appearances of entities in the content of articles. Each
     * appearance should be printed to the standard output as:
     * <ul>
     * <li>The title of the article where the mention occurs</li>
     * <li>TAB (\t)
     * <li>The entity mentioned</li>
     * <li>NEWLINE (\n)
     * </ul>
     * <p>
     * Hint: Go character by character, as in the lecture. It is not necessary
     * to go by word boundaries!
     */

    public static void main(String args[]) throws IOException {
        // Uncomment the following lines for your convenience.
        // Comment them out again before submission!
/*        args = new String[]{"/Users/peikun/Documents/M2/Knowledge_Base_Construction/lab1/wikipedia-first.txt",
                "/Users/peikun/Documents/M2/Knowledge_Base_Construction/lab1/entities.txt"};*/
        // write results into relust.txt
        File resultFile = new File("result.txt");
        BufferedWriter resultWrite = new BufferedWriter(new FileWriter(resultFile));

        Trie trie = new Trie(new File(args[1]));
        try (Parser parser = new Parser(new File(args[0]))) {
            while (parser.hasNext()) {
                Page nextPage = parser.next();
                String content = nextPage.content;
                String entity = "Elvis"; // TODO: do something smarter here

                for(int i = 0; i <= content.length(); i++){
                    int length = trie.containedLength(content, i);
                    if(length <= 0){
                        continue;
                    }
                    else{
                        // The title of the article where the mention occurs
                        resultWrite.write(nextPage.title);
                        System.out.print(nextPage.title);
                        resultWrite.write("\t");
                        System.out.print("\t");
                        // The entity mentioned
                        resultWrite.write(content.substring(i, i+length));
                        System.out.println(content.substring(i, i+length));
                        resultWrite.write("\n");

                    }
                }
                System.out.println(nextPage.title + "\t" + entity);
            }
        }

        resultWrite.close();
    }

}