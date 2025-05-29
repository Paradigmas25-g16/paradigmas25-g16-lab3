import java.util.*;

import feed.*;
import parser.*;
import subscription.*;
import httpRequest.*;
import namedEntity.*;
import namedEntity.heuristic.*;

public class FeedReaderMain {

	private static String parseSubUrl(String filePath){
		Scanner stdin = new Scanner(System.in);
		String URLCompleted = null;

        SubscriptionParser parser = new SubscriptionParser();
        Subscription subs = parser.parse(filePath);

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

	private static String feedRequest(String URL){
		httpRequester requester = new httpRequester();
		String feedData = requester.getFeedContent(URL);

		return feedData;
	}
	
	private static Feed parseFeed(String URL, String feedData){
		Feed feed = null;
		if(URL.contains("rss.nytimes.com")){
			RssParser parser = new RssParser();
			feed = parser.parse(feedData);
		}else if(URL.contains("www.reddit.com")){
			RedditParser parser = new RedditParser();
			feed = parser.parse(URL);
		}
		return feed;
	}

	private static List<NamedEntity> computeEntities(Feed feed){
		QuickHeuristic quickHeuristic = new QuickHeuristic();
		Application application = new Application();
		List <NamedEntity> namedEntities = new ArrayList<>();	
		namedEntities = application.main(feed, quickHeuristic);

		return namedEntities;
	}

	private static void printHelp(){
		System.out.println("Please, call this program in correct way: FeedReader [-ne]");
	}
	
	
	public static void main(String[] args) {
		System.out.println("************* FeedReader version 1.0 *************");
		String filepath = "config/subscriptions.json";

		if (args.length == 0) {

    		System.out.println("\n-----Parsing subscriptions.json-----\n");
			String UrlSub = parseSubUrl(filepath);

			System.out.println("\n-----Requesting feed with HTTP request-----\n");
			String feedData = feedRequest(UrlSub);

			System.out.println("\n-----Parsing feed-----\n");
			Feed feed = parseFeed(UrlSub, feedData);

			feed.prettyPrint();
		} else if (args.length == 1){
            System.out.println("\n-----Parsing subscriptions.json-----\n");
			String UrlSub = parseSubUrl(filepath);

			System.out.println("\n-----Requesting feed with HTTP request-----\n");
			String feedData = feedRequest(UrlSub);

			System.out.println("\n-----Parsing feed-----\n");
			Feed feed = parseFeed(UrlSub, feedData);

			System.out.println("\n-----Computing namedEntities-----\n");
			List<NamedEntity> namedEntities = computeEntities(feed);
			
			System.out.println("\n-----prettyPrint namedEntities-----\n");
			namedEntities.forEach(entity -> System.out.println(entity.toString()));
			Application application = new Application();
			application.categoryTable(namedEntities);
			
        } else {
            printHelp();
        }
	}

}
