import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Calculates similarity percentage between two documents
 * based on the number of common substrings found by Rabin-Karp matching.
 *
 * Formula: similarity = (common_substrings / total_unique_substrings) * 100
 */
public class SimilarityCalculator {

    // Calculates the Jaccard-style similarity between two sets of substrings
    //
    // Parameters:
    //   substrings1    - sliding window substrings from document 1
    //   substrings2    - sliding window substrings from document 2
    //   commonMatches  - substrings found in both documents (from Rabin-Karp matching)
    //
    // Returns: similarity as a percentage (0.0 to 100.0)
    //
    public double calculateSimilarity(List<String> substrings1, List<String> substrings2, List<String> commonMatches) {

        // Combine all unique substrings from both documents
        Set<String> allUnique = new HashSet<>();
        allUnique.addAll(substrings1);
        allUnique.addAll(substrings2);

        int totalUnique = allUnique.size();
        int commonCount = commonMatches.size();

        if (totalUnique == 0) {
            return 0.0;
        }

        // Calculate percentage
        double similarity = ((double) commonCount / totalUnique) * 100;

        // Round to 2 decimal places
        return Math.round(similarity * 100.0) / 100.0;
    }
}
