import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class APODService {
    public APOD fetchAPOD() throws IOException {
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

