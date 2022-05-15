package turing;

public class Judge_2 {
    public static void main(String[] args) {
        System.out.println(findJudge(3, new int[][]{{1,3},{2,3},{3,1}})); // return -1
        System.out.println(findJudge(3, new int[][]{{1,3},{2,3}})); // return 3
        System.out.println(findJudge(2, new int[][]{{1,2}})); // return 2


    }

    /**
     *
     * @param N : Number of people
     * @param trust A 2D array so each element of the array contains only 2 number [a,b] is present
     *              that means the person a trusts person b
     * @return -1 in case there is no judge or the person number that is the judge
     */
    /*
    [1,3] [2,3],[3,1]
    [1,3] -> The person 1 trusts person 3
    [2,3] -> The person 2 trusts person 3
    [3,1] -> The person 3 trusts person 1

 [0,0,1]
 [0,0,1]
 [1,0,0]
 Solution Approach:
 1) Using HashMaps  2) Using simple arrays  3) It can be solved using building this matrix
4) It could help to think of this problem in terms of directed graphs
5) Go over the elements in the array and if you find (a,b) put a in a set
Maintain a set consisting of people who trust someone.
So any person in the Set will not be a judge for sure because he trusts somebody.

   [1,3] [2,3],[3,1]  ->
   1 X
   2 X
   3 X

  Main observations
  (i) If there is judge he will not trust anyone. So if person #a is a judge you will not find a pair [a,b]
  (ii) If there is judge everyone will trust him.

  [1,3][2,3]
  1 X
  2 X
  3 -> (i)It doesn't trust anyone, (ii) Everyone else trusts him. This is the judge


  Algorithm:
 (i) net trust of a person A = #of people trusts him - #Of people he trusts;
  (ii) Find the person with net trust value = N-1 where N is the total number of people
   [1,3] [2,3],[3,1]
   net trust of 1 = 1 - 1 = 0
   net trust of 2 = 0 - 1 = -1
   net trust of 3 = 2 - 1 = 1
   For a judge to be present the net trust value for a person = N-1 = 3-1 = 2
   return -1;


   [1,3],[2,3]
   net trust of 1 = 0 - 1 = -1
   net trust of 2 = 0 - 1 = -1
   net trsut of 3 = 2 - 0 = 2 = N-1 = 3 -1
   So 3 is the judge

   [1,2]
   net trust of 1 = 0 - 1 = -1
   net trust of 2 = 1 - 0 = 1 = N-1 = 2-1
   So 2 is the judge



     */
    public static int findJudge(int N, int[][] trust){
       int judge=-1;
       // (i) net trust of a person A = #of people trusts him - #Of people he trusts;
       int[] netTrust = new int[N+1]; // 1 to N
        /* build this array
     loop over the trust array (a,b)
     a trusts b
     decrement net trust of a by 1.
     increment net trust of b by 1.

     At the end of this loop net trust will hold the final values for each person
     when you have loop till the ith person the net trust array contains the net trust value of that person
     till the ith relation ship
*/
        for(int[] trst: trust){
            netTrust[trst[0]]--;
            netTrust[trst[1]]++;
        }
        // the netTrust array is built

      //  (ii) Find the person with net trust value = N-1 where N is the total number of people
        for(int i=1;i<=N;i++)
           if(netTrust[i]==N-1)
               return i;
       return judge;
    }

}
