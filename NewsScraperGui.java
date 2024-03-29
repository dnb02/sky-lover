import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.List;
import java.net.*;

public class NewsScraperGui extends JFrame {

    public NewsScraperGui(List<String> headlines) {
        setTitle("News Headlines");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null); // Center the window

        JTextArea newsTextArea = new JTextArea();
        newsTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(newsTextArea);

        // Append each headline to the text area
        for (String headline : headlines) {
            newsTextArea.append(headline + "\n");
        }

        add(scrollPane, BorderLayout.CENTER);
    }
}
