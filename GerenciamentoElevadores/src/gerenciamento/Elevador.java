package gerenciamento;

public class Elevador extends Thread {
	public int id;
	private int andar;
	private int capacidade;
	
	public Elevador(int id, int andar, int capacidade){
		this.id = id;
		this.andar = andar;
		this.capacidade = capacidade;
		System.out.println("Criada thread " + id);
	}

	public void run(){
		System.out.println("Rodando thread " + id);
	}
}
