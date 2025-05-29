package _test_;

import httpRequest.httpRequester;

public class httpRequesterTEST {
    public static void main(String[] args) {
        // 1. Crear una instancia de httpRequester
        httpRequester requester = new httpRequester();
        
        // 2. URL de prueba (feed RSS de NYTimes - Business)
        String testUrl = "https://rss.nytimes.com/services/xml/rss/nyt/Business.xml";

        System.out.println("Testeando getFeedRss() con URL: " + testUrl);
        String feedContent = requester.getFeedContent(testUrl);
        
        // 4. Verificar resultados
        if (feedContent == null) {
            System.err.println("Error: La respuesta es null. Revisar conexión o URL.");
        } else if (feedContent.isEmpty()) {
            System.err.println("Error: La respuesta está vacía.");
        } else {
            System.out.println("¡Éxito! Feed obtenido correctamente.");
            
            System.out.println("=== Feed RSS Completo ===");
            System.out.println(feedContent);  // Imprime el contenido completo
            System.out.println("=== Fin del Feed ===");
            
            // Verificar si es XML válido (contiene tags típicos de RSS)
            if (feedContent.contains("<rss") && feedContent.contains("<channel>")) {
                System.out.println("\nFormato RSS/XML válido detectado.");
            } else {
                System.out.println("\nAdvertencia: El contenido no parece ser RSS/XML válido.");
            }
        }
    }
}