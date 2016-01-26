package gerenciamento;

public class Elevador extends Thread {
	public int id;
	
	public Elevador(int id){
		this.id = id;
		System.out.println("Criada thread " + id);
	}

	public void run(){
		System.out.println("Rodando thread " + id);
	}
}
