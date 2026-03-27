import java.util.ArrayList;
import java.util.List;

/**
 * Rabin-Karp Algorithm for Plagiarism Detection
 *
 * How it works:
 * 1. Take a pattern and a text
 * 2. Compute hash of the pattern
 * 3. Slide a window of same length across the text, computing rolling hash
 * 4. If hashes match → verify actual strings to avoid false positives
 *
 * For plagiarism detection:
 * - Split Document 1 into fixed-length substrings (patterns)
 * - Use Rabin-Karp to search each pattern in Document 2 (text)
 * - If pattern is found → it's a matching phrase
 */
public class RabinKarpMatcher {

    private static final long BASE = 31;        // Base for polynomial hash
    private static final long MOD = 1000000007; // Large prime to reduce collisions

    // ===== Classic Rabin-Karp Search =====
    //
    // Searches for 'pattern' inside 'text' using rolling hash and sliding window.
    //
    // Example:
    //   text    = "the cat sat on the mat"
    //   pattern = "sat on"
    //
    //   Step 1: Compute hash of pattern "sat on"
    //   Step 2: Take first window of text (same length as pattern) → "the ca"
    //   Step 3: Compute its hash, compare with pattern hash
    //   Step 4: Slide window by one character → "he cat"
    //   Step 5: Recompute hash using rolling hash formula
    //   Step 6: Repeat until end of text
    //   Step 7: If hashes match → compare actual strings to confirm
    //
    // Returns true if pattern is found in text, false otherwise.
    //
    public boolean search(String text, String pattern) {
        int textLen = text.length();
        int patternLen = pattern.length();

        // Pattern can't be longer than text
        if (patternLen > textLen) {
            return false;
        }

        // Compute hash of the pattern
        long patternHash = computeHash(pattern);

        // Compute hash of the first window in text
        long textHash = computeHash(text.substring(0, patternLen));

        // Precompute BASE^(patternLen-1) % MOD for rolling hash removal
        long highPow = 1;
        for (int i = 0; i < patternLen - 1; i++) {
            highPow = (highPow * BASE) % MOD;
        }

        // Slide the window across the text, one character at a time
        for (int i = 0; i <= textLen - patternLen; i++) {

            // If hashes match, verify actual strings (avoid false positive)
            if (textHash == patternHash) {
                String window = text.substring(i, i + patternLen);
                if (window.equals(pattern)) {
                    return true; // Pattern found in text
                }
            }

            // Slide window: remove leftmost character, add next character
            if (i < textLen - patternLen) {
                // Rolling hash formula:
                // newHash = (oldHash - text[i] * BASE^(len-1)) * BASE + text[i + len]
                textHash = (textHash - text.charAt(i) * highPow % MOD + MOD) % MOD;
                textHash = (textHash * BASE + text.charAt(i + patternLen)) % MOD;
            }
        }

        return false; // Pattern not found
    }

    // ===== Hash Function =====
    // Computes polynomial hash: hash = (c0*BASE^(n-1) + c1*BASE^(n-2) + ... + cn) % MOD
    public long computeHash(String str) {
        long hash = 0;
        for (int i = 0; i < str.length(); i++) {
            hash = (hash * BASE + str.charAt(i)) % MOD;
        }
        return hash;
    }

    // ===== Plagiarism Detection =====
    //
    // Splits text1 into fixed-length word patterns using sliding window,
    // then searches for each pattern in text2 using Rabin-Karp.
    //
    // Parameters:
    //   text1       - first document (used to generate patterns)
    //   text2       - second document (used as text to search in)
    //   windowSize  - number of consecutive words per pattern
    //
    // Returns: list of matching phrases found in both documents
    //
    public List<String> findMatches(String text1, String text2, int windowSize) {
        List<String> matches = new ArrayList<>();

        // Generate patterns from text1 using sliding window over words
        List<String> patterns = generatePatterns(text1, windowSize);

        // Search each pattern in text2 using Rabin-Karp
        for (String pattern : patterns) {
            if (search(text2, pattern)) {
                // Avoid adding duplicate matches
                if (!matches.contains(pattern)) {
                    matches.add(pattern);
                }
            }
        }

        return matches;
    }

    // ===== Generate Patterns using Sliding Window =====
    //
    // Splits text into words, then slides a window of 'windowSize' words.
    //
    // Example: text = "the cat sat on the mat", windowSize = 3
    // Patterns: ["the cat sat", "cat sat on", "sat on the", "on the mat"]
    //
    public List<String> generatePatterns(String text, int windowSize) {
        List<String> patterns = new ArrayList<>();
        String[] words = text.split(" ");

        for (int i = 0; i <= words.length - windowSize; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = i; j < i + windowSize; j++) {
                if (j > i) sb.append(" ");
                sb.append(words[j]);
            }
            patterns.add(sb.toString());
        }

        return patterns;
    }

    // Returns the number of patterns that can be generated from a text
    public int getPatternCount(String text, int windowSize) {
        String[] words = text.split(" ");
        return Math.max(words.length - windowSize + 1, 0);
    }
}
