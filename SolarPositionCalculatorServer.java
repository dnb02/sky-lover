import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import net.e175.klaus.solarpositioning.*;
import java.time.ZonedDateTime;

public class SolarPositionCalculatorServer {
    public static void main(String[] args) {
        final int PORT = 12347;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("SolarPositionCalculatorServer started. Waiting for requests...");

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
                // Receive Spos object from the client
                ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
                Spos spos = (Spos) inputStream.readObject();
                System.out.println("Received Spos object from client: " + spos);

                // Calculate sun position and update Spos object
                calculateSunPosition(spos);

                // Send updated Spos object back to the client
                ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                outputStream.writeObject(spos);
                outputStream.flush();
                inputStream.close();
                outputStream.close();
                System.out.println("Sent updated Spos object back to client: " + spos);

                // Close client socket
                clientSocket.close();
            } catch (IOException | ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }

        private void calculateSunPosition(Spos spos) throws SQLException {
            double latitude = spos.getLatitude();
            double longitude = spos.getLongitude();
            double elevation = spos.getElevation();
            double pressure = spos.getAirPressure();
            double temperature = spos.getTemperature();
			
            ZonedDateTime dateTime = ZonedDateTime.now();

            // Calculate solar position
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
}

