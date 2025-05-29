package namedEntity.category;

import namedEntity.NamedEntity;

public class Product extends NamedEntity {

    public Product(String name, String category, int frequency) {
        super(name, category, frequency);
    }
    
    @Override
	public String toString() {
		return "[Nombre:" + getName() + " | Frecuencia:" + getFrequency() + " | Tipo: Producto | Tema:" + getCategory() + "]";
	}
}
