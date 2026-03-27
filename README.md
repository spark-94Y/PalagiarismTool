# Plagiarism Detection Tool Using String Matching
## Project Documentation

---

## 1. Introduction

### 1.1 What is Plagiarism?

Plagiarism is the act of using someone else's work, ideas, or text without proper attribution, presenting it as one's own original work. In the modern digital era, where information is freely available on the internet, plagiarism has become a widespread issue across academic institutions, research organizations, publishing houses, and content platforms.

Plagiarism can take many forms:
- **Direct Copying:** Copying text word-for-word from another source without quotation marks or citation.
- **Paraphrasing Without Credit:** Rewording someone else's ideas and presenting them as original.
- **Mosaic Plagiarism:** Mixing copied phrases from multiple sources into one's own writing.
- **Self-Plagiarism:** Resubmitting one's own previously submitted work as new.

### 1.2 Need for Automated Plagiarism Detection

Manual detection of plagiarism is impractical for large volumes of text. A document may contain thousands of words, and comparing it against even a small set of other documents becomes a time-consuming task. Automated plagiarism detection tools solve this by using algorithms to compare textual content quickly and quantify the degree of overlap.

The key requirements for such a tool are:
- **Speed:** Able to process and compare large texts efficiently.
- **Accuracy:** Must not flag unrelated content as plagiarized (false positives), nor miss genuinely copied content (false negatives).
- **Scalability:** Should handle increasing document sizes and corpus sizes gracefully.

### 1.3 Project Overview

This project implements a **Plagiarism Detection Tool** in Java that compares two text documents and produces a **similarity percentage** indicating how much content overlaps between them. The tool relies almost entirely on one foundational algorithm from computer science:

1. **Rabin-Karp String Matching Algorithm:** Uses polynomial hashing and a sliding window approach to efficiently find text patterns within a larger document.

The similarity is measured using a **Jaccard-style coefficient**, which expresses the ratio of common patterns to total unique patterns as a percentage.

---

## 2. Problem Statement

> *"Given two text documents, determine the degree of textual similarity between them using efficient string matching algorithms and produce a quantitative similarity score."*

### 2.1 Scope

- Compare exactly **two plain text documents** (.txt format).
- Detect **exact and near-exact textual overlaps** at the phrase level using window-based pattern generation.
- Produce a **percentage score** (0% = completely different, 100% = identical).
- List all **matching phrases** found between the two documents.

### 2.2 Out of Scope

- Synonym detection (e.g., treating "fast" and "quick" as equivalent).
- Paraphrase detection (detecting reworded sentences with same meaning).
- Support for non-text formats (PDF, DOCX, HTML).
- Comparison against a large corpus (only 2-document comparison).

---

## 3. Objectives

1. **Build a functional plagiarism detection tool** that reads two text files and reports a similarity percentage.
2. **Implement the Rabin-Karp algorithm** for efficient hash-based string searching via sliding window architectures.
3. **Use sentence segmenting** to break text into overlapping patterns (consecutive chunks of words) to allow accurate phrase-level comparison.
4. **Apply Jaccard-style similarity** to quantify the degree of textual overlap.
5. **Eliminate false positives** by verifying pattern hash matches with actual string comparison.
6. **Maintain clean, modular code** with separate classes for each distinct responsibility.

---

## 4. Technology Stack

| Component             | Technology              |
|-----------------------|-------------------------|
| Programming Language  | Java (JDK 8+)           |
| Application Type      | Console & Web Application |
| Input Format          | Plain Text Files (.txt)  |
| Build Tool            | Manual compilation (javac) |
| Web Server            | Embedded `HttpServer`   |
| External Dependencies | None (pure Java)         |

### Why Java?

- **Platform Independent:** Java's JVM model allows the tool to run on any operating system without modifications.
- **Rich Standard Library:** Built-in data structures like `HashSet`, `ArrayList`, and `StringBuilder` provide the building blocks needed for the algorithms.
- **No External Dependencies:** The entire project runs on the Java standard library — no need to install third-party libraries, making it simple to compile and deploy.
- **Academic Suitability:** Java's explicit typing and object-oriented structure make the code easy to follow in academic presentations.

---

## 5. System Architecture

### 5.1 High-Level Pipeline

The plagiarism detection process follows a sequential pipeline:

