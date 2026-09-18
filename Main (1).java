import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;

/*
 * Main.java - a tiny web server that serves index.html (and any other
 * files placed in the same folder) as a website.
 *
 * No frameworks needed - just Java's built-in HttpServer.
 *
 * Run:
 *   javac Main.java
 *   java Main
 * Then open http://localhost:8080/ in a browser.
 *
 * Make sure index.html is in the SAME FOLDER as Main.java.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new StaticFileHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server running at http://localhost:8080/");
    }

    static class StaticFileHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            if (path.equals("/")) path = "/index.html";

            File baseDir = new File(".").getCanonicalFile();
            File file = new File(baseDir, path).getCanonicalFile();

            // Basic safety check: don't allow escaping the base folder
            boolean safe = file.getPath().startsWith(baseDir.getPath());

            if (!safe || !file.exists() || file.isDirectory()) {
                byte[] notFound = "404 Not Found".getBytes();
                exchange.getResponseHeaders().set("Content-Type", "text/plain");
                exchange.sendResponseHeaders(404, notFound.length);
                OutputStream os = exchange.getResponseBody();
                os.write(notFound);
                os.close();
                return;
            }

            byte[] bytes = Files.readAllBytes(file.toPath());
            exchange.getResponseHeaders().set("Content-Type", guessContentType(file.getName()));
            exchange.sendResponseHeaders(200, bytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
        }

        static String guessContentType(String filename) {
            if (filename.endsWith(".html")) return "text/html; charset=utf-8";
            if (filename.endsWith(".css")) return "text/css";
            if (filename.endsWith(".js")) return "application/javascript";
            if (filename.endsWith(".png")) return "image/png";
            if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) return "image/jpeg";
            if (filename.endsWith(".svg")) return "image/svg+xml";
            return "application/octet-stream";
        }
    }
}
