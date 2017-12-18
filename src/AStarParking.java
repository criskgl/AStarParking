import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Stack.*;
import java.util.Scanner;
import java.util.Stack;

public class AStarParking {

    public static void main(String[] args) throws FileNotFoundException {
        // TODO Auto-generated method stub


        /*PARSER*/

        String actual = "";
        String objetivo = "";

        Scanner input = new Scanner(new File("src//parking-actual.init"));

        while(input.hasNext()) {
            String i = input.next();
            actual += i;
        }

        input = new Scanner(new File("src//parking-objetivo.goal"));

        while(input.hasNext()) {
            String i = input.next();
            objetivo += i;
        }

        int st = Character.getNumericValue(actual.charAt(0)); //number of streets
        int pl = Character.getNumericValue(actual.charAt(1)); //number of parking lots per street

        actual = actual.substring(2);
        objetivo = objetivo.substring(2);

        System.out.println("ACTUAL: "+actual);
        System.out.println("OBJETIVO: "+objetivo);
        System.out.println(st);
        System.out.println(pl);

        /**/        

        /*crear una matriz para cada parking*/
        Coche[][] parkingActual = new Coche[st][pl];//será nodo inicial
        Coche[][] parkingObjetivo = new Coche[st][pl];

        /*llenar cada matriz con sus respectivos coches*/
        /*Llenado PK inicial*/

        for(int i = 0; i < st; i++){
            for(int j = 0; j < pl; j++){


                Coche carActual = new Coche();//crear un nuevo coche
                /*asignar fila, columna actuales y coches*/             
                carActual.car = actual.substring(0, 2);
                carActual.rowNow = i;
                carActual.columnNow = j;
                parkingActual[i][j] = carActual;

                /*acortar la entrada*/
                actual = actual.substring(2);
            }   
        }

        /*Llenado PK objetivo*/
        for(int i = 0; i < st; i++){
            for(int j = 0; j < pl; j++){


                Coche carActual = new Coche();//crear un nuevo coche
                /*asignar fila, columna actuales y coches*/             
                carActual.car = objetivo.substring(0, 2);
                carActual.rowNow = i;
                carActual.columnNow = j;
                parkingObjetivo[i][j] = carActual;

                /*acortar la entrada*/
                objetivo = objetivo.substring(2);
            }   
        }

        System.out.println("ORDEN COCHES EN PARKING ACTUAL");
        for(int i = 0; i < st; i++){
            for(int j = 0; j < pl; j++){

                System.out.print(parkingActual[i][j].car+ " ");
            }
            System.out.println("");
        }

        System.out.println("ORDEN COCHES EN PARKING OBJETIVO");
        for(int i = 0; i < st; i++){
            for(int j = 0; j < pl; j++){

                System.out.print(parkingObjetivo[i][j].car+ " ");

            }
            System.out.println("");
        }


        /*INICIO ALGORITMO A* */

        //1.Crear Nodo Inicial a partir del parking inicial proporcionado
        NodoEstado nodoInit = new NodoEstado(parkingActual, parkingObjetivo);
        //2.Crear Listas Abierta y Cerrada
        ArrayList<NodoEstado> listaAbierta = new ArrayList<NodoEstado>();
        ArrayList<NodoEstado> listaCerrada = new ArrayList<NodoEstado>();
        //3.Meter Nodo inicial en Abierta
        listaAbierta.add(nodoInit);

        int iteracion = 0;
        boolean exito = true;
        boolean expandir = false;

        while(listaAbierta.isEmpty() == false || !exito){

        	iteracion +=1;
        	NodoEstado nodoAEvaluar = listaAbierta.get(listaAbierta.size()-1);//guardar el nodo a evaluar(solo implementacion, no algoritmo)        		
        		
            listaCerrada.add(nodoAEvaluar);//copiar el primer elemento de lista abierta y ponerlo en cerrada
            listaAbierta.remove(0);//quitar el elemento copiado a cerrada de lista abierta
            System.out.println("Iteracion : " +iteracion); 
            System.out.println("************");
            nodoAEvaluar.printParking();
            System.out.println("************");
            
            //si el N es estado final --> Exito
            exito = checkExito(nodoAEvaluar.parkingActual, nodoAEvaluar.parkingFinal);
            if(exito) break;
            else{
                //para cada coche expandiremos todos sus posibles movimientos generando 
                //todos los posibles estados a partir de los posibles movimientos de un coche
                for(int i = 0; i < st; i++ ){
                    for(int j = 0; j < pl; j++ ){//para cada coche...

                    	
                    	for(int k = 0; k < st; k++ ){//operar con todas las demas posiciones
                            for(int l = 0; l < pl; l++ ){
                            	
                                int movimientosDer = 1;
                                int movimientosIzq = 1;
                                int calleEntraFrente = 0;
                                int calleEntraAtras = 0;
                            
                            	
	                           if(k != i || l != j){//Si no se trada de la misma casilla del coche que está siendo evaluado
	                        	   
	                        	   //GENERAR ESTADOS DE MOVER A DERECHA
	                        	   while(movimientosDer < pl){
	                        		   	
	                        		   /*
	                        		   	NodoEstado nodoAEvaluarDuplicate  =  new NodoEstado(nodoAEvaluar);
	                        	       */	                        	      
	                        		   
		                        		NodoEstado nodoAEvaluarDuplicate = new NodoEstado(nodoAEvaluar);

	                        		   expandir = nodoAEvaluarDuplicate.moverDerecha(movimientosDer, i, j);
	                        	       
	                        	        boolean repetidoEnCerrada = parkingRepetidoEnLista(listaCerrada, nodoAEvaluarDuplicate.parkingActual);
	                        	        boolean repetidoEnAbierta = parkingRepetidoEnLista(listaAbierta, nodoAEvaluarDuplicate.parkingActual);
	                        	        
	                        	        //TODO Si se repiten nodos comprobar que estamos cogiendo el nodo con menos coste
	                        	        if(expandir && !repetidoEnCerrada && !repetidoEnAbierta) {
	                        	        	
	                        	        	nodoAEvaluarDuplicate.printParking();
	                        	        	printLista(listaCerrada);
	                        	        	System.out.println("");
	                        	        	printLista(listaAbierta);
	                        	        	System.out.println("");
	                        	        	
	                        	        	System.out.println("^^^^MOV DERECHA^^^");
	                        	        	nodoAEvaluarDuplicate.prev = nodoAEvaluar;
		                        	        listaAbierta.add(0, nodoAEvaluarDuplicate);
	                        	        }
	                        	        
	                        	        /*Para cada s de S que estuviera ya en ABIERTA o CERRADA
	                        	        decidir si redirigir o no sus punteros hacia N
	                        	        Para cada s de S que estuviera ya en CERRADA
	                        	        decidir si redirigir o no los punteros de los nodos en sus sub�arboles
	                        	        Reordenar ABIERTA segun f (n)*/
	                        	        
	                        		   movimientosDer ++;
	                        	   }
	                        	   
	                        	   //GENERAR ESTADOS DE MOVER A IZDA
	                        	   while(j - movimientosIzq > 0){
	                        		   	
	                        		   NodoEstado nodoAEvaluarDuplicate = new NodoEstado(nodoAEvaluar);                      	    
	                        	        
	                        	        expandir = nodoAEvaluarDuplicate.moverIzda(movimientosIzq, i, j);
	                        	        
	                        	        
	                        	        boolean repetidoEnCerrada = parkingRepetidoEnLista(listaCerrada, nodoAEvaluarDuplicate.parkingActual);
	                        	        boolean repetidoEnAbierta = parkingRepetidoEnLista(listaAbierta, nodoAEvaluarDuplicate.parkingActual);
	                        	        
	                        	        //TODO Si se repiten nodos comprobar que estamos cogiendo el nodo con menos coste
	                        	        if(expandir && !repetidoEnCerrada && !repetidoEnAbierta) {
	                        	        	
	                        	        	nodoAEvaluarDuplicate.printParking();
	                        	        	printLista(listaCerrada);
	                        	        	System.out.println("");
	                        	        	printLista(listaAbierta);
	                        	        	System.out.println("");
	                        	        	System.out.println("^^^^MOV IZDA^^^");
	                        	        	nodoAEvaluarDuplicate.prev = listaCerrada.get(0);
		                        	        listaAbierta.add(0, nodoAEvaluarDuplicate);
	                        	        }
	                        	        
	                        	        
	                        	        /*Para cada s de S que estuviera ya en ABIERTA o CERRADA
	                        	        decidir si redirigir o no sus punteros hacia N
	                        	        Para cada s de S que estuviera ya en CERRADA
	                        	        decidir si redirigir o no los punteros de los nodos en sus sub�arboles
	                        	        Reordenar ABIERTA segun f (n)*/
	                        	        movimientosIzq ++;
	                        	   }          	   
	                           }
                            
	                           //GENERAR ESTADOS DE CAMBIAR DE CALLE ENTRANDO DE CARA
	                           
	                           while(calleEntraFrente < st){// para todas las plazas incluyendo la propia del coche...
	                        	   
	                        	   NodoEstado nodoAEvaluarDuplicate = new NodoEstado(nodoAEvaluar);
	                       	        expandir = nodoAEvaluarDuplicate.moverCallePrincipio(calleEntraFrente, i, j);
	                       	        
	                       	        
	                       	        boolean repetidoEnCerrada = parkingRepetidoEnLista(listaCerrada, nodoAEvaluarDuplicate.parkingActual);
	                       	     	boolean repetidoEnAbierta = parkingRepetidoEnLista(listaAbierta, nodoAEvaluarDuplicate.parkingActual);
	                       	     	
	                       	     //TODO Si se repiten nodos comprobar que estamos cogiendo el nodo con menos coste
                     	        	if(expandir && !repetidoEnCerrada && !repetidoEnAbierta) {
                     	        		
                     	        		nodoAEvaluarDuplicate.printParking();
                     	        		printLista(listaCerrada);
                     	        		System.out.println("");
                        	        	printLista(listaAbierta);
                        	        	System.out.println("");
                     	        		System.out.println("^^^^ENTRADA DELANTE^^^");
                     	        		nodoAEvaluarDuplicate.prev = listaCerrada.get(0);
		                        	        listaAbierta.add(0, nodoAEvaluarDuplicate);
	                       	        }
	                       	     calleEntraFrente++;
	                           }
	                           
	                           //GENERAR ESTADOS DE CAMBIAR DE CALLE ENTRANDO MARCHA ATRAS
	                           
	                           while(calleEntraAtras < st){// para todas las calles incluyendo la propia del coche...

	                        	   NodoEstado nodoAEvaluarDuplicate = new NodoEstado(nodoAEvaluar);
	                       	        expandir = nodoAEvaluarDuplicate.moverCalleFinal(calleEntraAtras, i, j);
	                       	        
	                       	        
	                       	        boolean repetidoEnCerrada = parkingRepetidoEnLista(listaCerrada, nodoAEvaluarDuplicate.parkingActual);
	                       	        boolean repetidoEnAbierta = parkingRepetidoEnLista(listaAbierta, nodoAEvaluarDuplicate.parkingActual);
                     	        
	                       	        //TODO Si se repiten nodos comprobar que estamos cogiendo el nodo con menos coste
                     	        	if(expandir && !repetidoEnCerrada && !repetidoEnAbierta) {
                     	        		
                     	        		nodoAEvaluarDuplicate.printParking();
                     	        		printLista(listaCerrada);
                     	        		System.out.println("");
                        	        	printLista(listaAbierta);
                        	        	System.out.println("");
                     	        		System.out.println("^^^^ENTRADA DETRAS^^^");
                     	        		nodoAEvaluarDuplicate.prev = listaCerrada.get(listaCerrada.size()-1);
		                        	    listaAbierta.add(0, nodoAEvaluarDuplicate);
	                       	        }
	                        	   calleEntraAtras++;
	                           }  
	                           
                            }
                            
                        }       
                    }
                }
                
                sortArrayList(listaAbierta);
                
            }
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
        	
        	NodoEstado step = new NodoEstado();
        	while(!solucion.isEmpty()) {
        		paso++;
        		System.out.println("\nPaso "+paso +" ");
            	step = solucion.pop();
            	for(int i = 0; i < st; i++){
            		for(int j = 0; j < pl; j++){
            			System.out.print(step.parkingActual[i][j].car + " ");
            		}System.out.println("");
            	}
            	
        	}
        }
    }

