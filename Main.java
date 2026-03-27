import java.util.List;

public class Main {

    public static void main(String[] args) {

        try {
            // ========== STEP 1: Setup ==========
            String file1 = "input1.txt";
            String file2 = "input2.txt";
            int windowSize = 3; // Sliding window size (3 consecutive words per pattern)

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

            // ========== STEP 4: Run Rabin-Karp Algorithm ==========
            // - Generate patterns from text1 using sliding window
            // - Search each pattern in text2 using Rabin-Karp hashing
            RabinKarpMatcher matcher = new RabinKarpMatcher();

            List<String> patterns1 = matcher.generatePatterns(text1, windowSize);
            List<String> patterns2 = matcher.generatePatterns(text2, windowSize);

            System.out.println("Patterns from File 1: " + patterns1.size());
            System.out.println("Patterns from File 2: " + patterns2.size());
            System.out.println();

            // Find matching patterns using Rabin-Karp search
            List<String> matchingPhrases = matcher.findMatches(text1, text2, windowSize);

            // ========== STEP 5: Calculate similarity ==========
            SimilarityCalculator calculator = new SimilarityCalculator();

            double similarity = calculator.calculateSimilarity(patterns1, patterns2, matchingPhrases);

            // ========== STEP 6: Display result ==========
            System.out.println("==============================");
            System.out.println(" PLAGIARISM DETECTION RESULT");
            System.out.println("==============================");
            System.out.println("Similarity Score: " + similarity + "%");
            System.out.println();

            System.out.println("Matching Phrases:");
            if (matchingPhrases.isEmpty()) {
                System.out.println("- No matching phrases found");
            } else {
                for (String match : matchingPhrases) {
                    System.out.println("- " + match);
                }
            }

            System.out.println("==============================");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
