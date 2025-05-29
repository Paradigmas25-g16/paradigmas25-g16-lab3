package namedEntity.category;

public class Country extends Place {

    public Country(String name, String category, int frequency) {
        super(name, category, frequency);
    }
    @Override
	public String toString() {
		return ("[Nombre:" + getName() + " | Frecuencia:" + getFrequency() + " | Tipo: Lugar -> País | Tema:" + getCategory() + "]");
	}
}