    public static boolean checkExito(Coche[][] parkingActual, Coche[][] parkingObjetivo) {

        boolean result = true;
        for (int i = 0; i < parkingActual.length; i++) {
            for (int j = 0; j < parkingActual[0].length; j++) {
                if (parkingActual[i][j].car.compareTo(parkingObjetivo[i][j].car) != 0) {
                    result = false;
                }
            }
        }
        return result;
    }
    
    public static boolean parkingRepetidoEnLista(ArrayList<NodoEstado> lista, Coche[][] parking){
    	
    	boolean repetido = true;
    	if(lista.isEmpty()) return false;
    	
    	int numCalles = parking.length;
    	int numPlazas = parking[0].length;
    	for(int posicionLista = 0; posicionLista < lista.size(); posicionLista++)
	    	for(int i = 0; i < numCalles; i++){
	    		for(int j = 0; j < numPlazas; j++){
	    			if(parking[i][j].car.compareTo(lista.get(posicionLista).parkingActual[i][j].car) != 0){
	    				repetido = false;
	    				break;
	    			}
	    		}
	    	}
    	return repetido;
    }
    
    
    public static void sortArrayList(ArrayList<NodoEstado> lista) {
        int n = lista.size();
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
    				System.out.print(lista.get(posLista).parkingActual[i][j].car+" ");
    			}
    
    		}	
    	}
    	System.out.print("]");
    }

}
