package amazon;

import java.util.*;

/*
Two sum
Given a set of integers and a sum S
we have to find two integers in the set x and y such that x + y = S
Map<Integer,Integer> map = new HashMap<>()
loop over the list of integers
  and put each integer in the map


  Now is there are two integers x and y that sum to S
  x + y = S
  x = S - y
  loop over each element of the Set S
  for y in S
      S-y must be in S

  x in S such that x = S-y and therefor x+y =S


  for each integer y in S
       int z = S-y
       if(map.containsKey(z)) // O(1) time on average
              {
              // there is an x in S such x = S-y
              and so we have found our pairs
              }
 This finds the pair of integers x and y such that x + y =S
 Time Complexity = O(n) <-- Benefits of using HashMap

 Brute force approach: Check all pairs (x,y) and if they sum to S
 O(n^2)
 (x,x) ---> x + x = S
 list of songs and we have to find two songs in it that sum to the rideDuration-30
 The songs must be different
 (song1Duration, song2Duration)
(10,60), (20,50)

(10,60) <-- this is something that will be returned.


3) If two songs have the same duration then select the option with the lowest index
 */
public class Q1 {
    public static void main(String[] args) {
        System.out.println(findSongs(250, Arrays.asList(100, 180, 40, 100, 10,100,40,1,110)));
     //   System.out.println(findSongs(250, Arrays.asList(100, 180, 40, 120, 10)).equals(Arrays.asList(1, 2)));
       // System.out.println(findSongs(90, Arrays.asList(1, 10, 25, 35, 60)).equals(Arrays.asList(2, 3)));

    }

    public static List<Integer> findSongs(int rideDuration, List<Integer> songDuration){
        // key is the song duration and the value is the list of indexes where the song appears
        Map<Integer, List<Integer>> map = new HashMap<>();
        // We are building the hashmap
        for(int i=0;i<songDuration.size();i++){
            Integer duration = songDuration.get(i);
            if(map.containsKey(duration)){
                map.get(duration).add(i);
            } else {
                List<Integer> list = new ArrayList<>();
                list.add(i);
                map.put(duration, list);
            }
        }
        System.out.println(map);
     return new ArrayList<>();
    }


}
