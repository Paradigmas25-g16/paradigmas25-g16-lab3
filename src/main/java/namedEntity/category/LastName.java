package namedEntity.category;

public class LastName extends Person implements java.io.Serializable {

    public LastName(String name, String category, int frequency) {
        super(name, category, frequency);
    }

    @Override
    public String toString() {
		return "[Nombre:" + getName() + " | Frecuencia:" + getFrequency() + " | Tipo: Persona -> Apellido | Tema:" + getCategory() + "]";
	}

}