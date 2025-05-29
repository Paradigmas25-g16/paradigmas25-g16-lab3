package namedEntity.category;

public class Address extends Place {

    public Address(String name, String category, int frequency) {
        super(name, category, frequency);
    }

    @Override
	public String toString() {
		return ("[Nombre:" + getName() + " | Frecuencia: " + getFrequency() + " | Tipo: Lugar->Dirección | Tema: " + getCategory() + "]");
	}
}
