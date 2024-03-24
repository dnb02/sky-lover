import com.google.gson.Gson;
import java.io.*;
import java.net.*;

public class APODService {
    public static void main(String[] args) {
        final int PORT = 12345; // Change this to the desired port number
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("APODService started. Waiting for requests...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket);

                // Create a new thread to handle each client request
                Thread clientHandler = new Thread(new ClientHandler(clientSocket));
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
                // Fetch APOD data
                APOD apod = fetchAPOD();

                // Send APOD data to client
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                out.writeObject(apod);
                out.flush();
                System.out.println("Sent APOD data to client: " + apod.getTitle());

                // Close resources
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private APOD fetchAPOD() throws IOException {
            // Your existing fetchAPOD() logic goes here
            // Just make sure it returns an APOD object
            return new APOD("Sample Title", "Sample Explanation", "2024-03-24", "https://example.com/image.jpg", "1.0", "Sample Copyright", "image", "https://example.com");
        }
    }
}

