package javacore;

public class binarysearch {
	public int binarysearch(int[] arr,int t) {
		int start=0;
		int end =arr.length-1;
		int mid=start+(end-start)/2;
		while(start<=end) {
			if(t>arr[mid]) {
				start=mid+1;
				
			}
			else if(t<arr[mid]){
				end=mid-1;
				
			}
			else {
				return mid;
				
			}
			
		}
		return -1;
	}
	

	public static void main(String[] args) {
		binarysearch bs=new binarysearch();
		int[] arr= {-1,-4,0,3,4,5,7};
		int target=3;
		int index=bs.binarysearch(arr,target);
		if(index==-1) {
		System.out.println("Element not found");
		}
		else {
		System.out.println("Elemnt found at index "+index);
		}
		
		
		

	}

}
