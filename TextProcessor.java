public class TextProcessor {

    // Converts text to lowercase
    // "Hello World" -> "hello world"
    public String toLowerCase(String text) {
        return text.toLowerCase();
    }

    // Removes punctuation and extra spaces
    // "Hello, World!" -> "hello world"
    public String removePunctuation(String text) {
        // Remove everything except letters, digits, and spaces
        String cleaned = text.replaceAll("[^a-zA-Z0-9 ]", "");

        // Replace multiple spaces with single space
        cleaned = cleaned.replaceAll("\\s+", " ").trim();

        return cleaned;
    }
}
