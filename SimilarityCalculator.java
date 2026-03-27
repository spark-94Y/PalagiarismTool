import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SimilarityCalculator {

    // Formula: similarity = (common_kgrams / total_unique_kgrams) * 100
    public double calculateSimilarity(List<String> kGrams1, List<String> kGrams2, List<String> commonKGrams) {

        // Combine all unique k-grams from both files
        Set<String> allUnique = new HashSet<>();
        allUnique.addAll(kGrams1);
        allUnique.addAll(kGrams2);

        int totalUnique = allUnique.size();
        int commonCount = commonKGrams.size();

        if (totalUnique == 0) {
            return 0.0;
        }

        // Calculate percentage
        double similarity = ((double) commonCount / totalUnique) * 100;

        // Round to 2 decimal places
        return Math.round(similarity * 100.0) / 100.0;
    }
}
