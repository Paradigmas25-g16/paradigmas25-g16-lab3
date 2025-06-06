package parser;

import org.w3c.dom.*;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import feed.Article;
import feed.Feed;

/* Esta clase implementa el parser de feed de tipo rss (xml)
 * https://www.tutorialspoint.com/java_xml/java_dom_parse_document.htm 
 * */

public class RssParser extends GeneralParser {
    public Feed parse(String xmlString) {
        Feed feed = new Feed(null);
        try {
            ByteArrayInputStream input = new ByteArrayInputStream(xmlString.getBytes(StandardCharsets.UTF_8));
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = factory.newDocumentBuilder();
            Document doc = docBuilder.parse(input);
            doc.getDocumentElement().normalize();

            // Detectar si es RSS o Atom
            if (doc.getElementsByTagName("channel").getLength() > 0) {
                // Procesar como RSS
                Element channel = (Element) doc.getElementsByTagName("channel").item(0);
                NodeList titleNodes = channel.getElementsByTagName("title");
                String siteName = (titleNodes != null && titleNodes.getLength() > 0) ? titleNodes.item(0).getTextContent() : "Sin título";
                feed.setSiteName(siteName);

                NodeList itemList = doc.getElementsByTagName("item");
                for (int i = 0; i < itemList.getLength(); i++) {
                    Node item = itemList.item(i);
                    if (item.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) item;
                        String title = getElementTextContent(element, "title");
                        String description = getElementTextContent(element, "description");
                        String link = getElementTextContent(element, "link");
                        Date pubDate = parseDate(getElementTextContent(element, "pubDate"));
                        feed.addArticle(new Article(title, description, pubDate, link));
                    }
                }
            } else if (doc.getElementsByTagName("feed").getLength() > 0) {
                // Procesar como Atom
                Element feedElement = (Element) doc.getElementsByTagName("feed").item(0);
                NodeList titleNodes = feedElement.getElementsByTagName("title");
                String siteName = (titleNodes != null && titleNodes.getLength() > 0) ? titleNodes.item(0).getTextContent() : "Sin título";
                feed.setSiteName(siteName);

                NodeList entryList = doc.getElementsByTagName("entry");
                for (int i = 0; i < entryList.getLength(); i++) {
                    Node entry = entryList.item(i);
                    if (entry.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) entry;
                        String title = getElementTextContent(element, "title");
                        String description = getElementTextContent(element, "content");
                        String link = getElementTextContent(element, "id");
                        Date pubDate = parseDate(getElementTextContent(element, "updated"));
                        feed.addArticle(new Article(title, description, pubDate, link));
                    }
                    //2025-05-22T00:00:00.000
                }
            } else {
                System.err.println("Formato desconocido: no es ni RSS ni Atom.");
            }
        } catch (Exception e) {
            System.err.println("Error parsing XML: " + e.getMessage());
        }
        return feed;
    }

    private String getElementTextContent(Element element, String tagName) {
        NodeList nodes = element.getElementsByTagName(tagName);
        return (nodes != null && nodes.getLength() > 0) ? nodes.item(0).getTextContent() : "";
    }

    private Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return new Date();
        // Intentar formato RSS
        try {
            SimpleDateFormat rssFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", java.util.Locale.ENGLISH);
            return rssFormat.parse(dateStr);
        } catch (Exception ignored) {}
        // Intentar formato Atom (ISO 8601)
        try {
            // Quitar 'Z' si está presente
            String clean = dateStr.endsWith("Z") ? dateStr.substring(0, dateStr.length() - 1) : dateStr;
            // Intentar con milisegundos
            SimpleDateFormat atomFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", java.util.Locale.ENGLISH);
            return atomFormat.parse(clean);
        } catch (Exception ignored) {}
        try {
            // Intentar sin milisegundos
            SimpleDateFormat atomFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.ENGLISH);
            return atomFormat2.parse(dateStr.replace("Z", ""));
        } catch (Exception ignored) {}
        System.err.println("Error parsing date: " + dateStr);
        return new Date();
    }
}
