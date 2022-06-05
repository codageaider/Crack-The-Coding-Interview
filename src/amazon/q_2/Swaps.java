package amazon.q_2;

/**
 * input : 101000
 * 101000 -> 0100 [1] -> 10 [1]->[2]
 * 2 pairs of dislocations => number of swaps = 2/2 = 1
 *
 *
 * input: 1110
 * 1110->11 [1]->[1]  the number of dislocations in this is odd
 * And the length of string is = 4 which is even.
 * If the number of dislocations is odd and length of string is even then there is
 * no way to make that string a palindrom
 * Length of the string = 2n, number of dislocations = 2m+1
 * #0 will be even if we only include non-dislocated positions
 * #0 2m+1 0's and 2m + 1 1's
 * #0 is odd and #1s is also odd
 * For a string of even length to be a palindrom #0's and #1's must be even.
 *
 * return -1
 *
 */
public class Swaps {
    public static void main(String[] args) {
        System.out.println(numOfSwaps("0100101"));
        System.out.println(numOfSwaps("101000"));

    }
    public static int numOfSwaps(String str){
         int countNumDislocations=0;
         for(int i=0;i<str.length()/2;i++)
             if(str.charAt(i)!=str.charAt(str.length()-1-i))
                 countNumDislocations++;
             int numOfSwaps=0;
             if(countNumDislocations%2==1 && str.length()%2==0)
                 return -1;
             if(countNumDislocations%2==0)
                 numOfSwaps=countNumDislocations/2;
             else
                 numOfSwaps=(countNumDislocations/2)+1;
             return numOfSwaps;
    }
}
