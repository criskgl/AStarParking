import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

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

        boolean exito = true;
        boolean expandir = false;

        while(listaAbierta.isEmpty() || exito){

            listaCerrada.add(listaAbierta.get(0));//copiar el primer elemento de lista abierta y ponerlo en cerrada
            //listaAbierta.remove(0);//quitar el elemento copiado a cerrada de lista abierta
            
            exito = checkExito(listaCerrada.get(0).parkingActual, listaCerrada.get(0).parkingFinal, st, pl);
            if(exito) break;
            else{
                //para cada coche expandiremos todos sus posibles movimientos generando 
                //todos los posibles estados a partir de los posibles movimientos de un coche
                for(int i = 0; i < st; i++ ){
                    for(int j = 0; j < pl; j++ ){//para cada coche...
                    	NodoEstado estadoActual = new NodoEstado(listaAbierta.get(0).parkingActual, listaAbierta.get(0), parkingObjetivo);

                    	for(int k = 0; k < st; k++ ){//operar con todas las demas posiciones
                            for(int l = 0; l < pl; l++ ){
                            	
                                int movimientosDer = 1;
                                int movimientosIzq = 1;
                                int calleEntraFrente = 0;
                                int calleEntraAtras = 0;
                            
                            	
	                           if(k != i || l != j){//Si no se trada de la misma casilla del coche que está siendo evaluado
	                        	   
	                        	   //GENERAR ESTADOS DE MOVER A DERECHA
	                        	   while(movimientosDer < pl){
	                        	        NodoEstado nodoExpansion = new NodoEstado(parkingActual, estadoActual, parkingObjetivo);
	                        	        expandir = nodoExpansion.moverDerecha(movimientosDer, i, j);
	                        	        
	                        	        if(expandir) {
	                        	        	nodoExpansion.prev = listaCerrada.get(0);
		                        	        listaCerrada.get(0).next = nodoExpansion;
		                        	        listaAbierta.add(0, nodoExpansion);
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
	                        		    NodoEstado nodoExpansion = new NodoEstado(parkingActual, estadoActual,parkingObjetivo);
	                        	        expandir = nodoExpansion.moverIzda(movimientosIzq, k, l);
	                        	        
	                        	        if(expandir) {
	                        	        	nodoExpansion.prev = listaCerrada.get(0);
		                        	        listaCerrada.get(0).next = nodoExpansion;
		                        	        listaAbierta.add(0, nodoExpansion);
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
	                           
	                           while(calleEntraFrente < st){// para todas las calles incluyendo la propia del coche...
	                        	   
		                        	NodoEstado nodoExpansion = new NodoEstado(parkingActual, estadoActual, parkingObjetivo);
	                       	        expandir = nodoExpansion.moverCallePrincipio(calleEntraFrente, i, j);
	                       	        
	                       	        if(expandir) {
	                       	        	nodoExpansion.prev = listaCerrada.get(0);
		                        	        listaCerrada.get(0).next = nodoExpansion;
		                        	        listaAbierta.add(0, nodoExpansion);
	                       	        }
	                       	     calleEntraFrente++;
	                           }
	                           
	                           //GENERAR ESTADOS DE CAMBIAR DE CALLE ENTRANDO MARCHA ATRAS
	                           
	                           while(calleEntraAtras < st){// para todas las calles incluyendo la propia del coche...

	                        	   NodoEstado nodoExpansion = new NodoEstado(parkingActual, estadoActual,parkingObjetivo);
	                       	        expandir = nodoExpansion.moverCallePrincipio(calleEntraAtras, i, j);
	                       	        
	                       	        if(expandir) {
	                       	        	nodoExpansion.prev = listaCerrada.get(0);
		                        	        listaCerrada.get(0).next = nodoExpansion;
		                        	        listaAbierta.add(0, nodoExpansion);
	                       	        }
	                        	   calleEntraAtras++;
	                           }    
                            }
                        }       
                    }
                }
                System.out.println("ListaAbierta: "+listaAbierta.toString());
            }
        }
    }

    public static boolean checkExito(Coche[][] parkingActual, Coche[][] parkingObjetivo, int st, int pl) {

        boolean result = true;
        for (int i = 0; i < st; i++) {
            for (int j = 0; j < pl; j++) {
                if (parkingActual[i][j].car.compareTo(parkingObjetivo[i][j].car) != 0) {
                    result = false;
                }
            }
        }
        return result;
    }
    
    public static boolean parkingRepetidoEnLista(ArrayList<NodoEstado> lista, Coche[][] parking){
    	
    	int posicionLista = 0; 
    	boolean repetido = true;
    	
    	for(int i = 0; i < parking.length; i++){
    		for(int j = 0; j < parking[j].length; j++){
    			if(parking[i][j].car.compareTo(lista.get(posicionLista).parkingActual[i][j].car) != 0){
    				repetido = false;
    				break;
    			}
    		}
    	}
    	return repetido;
    }

}
