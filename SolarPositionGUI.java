import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class SolarPositionGUI extends JFrame {
    private JLabel latitudeLabel, longitudeLabel, elevationLabel, pressureLabel, temperatureLabel;
    private JTextField latitudeField, longitudeField, elevationField, pressureField, temperatureField;
    private JButton calculateButton;
    private JTextArea resultArea;

    public SolarPositionGUI() {
        setTitle("Solar Position Calculator");
        setSize(500, 400);
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
        elevationField = new JTextField(10);
        pressureField = new JTextField(10);
        temperatureField = new JTextField(10);

        calculateButton = new JButton("Calculate");
        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);

        // Layout
        setLayout(new GridLayout(6, 2));
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
                sendRequest();
            }
        });
    }

    private void sendRequest() {
        try {
            double latitude = Double.parseDouble(latitudeField.getText());
            double longitude = Double.parseDouble(longitudeField.getText());
            double elevation = Double.parseDouble(elevationField.getText());
            double pressure = Double.parseDouble(pressureField.getText());
            double temperature = Double.parseDouble(temperatureField.getText());

            // Connect to the server
            Socket socket = new Socket("localhost", 12347); // Use the same port number as the server

            // Send data to the server
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeDouble(latitude);
            out.writeDouble(longitude);
            out.writeDouble(elevation);
            out.writeDouble(pressure);
            out.writeDouble(temperature);
            out.flush();

            // Receive result from the server
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            SolarPosition position = (SolarPosition) in.readObject();
            resultArea.setText("Solar Position:\n" + position.toString());

            // Close resources
            out.close();
            in.close();
            socket.close();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error calculating solar position!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SolarPositionGUI().setVisible(true);
            }
        });
    }
}
