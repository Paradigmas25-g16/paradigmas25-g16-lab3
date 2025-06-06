package namedEntity.category;

import namedEntity.NamedEntity;

public class Dates extends NamedEntity implements java.io.Serializable {

    public Dates(String name, String category, int frequency) {
        super(name, category, frequency);
    }

    @Override
    public String toString() {
		return ("[Nombre:" + getName() + " | Frecuencia:" + getFrequency() + " | Tipo: Fecha | Tema:" + getCategory() + "]");
	}
}
