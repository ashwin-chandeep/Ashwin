package javacore;


public class LinearSearchIn2dArray {

    public int[] srh(int[][] arr, int t) {
        for (int row = 0; row < arr.length; row++) {
            for (int col = 0; col < arr[row].length; col++) {
                if (arr[row][col] == t) {
                    return new int[] { row, col };
                }
            }
        }
        return new int[] { -1, -1 };
    }

    public static void main(String[] args) {
        int[][] arr = {
            { 2, 3, 4 },
            { 1, 22, 43 },
            { 12, 42, 32 }
        };

        int t = 20;

        LinearSearchIn2dArray obj = new LinearSearchIn2dArray();
        int[] index = obj.srh(arr, t);

        if (index[0] == -1) {
            System.out.println("Element not found.");
        } else {
            System.out.println("Element found at row " + index[0] + ", column " + index[1]);
        }
    }
}
