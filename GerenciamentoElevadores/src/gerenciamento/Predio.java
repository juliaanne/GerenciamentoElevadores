package gerenciamento;

import java.util.ArrayList;
import java.util.List;

public class Predio {
	// Andares é um atributo estático, visto que pertence a classe e é compartilhado por todos os objetos de prédio, embora tenhamos apenas um
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
	
	public int getNumeroAndares(){
		return andares.size();
	}
	
	// Verifica se ha requisicao pendente
	public synchronized boolean requisicaoPendente(){
		boolean result = false;
		
		// Verifica se ainda tem requisicao na fila de algum andar, ou seja, se ha alguma fila com tamanho diferente de zero
		for (Andar andar : andares) {
			if(andar.getTamanhoFila() != 0){
				result = true;
				break;
			}
		}
		
		return result;
	}
	
	// Verifica em quais andares ha resquicoes pendentes
	public List<Integer> andaresPendentes(){
		List<Integer> andaresPendentes = new ArrayList<>();
		
		// Para cada andar que ainda tem requisicao na fila, ou seja, tamanho da fila diferente de zero
		for (Andar andar : andares) {
			if(andar.getTamanhoFila() != 0){
				// Retorna o numero do andar
				andaresPendentes.add(andar.getNumero());
			}
		}
		
		return andaresPendentes;
	}

}
