package gerenciamento;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Elevador extends Thread {
	public int id;
	private int andarInicial;
	private static int capacidade;
	private static Predio predio;
	private static Semaphore semaforo = new Semaphore(1);
	private List<Requisicao> atendimentos = new ArrayList<Requisicao>();
	
	// Contrutor de elevador com o seu id, andar inicial, capacidade e Predio
	public Elevador(int id, int andar, int capacidade, Predio predio){
		this.id = id;
		this.andarInicial = andar;
		Elevador.capacidade = capacidade;
		Elevador.predio = predio;
	}

	
	// Metodo chamado quando as threads startam
	public void run(){
		//System.out.println("Elevador " + id + " iniciou no andar " + andarInicial);
		
		// Enquanto ha requisicoes pendentes em algum lugar do edificio:
		while(predio.requisicaoPendente()){
			// Seleciona as requisicoees ate sua capacidade do andar mais próximo
			atendimentos =  requisicoesMaisProximas();
			
			// Calcula trajeto
			atendimentos = calculaTrajeto();
			System.out.println("Elevador " + this.id + " no andar Inicial " + andarInicial +" atendera para os seguintes destinos: " + atendimentos);
			
			// Desloca-se parando nos andares destinos de suas requisicoes
			// Retorna o andar final apos o trajeto
			// TODO percorreTrejeto
			this.andarInicial = percorreTrajeto(atendimentos);
			break;
		}
		
	}
	
	private int percorreTrajeto(List<Requisicao> atendimentos2) {
		// TODO Auto-generated method stub
		return 0;
	}


	private List<Requisicao> requisicoesMaisProximas(){
		int primeiraOpcao = Integer.MAX_VALUE, tamanhoPrimeira;
		int segundaOpcao = Integer.MAX_VALUE, tamanhoSegunda;
		List<Requisicao> atendimentosPrimeira;
		
		try {
			semaforo.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Se ha requisicao no proprio andar que ele esta, atende
		if( predio.getAndares().get(this.andarInicial).getTamanhoFila() != 0 ){
			System.out.println("Elevador " + this.id + " pegou as pessoas do seu andar inicial " + this.andarInicial);
			primeiraOpcao = this.andarInicial;
		} else {
			// Procura requisicoes no predio
			List<Integer> andaresPendentes = predio.andaresPendentes();
			System.out.println("Elevador " + this.id + " vai procurar a melhor opçao");
			
			for (Integer andar : andaresPendentes) {
				System.out.println("Elevador " + this.id + " esta procurando andares pendentes");
				// Se o andar atual é IGUAL a opcao que eu ja tenho, entao devo reservar pois pode ser igual
				if(Math.abs(andar - this.andarInicial) == primeiraOpcao) segundaOpcao = andar;
				
				// Se o andar atual é menor do que a opcao que eu tenho, entao esta passa a ser a melhor opcao
				if(Math.abs(andar - this.andarInicial) < primeiraOpcao) primeiraOpcao = andar;
				
			}
			
			System.out.println("Elevador " + this.id + " pode atender ao andar " + primeiraOpcao + " ou ao andar " + segundaOpcao);
			
			if(primeiraOpcao == segundaOpcao){
				System.out.println("Elevador " + this.id + " está escolhendo qual tem mais pessoas na fila");
				// Caso as distancias sejam as mesmas
				tamanhoPrimeira = predio.getAndares().get(primeiraOpcao).getTamanhoFila();
				tamanhoSegunda = predio.getAndares().get(segundaOpcao).getTamanhoFila();
				// Escolho a com mais mais requisicoes
				if(tamanhoSegunda > tamanhoPrimeira)
					primeiraOpcao = segundaOpcao;
			}
		}
		
		// Remove as requisicoes da fila
		atendimentosPrimeira = predio.getAndares().get(primeiraOpcao).forneceRequisicoes(capacidade);
		
		System.out.println("Elevador " + this.id + " escolheu atender ao andar " + primeiraOpcao);
		
		semaforo.release();
		
		//System.out.println(atendimentosPrimeira);
		
		return atendimentosPrimeira;
	}


	// Calcula o trajeto que o elevador fara com as requisicoes colhidas do andar
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
