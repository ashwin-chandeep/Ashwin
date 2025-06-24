package javacore;

public class linearsearch {
	public int search(int[] arr,int t){
		for(int i=0;i<arr.length;i++) {
			if(t==arr[i]) {
				return i;
			}
		
			
		}
		return -1;
		
	}
	public static void main(String [] args) {
		linearsearch l=new linearsearch();
		int[] arr= {1,12,33,21};
		int target=33;
		int index=l.search(arr,target);
		if (index==-1) {
			System.out.println("Element not found");
		}
		else {
			System.out.println("Element found at index "+ index);
		}
		
	}

}
