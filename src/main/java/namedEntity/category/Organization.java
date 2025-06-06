package namedEntity.category;

import namedEntity.NamedEntity;

public class Organization extends NamedEntity implements java.io.Serializable {

    public Organization(String name, String category, int frequency) {
        super(name, category, frequency);
    }

    @Override
	public String toString() {
		return "[Nombre:" + getName() + " | Frecuencia:" + getFrequency() + " | Tipo: Organización | Tema:" + getCategory() + "]";
	}
}
