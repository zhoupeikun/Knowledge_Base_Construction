// package lab6;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Skeleton class for a program that maps the entities from one KB to the
 * entities of another KB.
 *
 * @author Fabian
 * @author peikun
 */
public class EntityMapper {

    /**
     * Takes as input (1) one knowledge base (2) another knowledge base and (3)
     * an output file.
     * <p>
     * Writes into the output file "entity1 TAB entity2 NEWLINE", if the first
     * entity from the first knowledge base is the same as the second entity
     * from the second knowledge base. Output 0 or 1 line per entity1.
     */
    public static void main(String[] args) throws IOException {
        // Uncomment for your convenience. Comment it again before submission!
        /*
         * args = new String[] {
         * "/Users/fabian/Data/KBC-2016-Entity-Mapping/yago-anonymous.tsv",
         * "/Users/fabian/Data/KBC-2016-Entity-Mapping/dbpedia.tsv",
         * "/Users/fabian/Data/KBC-2016-Entity-Mapping/result.tsv" };
         */

        args = new String[]{
                "yago-anonymous.tsv",
                "dbpedia.tsv",
                "result.tsv"
        };
        KnowledgeBase kb1 = new KnowledgeBase(new File(args[0]));
        KnowledgeBase kb2 = new KnowledgeBase(new File(args[1]));
        // Map the sample of YAGO(kb1) to the sample of DBpedia(kb2)
        try (Writer result = new OutputStreamWriter(new FileOutputStream(args[2]), "UTF-8")) {
            for (String entity1 : kb1.facts.keySet()) {
                String mostLikelyCandidate = null;

                for (String entity2 : kb2.facts.keySet()) {
                    // Something smart here
/*                    if (kb1.facts.get(entity1).get("rdfs:label") == kb2.facts.get(entity2).get("rdfs:label")) {
                        mostLikelyCandidate = entity2;
                    }*/

                    int countMatch = 0;
                    Set<String> labels1 = kb1.facts.get(entity1).get("rdfs:label");
                    Set<String> labels2 = kb2.facts.get(entity2).get("rdfs:label");
                    if (!labels1.isEmpty() && !labels2.isEmpty()) {
                        for (String s1 : labels1) {
                            for (String s2 : labels2) {
                                if (s1.toLowerCase().equals(s2.toLowerCase())) {
                                    countMatch++;
                                }
                            }
                        }
                    }

                    if (countMatch > 0)
                        mostLikelyCandidate = entity2;

                }
                if (mostLikelyCandidate != null) {
                    result.write(entity1 + "\t" + mostLikelyCandidate + "\n");
                }
            }
        }
        // evaluate(args[2], "gold-standard-sample.tsv");
    }

    public static void evaluate(String output, String goldStandard) throws IOException {
        Map<String, String> outputMap = new HashMap<>();
        Map<String, String> goldStandardMap = new HashMap<>();
        BufferedReader outputReader = new BufferedReader(new InputStreamReader(new FileInputStream(output), "UTF-8"));
        BufferedReader goldStandardReader = new BufferedReader(new InputStreamReader(new FileInputStream(goldStandard), "UTF-8"));
        String line;
        int numCorrect = 0;
        int numFalse = 0;
        while ((line = outputReader.readLine()) != null) {
            putEntityTypeIntoMap(outputMap, line);
        }
        while ((line = goldStandardReader.readLine()) != null) {
            putEntityTypeIntoMap(goldStandardMap, line);
        }
        for (Map.Entry<String, String> entry : outputMap.entrySet()) {
            String entity = entry.getKey();
            String type = entry.getValue();
            if (goldStandardMap.containsKey(entity)) {
                if (goldStandardMap.get(entity).equals(type)) {
                    numCorrect++;
                } else {
                    numFalse++;
                }
            }
        }

        int numCorrect2 = 0;
        int numFalse2 = 0;
        for (Map.Entry<String, String> entry : goldStandardMap.entrySet()) {
            String entity = entry.getKey();
            String type = entry.getValue();
            if (outputMap.containsKey(entity)) {
                if (outputMap.get(entity).equals(type)) {
                    numCorrect2++;
                } else {
                    numFalse2++;
                }
            }
        }
        System.out.println("Precision: " + (float) numCorrect / (numCorrect + numFalse));
        System.out.println("Recall: " + (float) numCorrect2 / (numCorrect2 + numFalse2));
    }

    private static void putEntityTypeIntoMap(Map<String, String> map, String line) {
        String[] entityAndType = line.split("\t");
        String entity = entityAndType[0];
        String type = entityAndType[1];
        map.put(entity, type);
    }

    /*
    * This method is for the evaluation of exact match of the two paired entities of two knowledge base
    * */
    private static void evaluateNoAnonymous(String output) throws IOException {
        BufferedReader outputReader = new BufferedReader(new InputStreamReader(new FileInputStream(output), "UTF-8"));
        String line;
        int total = 0;
        int numCorrect = 0;
        while ((line = outputReader.readLine()) != null) {
            total++;
            String[] twoEntities = line.split("\t");
            String entity1 = twoEntities[0];
            String entity2 = twoEntities[1];
            if (entity1.equals(entity2)) {
                numCorrect++;
            }
        }
        System.out.println("Precision: " + (float) numCorrect / total);
    }



}
