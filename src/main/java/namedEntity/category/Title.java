package namedEntity.category;

public class Title extends Person implements java.io.Serializable {

    public Title(String name, String category, int frequency) {
        super(name, category, frequency);
    }

    @Override
    public String toString() {
		return "[Nombre:" + getName() + " | Frecuencia:" + getFrequency() + " | Tipo: Persona -> Titulo | Tema:" + getCategory() + "]";
	}
}