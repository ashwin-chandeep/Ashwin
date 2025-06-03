package javacore;
import java.util.Scanner;

public class palindromeofstring {

	public static void main(String[] args) {
		Scanner s=new Scanner(System.in);
		System.out.println("Enter a String:");
		String n=s.next();
		int len=n.length();
		String temp="";
		for(int i=len-1;i>=0;i--) {
			temp=temp+n.charAt(i);
		}
		if(temp.equals(n)) {
			System.out.println("It is a Palindrome");
		}
		else {
			System.out.println("It is not a palindrome");
		}
		
		
		// TODO Auto-generated method stub

	}

}

