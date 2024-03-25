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
 		APOD processedApod;
                if (apod.getMediaType().equals("image")) {
                    processedApod = new APODPic(apod.getTitle(), apod.getExplanation(), apod.getDate(), apod.getHdurl(), apod.getServiceVersion(), apod.getCopyright(), apod.getMediaType(), apod.getUrl());
                } else if (apod.getMediaType().equals("video")) {
                    processedApod = new APODVideo(apod.getTitle(), apod.getExplanation(), apod.getDate(), apod.getHdurl(), apod.getServiceVersion(), apod.getCopyright(), apod.getMediaType(), apod.getUrl());
                } else {
                    // Handle other media types if needed
                    processedApod = apod;
                }
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

       // API URL
        String apiUrl = "https://apod.ellanan.com/api";

        // Make HTTP request to fetch APOD data
        HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
        connection.setRequestMethod("GET");

        // Read response
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        // Parse JSON response
        Gson gson = new Gson();
        return gson.fromJson(response.toString(), APOD.class);
        }
    }
}

