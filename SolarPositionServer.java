import java.io.*;
import java.net.*;
import java.time.ZonedDateTime;
import net.e175.klaus.solarpositioning.*;

public class SolarPositionServer {
    public static void main(String[] args) {
        final int PORT = 12347; // Choose a port number
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Solar Position Server started. Waiting for requests...");

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
                // Receive data from client
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                double latitude = in.readDouble();
                double longitude = in.readDouble();
                double elevation = in.readDouble();
                double pressure = in.readDouble();
                double temperature = in.readDouble();

                // Calculate solar position
                ZonedDateTime dateTime = ZonedDateTime.now();
                SolarPosition position = SPA.calculateSolarPosition(
                        dateTime,
                        latitude,
                        longitude,
                        elevation,
                        DeltaT.estimate(dateTime.toLocalDate()),
                        pressure,
                        temperature
                );

                // Send result back to client
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                out.writeObject(position);
                out.flush();

                // Close resources
                out.close();
                in.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
