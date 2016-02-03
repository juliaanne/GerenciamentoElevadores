package gerenciamento;

import java.util.UUID;

public class Requisicao  {
	private UUID id;
	private int andarDestino;
	
	public Requisicao(){
		this.id = UUID.randomUUID();
	}
	
	public Requisicao(int i){
		this.id = UUID.randomUUID();
		this.andarDestino = i;
	}

	public int getAndarDestino() {
		return andarDestino;
	}

	public UUID getId() {
		return id;
	}
	
	@Override
	public String toString() {
//		return ("ID " + this.id + " Andar destino " + this.andarDestino);
		return ("Destino: "+ this.andarDestino);
	}

	
	public void setAndarDestino(int andarDestino) {
		this.andarDestino = andarDestino;
	}

}
