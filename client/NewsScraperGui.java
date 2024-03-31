import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.util.List;

public class NewsScraperGui extends JFrame {

    public NewsScraperGui(List<String> headlines) {
        setTitle("Today's Astronomy Headlines");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Center the window

        // Panel to hold all components
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Add margin

        // Heading Label
        JLabel headingLabel = new JLabel("Today's Astronomy Headlines");
        headingLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        headingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(headingLabel, BorderLayout.NORTH);

        // Text Pane to display headlines with justified alignment
        JTextPane newsTextPane = new JTextPane();
        newsTextPane.setEditable(false);
        newsTextPane.setFont(newsTextPane.getFont().deriveFont(Font.BOLD)); // Set text to bold

        // Create a SimpleAttributeSet to set text alignment to justified
        SimpleAttributeSet justified = new SimpleAttributeSet();
        StyleConstants.setAlignment(justified, StyleConstants.ALIGN_JUSTIFIED);
        newsTextPane.setParagraphAttributes(justified, false);

        // Append each headline to the text pane
        for (String headline : headlines) {
            newsTextPane.setText(newsTextPane.getText() + headline + "\n");
        }

        JScrollPane scrollPane = new JScrollPane(newsTextPane);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }
}

