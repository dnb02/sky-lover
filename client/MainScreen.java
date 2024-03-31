import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;
import java.net.*;

public class MainScreen extends JFrame {

    public MainScreen() {
        setTitle("Main Screen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null); // Center the window
        setLayout(new FlowLayout());

        JButton button1 = new JButton("Button 1");
        JButton button2 = new JButton("Button 2");
        JButton button3 = new JButton("Button 3");

        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // Connect to APODService server
                    Socket socket = new Socket("10.110.11.34", 12345);

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
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(MainScreen.this, "Error fetching APOD data!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open the SolarPositionCalculator GUI
                //openSolarPositionCalculatorGUI();
            }
        });

        button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // Connect to NewsScraperServer
                    Socket socket = new Socket("10.110.11.34", 12346);
        
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
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(MainScreen.this, "Error fetching news!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        add(button1);
        add(button2);
        add(button3);
    }

    private void openSolarPositionCalculator() {
        SolarPositionCalculator calculator = new SolarPositionCalculator();
        calculator.setVisible(true);
    }

    private void displayAPOD(APOD apod) {
        // Create an ApodGui instance
        ApodGui apodGui = new ApodGui();
        apodGui.displayImage(apod.getHdurl());
        apodGui.displayExplanation(apod.getExplanation());
        apodGui.setVisible(true);
    }

    private void displayNews(List<String> headlines) {
        // Create a new window to display news headlines
        JFrame newsFrame = new JFrame("Astronomy News Headlines");
        newsFrame.setSize(400, 300);
        newsFrame.setLayout(new BorderLayout());
        newsFrame.setLocationRelativeTo(null); // Center the window

        JTextArea newsTextArea = new JTextArea();
        newsTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(newsTextArea);

        // Append each headline to the text area
        for (String headline : headlines) {
            newsTextArea.append(headline + "\n");
        }

        newsFrame.add(scrollPane, BorderLayout.CENTER);
        newsFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainScreen().setVisible(true);
            }
        });
    }
}

