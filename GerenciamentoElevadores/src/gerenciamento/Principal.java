package gerenciamento;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal{
	static int nthreads;
	public static int andares;
	public static int elevadores;
	public static int capacidade;
	private static List<Integer> andaresAtuais = new ArrayList<>();
	private static PrintWriter printWriter;
	private static File arquivo;
	
	public static Predio predio;
	
	public static void leEntrada(String[] args) throws FileNotFoundException{
		Scanner sc;
		
		// Lendo entradas tanto pelo eclipse quanto pela linha de comando
	    if (args!=null && args.length>0 && args[0].equals("-d")){
	        sc = new Scanner(new File(args[1]));
	    } else {
	        sc = new Scanner(System.in);
	    }
		
	    // Colhendo andares, elevadores e capacidade, respectivamente
	    String aec = sc.nextLine();
	    validaAEC(aec);
	    
		// Instanciando andares iniciais
		String andaresIniciais = sc.nextLine();
		validaAndaresIniciais(andaresIniciais);
		
		// Instanciando predio
		predio = new Predio(andares);
		
		int andar = 0;
		while(sc.hasNextLine()){
			String requisicoes = sc.nextLine();
			validaRequisicoes(requisicoes, andar);
			andar++;
			validaAndares(andar);
		}
		
		validaQuantidadeAndares(andar);
		
		sc.close();
	}
	
	private static void validaAndares(int qtdeAndares) {
		if(qtdeAndares > andares){
			printWriter.println("Na entrada, não deve existir requisicoes sobrando");
			System.out.println("Na entrada, não deve existir requisicoes sobrando");
			System.exit(0);
		}	
	}

	private static void validaQuantidadeAndares(int qtdeAndares) {
		if(qtdeAndares != andares){
			printWriter.println("Na entrada, todos os andares necessitam possuir requisicoes.");
			System.out.println("Na entrada, todos os andares necessitam possuir requisicoes.");
			System.exit(0);
		}
	}

	private static void validaRequisicoes(String requisicoes, int andarAtual) {
		String[] requisicoesSplited = requisicoes.split(" ");
		
		int quantidadeRequisicoes = Integer.parseInt(requisicoesSplited[0]);
		
		if(requisicoesSplited.length != quantidadeRequisicoes+1){
			printWriter.println("Na entrada, use #pessoasNaFila e os respectivos destinos de CADA pessoa na fila");
			System.out.println("Na entrada, use #pessoasNaFila e os respectivos destinos de CADA pessoa na fila");
			System.exit(0);
		}
		
		for(int i = 1; i < quantidadeRequisicoes; i++){
			int andarDestino = Integer.parseInt(requisicoesSplited[i]);
			Requisicao requisicao = new Requisicao();
			requisicao.setAndarDestino(andarDestino);
			predio.getAndares().get(andarAtual).getFila().add(requisicao);
		}
		
	}

	private static void validaAndaresIniciais(String andaresIniciais) {
		String[] andaresSplited = andaresIniciais.split(" ");
		
		if(andaresSplited.length != elevadores){
			printWriter.println("Na entrada, defina andares iniciais para CADA elevador");
			System.out.println("Na entrada, defina andares iniciais para CADA elevador");
			System.exit(0);
		}
		
		for (int i=0; i < elevadores; i++) {
			int andarInicialAtual = Integer.parseInt(andaresSplited[i]); 
			
			if(andarInicialAtual >= andares){
				printWriter.println("Na entrada, os andares iniciais devem existir no predio");
				System.out.println("Na entrada, os andares iniciais devem existir no predio");
				System.exit(0);
			}
			
			andaresAtuais.add(andarInicialAtual);
			
		}
		
	}

	public static void validaAEC(String aec){
		String[] aecSplited = aec.split(" ");
		
		if(aecSplited.length != 3){
			printWriter.println("Na entrada, use #andares #elevadores #capacidade");
			System.out.println("Na entrada, use #andares #elevadores #capacidade");
			System.exit(0);
		}
		
		andares = Integer.parseInt(aecSplited[0]);
		elevadores = Integer.parseInt(aecSplited[1]);
		capacidade = Integer.parseInt(aecSplited[2]);
		
		if(andares < 5 || andares > 100 ){
			printWriter.println("Elevadores devem ser de 5 a 100");
			System.out.println("Elevadores devem ser de 5 a 100");
			System.exit(0);
		}
		
		if(elevadores < 5 || elevadores > 20 ){
			printWriter.println("Elevadores devem ser de 5 a 20");
			System.out.println("Elevadores devem ser de 5 a 20");
			System.exit(0);
		}
		
		if(capacidade < 5 || capacidade > 20 ){
			printWriter.println("Capacidade deve ser de 5 a 20");
			System.out.println("Capacidade deve ser de 5 a 20");
			System.exit(0);
		}
	}
	
	public static void main (String[] args) throws IOException {
		
		arquivo = new File("principal.txt");
		printWriter = new PrintWriter(arquivo);
		
		printWriter.println("O SGE iniciou...");
		System.out.println("O SGE iniciou...");
		
		leEntrada(args);
		
		nthreads = elevadores;
		
		// Reservando espaço para vetor de threads
		Thread[] threads = new Thread[nthreads];
		
		// Criando threads
		for (int i=0; i<threads.length; i++){
			threads[i] = new Elevador(i, andaresAtuais.get(i), capacidade, predio);
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
		printWriter.println("O SGE finalizou!");			
		System.out.println("O SGE finalizou!");
		printWriter.close();
	}
	
}
