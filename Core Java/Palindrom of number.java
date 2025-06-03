package javacore;
import java.util.Scanner;
public class palindromeofnumber {

	public static void main(String[] args) {
		int rev=0;
		Scanner s=new Scanner(System.in);
		System.out.println("Enter a number");
		int n=s.nextInt();
		int temp=n;
		while(n!=0) {
			rev=(rev*10)+(n%10);
			n=n/10;
			
		}
		if(rev==temp) {
			System.out.println("It is a palindrome Number");
		}
		else {
			System.out.println("It is not a palindrome Number");
		}
		
		

	}

}
