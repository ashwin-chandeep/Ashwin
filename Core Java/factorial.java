package javacore;
import java.util.Scanner;
public class factorial {
	public static void main(String []args) {
		int n,sum=1,i;
		Scanner s=new Scanner (System.in);
		System.out.println("Enter a number:");
		n=s.nextInt();
		for(i=n;i>1;i--) {
			sum=sum*i;
		}
		System.out.println("Factorial of number is "+sum);
		

}
}
