import java.util.ArrayList;

public class NodoEstado {
	
	public static Coche[][] parkingActual;
	public static Coche[][] parkingFinal;
	public static int costeActual;
	public int EvaluacionValue;
	public static int HeuristicaValue;
	
	/*REVISAR!!*/
	public NodoEstado next;
	public NodoEstado prev;
	public static NodoEstado antecesor;
	
	public static int numCalles;
	public static int numPlazas;
	
		//LOS NODOS ALBERGARAN EL ESTADO DEL PARKING.
	   /*constructor para nodo inicial*/

		public NodoEstado(Coche[][] parkingInicial, Coche[][] parkingFinal){
			
			this.parkingActual = parkingInicial;
			this.parkingFinal = parkingFinal;
			getHeuristicValue(); //TODO
			
			numCalles = parkingActual.length;
			numPlazas = parkingActual[0].length;
		}
		
		/*constructor para cualquier otro nodo*/
		
		//recibe un parking
		//recibe su padre para poder hacer seguimiento
		//cost es el coste de la operacion que se va a realizar
		public NodoEstado(Coche[][] parking, NodoEstado padre, Coche[][] parkingObjetivo){
			this.parkingFinal = parkingObjetivo;
			this.parkingActual = parking;//recibe un parking
			getHeuristicValue();//toma el valor segun f heuristica
			this.costeActual = this.antecesor.costeActual;//hereda el coste de su padre
			EvaluacionValue = costeActual + HeuristicaValue;
			
			numCalles = parkingActual.length;
			numPlazas = parkingActual[0].length;
		}
		
		//OPERADOR PARA MOVER COCHE A SU DERECHA
		public static boolean moverDerecha(int movimientos, int calle, int plaza){
		
			boolean libre = true;
			//No podemos movernos a derecha estando en un extremo
			if(plaza + movimientos < parkingActual[calle].length){
				
				for(int i = plaza+1; i < plaza+movimientos; i++){
						if(parkingActual[calle][i].car.compareTo("__") != 0){
							libre = false;
									break;
						}
				}
				if(libre){//mover Derecha
					cambiarPos(plaza, calle, calle, plaza+movimientos);
					costeActual = antecesor.costeActual + 1;//añadir al coste acumulado el coste de mover a derecha(1)
					//recalcular heuristica
					getHeuristicValue();
				}
			}
			
			return libre;
		
		}
		//OPERADOR PARA MOVER COCHE A SU IZQUIERDA
		public static boolean moverIzda(int movimientos, int calle, int plaza){
			
			boolean libre = true;
			//No podemos movernos a derecha estando en un extremo
			if(plaza - movimientos >= parkingActual[calle].length){
				
				for(int i = plaza; i < 0; i--){
						if(parkingActual[calle][i].car.compareTo("__") != 0){
							libre = false;
									break;
						}
				}
				if(libre){//mover Izda
					cambiarPos(plaza, calle, calle, plaza-movimientos);
					costeActual = antecesor.costeActual + 2;//añadir al coste acumulado el coste de mover a izda(2)
				}
			}
			
			return libre;
		}
		//OPERADOR PARA MOVER A OTRA CALLE ENTRADO HACIA ADELANTE !DECISION DE IMPLEMENTACION MEMORIA!!!
		public static boolean moverCallePrincipio(int calleObjetivo, int calle, int plaza){
			
			if(parkingActual[calle][plaza].car.compareTo("__") == 0) return false;//comprobar que la casilla que se va a intentar mover no es un espacio vacio
			
			if(plaza == numPlazas && parkingActual[calleObjetivo][0].car.compareTo("__") == 0){// si el coche está al final de la calle...
				cambiarPos(plaza, calle, calleObjetivo, 0);
				costeActual = antecesor.costeActual + 3;//añadir al coste acumulado (3)
				return true;
			}
			return false;
		}
		
		//OPERADOR PARA MOVER A OTRA CALLE ENTRADO HACIA ATRAS 
		public static boolean moverCalleFinal(int calleObjetivo, int calle, int plaza){
			
			if(parkingActual[calle][plaza].car.compareTo("__") == 0) return false;//comprobar que la casilla que se va a intentar mover no es un espacio vacio
			
			if(plaza == 0 && parkingActual[calleObjetivo][numPlazas].car.compareTo("__") == 0){// si el coche está al inicio de la calle...
				cambiarPos(plaza, calle, calleObjetivo, parkingActual[calleObjetivo].length - 1);
				costeActual = antecesor.costeActual + 4;//añadir al coste acumulado el coste de mover a izda(4)
				return true;
			}
			return false;
		}
		
		public static void cambiarPos(int filaInicial, int colInicial, int filaFinal,int colFinal){//mueve un coche una posicion FINAL
			
			parkingActual[filaFinal][colFinal].car = parkingActual[filaInicial][colInicial].car;
			
			parkingActual[filaInicial][colInicial].car = "__";
		}
		
		public static  void getHeuristicValue()//Depende de los coches bien colocados
		{
			int contadorHeuristics = 0;
			
			for(int i = 0; i < parkingActual.length; i++){
				for(int j = 0; j < parkingActual[i].length; j++){
					String coche = parkingActual[i][j].car;
					if(coche.compareTo("__") != 0) {
						for(int k = 0; k < parkingActual.length; k++){
							for(int l = 0; l < parkingActual[i].length; l++){
								if(parkingActual[i][j].car.equals(parkingFinal[k][l].car)){
									//si el coche esta bien colocado en calle y en plaza, no aumenta el valor heuristico
									if(i == k && j  != l) contadorHeuristics += 1; //misma calle y distinta plaza para coche inicial y final
									if(i != k && ((j == 0 || j == parkingActual[i].length-1))) contadorHeuristics += 2; //calle distinta con coche inicial/final en extremos
									if(i != k && (j > 0 && j != parkingActual[i].length-1)) contadorHeuristics += 3; //calle distinta con coche inicial/final no en extremos
								}
							}	
						}
					}
				}	
			}
			HeuristicaValue = contadorHeuristics;// h(n)
			
		}
}
