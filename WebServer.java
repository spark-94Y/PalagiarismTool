import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Simple HTTP server for the Plagiarism Detector web UI.
 *
 * Endpoints:
 * GET / → Serves index.html
 * POST /analyze → Receives two texts, runs plagiarism detection, returns JSON
 *
 * Uses Java's built-in HttpServer — no external dependencies needed.
 *
 * Usage:
 * javac *.java
 * java WebServer
 * Open http://localhost:8080 in your browser
 */
public class WebServer {

    private static final int PORT = 8080;
    private static final int K = 2; // k-gram size (2 words)

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // Serve the HTML page
        server.createContext("/", new PageHandler());

        // Handle plagiarism analysis requests
        server.createContext("/analyze", new AnalyzeHandler());

        server.start();
        System.out.println("==============================");
        System.out.println(" Plagiarism Detector Server");
        System.out.println("==============================");
        System.out.println("Server running at: http://localhost:" + PORT);
        System.out.println("Press Ctrl+C to stop.");
    }

    // ===== Handler: Serve index.html =====
    static class PageHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                // Read index.html file
                byte[] htmlBytes = Files.readAllBytes(Paths.get("index.html"));
                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                exchange.sendResponseHeaders(200, htmlBytes.length);
                OutputStream os = exchange.getResponseBody();
                os.write(htmlBytes);
                os.close();
            } else {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        }
    }

    // ===== Handler: Analyze two texts =====
    static class AnalyzeHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Add CORS header
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");

            if ("POST".equals(exchange.getRequestMethod())) {
                // Read the request body
                String body = new String(exchange.getRequestBody().readAllBytes(), "UTF-8");

                // Parse the two texts from form data
                String text1 = "";
                String text2 = "";

                String[] parts = body.split("&");
                for (String part : parts) {
                    String[] keyValue = part.split("=", 2);
                    if (keyValue.length == 2) {
                        String key = URLDecoder.decode(keyValue[0], "UTF-8");
                        String value = URLDecoder.decode(keyValue[1], "UTF-8");
                        if ("text1".equals(key)) {
                            text1 = value;
                        } else if ("text2".equals(key)) {
                            text2 = value;
                        }
                    }
                }

                // Run the plagiarism detection pipeline
                String json = runAnalysis(text1, text2);

                // Send JSON response
                byte[] responseBytes = json.getBytes("UTF-8");
                exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
                exchange.sendResponseHeaders(200, responseBytes.length);
                OutputStream os = exchange.getResponseBody();
                os.write(responseBytes);
                os.close();

            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }

    // ===== Plagiarism Detection Pipeline =====
    static String runAnalysis(String rawText1, String rawText2) {

        // Step 1: Preprocess text
        TextProcessor processor = new TextProcessor();
        String text1 = processor.toLowerCase(rawText1);
        text1 = processor.removePunctuation(text1);
        String text2 = processor.toLowerCase(rawText2);
        text2 = processor.removePunctuation(text2);

        // Step 2: Generate k-grams
        KGramGenerator generator = new KGramGenerator();
        List<String> kGrams1 = generator.generateKGrams(text1, K);
        List<String> kGrams2 = generator.generateKGrams(text2, K);

        // Step 3: Find matches using Rabin-Karp
        RabinKarpMatcher matcher = new RabinKarpMatcher();
        List<String> matchingKGrams = matcher.findMatches(kGrams1, kGrams2);

        // Step 4: Calculate similarity
        SimilarityCalculator calculator = new SimilarityCalculator();
        double similarity = calculator.calculateSimilarity(kGrams1, kGrams2, matchingKGrams);

        // Build JSON response manually (no external library needed)
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"similarity\":").append(similarity).append(",");
        json.append("\"kGrams1Count\":").append(kGrams1.size()).append(",");
        json.append("\"kGrams2Count\":").append(kGrams2.size()).append(",");
        json.append("\"matchingPhrases\":[");

        for (int i = 0; i < matchingKGrams.size(); i++) {
            if (i > 0)
                json.append(",");
            json.append("\"").append(escapeJson(matchingKGrams.get(i))).append("\"");
        }

        json.append("]");
        json.append("}");

        return json.toString();
    }

    // Escape special characters for JSON
    static String escapeJson(String text) {
        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
