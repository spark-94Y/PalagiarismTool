import java.util.ArrayList;
import java.util.List;

public class KGramGenerator {

    // Generates word-based k-grams from text
    //
    // Example: text = "data structures and algorithms", k = 2
    // Output: ["data structures", "structures and", "and algorithms"]
    //
    public List<String> generateKGrams(String text, int k) {
        List<String> kGrams = new ArrayList<>();

        // Split text into individual words
        String[] words = text.split(" ");

        // Slide a window of size k across the words
        for (int i = 0; i <= words.length - k; i++) {

            // Build one k-gram by joining k consecutive words
            StringBuilder kGram = new StringBuilder();
            for (int j = i; j < i + k; j++) {
                if (j > i) {
                    kGram.append(" ");
                }
                kGram.append(words[j]);
            }

            kGrams.add(kGram.toString());
        }

        return kGrams;
    }
}
