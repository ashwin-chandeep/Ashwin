package javacore;

public class evendigits {
	public int check(int[] arr) {
		int count=0;
		for(int num:arr) {
		if(even(num)) {
			count++;
			
			
		}
		
		}
		return count;
		
	}
	public boolean even(int num) {
		int n=digits(num);
		if(n%2==0) {
			return true;
		}
		return false;
		
	}
	public int digits(int n) {
		int no=0;
		while(n>0) {
			no++;
			n=n/10;
			
		}
		return no;
		
	}
	
	
	
	
	public static void main(String [] args) {
		evendigits e=new evendigits();
		int arr[]= {2,4,53,23};
		int count=e.check(arr);
		System.out.println("No of even digit :"+ count);
	}

}
