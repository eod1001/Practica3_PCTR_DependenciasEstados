package src.p03.c01;

import java.util.Enumeration;
import java.util.Hashtable;

public class Parque implements IParque{


	// La variable aforo_maximo indica el numero maximo de personas que entra en el parque 
	private int aforo_maximo;
	private int contadorPersonasTotales;
	private Hashtable<String, Integer> contadoresPersonasPuerta;
	
	
	public Parque(int aforo) {	//El constructor recibe como parámetro el aforo máximo del parque
		contadorPersonasTotales = 0;
		contadoresPersonasPuerta = new Hashtable<String, Integer>();
		//Asignamos a la variable aforo maximo el dato pasado como argumento
		aforo_maximo=aforo;
	}


	@Override
	public synchronized void entrarAlParque(String puerta){	//El metodo entrarAlParque debe estar sincronizado para evitar dos accesos del parque a la vez
		
		// Si no hay entradas por esa puerta, inicializamos
		if (contadoresPersonasPuerta.get(puerta) == null){
			contadoresPersonasPuerta.put(puerta, 0);
		}
		
		//Comprobación de las pre-condiciones
		try {
			comprobarAntesDeEntrar();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
		
		// Aumentamos el contador total y el individual
		contadorPersonasTotales++;		
		contadoresPersonasPuerta.put(puerta, contadoresPersonasPuerta.get(puerta)+1);
		
		// Imprimimos el estado del parque
		imprimirInfo(puerta, "Entrada");
		
		// Comprobación de post-condiciones
		checkInvariante();
		
		// Notificamos al resto de hilos que estan en espera
		notifyAll();
	}
	
	// 
	// Método salirDelParque
	//
	@Override
	public synchronized void salirDelParque(String puerta){ //El metodo salirDelParque debe estar sincronizado para evitar dos salidas del parque a la vez
		
		// Si no hay entradas o salidas por esa puerta, inicializamos
				if (contadoresPersonasPuerta.get(puerta) == null){
					contadoresPersonasPuerta.put(puerta, 0);
				}
			
		// Comprobacion pre-condiciones
		try {
			comprobarAntesDeSalir();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
		
		// Disminuimos el contador total y el individual
		contadorPersonasTotales--;		
		contadoresPersonasPuerta.put(puerta, contadoresPersonasPuerta.get(puerta)-1);
		
		// Imprimimos el estado del parque
		imprimirInfo(puerta, "Salida");
		
		// Comprobacion de las post-condiciones
		checkInvariante();
		
		// // Notificamos al resto de hilos que estan en espera
		notifyAll();
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
		// El número de personas en el parque nunca podrá superar el aforo máximo permitido 
		assert contadorPersonasTotales <= aforo_maximo : "INV: El nº de personas en el parque no puede superar el aforo";
		// El número de personas en el parque no puede ser menor que 0, es fisicamente imposible
		assert contadorPersonasTotales >= 0 : "INV: El nº de personas en el parque no puede ser menor que 0";
	}

	protected void comprobarAntesDeEntrar() throws InterruptedException{
		//
		// En caso de no haber espacio en el parque, el hilo se queda en espera hasta que haya una nueva salida. 
		//
		while(contadorPersonasTotales==aforo_maximo)
			wait();
	}

	protected void comprobarAntesDeSalir() throws InterruptedException{		// TODO
		//
		// En caso de no haber personas en el parque, el hilo se queda en espera hasta que haya una nueva entrada
		//
		while(contadorPersonasTotales==0)
			wait();
	}


}