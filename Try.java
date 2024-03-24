
import java.io.*;
import kong.unirest.core.*;
import com.google.gson.*;

public class Try {
    public static void main(String[] args) {
        try {
            HttpResponse<String> result = Unirest.get("https://apod.ellanan.com/api").asString();
            String jsonString = result.getBody();

            Gson gson = new Gson();
            APOD apod = gson.fromJson(jsonString, APOD.class);

            // Now you can use the APOD object
            System.out.println("Title: " + apod.getTitle());
            System.out.println("Explanation: " + apod.getExplanation());
            // Similarly, print other attributes as needed
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
