package lab1;

public class Cicle {
	
	public static void main(String args[]){
		double epsil;
		epsil = 0.0001;	
		double an;
		an=1;
		int i;
		i=0;
		
		while (an>epsil){
			i++;
			an=1.0/Math.pow(1+i, 2);
			System.out.println("a"+i+" = "+an);
		}
		System.out.println("Min: "+an);
	}
}
