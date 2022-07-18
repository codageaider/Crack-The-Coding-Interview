package amazon.live_round_q1;

import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/*
Suppose in any sequence of ladders and bricks you have used
There is a climb of 10 units using bricks and 5 units using ladder
and currently you are left with L ladders and B bricks
1 ladder to make a climb of 10 units and 5 bricks to make a climb of 5 units
L, B+5
So we have more bricks now.
If the climb of the ladder is less than the climb of the bricks then swap them

10 units of climb using bricks and 3 units of climb using ladder
perform the swap, L , B+7

If we are making a swap, swap with the minimal climb ladder. This will ensure
that you have more bricks to move forward.

The moves should be optimal: There is no swap possible
Algorithm:
1) First use all the ladders and reach the furthest building possible
2) Now no ladders are left
3)
(i) Suppose the climb is of 7 units and you have 5 units of bricks left

     (1)
     - you used a ladder to climb 2 units of height.
     - replace it with bricks, 5-2=3 , bricks=3 and 1 ladder left
     - using the ladder we can move ahead
     (2)
      the minimal jump made with ladder if of height H1
      and the next jump that you need to make is of height H2
      And you have exhausted all the ladders, and you are left with some bricks.
      Suppose B bricks. B<H2 -> I cannot use the bricks to reach the next building.

      (3)
      I replace the ladder at height H1 with bricks.
          if B<H1 -> swap is not possible
          -> There are no ladders and not sufficient bricks to climb the next building.
              and the minimal ladder jump is greater than the number of bricks we are left with.
              A swap is not possible.
       We have reached the farthest building possible.

       (4) I replace the ladder at the point of climb of H1 units.
             if B>=H1 -> A swap is possible
             B-H1 bricks left and an additional ladder left.
             Using this I can reach the next building.

       (5) B >=H2 , that means we have sufficient number of bricks to reach the next building.
            Should I use bricks or should I use ladder?
            To get a ladder we need a swap
            If the ladder of the minimal possible climb <H2 then I can use bricks here and
            ladder at the current position.

// WE always swap with the ladder with the minimal climb;
// We will need to extract the minimum element from the ladder climbs array
// -> O(n^2)
// Min Heap:E
Extract -> O(1)
Insertion -> O(log n)
Deletion -> O(log n)

Using min heap we can solve this problem in O(n log n)
Java has PriorityQueue for MinHeap
offer() -> adds an element to the PQ
poll() -> This removes the minimum element from PQ
peek() -> return the minimum element from the PQ. But it won't remove it like poll()
Time Complexity:

Space Complexity:
 */
public class Skyline {
    public static void main(String[] args) {
        System.out.println(furthestBuilding(Arrays.asList(4,2,20,1,5),1,4));// 4
        System.out.println(furthestBuilding(Arrays.asList(4,12,2,7,3,18,20,3,19),10,2)); //7
    }

    public static int furthestBuilding(List<Integer> skyline, int bricks, int ladder) {
        Queue<Integer> ladderClimbsHeap = new PriorityQueue<>();
        int currBuilding;
        for (currBuilding = 0; currBuilding < skyline.size() - 1; currBuilding++) {
            int jump = skyline.get(currBuilding + 1) - skyline.get(currBuilding);
            if (jump <= 0)
                continue;
            if (ladder != 0) {
                ladder--;
                ladderClimbsHeap.offer(jump);
                continue;
            }
            if (bricks < jump) {// We cannot climb to the next building with the given number of bricks
                int minLadderJump = ladderClimbsHeap.peek();
                if (bricks < minLadderJump)
                    break;
                if (bricks >= minLadderJump) {
                    ladderClimbsHeap.poll();
                    bricks = bricks - minLadderJump;
                }
            } else if (bricks >= jump) { // It's possible to make the jump using the bricks
                // if we use the bricks to go to the next building we will have to use jump no. of bricks
                int minLadderJump = ladderClimbsHeap.peek();
                if (jump >= minLadderJump) {// use bricks instead of ladder at this min ladder jump position
                    bricks = bricks - minLadderJump;
                    ladderClimbsHeap.poll();
                    ladderClimbsHeap.offer(jump);
                    continue;
                } else if (jump < minLadderJump) {
                    bricks = bricks - jump;
                    continue;
                }
            }
        }
        return currBuilding;
    }
}
