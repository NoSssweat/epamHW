package lab1;

public class Massive {
	public static void main(String args[]){

		int[] array = new int[]{20, 11, 14, 20, 20, 34, 45, 12, 43, 45, 74, 43};
		int a; 
		int max;
		max=0;
		int x;
		x=0;
		int y;
		y=0;
		
		for(int i=0; i<(array.length/2); i++){
			a= array[i]+array[array.length-i-1];
			if(max < a){
				x=i+1;
				max=a;
				y=array.length-i;
			}		
		}		
		System.out.print("Максимальное число из выборки: "+max+", получено из суммы эллементов с индексами: ");
		System.out.println(x+" "+y);		
	}
}
