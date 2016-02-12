package gerenciamento;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Principal{
	
	static int nthreads;
	public static int andares;
	public static int elevadores;
	public static int capacidade;
	public static Integer andaresIniciais[];
	
	public static Predio predio;
	
	public static void leEntrada() throws FileNotFoundException{
		// TODO: Por linha de comando
		File entrada = new File(".//src/entrada.txt");		
		
		Scanner sc = new Scanner(entrada); 
		andares = sc.nextInt();
		elevadores = sc.nextInt();
		capacidade = sc.nextInt();
		
		predio = new Predio(andares);
		andaresIniciais = new Integer[elevadores];
		
		for (int i = 0; i < elevadores; i++) {
			andaresIniciais[i] = sc.nextInt();
		}
		
		for (int i = 0; i < andares; i++) {
			int pessoasNoAndar = sc.nextInt();
			for (int j = 0; j < pessoasNoAndar; j++) {
				Requisicao requisicao = new Requisicao();
				int andarDestino = sc.nextInt();
				requisicao.setAndarDestino(andarDestino);
				predio.getAndares().get(i).getFila().add(requisicao); // Essa orientacao a obj ta meio zuada, nao? 
																	  // A fila é private e podemos adc requisicao?
			}
		}
		
		sc.close();
	}
	
	public static void validaEntrada(){
		// TODO: Validar a entrada na moral e ver se não está rolando contradições dentro da própria entrada fornecida
		// TODO: MUITO SERIO PRECISAMOS VALIDAR ESSAS ENTRADA FDP!
		if(andares < 5 || andares > 100 ){
			System.out.println("Elevadores devem ser de 5 a 100");
			System.exit(0);
		}
		
		if(elevadores < 5 || elevadores > 20 ){
			System.out.println("Elevadores devem ser de 5 a 20");
			System.exit(0);
		}
		
		if(capacidade < 5 || capacidade > 20 ){
			System.out.println("Capacidade deve ser de 5 a 20");
			System.exit(0);
		}
	}
	
	public static void main (String[] args) throws FileNotFoundException {
		//System.out.println("O programa iniciou");
		
		leEntrada();
		validaEntrada();
		
		nthreads = elevadores;
		
		// Reservando espaço para vetor de threads
		Thread[] threads = new Thread[nthreads];
	
		
		// Criando threads
		for (int i=0; i<threads.length; i++){

			threads[i] = new Elevador(i, andaresIniciais[i], capacidade, predio);
		}
	
		// Iniciando threads
		for (int i=0; i<threads.length; i++){
			
			threads[i].start();
		}
	
		// Esperando threads
		for (int i=0; i<threads.length; i++){
			try { 
				threads[i].join(); 
			} catch (InterruptedException e) 
			{ 
				return; 
			}
		}
	
		//System.out.println("O programa terminou");
		
		
	}
	
}
