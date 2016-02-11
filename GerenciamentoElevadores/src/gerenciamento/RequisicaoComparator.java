package gerenciamento;

import java.util.Comparator;

public class RequisicaoComparator implements Comparator<Requisicao> {

	// Classe de comparação para ordenação das Requisicoes
	// Usada no método caculaTrajeto da classe Elevador
	
	@Override
	public int compare(Requisicao req1, Requisicao req2) {

		int distancia = req1.getAndarDestino() - req2.getAndarDestino();
		return distancia;
	}

}
