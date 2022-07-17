package amazon.live_round_q1;

import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/*
Question for recent Amazon virtual live coding round:
https://github.com/codageaider/Crack-The-Coding-Interview/blob/main/src/amazon/live_round_q1/skyline-ladder-bricks.png
 */

/*

Suppose at any point there is a jump of j1 unit required to reach the next building
Suppose that the minimal jump where the ladder was used had j2 units
suppose j2<j1

Ways to reach the next building
(i) j1 bricks to reach the next building
       L, B-j1
(ii) A ladder to reach the next building.
use bricks at the place where the ladder at j2 units was used.
L, B-j2


ex-2
At building no. 4 we are left with Ladders=0 and bricks = 10
How do I reach the 5th building? Use bricks or ladders
If I use bricks to reach the 5th building L=0, B-15
L=0, B-5

Given any series of ladders and bricks to reach the further building.
Where you used b bricks to climb to the next building
and you used a ladder to climb to a building of height <b;
If you replace the bricks  and ladders used at these two positions
Then you will be left with more bricks.

Main Idea: replace the ladder of minimal jump with bricks if the current jump
is higher than it.
Using this approach we will be left with more ladders and bricks.
That means using this approach we have optimized the numebrs of bricks and ladders
we have at any point in time.

Example: You reached the current building by using 100 bricks (100 units)
and at some previous point in time you used a ladder to climb 50 units
currently L , B -> (L,B-100) ->(L,B-50)

minHeap
Whenever I use a ladder for jumping j units
minHeap.add(j) to the minheap

pseudocode:
loop over the building heights
    currentjumpreq to reach the next building
        if currentjumpreq > minHeap.get()
            then interchange the use of bricks and ladders
        else {
            // if we have ladder use it.
            else {
                check if we have the required number of bricks.

            }

        }

Ensure that at no point you should have a jump with bricks which is higher
than the minimal ladder jump.

at building i I want the max ladders and max bricks left.
Reach jump ladder, 
ensure it is optimal 

use bricks, ensure it is optimal

 */
public class Skyline {
    public static void main(String[] args) {
        System.out.println(furtherBuilding(Arrays.asList(4, 2, 20, 1, 5), 1, 4) == 4);
        System.out.println(furtherBuilding(Arrays.asList(4, 12, 2, 7, 3, 18, 20, 3, 19), 10, 2) == 7);
    }

    /*
    Time Complexity:
    n is the size of skyline or the numebr of buildings
    (i) Every time we need to replace the ladder with bricks and place the ladder to climb the current building
    we will need to fetch the element from the heap -> O(1)
    and we will have to add the element to the heap -> O(log n)
    and to delete - > O(log n)

     --> O(n log n)
    https://en.wikipedia.org/wiki/Binary_heap

    Time Complexity = log1 + log 2 + ... log n  = log (n!) ~ n log n

    https://en.wikipedia.org/wiki/Stirling%27s_approximation
https://mathworld.wolfram.com/StirlingsApproximation.html
    Space Complexity: O(n)
     */
    public static int furtherBuilding(List<Integer> skyline, int bricks, int ladder) {
        Queue<Integer> ladderJumpsQueue = new PriorityQueue<>();
        int i;
        // i-> which building we need to reach next
        for (i = 0; i < skyline.size() - 1; i++) {  // -> n loops
            int jump = skyline.get(i + 1) - skyline.get(i);
            if (jump <= 0)
                continue;
            else {
                Integer minLadderJump = ladderJumpsQueue.peek();
                if (minLadderJump == null) {// when the queue is empty, I haven't used any ladder
                    if (ladder != 0) {
                        ladder--;
                        ladderJumpsQueue.offer(jump);

                    } else if (ladder == 0 && jump <= bricks) {// use bricks
                        bricks = bricks - jump;
                    } else {// ladders==0 and jump > bricks
                        break;
                    }
                } else {// I have ladders
                    // I have to make a climb of jump units
                    // can I use bricks a ladder too
                    if (bricks >= jump) {
                        if (minLadderJump < jump) {
                            bricks = bricks - minLadderJump;
                            ladderJumpsQueue.poll();
                            ladderJumpsQueue.offer(jump);
                        } else {
                            bricks = bricks - jump;
                        }
                    } else {
                        if (minLadderJump <= bricks) {
                            bricks = bricks - minLadderJump;
                            ladderJumpsQueue.poll();
                            ladderJumpsQueue.offer(jump);
                        } else if (ladder == 0)
                            break;
                        else if (ladder != 0) {
                            ladder--;
                            ladderJumpsQueue.offer(jump);
                        }
                    }

                }

            }


        }
        return i;
    }
}
