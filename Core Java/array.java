package javacore;

public class array {

	public static void main(String[] args) {
		int num[]=new int[4];  //Creates array of size 4....Each elemnt as 0
		num[0]=1;
		num[1]=21;
		num[2]=7;
		num[3]=5;
		
		int n=num.length;
		for(int i=0;i<4;i++) {
			System.out.println(num[i]);
		}
		
		

	}

}
