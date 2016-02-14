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
	public void run() {
		// System.out.println("Elevador " + id + " iniciou no andar " +
		// andarInicial);
		
		

		// Enquanto ha requisicoes pendentes em algum lugar do edificio:
		while (predio.requisicaoPendente()) {
			// Seleciona as requisicoes ate sua capacidade do andar mais
			// próximo
			atendimentos = requisicoesMaisProximas();
			if (atendimentos.size() == 0) {
				printWriter.println("Elevador " + this.id + " terminou no andar " + this.andarAtual);
				System.out.println("Elevador " + this.id + " terminou no andar " + this.andarAtual);
				break;
			}

			// Calcula trajeto
			atendimentos = calculaTrajeto();
			printWriter.println("Elevador " + this.id + " no andar " + andarAtual
					+ " atendera para os seguintes destinos: " + atendimentos);
			System.out.println("Elevador " + this.id + " no andar " + andarAtual
					+ " atendera para os seguintes destinos: " + atendimentos);

			// Desloca-se parando nos andares destinos de suas requisicoes e
			// retorna o andar final apos o trajeto
			this.andarAtual = percorreTrajeto(atendimentos);
			printWriter.println("Elevador " + this.id + " terminou no andar " + this.andarAtual);
			System.out.println("Elevador " + this.id + " terminou no andar " + this.andarAtual);
		}
		printWriter.close();
	}

	private int percorreTrajeto(List<Requisicao> atendimentosAtuais) {
		int andarFinal = predio.getNumeroAndares();

		for (Requisicao requisicao : atendimentosAtuais) {
			printWriter.println("Elevador " + this.id + " parou no andar " + requisicao.getAndarDestino()
			+ " para deixar um passageiro.");
			System.out.println("Elevador " + this.id + " parou no andar " + requisicao.getAndarDestino()
					+ " para deixar um passageiro.");
			andarFinal = requisicao.getAndarDestino();

			try {
				sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

		atendimentos.clear();

		return andarFinal;
	}

	private List<Requisicao> requisicoesMaisProximas() {
		int maisProximo = predio.getNumeroAndares(), maiorFila = 0;
		List<Requisicao> atendimentosMaisProximos;

		try {
			semaforo.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Procura requisicoes no predio
		List<Integer> andaresPendentes = predio.andaresPendentes();
		printWriter.println("Elevador " + this.id + " esta no andar " + this.andarAtual
				+ " vai procurar a melhor opçao dentre os andares pendentes " + andaresPendentes);
		System.out.println("Elevador " + this.id + " esta no andar " + this.andarAtual
				+ " vai procurar a melhor opçao dentre os andares pendentes " + andaresPendentes);

		if (andaresPendentes.size() == 0) {
			semaforo.release();
			atendimentosMaisProximos = new ArrayList<Requisicao>();
			return atendimentosMaisProximos;
		}

		if(predio.getAndares().get(this.andarAtual).getFila().size() > 0){
			maisProximo = this.andarAtual;
		}else{
			for (Integer andar : andaresPendentes) {
				
				
				int distancia = Math.abs(andar - this.andarAtual);
				if (distancia < maisProximo
						|| (distancia == maisProximo && predio.getAndares().get(andar).getTamanhoFila() > maiorFila)) {
					maisProximo = andar;
					maiorFila = predio.getAndares().get(andar).getTamanhoFila();
				}
			}
		}
		printWriter.println("Elevador " + this.id + " escolheu atender ao andar " + maisProximo);
		System.out.println("Elevador " + this.id + " escolheu atender ao andar " + maisProximo);
		this.andarAtual = maisProximo;

		// Remove as requisicoes da fila
		atendimentosMaisProximos = predio.getAndares().get(maisProximo).forneceRequisicoes(capacidade);

		semaforo.release();

		// System.out.println(atendimentosPrimeira);

		return atendimentosMaisProximos;
	}

	// Calcula o trajeto que o elevador fara com as requisicoes colhidas do
	// andar
	private List<Requisicao> calculaTrajeto() {
		Collections.sort(atendimentos, new RequisicaoComparator());
		int menorDistancia;

		Requisicao primeiro = atendimentos.get(0);
		Requisicao ultimo = atendimentos.get(atendimentos.size() - 1);

		int d1 = Math.abs(andarAtual - primeiro.getAndarDestino());
		int d2 = Math.abs(andarAtual - ultimo.getAndarDestino());
		menorDistancia = Math.min(d1, d2);

		// Se for mais perto da ultima requisicao, comeca atendendo a ultima e
		// reverte o restante da lista.
		if (menorDistancia == d2) {
			Collections.rotate(atendimentos, 1);
			Collections.reverse(atendimentos.subList(1, atendimentos.size()));
		}

		return atendimentos;
	}

}
