package _test_;

import parser.*;
import subscription.*;

public class subsParserTest {
    public static void main(String[] args) {
        String jsonpath = "/home/visitante/Escritorio/delete/paradigmas25-g16-lab2/config/subscriptions.json";
    
        SubscriptionParser parser = new SubscriptionParser();
        Subscription sub = parser.parse(jsonpath);    

        sub.prettyPrint();

    }
        
}
