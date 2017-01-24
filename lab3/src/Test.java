/**
 * Created by peikun on 02/01/2017.
 */

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Skeleton class to perform disambiguation
 *
 * @author Jonathan Lajus
 */
public class Test {

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
                if ((correspondingYagoEntity = disambiguate(db, pageContent, pageLabel)) != null)
                    System.out.println(pageTitle + "\t" + correspondingYagoEntity);
            }
        }
        /** The evaluation part, provide the ground truth path as the 4th argument **/
/*        if (args.length == 4) {
        }*/
        try (Parser parser = new Parser(wiki)) {
            evaluate(db, parser, "goldstandard-sample.tsv");
        }
    }

    public static String disambiguate(SimpleDatabase db, String content, String label) {
        Set<String> tokens = new HashSet<String>(Arrays.asList(content.split(" ")));

        Map<String, Set<String>> context = new HashMap<>();
        Set<String> entities = new HashSet<String>();
        Set<String> entityCandidates = new HashSet<String>();
        List<Pair<String, Integer>> candidateList = new ArrayList<>();
        if (db.reverseLabels.containsKey(label)) {
            // If the label is in our database, we can find the entity candidates, if only one is found, print it
            entityCandidates = db.reverseLabels.get(label);
            if (entityCandidates.size() <= 1) {
                String s[] = {};
                return entityCandidates.toArray(s)[0];
            }
            for (String candidate : entityCandidates) {
                context.put(candidate, new HashSet<String>());
                // get the related entities of the candidate and find their labels
                entities = db.links.get(candidate);
                if (entities == null)
                    break;
                for (String entity : entities) {
                    Set<String> labels = db.labels.get(entity);
                    if (labels == null)
                        break;
                    for (String l : labels) {
                        context.get(candidate).addAll(Arrays.asList(l.split(" ")));
                    }
                }
            }
            for (String candidate : entityCandidates) { // calculate the intersections
                context.get(candidate).retainAll(tokens);
                Pair<String, Integer> tmp = new Pair<>(candidate, context.get(candidate).size());
                candidateList.add(tmp);
            }
            Collections.sort(candidateList, new Comparator<Pair<String, Integer>>() {
                @Override
                public int compare(Pair<String, Integer> o1, Pair<String, Integer> o2) {
                    return o2.getValue() - o1.getValue();
                }
            });
            if (candidateList.get(0).getValue() < 1 || candidateList.get(0).getValue() - candidateList.get(1).getValue() < 1)
                return null;
            return candidateList.get(0).getKey();
        }
        return null;
    }

    public static void evaluate(SimpleDatabase db, Parser parser, String dir) throws IOException {
        Map<String, Pair<String, String>> m = new HashMap<>();
        while (parser.hasNext()) {
            Page p = parser.next();
            m.put(p.title, new Pair<>(p.content, p.label()));
        }
        File f = new File(dir);
        BufferedReader r = new BufferedReader(new FileReader(f));
        String line;
        List<Boolean> results = new ArrayList<>();
        while ((line = r.readLine()) != null) {
            String[] splitted = line.trim().split("\t");
            String title = splitted[0];
            String truth = splitted[1];
            if (!m.containsKey(title))
                continue;
            String result = disambiguate(db, m.get(title).getKey(), m.get(title).getValue());
            if (result == null) {
                results.add(null);
            } else {
                if (result.equals(truth)) {
                    results.add(true);
                } else {
                    results.add(false);
                }
            }
        }
        int numTrue = Collections.frequency(results, true);
        int numFalse = Collections.frequency(results, false);
        int numNull = Collections.frequency(results, null);
        System.out.println("True: " + numTrue + "\nFalse: " + numFalse + "\nNull " + numNull + "\nPrecision: " + (float) numTrue / (numTrue + numFalse) + "\nRecall: " +
                +(float) numTrue / 2000);
    }
}