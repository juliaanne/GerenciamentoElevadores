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

	
	// M�todo chamado quando as threads startam
	public void run(){
		System.out.println("Elevador " + id + " iniciou no andar " + andarInicial);
		
		// Enquanto h� requisi��es pendentes em algum lugar do edif�cio:
		
		//TODO Esse m�todo aqui tem  um problema, ele tem que ser syncronized
		while(predio.requisicaoPendente()){
			
			// Desloca-se at� este andar, decidindo qual andar mais proximo
			int maisProximo = calculaAndarProximo();

			// Seleciona as requisi��es at� sua capacidade
			atendimentos = predio.getAndares().get(maisProximo).forneceRequisicoes(capacidade);

			// Calcula trajeto
			atendimentos = calculaTrajeto();
			System.out.println("Elevador " + this.id + " no andar Inicial " + andarInicial +" atender� para os seguintes destinos: " + atendimentos);
			
			// Desloca-se parando nos andares destinos de suas requisi��es
			// Retorna o andar final ap�s o trajeto
			// ToDo percorreTrejeto
			int andarFinal = percorreTrajeto(atendimentos);
			
			// Setar andar final para andar inicial.
			this.andarInicial = andarFinal; 
		}
		
	}
	
	private int percorreTrajeto(List<Requisicao> atendimentos2) {
		// TODO Auto-generated method stub
		return 0;
	}




	// Calcula o trajeto que o elevador far� com as requisi��es colhidas do andar
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
