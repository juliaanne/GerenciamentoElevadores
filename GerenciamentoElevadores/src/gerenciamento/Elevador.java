package gerenciamento;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Elevador extends Thread {
	public int id;
	private int andarInicial;
	private static int capacidade;
	private static Predio predio;
	private List<Requisicao> atendimentos = new ArrayList<Requisicao>();
	
	// Contrutor de elevador com o seu id, andar inicial, capacidade e Predio
	public Elevador(int id, int andar, int capacidade, Predio predio){
		this.id = id;
		this.andarInicial = andar;
		Elevador.capacidade = capacidade;
		Elevador.predio = predio;
	}

	
	// Método chamado quando as threads startam
	public void run(){
		System.out.println("Elevador " + id + " iniciou no andar " + andarInicial);
		
		// Enquanto há requisições pendentes em algum lugar do edifício:
		while(predio.requisicaoPendente()){
			// Desloca-se até este andar, decidindo qual andar mais proximo
			int maisProximo = calculaAndarProximo();

			// Seleciona as requisições até sua capacidade
			atendimentos = predio.getAndares().get(maisProximo).forneceRequisicoes(capacidade);

			// Calcula trajeto
			atendimentos = calculaTrajeto();
			System.out.println("Elevador " + this.id + " no andar Inicial " + andarInicial +" atenderá para os seguintes destinos: " + atendimentos);
			
			// Desloca-se parando nos andares destinos de suas requisições
			// Retorna o andar final após o trajeto
			// ToDo percorreTrejeto
			int andarFinal = percorreTrajeto(atendimentos);
			
			// Setar andar final para andar inicial.
			this.andarInicial = andarFinal; 
		}
		
	}
	
	private int calculaAndarProximo() {
		int maisProximo = Integer.MAX_VALUE, distanciaMinima;
		int andarAtual = this.andarInicial;
		
		// Se há requisição no proprio andar que ele está, atende
		if( predio.getAndares().get(andarAtual).getTamanhoFila() != 0 ){
			maisProximo = andarAtual;
		} else {
			// Se há requisição pendente em mais de um andar, o elevador deve priorizar escolher: o andar mais perto, com mais requisicoes, tanto faz
			List<Integer> andaresPendentes = predio.andaresPendentes();
			for (Integer andar : andaresPendentes) {
				if(Math.abs(andar - andarAtual) < maisProximo){
					distanciaMinima = andar;
					// BUCETA DE LOGICA FDP :@ NAO ENTENDO O QUE EU TO FAZENDO PORQUE TO FAZENDO DE MANEIRA BURRA
				}
			}
			
		}

		return maisProximo;
	}


	// Calcula o trajeto que o elevador fará com as requisições colhidas do andar
	private List<Requisicao> calculaTrajeto(){
		Collections.sort(atendimentos, new RequisicaoComparator());
		int menorDistancia;
		
		Requisicao primeiro = atendimentos.get(0);
		Requisicao ultimo = atendimentos.get(atendimentos.size()-1);
		
		int d1 = Math.abs(andarInicial - primeiro.getAndarDestino());
		int d2 = Math.abs(andarInicial - ultimo.getAndarDestino());		
		menorDistancia = Math.min(d1, d2);
		
		// Se for mais perto da ultima requisicao, comeca atendendo a ultima e reverte o restante da lista.
		if(menorDistancia == d2){
			Collections.rotate(atendimentos, 1);
			Collections.reverse(atendimentos.subList(1, atendimentos.size()));
		}
		
		return atendimentos;
	}
	
}
