package javacore;

public class multidimensionarray {

	public static void main(String[] args) {
		int num[][]=new int[2][2];
		num[0][0]=1;
		num[0][1]=2;
		num[1][0]=3;
		num[1][1]=4;
		for(int i=0;i<num.length;i++) {
			for(int j=0;j<num[i].length;j++) {
				System.out.println(num[i][j]);
				
			}
		}
								

	}

}
