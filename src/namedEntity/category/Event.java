package namedEntity.category;

import namedEntity.NamedEntity;

public class Event extends NamedEntity {

    public Event(String name, String category, int frequency) {
        super(name, category, frequency);
    }

    @Override
	public String toString() {
		return ("[Nombre:" + getName() + " | Frecuencia:" + getFrequency() + " | Tipo: Evento | Tema:" + getCategory() + "]");
	}
}
