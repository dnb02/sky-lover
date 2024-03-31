import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;
import java.net.*;

public class MainScreen extends JFrame {

    private JTextField ipAddressField; // New field to input IP address

    public MainScreen() {
        setTitle("Main Screen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout());

        // Panel for IP address input
        JPanel ipPanel = new JPanel(new FlowLayout());
        JLabel ipLabel = new JLabel("Server IP Address:");
        ipAddressField = new JTextField(15);
        ipPanel.add(ipLabel);
        ipPanel.add(ipAddressField);

        // Panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton button1 = new JButton("Button 1");
        JButton button2 = new JButton("Button 2");
        JButton button3 = new JButton("Button 3");
        buttonPanel.add(button1);
        buttonPanel.add(button2);
        buttonPanel.add(button3);

        // Add action listeners to buttons
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                connectToAPODService();
            }
        });

        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openSolarPositionCalculator(ipAddressField.getText().trim());
            }
        });

        button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                connectToNewsScraperService();
            }
        });

        // Add panels to frame
        add(ipPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
    }

    private void connectToAPODService() {
        try {
            String ipAddress = ipAddressField.getText().trim(); // Get IP address from input field
            Socket socket = new Socket(ipAddress, 12345);

            // Send a request to the server
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject("APOD_REQUEST");
            out.flush();

            // Receive APOD data from the server
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            APOD apod = (APOD) in.readObject();

            // Display received APOD data
            displayAPOD(apod);

            // Close resources
            in.close();
            out.close();
            socket.close();
        } catch (ConnectException e) {
        JOptionPane.showMessageDialog(MainScreen.this, "Server not found. Please check the IP address.", "Connection Error", JOptionPane.ERROR_MESSAGE);
    }catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(MainScreen.this, "Error fetching APOD data!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void connectToNewsScraperService() {
        try {
            String ipAddress = ipAddressField.getText().trim(); // Get IP address from input field
            Socket socket = new Socket(ipAddress, 12346);

            // Send a request to the server
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject("NEWS_REQUEST");
            out.flush();

            // Receive scraped headlines from the server
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            List<String> headlines = (List<String>) in.readObject();

            // Display scraped headlines in a new window
            NewsScraperGui newsScraperGui = new NewsScraperGui(headlines);
            newsScraperGui.setVisible(true);

            // Close resources
            in.close();
            out.close();
            socket.close();
        } catch (ConnectException e) {
        JOptionPane.showMessageDialog(MainScreen.this, "Server not found. Please check the IP address.", "Connection Error", JOptionPane.ERROR_MESSAGE);
    }catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(MainScreen.this, "Error fetching news!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openSolarPositionCalculator(String ipAdd) {
        SolarPositionCalculator calculator = new SolarPositionCalculator(ipAdd);
        calculator.setVisible(true);
    }

    private void displayAPOD(APOD apod) {
        // Create an ApodGui instance
        ApodGui apodGui = new ApodGui();
        apodGui.displayImage(apod.getHdurl());
        apodGui.displayExplanation(apod.getExplanation());
        apodGui.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainScreen().setVisible(true);
            }
        });
    }
}

