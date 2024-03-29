import java.io.*;
import java.net.*;
import java.util.List;

public class NewsScraperServer {
    public static void main(String[] args) {
        final int PORT = 12346; // Choose any available port

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("News Scraper Server started. Waiting for requests...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket);

                // Create a new thread to handle each client request
                Thread clientHandler = new Thread(new ClientHandler(clientSocket));
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                // Receive request from client
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                in.readObject(); // Just read the request, no need to use it

                // Scrape news headlines
                List<String> headlines = scrapeNews();

                // Send scraped headlines to client
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                out.writeObject(headlines);
                out.flush();

                // Close resources
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        private List<String> scrapeNews() {
            // Your existing scraping logic goes here
            // Just make sure it returns a List<String> of headlines
            NewsScraper newsScraper = new NewsScraper();
            try {
                return newsScraper.getHeadlines("https://phys.org/space-news/astronomy/");
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
