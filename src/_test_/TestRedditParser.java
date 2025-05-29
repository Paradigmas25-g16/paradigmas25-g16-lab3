package _test_;

import parser.RedditParser;
import feed.Feed;

public class TestRedditParser {
    public static void main(String[] args) {
     
        String url = "https://www.reddit.com/r/Sales/hot/.json?count=100";

        RedditParser parser = new RedditParser();
        Feed feed = parser.parse(url);
        feed.prettyPrint();
    }
}
