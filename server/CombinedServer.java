import com.google.gson.Gson;
import net.e175.klaus.solarpositioning.*;
import java.time.ZonedDateTime;
import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.List;

public class CombinedServer {
    public static void main(String[] args) {
        final int APOD_PORT = 12345;
        final int NEWS_PORT = 12346;
        final int SUN_POS_PORT = 12347;

        // Start APOD service
        startService(APOD_PORT, "APODService", CombinedServer::handleAPODRequest);

        // Start News Scraper service
        startService(NEWS_PORT, "NewsScraperService", CombinedServer::handleNewsRequest);

        // Start Sun Position Calculator service
        startService(SUN_POS_PORT, "SolarPositionCalculatorService", CombinedServer::handleSunPosRequest);
    }

    private static void startService(int port, String serviceName, ServiceHandler handler) {
        Thread serviceThread = new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                System.out.println(serviceName + " started. Waiting for requests...");

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connected to " + serviceName + ": " + clientSocket);

                    // Create a new thread to handle each client request
                    Thread clientHandler = new Thread(() -> handler.handle(clientSocket));
                    clientHandler.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        serviceThread.start();
    }

    interface ServiceHandler {
        void handle(Socket clientSocket);
    }

    private static void handleAPODRequest(Socket clientSocket) {
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

    private static APOD fetchAPOD() throws IOException {
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
        APOD apod = gson.fromJson(response.toString(), APOD.class);
        if (apod.getHdurl() != null && !apod.getHdurl().isEmpty()) {
            apod.setMediaType("image");
        } else {
            apod.setMediaType("video");
        }
        return apod;
    }

    private static void handleNewsRequest(Socket clientSocket) {
        try {
            // Scrape news headlines
            List<String> headlines = scrapeNews();

            // Send scraped headlines to client
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.writeObject(headlines);
            out.flush();

            // Close resources
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> scrapeNews() {
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

    private static void handleSunPosRequest(Socket clientSocket) {
        try {
            // Receive Spos object from the client
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            Spos spos = (Spos) inputStream.readObject();

            // Calculate sun position and update Spos object
            calculateSunPosition(spos);

            // Send updated Spos object back to the client
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            outputStream.writeObject(spos);
            outputStream.flush();
            inputStream.close();
            outputStream.close();

            // Close client socket
            clientSocket.close();
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static void calculateSunPosition(Spos spos) throws SQLException {
        double latitude = spos.getLatitude();
        double longitude = spos.getLongitude();
        double elevation = spos.getElevation();
        double pressure = spos.getAirPressure();
        double temperature = spos.getTemperature();

        var dateTime = ZonedDateTime.now();

        // replace SPA with Grena3 as needed
        var position = SPA.calculateSolarPosition(
                dateTime,
                latitude, // latitude (degrees)
                longitude, // longitude (degrees)
                elevation, // elevation (m)
                DeltaT.estimate(dateTime.toLocalDate()), // delta T (s)
                pressure, // avg. air pressure (hPa)
                temperature); // avg. air temperature (°C)

        String sunPosition = position.toString();

        // Update Spos object with the calculated sun position
        spos.setSunpos(sunPosition);
    }
}

