package parser;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;

import org.json.JSONArray;

import subscription.*;

public class SubscriptionParser extends GeneralParser{
    
    public Subscription parse(String filePath) {
        try(FileReader reader = new FileReader(filePath)){
            JSONTokener tokener = new JSONTokener(reader);
            JSONArray array = new JSONArray(tokener);

            JSONObject rssJsonObject = array.getJSONObject(0);
            //rss singleSubscription
            String rssUrl = rssJsonObject.getString("url");
            String rssUrlType = rssJsonObject.getString("urlType");

            SingleSubscription rssSingleSubscription = new SingleSubscription(rssUrl, null, rssUrlType);

            JSONArray rssParams = rssJsonObject.getJSONArray("urlParams");
            for(int i=0; i<rssParams.length(); i++){
                String param = rssParams.getString(i);
                rssSingleSubscription.setUlrParams(param);
            }

            JSONObject redditJsonObject = array.getJSONObject(1);
            //reddit singleSubscription
            String redditUrl = redditJsonObject.getString("url");
            String redditUrlType = redditJsonObject.getString("urlType");

            SingleSubscription redditSingleSubscription = new SingleSubscription(redditUrl, null, redditUrlType);

            JSONArray redditParams = redditJsonObject.getJSONArray("urlParams");
            for(int i=0; i<redditParams.length(); i++){
                String param = redditParams.getString(i);
                redditSingleSubscription.setUlrParams(param);
            }
            
            //list of singleSubscriptions
            Subscription subs = new Subscription(null);
            subs.addSingleSubscription(rssSingleSubscription);
            subs.addSingleSubscription(redditSingleSubscription);
            return subs;
        }catch(Exception e){
            System.err.println("Error parsing JSONArray: " + e.getMessage());
        }
        
        
        return null;
    }    
}
