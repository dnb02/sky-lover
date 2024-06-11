import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import java.io.*;
import java.util.*;

public class NewsScraper {

    public List<String> getHeadlines(String url) throws IOException {
        List<String> newsHeadlines = new ArrayList<String>();
        Document newsPage = Jsoup.connect(url).get();
        Elements headlines = newsPage.select(".sorted-article-content h3 a");

        for (Element headline : headlines) {
            String headlineText = headline.text();
            newsHeadlines.add(headlineText);
        }

        return newsHeadlines;
    }
}

