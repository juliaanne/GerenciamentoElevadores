package gerenciamento;

import java.util.LinkedList;
import java.util.List;

public class Andar {
	private int numero;
	List<Requisicao> fila = new LinkedList<Requisicao>();
	
	public Andar(int numero) {
		this.numero = numero;
	}
			 
	void imprimeRequisicoes(){
		for (int i = 0; i < fila.size(); i++) {
			System.out.println("Requisicao " + fila.get(i));
		}
	}
	
	
}
