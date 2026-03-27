import java.util.List;

public class Main {

    public static void main(String[] args) {

        try {
            // ========== STEP 1: Setup ==========
            String file1 = "input1.txt";
            String file2 = "input2.txt";
            int k = 2; // k-gram size (using 3 words per k-gram)

            // ========== STEP 2: Read input files ==========
            FileHandler fileHandler = new FileHandler();

            String text1 = fileHandler.readFile(file1);
            String text2 = fileHandler.readFile(file2);

            System.out.println("File 1 loaded: " + file1);
            System.out.println("File 2 loaded: " + file2);
            System.out.println();

            // ========== STEP 3: Preprocess text ==========
            TextProcessor processor = new TextProcessor();

            text1 = processor.toLowerCase(text1);
            text1 = processor.removePunctuation(text1);

            text2 = processor.toLowerCase(text2);
            text2 = processor.removePunctuation(text2);

            // ========== STEP 4: Generate k-grams ==========
            KGramGenerator generator = new KGramGenerator();

            List<String> kGrams1 = generator.generateKGrams(text1, k);
            List<String> kGrams2 = generator.generateKGrams(text2, k);

            System.out.println("K-grams from File 1: " + kGrams1.size());
            System.out.println("K-grams from File 2: " + kGrams2.size());
            System.out.println();

            // ========== STEP 5 & 6: Match using Rabin-Karp hashing ==========
            RabinKarpMatcher matcher = new RabinKarpMatcher();

            List<String> matchingKGrams = matcher.findMatches(kGrams1, kGrams2);

            // ========== STEP 7: Calculate similarity ==========
            SimilarityCalculator calculator = new SimilarityCalculator();

            double similarity = calculator.calculateSimilarity(kGrams1, kGrams2, matchingKGrams);

            // ========== STEP 8: Display result ==========
            System.out.println("==============================");
            System.out.println(" PLAGIARISM DETECTION RESULT");
            System.out.println("==============================");
            System.out.println("Similarity Score: " + similarity + "%");
            System.out.println();

            System.out.println("Matching Phrases:");
            if (matchingKGrams.isEmpty()) {
                System.out.println("- No matching phrases found");
            } else {
                for (String match : matchingKGrams) {
                    System.out.println("- " + match);
                }
            }

            System.out.println("==============================");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
