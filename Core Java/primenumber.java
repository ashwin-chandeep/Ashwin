package javacore;

public class primenumber {

	
	    public static void main(String[] args) {
	        for (int num = 2; num <= 100; num++) {
	            boolean Prime = true;
	            for (int i = 2; i < num; i++) {
	                if (num % i == 0) {
	                    Prime = false;
	                    break;
	                }
	            }
	            if (Prime) {
	                System.out.println(num);
	            }
	        }
	    }
	

	}

