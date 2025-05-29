package _test_;

import parser.*;
import subscription.*;
import feed.*;
import httpRequest.*;
import namedEntity.heuristic.*;


import java.util.List;
import java.util.Scanner;



public class todoTestNE {

    public static String getUrlTest(String jsonpath) {
        Scanner stdin = new Scanner(System.in);
        String URLCompleted = null;

        SubscriptionParser parser = new SubscriptionParser();
        Subscription subs = parser.parse(jsonpath);

        List<SingleSubscription> subsList = subs.getSubscriptionsList();
        System.out.println("Choose a URL (1-"+ subsList.size() +"):");    
        System.out.println("Options:\n");
        for(int i=0 ; i < subsList.size() ; i++){
            int index = i+1;
            System.out.println("("+ index +") "+subsList.get(i).getUrl() + "\n");              //prints all the url types from subs
        }
        System.out.print("\nOption: ");
        int inputUrl = stdin.nextInt();                                   //gets from stdin the url type
        if (inputUrl>subsList.size() || inputUrl<1){
            System.err.println("Option out of range");
        }else{
            SingleSubscription singlesub = subs.getSingleSubscription(inputUrl-1);

            System.out.println("Choose a parameter for '%s'(1-"+ singlesub.getUlrParamsSize() +"):");
            System.out.println("Options:\n");
            for(int i=0 ; i < singlesub.getUlrParamsSize() ; i++){
                int index = i+1;
                System.out.println("("+ index +") "+singlesub.getUlrParams(i) + "\n");
            }
            System.out.print("\nOption: ");
            int inputParam = stdin.nextInt();
            if(inputParam>singlesub.getUlrParamsSize() || inputParam<1){
                System.err.println("Option out of range");
            }else{
                URLCompleted = singlesub.getFeedToRequest(inputParam-1);

            }
        }
        stdin.close();
        return URLCompleted;
    }

    public static String getFeedDataTest(String URL){
        httpRequester requester = new httpRequester();
        String feedData = requester.getFeedContent(URL);

        return feedData;
    }

    public static Feed parseFeedTest(String URL, String feedData){
        Feed feed = null;
        if (URL.contains("rss.nytimes.com")){
            System.out.println("Url is rss type -> using his correct parser");
            RssParser parser = new RssParser();
            feed = parser.parse(feedData);
        }else if(URL.contains("www.reddit.com")){
            System.out.println("Url is reddit type -> using his correct parser");
            RedditParser parser = new RedditParser();
            feed = parser.parse(URL);
        }
        return feed;
    }

    public static void main(String[] args) {

        /*
		Leer el archivo de suscription por defecto;
		Llamar al httpRequester para obtenr el feed del servidor
		Llamar al Parser especifico para extrar los datos necesarios por la aplicacion 
		Llamar al constructor de Feed
		Llamar a la heuristica para que compute las entidades nombradas de cada articulos del feed
		LLamar al prettyPrint de la tabla de entidades nombradas del feed.
		*/
        String jsonpath = "../../config/subscriptions.json";
        System.out.println("\nStarting test with NE\n");
        //---------------------------------

        System.out.println("\n-----Parse subscriptions.json Test-----\n");
        String URLCompleted = getUrlTest(jsonpath);
        if(URLCompleted == null){
            System.err.println("Error parsing subscriptions.json");
            System.exit(0);
        }else{
            System.out.println("Test: ✅ - got URL: " + URLCompleted);
        }
        
        //---------------------------------

        System.out.println("\n-----httpRequester Test-----\n");
        String feedData = getFeedDataTest(URLCompleted);
        if(feedData == null){
            System.err.println("Error requesting feedData");
            System.exit(0);
        }else{
            System.out.println("Test: ✅");
        }

        //---------------------------------

        System.out.println("\n-----Parse feedData Test-----\n");
        Feed feed = parseFeedTest(URLCompleted, feedData);
        if(feed == null){
            System.err.println("Error parsing feedData");
            System.exit(0);
        }else{
            System.out.println("Test: ✅");
        }

        //---------------------------------

        System.out.println("\n-----compute NE for each article Test-----\n");

        RandomHeuristic heuristic = new RandomHeuristic();
        for (feed.Article article : feed.getArticleList()) {
        article.computeNamedEntities(heuristic);
        }

        // 5. Pretty print de las entidades nombradas de cada artículo
        for (feed.Article article : feed.getArticleList()) {
            System.out.println("Entidades nombradas para el artículo: " + article.getTitle());
            for (namedEntity.NamedEntity ne : article.getNamedEntityList()) {
                System.out.println(ne.toString());
            }
            System.out.println();
        }




    }
}
