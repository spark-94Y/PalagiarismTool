import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RabinKarpMatcher {

    private static final int BASE = 31;       // Base for hash function
    private static final int MOD = 1000000007; // Large prime to avoid overflow

    // Simple hash function for a string
    // Converts each character into a number and combines them
    public long computeHash(String text) {
        long hash = 0;

        for (int i = 0; i < text.length(); i++) {
            hash = (hash * BASE + text.charAt(i)) % MOD;
        }

        return hash;
    }

    // Converts a list of k-grams into a set of hash values
    public Set<Long> hashKGrams(List<String> kGrams) {
        Set<Long> hashSet = new HashSet<>();

        for (String kGram : kGrams) {
            long hash = computeHash(kGram);
            hashSet.add(hash);
        }

        return hashSet;
    }

    // Finds matching k-grams between two documents
    // Step 1: Hash all k-grams from file 1
    // Step 2: Hash each k-gram from file 2 and check if it exists in file 1's hashes
    // Step 3: If hashes match AND strings match -> it's a real match (avoids false positives)
    public List<String> findMatches(List<String> kGrams1, List<String> kGrams2) {
        List<String> matchingKGrams = new ArrayList<>();

        // Create a set of k-grams from file 1 for quick lookup
        Set<String> set1 = new HashSet<>(kGrams1);

        // Hash all k-grams from file 1
        Set<Long> hashes1 = hashKGrams(kGrams1);

        // Check each k-gram from file 2
        for (String kGram : kGrams2) {
            long hash = computeHash(kGram);

            // If hash matches, verify the actual string to avoid false positive
            if (hashes1.contains(hash) && set1.contains(kGram)) {
                if (!matchingKGrams.contains(kGram)) {  // Avoid duplicates
                    matchingKGrams.add(kGram);
                }
            }
        }

        return matchingKGrams;
    }
}
