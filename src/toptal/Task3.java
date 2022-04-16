package toptal;

import java.util.Arrays;
import java.util.Collections;
import java.util.PriorityQueue;

/*

A=[5,19,8,1]  Sum = 5 + 19 + 8 + 1 = 33, sum(A) = 33 <-- at the beginning of the
code or starting any operation on it
I choose the element 19 and reduce it by half
A1 = [5,19/2,8,1] = [5,9.5,8,1]  , numOperation=1, currentSum = 23.5
Keep doing it till the sum <= half of initial sum =33/2 = 16.5
After doing one such operation A1 = [5,9.5,8,1] and currentCum = 23.5 and my aim is to make sum <=16.5
Lets select 9.5 again
A2 = [5,4.75,8,1], #op = 2, currentSum = 18.75 --> still not reached <=16.5
A3 = [5,4.75,4,1] , #op=3, currentSum = 14.75 <=16.5 so I have reached the goal
The number of operations/steps in above approach = 3



In the above we reached our goal at the 3rd operation.

A = [5,19,8,1]
A1=[2.5,19,8,1] , sum =30.5 > 16.5 so we haven't reached our goal.
So we have to keep selecting an element from the Array and reduce it by half
sum the array and check if the new sum is <=16.5 (half of the initial array)
A2=[2.5,19,8,0], sum = 29.5 >16.5
A3=[2.5,19,4,0], sum = 25.5 > 16.5
A4=[2.5, 19, 2, 0], sum = 23.5 > 16.5
A5 = [2.5,9.5,2,0] , sum = 14 <=16.5 so I have reached my aim / goal
The number of operations/steps in above approach = 5


Q: Write a function which given an array returns a number = minimum number of operations
you need to perform to reach a sum <= half of the initial array

Solve it?
Algorithm:

choosing a larger number at each step results in an array with a lesser sum...
And so we have a good chance of reaching half of the sum of original array in lesser of steps.
greedy strategy -> Choose the largest number half it and check the sum
again choose the largest number half it and check the sum
till the sum reaches the required goal of <= 16.5 ( half of original sum)


 */
public class Task3 {
    public static void main(String[] args) {
        System.out.println(solution(new int[]{5, 19, 8, 1})); // should return 3
        System.out.println(solution(new int[]{10, 10})); // should return 2
        System.out.println(solution(new int[]{3, 0,5})); // should return 2

    }

    private static int solution(int[] A) {
        PriorityQueue<Double> pq = new PriorityQueue<>(Collections.reverseOrder());
        // This is the max-heap
        // Collections.reverseOrder is a comparator
        // By default the priority queue is a min-heap
        for (int elem : A)
            pq.add(Double.valueOf(elem));
        // The above is building the max-heap
        Double initialSum = Double.valueOf(Arrays.stream(A).sum());
        // This calculates the initial sum
        Double totalPollution = initialSum;
        double aim = initialSum / 2; // we need to keep halving till we reach the aim
        int count = 0;
        while (totalPollution > aim) {
            Double maxElement = pq.remove();// This will remove the root element of the heap
            pq.add(maxElement/2.0); // Since we have removed the max element
            // we are inserting half of it
            totalPollution=totalPollution-maxElement/2.0;
            // recalculating the new sum
            count++; // counts the number of operations
        }

        return count;
    }
}
