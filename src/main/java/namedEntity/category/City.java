package namedEntity.category;

public class City extends Place implements java.io.Serializable {

    public City(String name, String category, int frequency) {
        super(name, category, frequency);
    }

    @Override
	public String toString() {
		return ("[Nombre:" + getName() + " | Frecuencia:" + getFrequency() + " | Tipo: Lugar -> Ciudad | Tema:" + getCategory() + "]");
	}
}