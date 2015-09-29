package lab1;

public class TwoDimensionMassiva {
	//111
	
	public static void main(String args[]){
		int k;
		int m;
		k = 20;
		m = 40;
		int c;
		
		int[][] array = new int[k][m];
		
		if (k>m){
			c=m;
		}
		else{
			c=k;
		}
		
		for(int i=0; i<c; i++){
			array[i][i] =1;
		}

		
		for(int i = 0, j = m-1; i<k; i++, j--){
			if (j<0){
				break;
			}
			else{
			array[i][j]=1;
			}
		}
		
		for(int i=0; i<k; i++){
			for(int j =0; j<m; j++){
				System.out.print(array[i][j]);
			}
		System.out.println();
		}		
	}
}
