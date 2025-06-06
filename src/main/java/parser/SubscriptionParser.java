package parser;


import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONArray;
import java.io.FileReader;

import subscription.*;
/*
 * Esta clase implementa el parser del  archivo de suscripcion (json)
 * Leer https://www.w3docs.com/snippets/java/how-to-parse-json-in-java.html
 * */

public class SubscriptionParser extends GeneralParser{
    public Subscription parse(String filepath){
        Subscription subscription = new Subscription(null);

        try(FileReader reader = new FileReader(filepath)){
            JSONTokener tockener = new JSONTokener(reader);
            JSONArray array = new JSONArray(tockener);

            //iterate over the array for any sub
            for(int i=0 ; i<array.length() ; i++){
                JSONObject object = array.getJSONObject(i);
                String subUrl = object.getString("url");
                String subUrlType = object.getString("urlType");

                SingleSubscription singleSub = new SingleSubscription(subUrl, null, subUrlType);

                JSONArray subParams = object.getJSONArray("urlParams");
                for(int j=0 ; j<subParams.length() ; j++){
                    String param = subParams.getString(j);
                    singleSub.setUlrParams(param);
                }
                subscription.addSingleSubscription(singleSub);
            }

            return subscription;
        }catch(Exception e){
            System.err.println("Error parsing JSONArray: " + e.getMessage());
        }
        return null;
    }
}
