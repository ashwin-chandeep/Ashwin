package javacore;
import java.util.*;

public class armstrongnumber {

	public static void main(String[] args) {
		int sum=0,r;
		Scanner s=new Scanner(System.in);
		System.out.print("Enter the number");
		int n=s.nextInt();
		int temp=n;
		while(n>0) {
			r=n%10;
			n=n/10;
			sum=sum+ (r*r*r);
			
			
		}
		if(temp==sum) {
			System.out.println("It is an2 armstrong Number");
		}
		else {
			System.out.println("It is not an armstrong number");
		}
		
		
		

	}

}
