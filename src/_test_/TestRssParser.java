package _test_;

import feed.Feed;
import httpRequest.httpRequester;
import parser.RssParser;

public class TestRssParser {
    public static void main(String[] args) {
        //obtener xmlfeed por url con httpRequest
        //parsear xmlfeed mediante parser 
        //mostrar resultados
        
        String testUrl = "https://rss.nytimes.com/services/xml/rss/nyt/Business.xml";

        httpRequester requester = new httpRequester();
        String feedRssXml = requester.getFeedContent(testUrl);

        RssParser parser = new RssParser();
        Feed feed = parser.parse(feedRssXml);
        
        feed.prettyPrint();

    }
}