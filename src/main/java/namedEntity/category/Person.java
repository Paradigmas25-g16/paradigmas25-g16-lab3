package namedEntity.category;

import namedEntity.NamedEntity;

public class Person extends NamedEntity implements java.io.Serializable {

    public Person(String name, String category, int frequency) {
        super(name, category, frequency);
    }
}