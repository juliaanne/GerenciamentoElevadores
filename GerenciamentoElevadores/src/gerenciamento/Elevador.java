package gerenciamento;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Elevador extends Thread {
	private int id, andarAtual;
	private static int capacidade;
	private static Predio predio;
	private static Semaphore semaforo = new Semaphore(1);
	private List<Requisicao> atendimentos = new ArrayList<Requisicao>();
	
	// Contrutor de elevador com o seu id, andar inicial, capacidade e Predio
	public Elevador(int id, int andar, int capacidade, Predio predio){
		this.id = id;
		this.andarAtual = andar;
		Elevador.capacidade = capacidade;
		Elevador.predio = predio;
	}

	
	// Metodo chamado quando as threads startam
	public void run(){
		//System.out.println("Elevador " + id + " iniciou no andar " + andarInicial);
		
		// Enquanto ha requisicoes pendentes em algum lugar do edificio:
		while(predio.requisicaoPendente()){
			// Seleciona as requisicoes ate sua capacidade do andar mais próximo
			atendimentos =  requisicoesMaisProximas();
			if(atendimentos.size() == 0){
				System.out.println("Elevador " + this.id + " terminou no andar " + this.andarAtual);
				break;
			}
			
			// Calcula trajeto
			atendimentos = calculaTrajeto();
			System.out.println("Elevador " + this.id + " no andar " + andarAtual +" atendera para os seguintes destinos: " + atendimentos);
			
			// Desloca-se parando nos andares destinos de suas requisicoes e retorna o andar final apos o trajeto
			percorreTrajeto(atendimentos);
			System.out.println("Elevador " + this.id + " terminou no andar " + this.andarAtual);
		}
		
	}
	
	private void percorreTrajeto(List<Requisicao> atendimentosAtuais) {
		
		for (Requisicao requisicao : atendimentosAtuais) {
			this.andarAtual = requisicao.getAndarDestino();
			System.out.println("Elevador " + this.id + " parou no andar " + this.andarAtual + " para deixar um passageiro.");
			
			try {
				sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
		atendimentos.clear();
	}


	private List<Requisicao> requisicoesMaisProximas(){
		int menorDistancia = predio.getNumeroAndares(), maiorFila = 0, maisProximo = predio.getNumeroAndares();
		List<Requisicao> atendimentosMaisProximos;
		
		try {
			semaforo.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Procura requisicoes no predio
		List<Integer> andaresPendentes = predio.andaresPendentes();
		
		System.out.println("Elevador " + this.id + " esta no andar " + this.andarAtual + " vai procurar a melhor opçao dentre os andares pendentes " + andaresPendentes);
		
		if(andaresPendentes.size() == 0){
			semaforo.release();
			atendimentosMaisProximos = new ArrayList<Requisicao>();
			return atendimentosMaisProximos;
		}
	
		for (Integer andar : andaresPendentes) {
			int distancia = Math.abs(andar - this.andarAtual);
			
			if ( distancia < menorDistancia || (distancia == menorDistancia && predio.getAndares().get(andar).getTamanhoFila() > maiorFila)){ 
				menorDistancia = distancia;
				maisProximo = andar;
				maiorFila = predio.getAndares().get(andar).getTamanhoFila();
			}
		}
		
		System.out.println("Elevador " + this.id + " escolheu atender ao andar " + maisProximo);
		this.andarAtual = maisProximo;
		
		// Remove as requisicoes da fila
		atendimentosMaisProximos = predio.getAndares().get(maisProximo).forneceRequisicoes(capacidade);
		
		semaforo.release();
		
		//System.out.println(atendimentosPrimeira);
		
		return atendimentosMaisProximos;
	}


	// Calcula o trajeto que o elevador fara com as requisicoes colhidas do andar
	private List<Requisicao> calculaTrajeto(){
		Collections.sort(atendimentos, new RequisicaoComparator());
		int menorDistancia;
		
		Requisicao primeiro = atendimentos.get(0);
		Requisicao ultimo = atendimentos.get(atendimentos.size()-1);
		
		int d1 = Math.abs(andarAtual - primeiro.getAndarDestino());
		int d2 = Math.abs(andarAtual - ultimo.getAndarDestino());		
		menorDistancia = Math.min(d1, d2);
		
		// Se for mais perto da ultima requisicao, comeca atendendo a ultima e reverte o restante da lista.
		if(menorDistancia == d2){
			Collections.rotate(atendimentos, 1);
			Collections.reverse(atendimentos.subList(1, atendimentos.size()));
		}
		
		return atendimentos;
	}
	
}
