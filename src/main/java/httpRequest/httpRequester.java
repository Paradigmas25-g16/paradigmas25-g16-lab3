package httpRequest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/* Esta clase se encarga de realizar efectivamente el pedido de feed al servidor de noticias
 * Leer sobre como hacer una http request en java
 * https://www.baeldung.com/java-http-request
 * */

public class httpRequester {
	
	@SuppressWarnings("deprecation")
	public String getFeedContent(String urlFeed){
		String feedData = null;
		try {
			URL url = new URL(urlFeed);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
		
			int status = connection.getResponseCode();
			if(status == HttpURLConnection.HTTP_OK){	//si hubo respuesta 200, continua
				InputStream inputStream = connection.getInputStream();	
				BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));

				String inputLine;
				StringBuffer content = new StringBuffer();
				while ((inputLine = input.readLine()) != null) {
					content.append(inputLine).append("\n");	//se lee por linea el inputstream, "\n" para que no sea toda una linea
				}

				feedData = content.toString();
				input.close();
				inputStream.close();	//se cierran ambas conections
			}else{
				System.err.println("Error Response, status code: " + status);
			}

        	connection.disconnect();	//se desconecta
			
		}
		catch (Exception e) {
            System.err.println("Error establishing connection, " + e.getMessage() + "\n");
        }

		return feedData;
	}

}

