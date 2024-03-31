import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import net.e175.klaus.solarpositioning.*;
import java.time.ZonedDateTime;

public class SolarPositionCalculator extends JFrame {
    private JLabel latitudeLabel, longitudeLabel, elevationLabel, pressureLabel, temperatureLabel;
    private JTextField latitudeField, longitudeField, elevationField, pressureField, temperatureField;
    private JButton calculateButton;
    private JTextArea resultArea;
    private JComboBox<String> cityComboBox;

    // Default values for elevation, air pressure, and air temperature
    private static final double DEFAULT_ELEVATION = 100.0; // meters
    private static final double DEFAULT_PRESSURE = 1013.25; // hPa
    private static final double DEFAULT_TEMPERATURE = 20.0; // °C

    public SolarPositionCalculator() {
        setTitle("Solar Position Calculator");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize components
        latitudeLabel = new JLabel("Latitude:");
        longitudeLabel = new JLabel("Longitude:");
        elevationLabel = new JLabel("Elevation (m):");
        pressureLabel = new JLabel("Average Air Pressure (hPa):");
        temperatureLabel = new JLabel("Average Air Temperature (°C):");

        latitudeField = new JTextField(10);
        longitudeField = new JTextField(10);
        elevationField = new JTextField(String.valueOf(DEFAULT_ELEVATION), 10); // Set default elevation
        pressureField = new JTextField(String.valueOf(DEFAULT_PRESSURE), 10); // Set default pressure
        temperatureField = new JTextField(String.valueOf(DEFAULT_TEMPERATURE), 10); // Set default temperature

        calculateButton = new JButton("Calculate");
        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);

        // Initialize cityComboBox and populate it with city names
        cityComboBox = new JComboBox<>();
        populateCities();

        // Layout
        setLayout(new GridLayout(8, 2));

        add(new JLabel("City:"));
        add(cityComboBox);
        add(latitudeLabel);
        add(latitudeField);
        add(longitudeLabel);
        add(longitudeField);
        add(elevationLabel);
        add(elevationField);
        add(pressureLabel);
        add(pressureField);
        add(temperatureLabel);
        add(temperatureField);
        add(calculateButton);
        add(new JScrollPane(resultArea));

        // Action listener for the Calculate button
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateAndDisplaySolarPosition();
            }
        });

        // Add action listener to cityComboBox to fill latitude and longitude fields
        cityComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fillCoordinates();
            }
        });
    }

    private void fillCoordinates() {
        try {
            // Connect to the SQLite database
            Connection conn = DriverManager.getConnection("jdbc:sqlite:cities.db");
            Statement stmt = conn.createStatement();

            // Get selected city name
            String selectedCity = (String) cityComboBox.getSelectedItem();

            // Execute query to get coordinates of the selected city
            ResultSet rs = stmt.executeQuery("SELECT latitude, longitude FROM cities WHERE name = '" + selectedCity + "'");

            // Display coordinates in the text fields
            if (rs.next()) {
                double latitude = rs.getDouble("latitude");
                double longitude = rs.getDouble("longitude");
                latitudeField.setText(String.valueOf(latitude));
                longitudeField.setText(String.valueOf(longitude));
            }

            // Close connections
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void calculateAndDisplaySolarPosition() {
        try {
            double latitude = Double.parseDouble(latitudeField.getText());
            double longitude = Double.parseDouble(longitudeField.getText());
            double elevation = Double.parseDouble(elevationField.getText());
            double pressure = Double.parseDouble(pressureField.getText());
            double temperature = Double.parseDouble(temperatureField.getText());

            // Create Spos object with input data
            Spos spos = new Spos(latitude, longitude, pressure, elevation, temperature, "");

            // Connect to the Solar Position Calculator Server
            try (Socket socket = new Socket("10.110.11.34", 12347)) {
                System.out.println("Connected to server.");

                // Send Spos object to the server
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(spos);
                outputStream.flush();
                System.out.println("Sent Spos object to server.");

                // Receive updated Spos object from the server
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                Spos updatedSpos = (Spos) inputStream.readObject();
                System.out.println("Received updated Spos object from server.");

                // Display solar position in the result area
                resultArea.setText(updatedSpos.getSunpos());

                // Close resources
                //inputStream.close();
                //outputStream.close();
	} catch (ConnectException ex) {
	    ex.printStackTrace();
	    JOptionPane.showMessageDialog(this, "Cannot connect to server. Please ensure the server is up and running.", "Connection Error", JOptionPane.ERROR_MESSAGE);
	} catch (IOException | ClassNotFoundException ex) {
	    ex.printStackTrace();
	    JOptionPane.showMessageDialog(this, "Error communicating with server.", "Error", JOptionPane.ERROR_MESSAGE);
	}

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter numbers only.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void populateCities() {
        try {
            // Connect to the SQLite database
            Connection conn = DriverManager.getConnection("jdbc:sqlite:cities.db");
            Statement stmt = conn.createStatement();

            // Execute query to get cities
            ResultSet rs = stmt.executeQuery("SELECT name FROM cities");

            // Populate the combo box with city names
            while (rs.next()) {
                String cityName = rs.getString("name");
                cityComboBox.addItem(cityName);
            }

            // Close connections
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading cities from database!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SolarPositionCalculator().setVisible(true);
            }
        });
    }
}

