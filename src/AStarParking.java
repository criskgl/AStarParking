import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class AStarParking {
	
	public static void main(String[] args) throws IOException{
		
		long start = System.nanoTime();
		
		String[][] parkingActual;//sera nodo inicial
		String[][] parkingObjetivo;
		int st;
		int pl;

		//variables para A*
		int iteracion = 0;
		boolean exito = true;
		boolean expandir = false;
		boolean repetidoEnCerrada;
		boolean cerradaInsertada;
		boolean repetidoEnAbierta;
		NodoEstado nodoExpandir;
		
		//variables para estadisticas
		int nodosExpandidos = 0;

		/*PARSER*/
		parkingActual = getParking("src//parking-actual.init");
		parkingObjetivo = getParking("src//parking-objetivo.goal");

		st = parkingActual.length; //numero de calles
		pl = parkingActual[0].length; //numero de plazas por calle     

//													------------------------- INICIO ALGORITMO A* ----------------------------

		NodoEstado nodoInit = new NodoEstado(parkingActual, parkingObjetivo);	//1.Crear Nodo Inicial a partir del parking inicial proporcionado 
		ArrayList<NodoEstado> listaAbierta = new ArrayList<NodoEstado>(); 		//2.Crear Listas Abierta y Cerrada
		ArrayList<NodoEstado> listaCerrada = new ArrayList<NodoEstado>();
		listaAbierta.add(nodoInit); 											//3.Meter Nodo inicial en Abierta


		while(listaAbierta.isEmpty() == false || !exito){

			iteracion +=1;
			NodoEstado nodoAEvaluar = listaAbierta.remove(0); //guardar el nodo a evaluar(solo implementacion, no algoritmo)        		
			cerradaInsertada = addCerrada(listaCerrada, nodoAEvaluar);//copiar el primer elemento de lista abierta y ponerlo en cerrada
				
			System.out.println("\n\nIteracion : " + iteracion); 
			System.out.println("************");
			nodoAEvaluar.printParking();
			System.out.println("************");
			
			
			if (cerradaInsertada) {
				if (checkExito(nodoAEvaluar.parkingActual, NodoEstado.parkingFinal))
					break; //si el N es estado final --> Exito
				else {

					//para cada coche expandiremos todos sus posibles movimientos generando todos los posibles estados a partir de los posibles movimientos de un coche
					for (int i = 0; i < st; i++) {//para cada coche...
						for (int j = 0; j < pl; j++) {
							for (int k = 0; k < st; k++) {//operar con todas las demas posiciones
								for (int l = 0; l < pl; l++) {

									//---------------------------------------------------------------GENERAR ESTADOS DE MOVER A DERECHA---------------------------------------------------------------

									if (i == k) { //solo para posiciones en la misma calle (Derecha e Izquierda)
										if (j < l) { //(si posicion inicial < posicion final)

											nodoExpandir = new NodoEstado(nodoAEvaluar);
											nodosExpandidos++;
											expandir = nodoExpandir.moverDerecha(l - j, i, j);

											repetidoEnCerrada = parkingRepetidoEnLista(listaCerrada,
											nodoExpandir.parkingActual);

											//TODO Si se repiten nodos comprobar que estamos cogiendo el nodo con menos coste
											if (expandir && !repetidoEnCerrada) {
												System.out.println("\n----MOV DERECHA----");
												nodoExpandir.printParking();
												System.out.print("\nCerrada: ");
												printLista(listaCerrada);
												addAbierta(listaAbierta, nodoExpandir);
												System.out.print("\nAbierta: ");
												printLista(listaAbierta);
												System.out.println("");

												nodoExpandir.prev = nodoAEvaluar;

											}
										}

										//-----------------------------------------------------------GENERAR ESTADOS DE MOVER A IZDA-------------------------------------------------------------

										if (j > l) {
											nodoExpandir = new NodoEstado(nodoAEvaluar);
											nodosExpandidos++;
											expandir = nodoExpandir.moverIzda(j - l, i, j);

											repetidoEnCerrada = parkingRepetidoEnLista(listaCerrada,
													nodoExpandir.parkingActual);
											repetidoEnAbierta = parkingRepetidoEnLista(listaAbierta,
													nodoExpandir.parkingActual);

											//TODO Si se repiten nodos comprobar que estamos cogiendo el nodo con menos coste
											if (expandir && !repetidoEnCerrada && !repetidoEnAbierta) {

												System.out.println("\n----MOV IZDA----");
												nodoExpandir.printParking();
												System.out.print("\nCerrada: ");
												printLista(listaCerrada);
												addAbierta(listaAbierta, nodoExpandir);
												System.out.print("\nAbierta: ");
												printLista(listaAbierta);
												System.out.println("");

											}
										}
									}

									//------------------------------------------------------GENERAR ESTADOS DE CAMBIAR DE CALLE ENTRANDO DE CARA---------------------------------------------------
									if (l == 0) { //fijamos las posiciones

										nodoExpandir = new NodoEstado(nodoAEvaluar);
										nodosExpandidos++;
										expandir = nodoExpandir.moverCallePrincipio(k, i, j);

										repetidoEnCerrada = parkingRepetidoEnLista(listaCerrada,
												nodoExpandir.parkingActual);
										repetidoEnAbierta = parkingRepetidoEnLista(listaAbierta,
												nodoExpandir.parkingActual);

										//TODO Si se repiten nodos comprobar que estamos cogiendo el nodo con menos coste
										if (expandir && !repetidoEnCerrada && !repetidoEnAbierta) {
											System.out.println("\n----ENTRADA DELANTE----");
											nodoExpandir.printParking();
											System.out.print("\nCerrada: ");
											printLista(listaCerrada);
											addAbierta(listaAbierta, nodoExpandir);
											System.out.print("\nAbierta: ");
											printLista(listaAbierta);
											System.out.println("");

										}
									}

									//--------------------------------------------------------GENERAR ESTADOS DE CAMBIAR DE CALLE ENTRANDO MARCHA ATRAS-----------------------------------------

									if (l == 0) {
										nodoExpandir = new NodoEstado(nodoAEvaluar);
										nodosExpandidos++;
										expandir = nodoExpandir.moverCalleFinal(k, i, j);

										repetidoEnCerrada = parkingRepetidoEnLista(listaCerrada,
												nodoExpandir.parkingActual);
										repetidoEnAbierta = parkingRepetidoEnLista(listaAbierta,
												nodoExpandir.parkingActual);

										//TODO Si se repiten nodos comprobar que estamos cogiendo el nodo con menos coste
										if (expandir && !repetidoEnCerrada && !repetidoEnAbierta) {
											System.out.println("\n----ENTRADA DETRAS----");
											nodoExpandir.printParking();
											System.out.print("\nCerrada: ");
											printLista(listaCerrada);
											addAbierta(listaAbierta, nodoExpandir);
											System.out.print("\nAbierta: ");
											printLista(listaAbierta);
											System.out.println("");

										}
									}

								}

							}

						}

					}
				} 
			}
			sortArrayList(listaAbierta);
		}
		//end of while
		if(exito){
			Stack<NodoEstado> solucion = new Stack<NodoEstado>();
			System.out.println("\nSolucion:");

			NodoEstado nodoSolucion = listaCerrada.get(listaCerrada.size()-1);
			int paso = 0;

			while(nodoSolucion != null) {	

				solucion.push(nodoSolucion);
				nodoSolucion = nodoSolucion.prev;
			}

			NodoEstado step;
			int coste = 0;
			
			PrintWriter out = new PrintWriter(new FileWriter("out.plan"));
			
			while(!solucion.isEmpty()) {
				paso++;
				out.write(paso +",");
				step = solucion.pop();
				out.write(step.parkingActual[step.calleInicial][step.plazaInicial] + ",");
				out.write("L"+step.calleInicial+" "+"P"+step.plazaInicial+ ", ");
				out.write("L"+step.calleFinal+" "+"P"+step.plazaFinal+ ", ");
				out.write(step.costeMovimiento + "\n");
				
				System.out.println(paso);
				for(int i = 0; i < st; i++){
					for(int j = 0; j < pl; j++){
						System.out.print(step.parkingActual[i][j] + " ");
					}System.out.println("");
				}System.out.println("");

			}
			
			out.close();
			
			double estimatedTime = ((System.nanoTime() - start) / 1000000000.0);
			
			PrintWriter out2 = new PrintWriter(new FileWriter("out.info"));
			
			out2.write("Número de pasos: "+ paso+"\n");
			out2.write("Tiempo total (segs): "+ estimatedTime+ "\n");
			out2.write("Coste total: " + coste+"\n");
			out2.write("Nodos expandidos: " + nodosExpandidos+"\n");
			out2.close();
		}
	}

	public static String[][] getParking(String path) throws FileNotFoundException{
		String actual = "";
		String[][] parking;

		Scanner input = new Scanner(new File(path)); //file reader
		while(input.hasNext()) {
			String i = input.next();
			actual += i;
		}input.close();

		int st = Character.getNumericValue(actual.charAt(0)); //number of streets
		int pl = Character.getNumericValue(actual.charAt(1)); //number of parking lots per street

		actual = actual.substring(2); //clear first line of file from String
		parking = new String[st][pl];

		for(int i = 0; i < st; i++){ //fill up the parking Array
			for(int j = 0; j < pl; j++){
				parking[i][j] = actual.substring(0, 2);
				actual = actual.substring(2);                
			}   
		}return parking;

	}

	public static boolean checkExito(String[][] parkingActual, String[][] parkingObjetivo) {

		boolean result = true;
		for (int i = 0; i < parkingActual.length; i++) {
			for (int j = 0; j < parkingActual[0].length; j++) {
				if (parkingActual[i][j].compareTo(parkingObjetivo[i][j]) != 0) {
					result = false;
				}
			}
		}
		return result;
	}

	public static boolean parkingRepetidoEnLista(ArrayList<NodoEstado> lista, String[][] parking){

		boolean repetido = true;
		if(lista.isEmpty()) return false;

		int numCalles = parking.length;
		int numPlazas = parking[0].length;
		for(int posicionLista = 0; posicionLista < lista.size(); posicionLista++)
			for(int i = 0; i < numCalles; i++){
				for(int j = 0; j < numPlazas; j++){
					if(parking[i][j].compareTo(lista.get(posicionLista).parkingActual[i][j]) != 0){
						repetido = false;
						break;
					}
				}if(repetido) return true;
			}
		return repetido;
	}

	public static void sortArrayList(ArrayList<NodoEstado> lista) {
		NodoEstado temp;

		for(int i = lista.size()-1; i >= 0; i--) {
			for(int j = 0; j < i; j++) {
				if(lista.get(j).EvaluacionValue < lista.get(j + 1).EvaluacionValue) {
					temp = lista.get(j);
					lista.set(j, lista.get(j + 1));
					lista.set(j + 1, temp);
				}
			}
		}

	}

	public static void printLista(ArrayList<NodoEstado> lista){
		System.out.print("[");
		for(int posLista = 0; posLista < lista.size(); posLista++){
			for(int i = 0; i < lista.get(0).parkingActual.length; i++){
				for(int j = 0; j < lista.get(0).parkingActual[0].length; j++){
					System.out.print(lista.get(posLista).parkingActual[i][j]+" ");
				}
			}System.out.print(" , ");	
		}System.out.print("]");
	}

	public static void addAbierta(ArrayList<NodoEstado> lista, NodoEstado nodo) {
		int cont = 0;
		while(cont < lista.size() && lista.get(cont).HeuristicaValue < nodo.HeuristicaValue) {
			cont++;
		}
		lista.add(cont, nodo);
	}
	
	public static boolean addCerrada(ArrayList<NodoEstado> lista, NodoEstado nodo) {
		int cont = 0;
		while(cont < lista.size()) {
			if(equalNode(nodo, lista.get(cont))) return false;
				cont++;
		}
		lista.add(nodo);
		return true;
	}
	
	public static boolean equalNode(NodoEstado nodo1, NodoEstado nodo2){
		for(int i = 0; i<nodo1.parkingActual.length; i++ ) {
			for(int j = 0; j<nodo1.parkingActual[0].length; j++ ) {
				if(nodo1.parkingActual[i][j].compareTo(nodo2.parkingActual[i][j]) != 0) return false; 
			}
		}
		return true;
	}

}
