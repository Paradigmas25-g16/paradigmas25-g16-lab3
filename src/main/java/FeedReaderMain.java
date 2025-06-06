//import java.lang.reflect.Array;
import java.util.*;
import org.apache.spark.api.java.*;

import org.apache.spark.sql.SparkSession;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;


import feed.*;
import parser.*;
import subscription.*;
import httpRequest.*;
import namedEntity.*;
import namedEntity.heuristic.*;

public class FeedReaderMain {

	private static List<String> parseSubsUrls(String filepath){
		List<String> urlList = new ArrayList<String>();

		SubscriptionParser parser = new SubscriptionParser();
		Subscription subs = parser.parse(filepath);

		List<SingleSubscription> subsList = subs.getSubscriptionsList();
		
		String url = null;
		for(SingleSubscription sub: subsList){	//itero sobre cada sub
			if(sub.getUlrParams().contains("default")){	//si params contiene default, es solo la url
				url = sub.getUrl();
				System.out.println("Adding default URL: " + url);
				urlList.add(url);
			}else{	//sino la contiene, entonces obtenongo todas las url
				for(int i=0 ; i<sub.getUlrParamsSize() ; i++ ){	//itero sobre cada param de la sub
					url = sub.getFeedToRequest(i);
					urlList.add(url);
					System.out.println("Adding URL: " + url);	
				}
			}
		}



		return urlList;
	}

	private static List<Article> fetchAndParseFeeds(List<String> urlList){
    	SparkSession spark = SparkSession
    	                    .builder()
    	                    .appName("FeedReaderMain")
    	                    .master("local[*]")
    	                    .getOrCreate();

    	JavaSparkContext Jcontext = new JavaSparkContext(spark.sparkContext());
    	JavaRDD<String> urlRdd = Jcontext.parallelize(urlList);

    	// Primer flatMap: obtener el contenido de cada URL
    	JavaRDD<String> contents = urlRdd.flatMap(
    	    new FlatMapFunction<String, String>() {
    	        @Override
    	        public Iterator<String> call(String url) {
    	            try {
    	                httpRequester requester = new httpRequester();
    	                String content = requester.getFeedContent(url);
    	                if (content != null) {
    	                    return Collections.singletonList(content).iterator();
    	                }
    	            } catch (Exception e) {
						System.err.println("Error fetching URL: " + url + " - " + e.getMessage());
    	            }
    	            return Collections.<String>emptyList().iterator();
    	        }
    	    });

    	// Segundo flatMap: parsear el contenido y obtener artículos
    	JavaRDD<Article> articles = contents.flatMap(
    	    new FlatMapFunction<String, Article>() {
    	        @Override
    	        public Iterator<Article> call(String content) {
    	            try {
    	                Feed feed = new RssParser().parse(content);
    	                return feed.getArticleList().iterator();
    	            } catch (Exception e) {
						System.err.println("Error parsing content: " + content + " - " + e.getMessage());
    	            }
    	            return Collections.<Article>emptyList().iterator();
    	        }
    	    });

    	List<Article> articleList = articles.collect();
    	Jcontext.close();
    	spark.stop();
    return articleList;
}
		
	private static List<List<NamedEntity>> computeEntities(List<Article> articles) {
    	SparkSession spark = SparkSession.builder()
    	        .appName("Compute_Entetities")
    	        .master("local[*]")
    	        .getOrCreate();
	
    	JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext());
	
    	JavaRDD<Article> articleRdd = jsc.parallelize(articles, articles.size());
	
    	JavaRDD<List<NamedEntity>> namedEntitiesPerArticle = articleRdd.map(
    	    new Function<Article, List<NamedEntity>>() {
    	        @Override
    	        public List<NamedEntity> call(Article article) {
    	            QuickHeuristic quickHeuristic = new QuickHeuristic();
    	            Application application = new Application();
    	            List<NamedEntity> entities = application.main(article, quickHeuristic);
    	            return entities != null ? entities : Collections.emptyList();
    	        }
    	    }
    	);
	
    List<List<NamedEntity>> result = namedEntitiesPerArticle.collect();
    jsc.close();
    spark.stop();
    return result;
}

	private static void printHelp(){
		System.out.println("Please, call this program in correct way: FeedReader [-ne]");
	}
	
	public static void main(String[] args) {
		System.out.println("************* FeedReader_Spark version 1.0 *************");
		String filepath = "target/classes/subscriptions.json";

		if (args.length == 0) {

    		System.out.println("\n-----Parsing subscriptions.json-----\n");
			List<String> UrlSub = parseSubsUrls(filepath);

			System.out.println("\n-----Requesting feed with HTTP request | Parsing feed-----\n");
			List<Article> parsedArticles = fetchAndParseFeeds(UrlSub);
			
			for (Article feed : parsedArticles){
				feed.prettyPrint();
			} 
			
		} else if (args.length == 1){
    		System.out.println("\n-----Parsing subscriptions.json-----\n");
			List<String> UrlSub = parseSubsUrls(filepath);

			System.out.println("\n-----Requesting feed with HTTP request | Parsing feed-----\n");
			List<Article> parsedArticles = fetchAndParseFeeds(UrlSub);

			System.out.println("\n-----Computing namedEntities-----\n");
			List<List<NamedEntity>> namedEntities = computeEntities(parsedArticles);

			int aux = 0;
			Application application = new Application();

			for (Article article : parsedArticles) {
				System.out.println("\n" + article.getTitle() + "\n");
				System.out.println("\n-----namedEntities-----\n");
				namedEntities.get(aux).forEach(entity -> System.out.println(entity.toString()));
				application.categoryTable(namedEntities.get(aux));
				aux ++;
			}
			
        } else {
            printHelp();
        }
	}	

}
