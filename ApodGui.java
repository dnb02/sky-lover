// ApodGui.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;

public class ApodGui extends JFrame {
    private JButton getAPODButton;
    private JLabel imageLabel;
    private JLabel explanationLabel;
    private ImageIcon placeholderIcon;

    public ApodGui() {
        setTitle("Welcome, lovers of the sky");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Set to full-screen mode
        setUndecorated(false); // Hide title bar and borders

        // Create placeholder icon
        placeholderIcon = new ImageIcon("placeholder.jpg");

        // Create components
        JLabel titleLabel = new JLabel("Astronomy Picture of the day!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel instructionLabel = new JLabel("Click the button below to get the astronomy picture of the day");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        imageLabel = new JLabel(placeholderIcon);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        explanationLabel = new JLabel();
        explanationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        explanationLabel.setVerticalAlignment(SwingConstants.TOP);
        explanationLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        explanationLabel.setPreferredSize(new Dimension(800, 200)); // Wider size
        explanationLabel.setMaximumSize(new Dimension(800, 200)); // Wider size
        explanationLabel.setMinimumSize(new Dimension(800, 200)); // Wider size
        explanationLabel.setOpaque(true);
        explanationLabel.setBackground(Color.WHITE);
        explanationLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        explanationLabel.setVerticalTextPosition(SwingConstants.TOP);
        explanationLabel.setHorizontalTextPosition(SwingConstants.LEFT);
        explanationLabel.setHorizontalAlignment(SwingConstants.LEFT);
        explanationLabel.setText("<html>Lets see what fortune holds in store for you!</html>");

        JScrollPane explanationScrollPane = new JScrollPane(explanationLabel);
        explanationScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        explanationScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Disable horizontal scrolling
        explanationScrollPane.setPreferredSize(new Dimension(800, 200)); // Wider size

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(Box.createVerticalStrut(20)); // Add vertical spacing
        mainPanel.add(titleLabel);
        mainPanel.add(instructionLabel);
        mainPanel.add(Box.createVerticalStrut(20)); // Add vertical spacing
        mainPanel.add(imageLabel);
        mainPanel.add(Box.createVerticalStrut(20)); // Add vertical spacing
        mainPanel.add(explanationScrollPane);
        mainPanel.add(Box.createVerticalStrut(20)); // Add vertical spacing

        // Set the main panel as the content pane
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Disable horizontal scrolling
        setContentPane(scrollPane);
        pack();
        setLocationRelativeTo(null); // Center the window on the screen
    }

    public void displayImage(String imageUrl) {
        try {
            // Fetch the image from URL
            ImageIcon imageIcon = new ImageIcon(new URL(imageUrl));
            Image image = imageIcon.getImage();
            Image scaledImage = image.getScaledInstance(1000, -1, Image.SCALE_SMOOTH); // Larger image size

            // Display the image
            imageLabel.setIcon(new ImageIcon(scaledImage));
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(ApodGui.this, "Error loading image!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void displayExplanation(String explanation) {
        explanationLabel.setText("<html>" + explanation + "</html>");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ApodGui apodGui = new ApodGui();
                apodGui.setVisible(true);
            }
        });
    }
}

