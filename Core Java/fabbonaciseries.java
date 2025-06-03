package javacore;

public class fabbonaciseries {

	public static void main(String[] args) {
		int fstnum=0;
		int sndnum=1;
		int nextnum;
		int n=10;
		for(int i=0;i<=n;i++) {
			System.out.print(fstnum+",");
			nextnum=fstnum+sndnum;
			fstnum=sndnum;
			sndnum=nextnum;
			
		}
		
		

	}

}