```
INPUT FILES → TEXT PREPROCESSING → RABIN-KARP PATTERN GENERATION & MATCHING
                                                               ↓
        OUTPUT ← SIMILARITY CALCULATION ← MATCHING DETECTION ←┘
```

### 5.2 Detailed Component Interaction

```
┌──────────────────────────────────────────────────────────────────┐
│                      Main.java / WebServer.java                  │
│                     (Orchestrator pipeline)                      │
│                                                                  │
│  ┌─────────────┐   ┌──────────────┐                             │
│  │ FileHandler  │──▶│TextProcessor │                            │
│  │ (Read Files) │   │(Normalize)   │                            │
│  └─────────────┘   └──────────────┘                             │
│                                │                                 │
│                                ▼                                 │
│  ┌───────────────────┐   ┌─────────────────────────────┐        │
│  │SimilarityCalculator│◀──│   RabinKarpMatcher          │        │
│  │(Jaccard Score)     │   │(Sliding Window Generation & │        │
│  └───────────────────┘   │   Hash-Based Search)        │        │
│                          └─────────────────────────────┘        │
└──────────────────────────────────────────────────────────────────┘
```

### 5.3 Step-by-Step Execution Flow

| Step | Action                                | Component              | Input                    | Output                        |
|------|---------------------------------------|------------------------|--------------------------|-------------------------------|
| 1    | Setup parameters                      | `Main.java`           | File paths, window size  | Configuration                 |
| 2    | Read input files                      | `FileHandler.java`    | File paths               | Raw text strings              |
| 3    | Convert to lowercase                  | `TextProcessor.java`  | Raw text                 | Lowercase text                |
| 4    | Remove punctuation & normalize spaces | `TextProcessor.java`  | Lowercase text           | Clean text                    |
| 5    | Generate overlapping patterns         | `RabinKarpMatcher.java`| Clean text, window size  | List of pattern strings        |
| 6    | Rabin-Karp Search                     | `RabinKarpMatcher.java`| Two texts / patterns    | List of matching patterns      |
| 7    | Calculate similarity percentage       | `SimilarityCalculator.java` | Pattern lists, matches | Similarity score (%)         |
| 8    | Display results                       | `Main.java` / Web UI  | Score, matching phrases  | Console/UI output             |

---

## 6. Algorithms — In-Depth Theory

### 6.1 Pattern Generation via Sliding Window

Instead of comparing entire documents which would never match (unless perfectly identical) or individual words which would over-match common words like "the" and "and", the system extracts fixed-length **patterns** comprising consecutive words using a sliding window.

#### What is Sliding Window Substring Generation?

Given a text string, the code splits the string into words, and subsequently combines $N$ chronological contiguous words into a "pattern" string. The window then slides by 1 word, allowing phrase overlap, so no copied sentence portions are missed.

**Input text:** "data structures and algorithms are important" (Size: 3 words)

```
Step 1:
["data structures and"]

Step 2:
["structures and algorithms"]

Step 3:
["and algorithms are"]

Step 4:
["algorithms are important"]
```

#### Why overlapping patterns are necessary

The sliding overlap allows robust searches. If an author plagiarizes text but changes a word before and after the stolen text chunk, the pattern matching natively covers the contiguous sequence because the sub-phrases persist dynamically within the search sets.

---

### 6.2 Rabin-Karp String Matching Algorithm

#### Background

The Rabin-Karp algorithm was invented by **Richard M. Karp** and **Michael O. Rabin** in 1987. It was originally designed for the problem of searching for a pattern string within a larger text string. The key insight is: instead of comparing strings character-by-character (which takes O(n) time per comparison), compute a **numeric hash** of each string and compare the hash values instead (O(1) time per comparison).

#### The Hash Function

This project uses a **polynomial rolling hash function**, which converts a string of characters into a single numeric value.

**Formula:**

For a string `s` of length `n`:
```
hash(s) = ( s[0] × BASE^(n-1) + s[1] × BASE^(n-2) + ... + s[n-1] × BASE^0 ) mod MOD
```

**Parameters:**
- `BASE = 31` — A small prime number used as the polynomial base. Primes reduce the chance of hash collisions.
- `MOD = 1,000,000,007 (10⁹ + 7)` — A large prime used for modular arithmetic to prevent integer overflow and distribute hash values evenly.

#### Why These Specific Values?

