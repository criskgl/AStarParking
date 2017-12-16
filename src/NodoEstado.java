
public class NodoEstado {
	
	public Coche[][] parkingActual;
	public Coche[][] parkingFinal;
	public int costeActual;
	public int EvaluacionValue;
	public int HeuristicaValue;
	
	/*REVISAR!!*/
	public NodoEstado next;
	public NodoEstado prev;
	public NodoEstado antecesor;
	
		//LOS NODOS ALBERGARAN EL ESTADO DEL PARKING.
	   /*constructor para nodo inicial*/

		public NodoEstado(Coche[][] parking){
			
			this.parkingActual = parking;
			this.HeuristicaValue = getHeuristicValue(); //TODO
		}
		
		/*constructor para cualquier otro nodo*/
		
		//recibe un parking
		//recibe su padre para poder hacer seguimiento
		//cost es el coste de la operacion que se va a realizar
		public NodoEstado(Coche[][] parking, NodoEstado padre, int cost){
			this.parkingActual = parking;//recibe un parking
			this.HeuristicaValue = getHeuristicValue();//toma el valor segun f heuristica
			this.costeActual = this.antecesor.costeActual;//hereda el coste de su padre
		}
		
		//TODO terminar getHeuristicValue
		public  int getHeuristicValue()
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
