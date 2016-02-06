package gerenciamento;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Andar {
	private int numero;
	List<Requisicao> fila = new LinkedList<Requisicao>();
	
	public Andar(int numero) {
		this.setNumero(numero);
	}
			 
	void imprimeRequisicoes(){
		for (int i = 0; i < fila.size(); i++) {
			System.out.println("Requisicao " + fila.get(i));
		}
	}

	public List<Requisicao> getFila() {
		return fila;
	}

	public void setFila(List<Requisicao> fila) {
		this.fila = fila;
	}
	
	public synchronized List<Requisicao> forneceRequisicoes(int nRequisicoes){
		List<Requisicao> requisicoes = new ArrayList<Requisicao>(nRequisicoes);

		// Verificando se o número de requisições solicitadas é maior do que as que existem para este andar 
		if( nRequisicoes > fila.size()){
			nRequisicoes = fila.size();
		}
		
		for (int i = 0; i < nRequisicoes; i++) {
			requisicoes.add(fila.remove(0));
		}
		return requisicoes;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}
	
}
