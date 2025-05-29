package namedEntity;
import namedEntity.category.*;
import namedEntity.heuristic.Heuristic;
import feed.*;
import java.util.*;


public class Application {

    public List<NamedEntity> main (Feed feed, Heuristic heuristic){
        List<NamedEntity> entitiesList = new ArrayList<NamedEntity>();

        for (Article article : feed.getArticleList()) {
            String text = article.getTitle();
            text = text + ". " + article.getText();
           
            String charsToRemove = ".,;:()'@€!?[-]${–}=*'0123456789/%’&^#“”~¨°|¬!\"\\\n"; 
            for (char c : charsToRemove.toCharArray()) {
                text = text.replace(String.valueOf(c), "");
            }
            
            for (String word : text.split(" ")) {
                if (heuristic.isEntity(word)) {
                        
                    int namedEntityIndex = searchEntities(word, entitiesList);
                    if (namedEntityIndex != -1) {
                        // si lo encuentra, incrementa la frecuencia
                        entitiesList.get(namedEntityIndex).incFrequency();
                    } 
                    else {
                        //chequeo categoría
                        String category = heuristic.getCategory(word);
                        //chequeo tema
                        String theme = heuristic.getTheme(word);
                        
                        if ("Apellido".equals(category)) {
                            LastName entity = new LastName(word, theme, 1);
                            entitiesList.add(entity);
                        } 
                        else if ("Nombre".equals(category)) {
                            Name entity = new Name(word, theme, 1);
                            entitiesList.add(entity);
                        } 
                        else if ("Titulo".equals(category)) {
                            Title entity = new Title(word, theme, 1);
                            entitiesList.add(entity);
                        }
                        else if ("Organizacion".equals(category)) {
                            Organization entity = new Organization(word, theme, 1);
                            entitiesList.add(entity);
                        }
                        else if ("Producto".equals(category)) {
                            Product entity = new Product(word, theme, 1);
                            entitiesList.add(entity);
                        }
                        else if ("Evento".equals(category)) {
                            Event entity = new Event(word, theme, 1);
                            entitiesList.add(entity);
                        }
                        else if ("Fecha".equals(category)) {
                            Dates entity = new Dates(word, theme, 1);
                            entitiesList.add(entity);
                        }
                        else if ("Pais".equals(category)) {
                            Country entity = new Country(word, theme, 1);
                            entitiesList.add(entity);
                        }
                        else if ("Ciudad".equals(category)) {
                            City entity = new City(word, theme, 1);
                            entitiesList.add(entity);
                        }
                        else if ("Direccion".equals(category)) {
                            Address entity = new Address(word, theme, 1);
                            entitiesList.add(entity);
                        }
                        else if ("LugarOtros".equals(category)) {
                            PlaceOther entity = new PlaceOther(word, theme, 1);
                            entitiesList.add(entity);
                        }
                        else if ("TypeOtros".equals(category)) {
                            Other entity = new Other(word, theme, 1);
                            entitiesList.add(entity);
                        } 
                        else {
                            Other entity = new Other(word, "Otros", 1);
                            entitiesList.add(entity);
                        }
                    }
                }
            }
        }
        return entitiesList;
    }
    
    public int searchEntities(String name, List<NamedEntity> entitiesList) {
        for (NamedEntity namedEntity : entitiesList) {
            if (namedEntity.getName().equals(name)) {
                // si lo encuentra, devuelve la posición
                return entitiesList.indexOf(namedEntity);
            }
        }
        return -1;
    } 

    public void categoryTable(List<NamedEntity> namedEntities) {
        Map<String, Integer> counter = new HashMap<>();
        counter.put("Deportes", 0);
        counter.put("Futbol", 0);
        counter.put("Basquet", 0);
        counter.put("Tenis", 0);
        counter.put("F1", 0);
        counter.put("Cultura", 0);
        counter.put("Cine", 0);
        counter.put("Musica", 0);
        counter.put("Politica", 0);
        counter.put("Internacional", 0);
        counter.put("Nacional", 0);

        String theme;
        for(NamedEntity ne: namedEntities){
            theme = ne.getCategory();
            if("Futbol".equals(theme)){
                counter.put("Deportes", counter.get("Deportes")+ne.getFrequency());
                counter.put("Futbol", counter.get("Futbol")+ne.getFrequency());
            }
            else if("Basquet".equals(theme)){
                counter.put("Deportes", counter.get("Deportes")+ne.getFrequency());
                counter.put("Basquet", counter.get("Basquet")+ne.getFrequency());
            }
            else if("Tenis".equals(theme)){
                counter.put("Deportes", counter.get("Deportes")+ne.getFrequency());
                counter.put("Tenis", counter.get("Tenis")+ne.getFrequency());
            }
            else if("F1".equals(theme)){
                counter.put("Deportes", counter.get("Deportes")+ne.getFrequency());
                counter.put("F1", counter.get("F1")+ne.getFrequency());
            }
            else if("Cine".equals(theme)){
                counter.put("Cultura", counter.get("Cultura")+ne.getFrequency());
                counter.put("Cine", counter.get("Cine")+ne.getFrequency());
            }
            else if("Musica".equals(theme)){
                counter.put("Cultura", counter.get("Cultura")+ne.getFrequency());
                counter.put("Musica", counter.get("Musica")+ne.getFrequency());
            }
            else if("Internacional".equals(theme)){
                counter.put("Politica", counter.get("Politica")+ne.getFrequency());
                counter.put("Internacional", counter.get("Internacional")+ne.getFrequency());
            }
            else if("Nacional".equals(theme)){
                counter.put("Politica", counter.get("Politica")+ne.getFrequency());
                counter.put("Nacional", counter.get("Nacional")+ne.getFrequency());
            }
        }

    System.out.println("\n-----Resumen-----\n");
    int deportes = counter.get("Futbol")+counter.get("Basquet")+counter.get("Tenis")+counter.get("F1");
    System.out.println("Deportes: " + deportes + "\n");
    System.out.println("    -Futbol:" +counter.get("Futbol") + "\n");
    System.out.println("    -Basquet:" +counter.get("Basquet") + "\n");
    System.out.println("    -Tenis:" +counter.get("Tenis") + "\n");
    System.out.println("    -F1:" +counter.get("F1") + "\n");
    int cultura = (counter.get("Cine")+counter.get("Musica"));
    System.out.println("Cultura: " + cultura + "\n");
    System.out.println("    -Cine:" +counter.get("Cine") + "\n");
    System.out.println("    -Musica:" +counter.get("Musica") + "\n");
    int politica = (counter.get("Internacional")+counter.get("Nacional"));
    System.out.println("Politica: " + politica + "\n");
    System.out.println("    -Internacional:" +counter.get("Internacional") + "\n");
    System.out.println("    -Nacional:" +counter.get("Nacional") + "\n");



    }
}
