package gerenciamento;

import java.util.UUID;

public class Requisicao  {
	private UUID id;
	private int andarDestino;
	
	// Construtor de Requisicao com uma id única
	public Requisicao(){
		this.id = UUID.randomUUID();
	}

	// Getters
	public int getAndarDestino() {
		return andarDestino;
	}

	public UUID getId() {
		return id;
	}
	
	// Impressão do destino
	@Override
	public String toString() {
		//return ("ID da requisição " + this.id + " com andar destino " + this.andarDestino);
		return ("Destino: "+ this.andarDestino);
	}

	// Setters
	public void setAndarDestino(int andarDestino) {
		this.andarDestino = andarDestino;
	}

}
