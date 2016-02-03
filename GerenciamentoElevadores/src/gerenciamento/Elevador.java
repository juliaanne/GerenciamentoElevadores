package gerenciamento;

import java.util.ArrayList;
import java.util.Collection;
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
		
		int primeiro = atendimentos.get(0).getAndarDestino();
		int ultimo = atendimentos.get(atendimentos.size()-1).getAndarDestino();
		
		int d1 = Math.abs(andarInicial - primeiro);
		int d2 = Math.abs(andarInicial - ultimo);
		
		menorDistancia = Math.min(d1, d2);
		
		if(menorDistancia == d1){
			//TODO
		}
		
		
	}
	
}
