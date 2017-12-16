import java.io.File;
import java.io.FileNotFoundException;
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
		
	}

}
