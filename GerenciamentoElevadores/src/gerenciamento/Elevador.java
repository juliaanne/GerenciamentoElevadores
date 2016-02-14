package gerenciamento;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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
	
	private PrintWriter printWriter;
	private File arquivo;
	
	// Contrutor de elevador com o seu id, andar inicial, capacidade e Predio
	public Elevador(int id, int andar, int capacidade, Predio predio) throws IOException {
		this.id = id;
		this.andarAtual = andar;
		Elevador.capacidade = capacidade;
		Elevador.predio = predio;
		
		// Inicia o arquivo que vai guardar o log de cada thread e um printWrite para escrever.
		arquivo = new File("elevador " + id + ".txt" );
		
		printWriter = new PrintWriter(arquivo);
	}

	
	// Metodo chamado quando as threads startam
	public void run(){
		//System.out.println("Elevador " + id + " iniciou no andar " + andarInicial);
		
		// Enquanto ha requisicoes pendentes em algum lugar do edificio:
		while(predio.requisicaoPendente()){
			// Seleciona as requisicoes ate sua capacidade do andar mais próximo
			atendimentos =  requisicoesMaisProximas();
			if(atendimentos.size() == 0){
				printWriter.println("Elevador " + this.id + " terminou no andar " + this.andarAtual);
				System.out.println("Elevador " + this.id + " terminou no andar " + this.andarAtual);
				break;
			}
			
			// Calcula trajeto
			atendimentos = calculaTrajeto();
			printWriter.println("Elevador " + this.id + " no andar " + andarAtual + " atendera para os seguintes destinos: " + atendimentos);
			System.out.println("Elevador " + this.id + " no andar " + andarAtual +" atendera para os seguintes destinos: " + atendimentos);
			
			// Desloca-se parando nos andares destinos de suas requisicoes e retorna o andar final apos o trajeto
			percorreTrajeto(atendimentos);
			printWriter.println("Elevador " + this.id + " terminou no andar " + this.andarAtual);
			System.out.println("Elevador " + this.id + " terminou no andar " + this.andarAtual);
		}
		
		printWriter.close();
		
	}
	
	private void percorreTrajeto(List<Requisicao> atendimentosAtuais) {
		// Enquanto houver requisicao na fila de atendimentos
		while(atendimentosAtuais.size() != 0){
			// Desloca-se até o andar destino
			this.andarAtual = atendimentosAtuais.get(0).getAndarDestino();
			printWriter.println("Elevador " + this.id + " parou no andar " + this.andarAtual  + " para deixar um passageiro.");
			System.out.println("Elevador " + this.id + " parou no andar " + this.andarAtual + " para deixar um passageiro.");
			// Remove o passageiro do elevador
			atendimentosAtuais.remove(0);
			
			// Espera um pouco até a proxima iteracao
			try {
				sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}


	private List<Requisicao> requisicoesMaisProximas(){
		int menorDistancia = predio.getNumeroAndares(), maiorFila = 0, maisProximo = predio.getNumeroAndares();
		List<Requisicao> atendimentosMaisProximos;
		
		// Decrementa o semaforo de um sinal
		try {
			semaforo.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Procura requisicoes no predio
		List<Integer> andaresPendentes = predio.andaresPendentes();
		
		printWriter.println("Elevador " + this.id + " esta no andar " + this.andarAtual + " vai procurar a melhor opçao dentre os andares pendentes " + andaresPendentes);
		System.out.println("Elevador " + this.id + " esta no andar " + this.andarAtual + " vai procurar a melhor opçao dentre os andares pendentes " + andaresPendentes);
		
		// Se nao houver andares pendentes retorna
		if(andaresPendentes.size() == 0){
			semaforo.release();
			atendimentosMaisProximos = new ArrayList<Requisicao>();
			return atendimentosMaisProximos;
		}
	
		// Para cada andar pendente
		for (Integer andar : andaresPendentes) {
			// Calcula a distancia do andar atual até o andar pendente
			int distancia = Math.abs(andar - this.andarAtual);
			
			// Caso a distancia obtida seja menor do que a menor distancia guardada
			// OU 
			// A distancia obtida for igual a menor distancia guardada E a fila do andar for maior
			if ( distancia < menorDistancia || (distancia == menorDistancia && predio.getAndares().get(andar).getTamanhoFila() > maiorFila)){
				// Atualizo a menor distancia, guardo o numero do andar mais proximo e atualizo a maior fila
				menorDistancia = distancia;
				maisProximo = andar;
				maiorFila = predio.getAndares().get(andar).getTamanhoFila();
			}
		}
		
		printWriter.println("Elevador " + this.id + " escolheu atender ao andar " + maisProximo);
		System.out.println("Elevador " + this.id + " escolheu atender ao andar " + maisProximo);
		// Desloca-se até o andar mais proximo
		this.andarAtual = maisProximo;
		
		// Remove as requisicoes da fila do andar mais proximo
		atendimentosMaisProximos = predio.getAndares().get(maisProximo).forneceRequisicoes(capacidade);
		
		// Incrementa um sinal no semáforo
		semaforo.release();
		
		// Retorna
		return atendimentosMaisProximos;
	}


	// Calcula o trajeto que o elevador fara com as requisicoes colhidas do andar
	private List<Requisicao> calculaTrajeto(){
		Collections.sort(atendimentos, new RequisicaoComparator());
		int menorDistancia;
		
		// Guarda qual é a primeira e a ultima requisicao
		Requisicao primeiro = atendimentos.get(0);
		Requisicao ultimo = atendimentos.get(atendimentos.size()-1);
		
		// Calcula as distancias entre o andar atual e o primeiro e entre o andar atual e do ultimo 
		int d1 = Math.abs(andarAtual - primeiro.getAndarDestino());
		int d2 = Math.abs(andarAtual - ultimo.getAndarDestino());		
		// Salva a menor distancia 
		menorDistancia = Math.min(d1, d2);
		
		// Se for mais perto da ultima requisicao, comeca atendendo a ultima e reverte o restante da lista.
		if(menorDistancia == d2){
			Collections.rotate(atendimentos, 1);
			Collections.reverse(atendimentos.subList(1, atendimentos.size()));
		}
		
		return atendimentos;
	}
	
}
