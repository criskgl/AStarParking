public class NodoEstado implements Cloneable{

	public String[][] parkingActual;
	public static String[][] parkingFinal;
	public int costeActual;
	public int costeMovimiento;
	public int EvaluacionValue;
	public int HeuristicaValue;

	public NodoEstado prev;

	public static int numCalles;
	public static int numPlazas;
	
	public int calleInicial, plazaInicial, calleFinal, plazaFinal; //variables para impresion del fichero


	public NodoEstado(String[][] parkingInicial, String[][] parkingFinal){ // Constructor para el nodo inicial

		this.parkingActual = parkingInicial;
		NodoEstado.parkingFinal = parkingFinal;
		getHeuristicValue(); //TODO

		numCalles = parkingActual.length;
		numPlazas = parkingActual[0].length;
	}

	public NodoEstado(String[][] parking, NodoEstado padre){ //Constructor para el resto de nodos
		this.parkingActual = parking;
		getHeuristicValue();
		this.costeActual = this.prev.costeActual; //hereda el coste del padre
		EvaluacionValue = costeActual + HeuristicaValue;
	}

	public NodoEstado(NodoEstado nodoADuplicar){ //Constructor para clonar un nodo

		this.HeuristicaValue = nodoADuplicar.HeuristicaValue;
		this.EvaluacionValue = nodoADuplicar.EvaluacionValue;
		this.costeActual = nodoADuplicar.costeActual;
		this.parkingActual = new String[numCalles][numPlazas];
		this.prev = nodoADuplicar;

		for(int i = 0; i < numCalles; i++){
			for(int j = 0; j < numPlazas; j++){        
				this.parkingActual[i][j] = nodoADuplicar.parkingActual[i][j];
			}
		}
	}

	//																-------------------OPERADORES------------------------

	//OPERADOR PARA MOVER COCHE A SU DERECHA
	public boolean moverDerecha(int movimientos, int calle, int plaza){

		boolean libre = true;

		if(parkingActual[calle][plaza].compareTo("__") == 0)return false;//comprobar que la casilla que se va a intentar mover no es un espacio vacio	
		if(plaza + movimientos >= numPlazas) return false; //No podemos movernos a derecha estando en un extremo

		for(int i = plaza+1; i <= plaza+movimientos; i++){ //comprobamos camino vacÌo entre inicial y final
			if(parkingActual[calle][i].compareTo("__") != 0){
				libre = false;
				break;
			}
		}
		
		if(libre){ //mover Derecha
			cambiarPos(calle, plaza, calle, plaza+movimientos);
			costeMovimiento = 1;
			costeActual = this.costeActual + costeMovimiento; //sumar al coste acumulado el coste de mover a derecha(1)
			getHeuristicValue();
		}

		return libre;

	}

	//OPERADOR PARA MOVER COCHE A SU IZQUIERDA
	public boolean moverIzda(int movimientos, int calle, int plaza){

		boolean libre = true; //No podemos movernos a derecha estando en un extremo

		if(parkingActual[calle][plaza].compareTo("__") == 0) return false; //comprobar que la plaza inicial no es un espacio vacio
		if(parkingActual[calle][plaza-movimientos].compareTo("__") != 0) return false;  //comprobar que la plaza que final no es un espacio vacio
		if(plaza - movimientos < 0) return false; //no nos salimos del parking

		for(int i = plaza-1; i > plaza-movimientos; i--){ //comprobar que el camino esta vacio entre las posiciones
			if(parkingActual[calle][i].compareTo("__") != 0){
				libre = false;
				break;
			}
		}
		
		if(libre){ //mover Izda
			cambiarPos(calle, plaza, calle,  plaza-movimientos);
			costeMovimiento = 2;
			costeActual = this.prev.costeActual + costeMovimiento;//a√±adir al coste acumulado el coste de mover a izda(2)
			getHeuristicValue();
		}
	
		return libre;
	}

	//OPERADOR PARA MOVER A OTRA CALLE ENTRADO HACIA ADELANTE 
	public boolean moverCallePrincipio(int calleObjetivo, int calle, int plaza){

		if((parkingActual[calle][plaza].compareTo("__") == 0) && plaza != numPlazas-1) return false; //comprobar que la casilla que se va a intentar mover no es un espacio vacio
		if(parkingActual[calleObjetivo][0].compareTo("__") != 0) return false; 

		cambiarPos(calle, plaza, calleObjetivo, 0);
		costeMovimiento = 3;
		costeActual = this.prev.costeActual + costeMovimiento; //sumar al coste acumulado (3)
		getHeuristicValue();
		return true;

	}

	//OPERADOR PARA MOVER A OTRA CALLE ENTRADO HACIA ATRAS 
	public boolean moverCalleFinal(int calleObjetivo, int calle, int plaza){

		if((parkingActual[calle][plaza].compareTo("__") == 0 )&& plaza == 0) return false; //comprobar que la casilla que se va a intentar mover no es un espacio vacio
		if(parkingActual[calleObjetivo][numPlazas-1].compareTo("__") != 0) return false;

		cambiarPos(calle, plaza, calleObjetivo, numPlazas-1);
		costeMovimiento = 4;
		this.costeActual = this.prev.costeActual + costeMovimiento; //sumar al coste acumulado el coste de mover a izda(4)
		getHeuristicValue();

		return true;
	}

	public void cambiarPos(int CalleInicial, int PlazaInicial, int CalleFinal,int PlazaFinal){//mueve un coche una posicion FINAL
		
		this.calleInicial = calleInicial+1; //variables para impresion del fichero
		this.plazaInicial = plazaInicial+1;
		this.calleFinal = calleFinal+1;
		this.plazaFinal = plazaFinal+1;
		
		parkingActual[CalleFinal][PlazaFinal] = parkingActual[CalleInicial][PlazaInicial];
		parkingActual[CalleInicial][PlazaInicial] = "__";
		
	}

	public void getHeuristicValue()
	{
		int contadorHeuristica = 0;

		for(int i = 0; i < parkingActual.length; i++){
			for(int j = 0; j < parkingActual[i].length; j++){
				String coche = parkingActual[i][j];
				if(coche.compareTo("__") != 0) {
					for(int k = 0; k < parkingActual.length; k++){
						for(int l = 0; l < parkingActual[i].length; l++){
							if(parkingActual[i][j].equals(parkingFinal[k][l])){
								//si el coche esta bien colocado en calle y en plaza, no aumenta el valor heuristico
								if(i == k && j  != l) contadorHeuristica += 1; //misma calle y distinta plaza para coche inicial y final
								if(i != k && ((j == 0 || j == parkingActual[i].length-1))) contadorHeuristica += 2; //calle distinta con coche inicial/final en extremos
								if(i != k && (j > 0 && j != parkingActual[i].length-1)) contadorHeuristica += 3; //calle distinta con coche inicial/final no en extremos
							}
						}	
					}
				}
			}	
		}
		HeuristicaValue = contadorHeuristica;// h(n)
	}

	public void printParking(){
		for(int i = 0; i < NodoEstado.numCalles; i++){
			for(int j = 0; j < NodoEstado.numPlazas; j++){
				System.out.print(this.parkingActual[i][j]);
			}
			System.out.println("");
		}
	}
}
