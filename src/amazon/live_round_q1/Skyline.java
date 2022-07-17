package amazon.live_round_q1;

import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
/*
(i) We are at building no. i, ensure that we have the max ladders and bricks
(ii) consider the next building requires a jump of j1 and a ladder was used at a point in time
where the jump was of j2 and suppose j1>j2 that is the jump required at the time when ladder was used is lesser
than the current jump.
(i) Suppose we use the bricks at current place.
Then we will be left with L ladders and B-j1 bricks
(ii) Suppose we place the ladder at j2 time at the current position and use the bricks at the time of ladder
then we will be left with L ladders and B-j2 bricks
Now B-j1<B-j2 becasue j1>j2
So choosing the (ii) option leaves us with more ladder and bricks in total
So its a better appraoch.


Main Idea: replace the ladder of minimal jump with bricks if the current jump is higher than it.
Using the above approach we will have the max ladders and bricks with us to move forward

Proof:



 */
public class Skyline {

    public static void main(String[] args) {
        System.out.println(furtherBuilding(Arrays.asList(4, 2, 20, 1, 5), 1, 4) == 4);
        System.out.println(furtherBuilding(Arrays.asList(4, 12,2,7,3,18,20,3,19), 10, 2) == 7);

    }

    public static int furtherBuilding(List<Integer> skyline, int bricks, int ladder) {
        Queue<Integer> ladderJumps = new PriorityQueue<>();
        // reach the ith building and updated the
        int i;
        for (i = 1; i < skyline.size() - 1; i++) {
            int jump = skyline.get(i + 1) - skyline.get(i);
            if (jump <= 0) // means that the next building is lower or equal height so we don't need to use anything
                continue;
            else {
                Integer minLadderJump = ladderJumps.peek();
                if (minLadderJump == null) {
                    if (ladder == 0 && bricks >= jump)
                        bricks--;
                    else if (ladder != 0)
                        ladder--;
                    else { // ladder and bricks are not sufficient to go to the next building
                        break;
                    }
                } else {
                    if (jump > minLadderJump) {
                        // use the ladder for this one and brick for the earlier one.
                        ladderJumps.poll();
                        ladderJumps.offer(jump); // we put the ladder with climb of jump
                        // and use bricks for the earlier ladder place
                        bricks = bricks - minLadderJump;
                        if (bricks < 0) // means the jump is not possible
                            break;
                    } else {
                        if (bricks >= jump)
                            bricks -= jump;
                        else
                            break;
                    }
                }


            }
        }
        return i;
    }
}
