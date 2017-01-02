package lab2;

import java.io.File;
import java.io.IOException;

/**
 * Skeleton code for a type extractor.
 */
public class TypeExtractor {

  /**
Given as argument a Wikipedia file, the task is to run through all Wikipedia articles,
and to extract for each article the type (=class) of which the article
entity is an instance. For example, from a page starting with "Leicester is a city",
you should extract "city". 

* extract the longest possible type ("American rock-and roll star") consisting of adjectives,
  nationalities, and nouns
* if the type cannot reasonably be extracted ("Mathematics was invented in the 19th century"),
  skip the article (do not output anything)
* take only the first item of a conjunction ("and")
* do not extract provenance ("from..", "in..", "by.."), but do extract complements
  ("body of water")
* do not extract too general words ("type of", "way", "form of"), but resolve like a
  human ("A Medusa  is one of two forms of certain animals" -> "animals")
* keep the plural

The output shall be printed to the screen in the form
    entity TAB type NEWLINE
with one or zero lines per entity.
   */
  public static void main(String args[]) throws IOException {
    //args = new String[] { "c:/fabian/data/wikipedia/wikipedia.txt" };
    try (Parser parser = new Parser(new File(args[0]))) {
      while (parser.hasNext()) {
        Page nextPage = parser.next();
        String type=null;
        // Magic happens here
        if(type!=null) System.out.println(nextPage.title+"\t"+type);
      }
    }
  }

}