import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import com.google.gson.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class Gui extends JFrame {
    private JButton getAPODButton;
    private JLabel imageLabel;
    private JTextArea explanationArea;

    public Gui() {
        setTitle("Welcome, lovers of the sky");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Set to full-screen mode
        setUndecorated(false); // Show title bar and borders

        // Create components
        JLabel titleLabel = new JLabel("APOD");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        
        JLabel instructionLabel = new JLabel("Click the button below to get the astronomy picture of the day");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        instructionLabel.setHorizontalAlignment(JLabel.CENTER);

        getAPODButton = new JButton("Click Me");

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);

        explanationArea = new JTextArea();
        explanationArea.setEditable(false);
        explanationArea.setLineWrap(true);
        explanationArea.setWrapStyleWord(true);
        explanationArea.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(explanationArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Set up the layout
        setLayout(new BorderLayout());
        add(titleLabel, BorderLayout.NORTH);
        add(instructionLabel, BorderLayout.CENTER);
        add(imageLabel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
        add(getAPODButton, BorderLayout.PAGE_END);

        // Add action listener to the button
        getAPODButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Fetch APOD data from API
                    APOD apod = fetchAPOD();
                    
                    // Display image and explanation
                    displayImage(apod.getHdurl());
                    displayExplanation(apod.getExplanation());
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(Gui.this, "Error fetching APOD data!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private APOD fetchAPOD() throws IOException {
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

    private void displayImage(String imageUrl) throws IOException {
        // Fetch the image from URL
        URL url = new URL(imageUrl);
        BufferedImage image = ImageIO.read(url);

        // Display the image
        imageLabel.setIcon(new ImageIcon(image));
    }

    private void displayExplanation(String explanation) {
        explanationArea.setText(explanation);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Gui gui = new Gui();
                gui.setVisible(true);
            }
        });
    }
}