- **BASE = 31:** The lowercase English alphabet has 26 letters. Choosing a prime greater than 26 ensures each character assignment gets a unique contribution. The number 31 is commonly used in hash functions (Java's own `String.hashCode()` uses 31).
- **MOD = 10⁹ + 7:** This is a standard large prime in competitive programming. It's small enough to fit in a 64-bit `long` (even when multiplied), but large enough to make collisions extremely rare — probability of collision ≈ 1/10⁹.

#### The Sliding Window Rolling Hash Search

The classic Rabin-Karp optimization natively integrates the **rolling hash**. When tracking a specific pattern sequentially across the master text string, the system slides 1 character at a time. Using the Rolling Hash architecture, the code does not need to recalculate the chunk entirely, which guarantees fast character-traversal computation.

```
Rolling hash formula:
  newHash = (oldHash - outgoingChar × BASE^(k-1)) × BASE + incomingChar

This avoids recomputing the entire hash from scratch.
```

The Plagiarism Tool workflow acts as follows:
1. Divide `Doc1` into fixed-length phrase patterns (e.g. 3-word combinations) using word-level sliding windows.
2. For each resulting Pattern, execute the `RabinKarp.search(Doc2, Pattern)` operation.
3. The function calculates the hash of the pattern, then generates a character-level sliding window traversing `Doc2` to find matches securely and swiftly.

#### Why Is Verification Needed?

Hash collisions occur when two different strings produce the same hash value. Although our hash function makes collisions rare, they can happen mathematically.

```
Example (hypothetical):
  hash("algorithms are") = 587291043
  hash("completely new")  = 587291043   ← COLLISION!

  Hash match? YES
  String match? NO → Not a real match → Correctly rejected ✓
```

As a result, if a hash-to-hash match is validated, `Window == Pattern` strings are evaluated via native `.equals()` methods to eliminate false assumptions.

---

### 6.3 Jaccard-Style Similarity Coefficient

#### The Concept

The Jaccard index (also known as Intersection over Union, or IoU) is a statistic used to measure the similarity between two sets. It is defined as:

```
J(A, B) = |A ∩ B| / |A ∪ B|
```

Where:
- `A ∩ B` = elements common to both sets (intersection)
- `A ∪ B` = all unique elements from both sets (union)

#### Application in This Project

```
Similarity(%) = (Number of matched patterns / Total unique patterns from both files) × 100
```

#### Detailed Example

```
File 1 patterns: {"data structures and", "structures and algorithms", "and algorithms are"}
File 2 patterns: {"data structures and", "structures and algorithms", "algorithms are cool"}

Intersection (common): {"data structures and", "structures and algorithms"}
  → Count = 2

Union (all unique):    {"data structures and", "structures and algorithms", "and algorithms are", "algorithms are cool"}
  → Count = 4

Similarity = (2 / 4) × 100 = 50.0%
```

#### Interpretation of Scores

| Score Range | Interpretation |
|-------------|---------------|
| 0% - 10%   | No significant overlap; documents are independent |
| 10% - 30%  | Low overlap; likely coincidental common phrases |
| 30% - 50%  | Moderate overlap; some passages may be shared |
| 50% - 70%  | High overlap; significant portions appear copied |
| 70% - 90%  | Very high overlap; likely plagiarism |
| 90% - 100% | Near-identical or identical documents |

---

## 7. Project Structure — Module Details

### 7.1 File Structure

```
PlagiarismDetector/
│
├── Main.java                 → Console entry point
├── WebServer.java            → Web Interface Server with built-in HTTPServer
├── FileHandler.java          → Reads text files into strings
├── TextProcessor.java        → Normalizes text (lowercase, remove punctuation)
├── RabinKarpMatcher.java     → Generates word-level patterns, executes hash rolling search
├── SimilarityCalculator.java → Computes Jaccard-style similarity percentage
│
└── index.html                → Web UI Frontend page
```

### 7.2 Module Responsibilities

#### FileHandler.java
- **Responsibility:** Read a text file from disk and return its full content as a single string.
- **Key method:** `readFile(String path) → String`
- **Implementation details:** Uses `BufferedReader` wrapped around `FileReader` for stable IO loading natively.

#### TextProcessor.java
- **Responsibility:** Normalize raw text for consistent comparison.
- **Key methods:** `toLowerCase()` and `removePunctuation()`.
- **Why normalization is critical:** Without it, `"Data."` and `"data"` would be treated as different words, leading to missed matches.

#### RabinKarpMatcher.java
- **Responsibility:** Employs the native Rabin-Karp rolling hash searches to find matching texts across substrings.
- **Key methods:**
  - `computeHash(String str) → long`: Polynomial hashing.
  - `search(String text, String pattern) → boolean`: Classic algorithm parsing character-by-character mathematically.
  - `findMatches(String text1, String text2, int windowSize)`: Aggregates the processes logic to compile phrase arrays and validate against texts.

#### SimilarityCalculator.java
- **Responsibility:** Compute the final similarity percentage using native HashSets.
- **Key method:** `calculateSimilarity(List<String> p1, List<String> p2, List<String> matches)`

#### Main.java / WebServer.java
- **Main:** Terminal interface. Automatically evaluates strings imported from testing files via `System.out.println`.
- **WebServer:** Java Http server mapping to `localhost:8080/analyze`. Binds form-file JSON inputs into the architecture logic to provide visual gauge dashboards dynamically for modern UI implementations.

---

## 8. Design Decisions & Trade-offs

| Decision | Choice | Rationale |
|----------|--------|-----------|
| Algorithm Strategy | **Strict Rabin-Karp** | Standard computer science theory integration, using the authentic array-search implementation instead of hashed K-Grams, staying completely true to the Karp mathematical paper principles. |
| Pattern Generation | **3-Word Windows** | Extracting consecutive multi-word fragments allows granular pattern identification without relying on individual noun/verb keywords which would spawn false assertions. |
| Hash function | **Polynomial (base 31, mod 10⁹+7)** | Standard, well-distributed hash function. Same base used by Java's `String.hashCode()`. |
| Collision handling | **Hash + String verification** | Completely eliminates false positives at minimal extra cost. |
| Similarity metric | **Jaccard coefficient** | Well-established set similarity measure. Intuitive percentage output. |

---

## 9. Advantages and Limitations

### Advantages

1. **Academic Alignment:** Relies exclusively on authentic Rabin-Karp character rolling evaluations exactly aligned to standardized computing curriculums.
2. **Zero False Positives:** Double verification (hash match + string match) ensures that only genuine duplicates are reported.
3. **Modular Architecture:** Five separate Java classes, each with a single responsibility. Easy to understand, test, modify, and extend independently.
4. **No External Dependencies:** Uses only Java standard library. Compiles and runs with just `javac` and `java`.
5. **Configurable Granularity:** The sliding-window pattern length can be adjusted to trade off between sensitivity and specificity based on use-case size.

### Limitations

1. **No Synonym Detection:** The word "quick" and the word "fast" have the same meaning but will not be matched since the tool compares exact text only.
2. **No Paraphrase Detection:** If a sentence is reworded with the same meaning, it will not be detected. For example, "The cat chased the mouse" and "The mouse was chased by the cat" would not match.
3. **Plain Text Only:** Does not support PDF, DOCX, HTML, or other rich document formats seamlessly.
4. **Language-Agnostic:** The tool does not understand grammar, sentence structure, or language semantics — it purely compares text patterns statistically.

---

## 10. How to Run the Project

### Prerequisites
- Java Development Kit (JDK) version 8 or later installed.
- A terminal or command prompt.

### Web Server Application (Recommended)

```bash
# Navigate to the project directory
cd PlagiarismDetector

# Compile all Java source files
javac *.java

# Run the WebServer endpoint
java WebServer
```
- Open `http://localhost:8080` in your Google Chrome or Mozilla browser to utilize the dashboard visualizer intuitively.

### Terminal Usage

```bash
# Set input1.txt and input2.txt locally

# Run the console pipeline
java Main
```

---

## 11. References

1. Karp, R.M. and Rabin, M.O. (1987). *Efficient Randomized Pattern-Matching Algorithms*. IBM Journal of Research and Development, 31(2), pp.249-260.
2. Cormen, T.H., Leiserson, C.E., Rivest, R.L. and Stein, C. (2009). *Introduction to Algorithms* (3rd Edition), Chapter 32: String Matching. MIT Press.
3. GeeksForGeeks — Rabin-Karp Algorithm for Pattern Searching. https://www.geeksforgeeks.org/rabin-karp-algorithm-for-pattern-searching/

---

*Project Documentation — Plagiarism Detection Tool Using String Matching*
