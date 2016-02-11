package gerenciamento;

import java.util.ArrayList;
import java.util.List;

public class Predio {
	// Andares é um atributo estático, visto que pertence a classe e é compartilhado por todos os objetos de prédio
	// Embora só tenhamos um
	private static List<Andar> andares = new ArrayList<>();

	// Construtor de prédio criando todos os andares do prédio 
	public Predio(int numeroAndares) {
		for (int i = 0; i < numeroAndares; i++) {
			Andar andar = new Andar(i);
			andares.add(i, andar);
		}
	}

	// Setters
	public void setAndares(List<Andar> andares) {
		Predio.andares = andares;
	}

	// Getters
	public List<Andar> getAndares() {
		return andares;
	}
	
	

}
