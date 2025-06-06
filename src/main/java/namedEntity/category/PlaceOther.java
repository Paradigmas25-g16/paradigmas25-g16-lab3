package namedEntity.category;

public class PlaceOther extends Place implements java.io.Serializable {

    public PlaceOther(String name, String category, int frequency) {
        super(name, category, frequency);
    }

    @Override
	public String toString() {
		return "[Nombre:" + getName() + " | Frecuencia:" + getFrequency() + " | Tipo: Lugar -> Otros | Tema:" + getCategory() + "]";
	}
}