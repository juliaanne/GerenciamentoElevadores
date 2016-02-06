package gerenciamento;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Elevador extends Thread {
	public int id;
	private int andarInicial;
	private int capacidade;
	private Predio predio;
	private List<Requisicao> atendimentos = new ArrayList<Requisicao>();
	
	public Elevador(int id, int andar, int capacidade, Predio predio){
		this.id = id;
		this.andarInicial = andar;
		this.capacidade = capacidade;
		this.predio = predio;
	}

	public void run(){
		System.out.println("Elevador: " + id + " Iniciou no andar:" + andarInicial);
		atendimentos = predio.getAndares().get(andarInicial).forneceRequisicoes(capacidade);
		// System.out.println(atendimentos);
		
		calculaTrajeto();
	}
	
	public void calculaTrajeto(){
		Collections.sort(atendimentos,new RequisicaoComparator());
		int menorDistancia;
		
		Requisicao primeiro = atendimentos.get(0);
		Requisicao ultimo = atendimentos.get(atendimentos.size()-1);
		
		int d1 = Math.abs(andarInicial - primeiro.getAndarDestino());
		int d2 = Math.abs(andarInicial - ultimo.getAndarDestino());
		
		menorDistancia = Math.min(d1, d2);
		
		if(menorDistancia == d2){
			Collections.rotate(atendimentos, 1);
			Collections.reverse(atendimentos.subList(1, atendimentos.size()));
		}
		
		System.out.println("Elevador: " + this.id + " Andar Inicial " + andarInicial +" Atendimentos " + atendimentos);
	}
	
}
