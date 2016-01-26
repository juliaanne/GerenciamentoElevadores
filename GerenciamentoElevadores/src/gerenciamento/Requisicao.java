package gerenciamento;

import java.util.UUID;

public class Requisicao {
	private UUID id;
	private int andarDestino;
	
	public Requisicao(){
		this.id = UUID.randomUUID();
	}

	public int getAndarDestino() {
		return andarDestino;
	}

	public UUID getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return ("ID " + this.id + " Andar destino " + this.andarDestino);
	}

	
	public void setAndarDestino(int andarDestino) {
		this.andarDestino = andarDestino;
	}
}
