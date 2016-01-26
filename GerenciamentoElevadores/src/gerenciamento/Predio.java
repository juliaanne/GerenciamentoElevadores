package gerenciamento;

import java.util.ArrayList;
import java.util.List;

public class Predio {
	List<Andar> andares = new ArrayList<>();

	public Predio(int numeroAndares) {
		for (int i = 0; i < numeroAndares; i++) {
			Andar andar = new Andar(i);
			andares.add(i, andar);
		}
	}

	public void setAndares(List<Andar> andares) {
		this.andares = andares;
	}

	public List<Andar> getAndares() {
		return andares;
	}
	
	

}
