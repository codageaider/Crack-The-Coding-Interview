package amazon.live_round_q1;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;

class Solution {
    public static void main(String[] args) {
        System.out.println(furthestBuilding(new int[]{4,2,20,1,5},1,4));// 4
        System.out.println(furthestBuilding(new int[]{4,12,2,7,3,18,20,3,19},10,2)); //7
    }
    public static int furthestBuilding(int[] heights, int bricks, int ladders) {
        int bricksUsed=0;
        
        Queue<Integer> pq=new PriorityQueue<>();
        
        for(int i=1;i<heights.length;i++){
            int diff=heights[i]-heights[i-1];
            
            if(diff>0){
                pq.add(diff);
                if(pq.size()>ladders){
                bricksUsed+=pq.remove();
                
                    if(bricksUsed>bricks){
                        return i-1;
                    }
                }
            }
            
        }
        
        return heights.length-1;
    }
}