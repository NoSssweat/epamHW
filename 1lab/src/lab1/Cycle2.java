/*
 * *Пытался сделать вывод таблицы через printf() с использованием %d, но этот
 */
package lab1;

public class Cycle2 {
	public static void main(String args[]){
		
		int a;
		int b;
		a=0;
		b=5;
		int h = 1;		
		
		System.out.print("x: ");
		System.out.println("  f(x):");		
				
		for(int x=a;x<b;x+=h){
			System.out.print(x+1);
			System.out.println("    "+Function.f(x));			
		}
		
	}

}
