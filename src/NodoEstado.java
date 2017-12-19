import java.util.ArrayList;

public class NodoEstado implements Cloneable{
	
	public Coche[][] parkingActual;
	public static Coche[][] parkingFinal;
	public int costeActual;
	public int EvaluacionValue;
	public int HeuristicaValue;
	
	/*REVISAR!!*/
	public NodoEstado prev;
	
	public static int numCalles;
	public static int numPlazas;
	
		//LOS NODOS ALBERGARAN EL ESTADO DEL PARKING.
	   /*constructor para nodo inicial*/

		public NodoEstado(){}
	
		public NodoEstado(Coche[][] parkingInicial, Coche[][] parkingFinal){
			
			this.parkingActual = parkingInicial;
			NodoEstado.parkingFinal = parkingFinal;
			getHeuristicValue(); //TODO
			
			numCalles = parkingActual.length;
			numPlazas = parkingActual[0].length;
		}
		
		/*constructor para cualquier otro nodo*/
		
		//recibe un parking
		//recibe su padre para poder hacer seguimiento
		//cost es el coste de la operacion que se va a realizar
		public NodoEstado(Coche[][] parking, NodoEstado padre){
			this.parkingActual = parking;//recibe un parking
			getHeuristicValue();//toma el valor segun f heuristica
			this.costeActual = this.prev.costeActual;//hereda el coste de su padre
			EvaluacionValue = costeActual + HeuristicaValue;
		}
		
		//CONTRUCTOR DEFINITIVO PARA DUPLICAR NODOS DE MANERA INDEPENDIENTE 
		public NodoEstado(NodoEstado nodoADuplicar){
			
			this.HeuristicaValue = nodoADuplicar.HeuristicaValue;
    		this.EvaluacionValue = nodoADuplicar.EvaluacionValue;
    		this.costeActual = nodoADuplicar.costeActual;
    		this.parkingActual = new Coche[numCalles][numPlazas];
    		this.prev = nodoADuplicar;
    		
			for(int i = 0; i < numCalles; i++){
				for(int j = 0; j < numPlazas; j++){
					 Coche carActual = new Coche();//crear un nuevo coche
		                /*asignar fila, columna actuales y coches*/             
		                carActual.car = nodoADuplicar.parkingActual[i][j].car;
		                carActual.rowNow = i;
		                carActual.columnNow = j;
		                this.parkingActual[i][j] = carActual;
				}
			}
		}
		
		//OPERADOR PARA MOVER COCHE A SU DERECHA
		public boolean moverDerecha(int movimientos, int calle, int plaza){
		
			boolean libre = true;
			
			if(parkingActual[calle][plaza].car.compareTo("__") == 0) {
				getHeuristicValue();
				return false;//comprobar que la casilla que se va a intentar mover no es un espacio vacio
			}
			
			//No podemos movernos a derecha estando en un extremo
			if(plaza + movimientos < numPlazas){
				
				for(int i = plaza+1; i <= plaza+movimientos; i++){
						if(parkingActual[calle][i].car.compareTo("__") != 0){
							libre = false;
							break;
						}
				}
				if(libre){//mover Derecha
					cambiarPos(calle, plaza, calle, plaza+movimientos);
					costeActual = this.costeActual + 1;//añadir al coste acumulado el coste de mover a derecha(1)
					//recalcular heuristica
					
				}
			}
			getHeuristicValue();
			return libre;
		
		}
		//OPERADOR PARA MOVER COCHE A SU IZQUIERDA
		public boolean moverIzda(int movimientos, int calle, int plaza){
			
			boolean libre = true;
			//No podemos movernos a derecha estando en un extremo
			
			if(parkingActual[calle][plaza].car.compareTo("__") == 0) {
				getHeuristicValue();
				return false;//comprobar que la casilla que se va a intentar mover no es un espacio vacio
			}
			if(parkingActual[calle][plaza-movimientos].car.compareTo("__") != 0) {
				getHeuristicValue();
				return false;
			}
			
			if(plaza - movimientos >= 0){
				
				for(int i = plaza-1; i > plaza-movimientos; i--){
						if(parkingActual[calle][i].car.compareTo("__") != 0){
							libre = false;
							break;
						}
				}
				if(libre){//mover Izda
					cambiarPos(calle, plaza, calle,  plaza-movimientos);
					costeActual = this.prev.costeActual + 2;//añadir al coste acumulado el coste de mover a izda(2)
				}
			}
			getHeuristicValue();
			return libre;
		}
		//OPERADOR PARA MOVER A OTRA CALLE ENTRADO HACIA ADELANTE !DECISION DE IMPLEMENTACION MEMORIA!!!
		public boolean moverCallePrincipio(int calleObjetivo, int calle, int plaza){
			
			if(parkingActual[calle][plaza].car.compareTo("__") == 0) {
				getHeuristicValue();
				return false;//comprobar que la casilla que se va a intentar mover no es un espacio vacio
			}
			
			if(plaza == numPlazas-1 && parkingActual[calleObjetivo][0].car.compareTo("__") == 0){// si el coche está al final de la calle...
				cambiarPos(calle, plaza, calleObjetivo, 0);
				costeActual = this.prev.costeActual + 3;//añadir al coste acumulado (3)
				getHeuristicValue();
				return true;
			}
			getHeuristicValue();
			return false;
		}
		
		//OPERADOR PARA MOVER A OTRA CALLE ENTRADO HACIA ATRAS 
		public boolean moverCalleFinal(int calleObjetivo, int calle, int plaza){
			
			if(parkingActual[calle][plaza].car.compareTo("__") == 0) {
				getHeuristicValue();
				return false;//comprobar que la casilla que se va a intentar mover no es un espacio vacio
			}
			
			if(plaza == 0 && parkingActual[calleObjetivo][numPlazas-1].car.compareTo("__") == 0){// si el coche está al inicio de la calle...
				cambiarPos(calle, plaza, calleObjetivo, numPlazas-1);
				this.costeActual = this.prev.costeActual + 4;//añadir al coste acumulado el coste de mover a izda(4)
				getHeuristicValue();
				return true;
			}
			getHeuristicValue();
			return false;
		}
		
		public void cambiarPos(int CalleInicial, int PlazaInicial, int CalleFinal,int PlazaFinal){//mueve un coche una posicion FINAL
			
			parkingActual[CalleFinal][PlazaFinal].car = parkingActual[CalleInicial][PlazaInicial].car;
			
			parkingActual[CalleInicial][PlazaInicial].car = "__";
		}
		
		public void getHeuristicValue()//Depende de los coches bien colocados
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
		
		public void printParking(){
			for(int i = 0; i < NodoEstado.numCalles; i++){
				for(int j = 0; j < NodoEstado.numPlazas; j++){
					System.out.print(this.parkingActual[i][j].car);
				}
				System.out.println("");
			}
		}
}
