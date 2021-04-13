package src.p03.c01;

import java.util.Enumeration;
import java.util.Hashtable;

public class Parque implements IParque{


	// TODO 
	private int aforo_maximo;
	private int contadorPersonasTotales;
	private Hashtable<String, Integer> contadoresPersonasPuerta;
	
	
	public Parque(int aforo) {	// TODO
		contadorPersonasTotales = 0;
		contadoresPersonasPuerta = new Hashtable<String, Integer>();
		// TODO
		aforo_maximo=aforo;
	}


	@Override
	public synchronized void entrarAlParque(String puerta){		// TODO
		
		// Si no hay entradas por esa puerta, inicializamos
		if (contadoresPersonasPuerta.get(puerta) == null){
			contadoresPersonasPuerta.put(puerta, 0);
		}
		/*
		// TODO
		try {
			comprobarAntesDeEntrar();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		*/
		// Aumentamos el contador total y el individual
		contadorPersonasTotales++;		
		contadoresPersonasPuerta.put(puerta, contadoresPersonasPuerta.get(puerta)+1);
		
		// Imprimimos el estado del parque
		imprimirInfo(puerta, "Entrada");
		
		// TODO
		checkInvariante();
		
		// TODO
		notify();
	}
	
	// 
	// TODO M�todo salirDelParque
	//
	@Override
	public synchronized void salirDelParque(String puerta){		// TODO
		
		
	/*	// TODO
		try {
			comprobarAntesDeSalir();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
*/		
		// Aumentamos el contador total y el individual
		contadorPersonasTotales--;		
		contadoresPersonasPuerta.put(puerta, contadoresPersonasPuerta.get(puerta)-1);
		
		// Imprimimos el estado del parque
		imprimirInfo(puerta, "Salida");
		
		// TODO
		checkInvariante();
		
		// TODO
		notify();
	}
	
	private void imprimirInfo (String puerta, String movimiento){
		System.out.println(movimiento + " por puerta " + puerta);
		System.out.println("--> Personas en el parque " + contadorPersonasTotales); //+ " tiempo medio de estancia: "  + tmedio);
		
		// Iteramos por todas las puertas e imprimimos sus entradas
		for(String p: contadoresPersonasPuerta.keySet()){
			System.out.println("----> Por puerta " + p + " " + contadoresPersonasPuerta.get(p));
		}
		System.out.println(" ");
	}
	
	private int sumarContadoresPuerta() {
		int sumaContadoresPuerta = 0;
			Enumeration<Integer> iterPuertas = contadoresPersonasPuerta.elements();
			while (iterPuertas.hasMoreElements()) {
				sumaContadoresPuerta += iterPuertas.nextElement();
			}
		return sumaContadoresPuerta;
	}
	
	protected void checkInvariante() {
		assert sumarContadoresPuerta() == contadorPersonasTotales : "INV: La suma de contadores de las puertas debe ser igual al valor del contador del parte";
		// TODO 
		assert contadorPersonasTotales <= aforo_maximo : "INV: El n� de personas en el parque no puede superar el aforo";
		// TODO
		assert contadorPersonasTotales >= 0 : "INV: El n� de personas en el parque no puede ser menor que 0";
	}

	protected void comprobarAntesDeEntrar() throws InterruptedException{	// TODO
		//
		// TODO
		//
		while(contadorPersonasTotales==aforo_maximo)
			wait();
	}

	protected void comprobarAntesDeSalir() throws InterruptedException{		// TODO
		//
		// TODO
		//
		while(contadorPersonasTotales==0)
			wait();
	}


}