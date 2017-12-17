import java.util.ArrayList;

public class NodoEstado {
	
	public static Coche[][] parkingActual;
	public Coche[][] parkingFinal;
	public static int costeActual;
	public int EvaluacionValue;
	public int HeuristicaValue;
	
	/*REVISAR!!*/
	public NodoEstado next;
	public NodoEstado prev;
	public static NodoEstado antecesor;
	
		//LOS NODOS ALBERGARAN EL ESTADO DEL PARKING.
	   /*constructor para nodo inicial*/

		public NodoEstado(Coche[][] parkingInicial, Coche[][] parkingFinal){
			
			this.parkingActual = parkingInicial;
			this.parkingFinal = parkingFinal;
			this.HeuristicaValue = this.getHeuristicValue(); //TODO
		}
		
		/*constructor para cualquier otro nodo*/
		
		//recibe un parking
		//recibe su padre para poder hacer seguimiento
		//cost es el coste de la operacion que se va a realizar
		public NodoEstado(Coche[][] parking, NodoEstado padre, Coche[][] parkingObjetivo){
			this.parkingFinal = parkingObjetivo;
			this.parkingActual = parking;//recibe un parking
			this.HeuristicaValue = getHeuristicValue();//toma el valor segun f heuristica
			this.costeActual = this.antecesor.costeActual;//hereda el coste de su padre
		}
		
		//OPERADOR PARA MOVER COCHE A SU DERECHA
		public static boolean moverDerecha(int movimientos, int calle, int plaza){
			
			
			boolean libre = true;
			//No podemos movernos a derecha estando en un extremo
			if(plaza + movimientos <= parkingActual[calle].length - 1){
				
				for(int i = plaza; i < parkingActual[calle].length - 1; i++){
						if(parkingActual[calle][i].car.compareTo("__") != 0){
							libre = false;
									break;
						}
				}
				if(libre){//mover Derecha
					cambiarPos(plaza, calle, calle, plaza+movimientos);
					costeActual = antecesor.costeActual + 1;//añadir al coste acumulado el coste de mover a derecha(1)
				}
			}
			
			return libre;
		
		}
		//OPERADOR PARA MOVER COCHE A SU IZQUIERDA
		public static boolean moverIzda(int movimientos, int calle, int plaza){
			
			boolean libre = true;
			//No podemos movernos a derecha estando en un extremo
			if(plaza - movimientos >= parkingActual[calle].length - 1){
				
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
			if(plaza == parkingActual[calle].length - 1 && parkingActual[calleObjetivo][0].car.compareTo("__") == 0){// si el coche está al final de la calle...
				cambiarPos(plaza, calle, calleObjetivo, 0);
				costeActual = antecesor.costeActual + 3;//añadir al coste acumulado (3)
				return true;
			}
			return false;
		}
		
		//OPERADOR PARA MOVER A OTRA CALLE ENTRADO HACIA ATRAS 
		public static boolean moverCalleFinal(int calleObjetivo, int calle, int plaza){
			if(plaza == 0 && parkingActual[calleObjetivo][parkingActual[calleObjetivo].length -1].car.compareTo("__") == 0){// si el coche está al inicio de la calle...
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
		
		public  int getHeuristicValue()//Depende de los coches bien colocados
		{
			int contadorHeuristics = 0;
			
			for(int i = 0; i < this.parkingActual.length; i++){
				for(int j = 0; j < this.parkingActual[i].length; j++){
					
					if(parkingActual[i][j].car.equals(parkingFinal[i][j].car)){
						contadorHeuristics += 1;
					}
				}	
			}
			return contadorHeuristics;
			
		}
}
