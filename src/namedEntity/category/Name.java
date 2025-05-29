package namedEntity.category;

public class Name extends Person {

    public Name(String name, String category, int frequency) {
        super(name, category, frequency);
    }

    @Override
    public String toString() {
		return "[Nombre:" + getName() + " | Frecuencia:" + getFrequency() + " | Tipo: Persona -> Nombre | Tema:" + getCategory() + "]";
	}
}
