import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.URL;
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
        JLabel titleLabel = new JLabel("Astronomy Picture of the day!");
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
                    // Connect to APODService server
                    Socket socket = new Socket("localhost", 12345);

                    // Send a request to the server
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    out.writeObject("APOD_REQUEST");
                    out.flush();

                    // Receive APOD data from the server
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    APOD apod = (APOD) in.readObject();

                    // Display received APOD data
                    displayImage(apod.getHdurl());
                    displayExplanation(apod.getExplanation());

                    // Close resources
                    in.close();
                    out.close();
                    socket.close();
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(Gui.this, "Error fetching APOD data!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void displayImage(String imageUrl) {
        try {
            // Fetch the image from URL
            ImageIcon imageIcon = new ImageIcon(new URL(imageUrl));
            Image image = imageIcon.getImage();
            Image scaledImage = image.getScaledInstance(500, -1, Image.SCALE_SMOOTH);

            // Display the image
            imageLabel.setIcon(new ImageIcon(scaledImage));
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(Gui.this, "Error loading image!", "Error", JOptionPane.ERROR_MESSAGE);
        }
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

