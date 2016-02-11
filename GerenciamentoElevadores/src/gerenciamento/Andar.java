package gerenciamento;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Andar {
	private int numero;
	private List<Requisicao> fila = new LinkedList<Requisicao>();
	
	// Contrutor do andar com seu respectivo número
	public Andar(int numero) {
		this.setNumero(numero);

	}
			 
	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	void imprimeRequisicoes(){
		for (int i = 0; i < fila.size(); i++) {
			System.out.println("Requisicao " + fila.get(i));
		}

	}

	// Getters
	public List<Requisicao> getFila() {
		return fila;
	}
	
	public synchronized int getTamanhoFila(){
		return this.fila.size();
	}

	// Setters
	public void setFila(List<Requisicao> fila) {
		this.fila = fila;
	}


	

	// Fornece as requisições do Andar para o Elevador.
	public synchronized List<Requisicao> forneceRequisicoes(int nRequisicoes){
		List<Requisicao> requisicoes = new ArrayList<Requisicao>(nRequisicoes);

		// Verificando se o número de requisições solicitadas é maior do que as que existem para este andar 
		if( nRequisicoes > fila.size()){
			nRequisicoes = fila.size();
		}
		
		// Adiciona as requisições para retornar para o elevador e as remove dos andares 
		for (int i = 0; i < nRequisicoes; i++) {
			requisicoes.add(fila.remove(0));
		}
		
		return requisicoes;
	}


	
}
