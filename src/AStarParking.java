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

        boolean exito = false;

        while(listaAbierta.isEmpty() || exito){

            listaCerrada.add(listaAbierta.get(0));//copiar el primer elemento de lista abierta y ponerlo en cerrada
            listaAbierta.remove(0);//quitar el elemento copiado a cerrada de lista abierta

            exito = checkExito(listaCerrada.get(0).parkingActual, listaCerrada.get(0).parkingFinal, st, pl);
            if(exito) break;
            else{
                //para cada coche expandiremos todos sus posibles movimientos generando 
                //todos los posibles estados a partir de los posibles movimientos de un coche
                for(int i = 0; i < st; i++ ){
                    for(int j = 0; j < pl; j++ ){//para cada coche...
                    	NodoEstado estadoActual = new NodoEstado(listaCerrada.get(0).parkingActual, listaCerrada.get(0));
                        int movimientosDer = 1;
                        int movimientosIzq = 1;
                        int calleEntraFrente = 0;
                        int calleEntraAtras = 0;
                    	for(int k = 0; k < st; k++ ){//operar con todas las demas posiciones
                            for(int l = 0; l < pl; l++ ){

                            	//TODO LOS OPERADORES POR CADA MOVIMIENTO QUE HACEN DEBEN DEVOLVER UNA CONFIGURACIÓN NUEVA!!!!!!!
                            	
	                           if(k != i && l != j){//Si no se trada de la misma casilla del coche que está siendo evaluado
	                        	   
	                        	   //GENERAR ESTADOS DE MOVER A DERECHA
	                        	   while(movimientosDer < pl){
	                        		   //[NUEVO ESTADO DE PARKING] = estadoActual.moverDerecha(movimientosDer, k, l);
	                        		   movimientosDer += movimientosDer;
	                        	   }
	                        	   
	                        	   //GENERAR ESTADOS DE MOVER A IZDA
	                        	   while(movimientosIzq > 0){
	                        		   //[NUEVO ESTADO DE PARKING] = estadoActual.moverIzquierda(movimientosIzq, k, l);
	                        		   movimientosDer += movimientosIzq;
	                        	   }          	   
	                           }
                            
	                           //GENERAR ESTADOS DE CAMBIAR DE CALLE ENTRANDO DE CARA
	                           
	                           while(calleEntraFrente < st){// para todas las calles incluyendo la propia del coche...
	                        	   
	                        	   //[NUEVO ESTADO PARKING] = estadoActual.moverCallePrincipio(calleEntraFrente, k, l);
	                           }
	                           
	                           //GENERAR ESTADOS DE CAMBIAR DE CALLE ENTRANDO MARCHA ATRAS
	                           
	                           while(calleEntraAtras < st){// para todas las calles incluyendo la propia del coche...
	                        	   
	                        	   //[NUEVO ESTADO PARKING] = estadoActual.moverCalleFinal(calleObjetivo, k, l);
	                           }
                            }
                        }       
                    }
                }
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

}
