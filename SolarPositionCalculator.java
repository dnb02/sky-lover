import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.time.ZonedDateTime;
import net.e175.klaus.solarpositioning.*;

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

        // Populate city names in the combo box
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
                calculateSolarPosition();
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
        }
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

    private void calculateSolarPosition() {
        try {
            double latitude = Double.parseDouble(latitudeField.getText());
            double longitude = Double.parseDouble(longitudeField.getText());
            double elevation = Double.parseDouble(elevationField.getText());
            double pressure = Double.parseDouble(pressureField.getText());
            double temperature = Double.parseDouble(temperatureField.getText());

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

            resultArea.setText("Solar Position:\n" + position.toString());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter numbers only.", "Error", JOptionPane.ERROR_MESSAGE);
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

