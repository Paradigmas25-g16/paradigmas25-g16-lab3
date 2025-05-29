package parser;

import feed.Article;
import feed.Feed;
import httpRequest.httpRequester;
import java.util.Date;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONArray;


/*
 * Esta clase implementa el parser de feed de tipo reddit (json)
 * pero no es necesario su implemntacion 
 * */
public class RedditParser extends GeneralParser {

    public Feed parse(String urlfeed) {
        httpRequester requester = new httpRequester();
        Feed result = new Feed("Reddit Feed");

        try {
            String jsonResponse = requester.getFeedContent(urlfeed);

            if (jsonResponse != null) {
                // Parsear el JSON usando org.json
                JSONTokener tokener = new JSONTokener(jsonResponse);
                JSONObject rootObject = new JSONObject(tokener);
                
                JSONObject dataObject = rootObject.getJSONObject("data");
                JSONArray childrenArray = dataObject.getJSONArray("children");

                for (int i = 0; i < childrenArray.length(); i++) {
                    JSONObject postWrapper = childrenArray.getJSONObject(i);
                    JSONObject postData = postWrapper.getJSONObject("data");

                    String title = postData.optString("title", "");
                    String text = postData.optString("selftext", "");
                    long createdUtc = postData.optLong("created_utc", 0) * 1000;
                    String permalink = postData.optString("permalink", "");
                    String link = "https://www.reddit.com" + permalink;

                    Article article = new Article(
                        title,
                        text,
                        new Date(createdUtc),
                        link
                    );

                    result.addArticle(article);
                }
            }

        } catch (Exception e) {
            System.err.println("Error parsing Reddit feed: " + e.getMessage());
        }

        return result;
    }
}
