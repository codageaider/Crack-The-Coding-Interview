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

2) select the longest duration pair
3) If two songs have the same duration then select the option with the lowest index
 */
public class Q1 {
    // (x + y = rideduration - 30
    //  x<y    (x,y)   (y,x)
    public static void main(String[] args) {
      //  System.out.println(findSongs(250, Arrays.asList(100, 180, 40, 110, 110, 100, 10, 100, 40, 1, 110, 110)));
           System.out.println(findSongs(250, Arrays.asList(100, 180, 40, 120, 10)).equals(Arrays.asList(1, 2)));
         System.out.println(findSongs(90, Arrays.asList(1, 10, 25, 35, 60)).equals(Arrays.asList(2, 3)));

    }
// Total time complexity = O(n)
    // space complexity = O(n)
    public static List<Integer> findSongs(int rideDuration, List<Integer> songDuration) {
        // key is the song duration and the value is the list of indexes where the song appears
        // O(n)
        Map<Integer, List<Integer>> map = buildSongDurationAndIndexMap(songDuration);
        //O(n)
        List<Pair> validPairs = findValidPairOfSongs(rideDuration, songDuration, map);
        // we have built the validPairs  // x + y = rideDuration-30
//        2) select the longest duration pair
//        3) If two songs have the same duration then select the option with the lowest index
       // O(n)
         if(validPairs.isEmpty()){
             return Arrays.asList(-1,-1);
         }
        Pair pair = findMaxPairWithLowestIndex(validPairs);
        List<Integer> output = new ArrayList<>();
        output.add(Math.min(pair.index1,pair.index2));
        output.add(Math.max(pair.index1,pair.index2));
        return output;
    }
//O(n) time
    private static Pair findMaxPairWithLowestIndex(List<Pair> validPairs) {
        Pair pair = null; // currentMaxSongsLength
        for (Pair p : validPairs) {
            if (pair == null) {
                pair = p;
            } else {
                if (p.compareTo(pair) > 0)
                    pair = p;
            }
        }
        return pair;
    }

    // any two distinct song that sum to rideduration-30  is valid
    // O(n) time
    private static List<Pair> findValidPairOfSongs(int rideDuration, List<Integer> songDuration, Map<Integer, List<Integer>> map) {
        List<Pair> validPairs = new ArrayList<>();
        for (int i = 0; i < songDuration.size(); i++) {
            int x = songDuration.get(i);
            int y = rideDuration - x - 30;
            /*We need a song with this durationin the songDuration list to make a pair
             x is certainly there in the songDuration but if y is there then this is a probably canditate for the valid pair
             */

            if(!map.containsKey(y)){

            } else {
                // we need to avoid selection the song with the same duration and same index
                if(x!=y && x<y){
                    Pair p = new Pair();p.songDuration1=x;p.songDuration2=y;p.index1=i;p.index2= map.get(y).get(0);
                    validPairs.add(p);
                } else {
                     //  x and y are same
                    List<Integer> indexes = map.get(x);
                    if(indexes.size()==1){
                        // we are selecting the same song
                    } else {
                        Pair p = new Pair();p.songDuration1=x;p.songDuration2=y;p.index1= map.get(y).get(0);p.index2= map.get(y).get(1);
                        if(!validPairs.contains(p))
                        validPairs.add(p);
                    }
                }
            }
        }
        return validPairs;
    }

    /*
    key, value map key is the duration and value is the list of indexes where it appears.
    ~ time = size of songDuration = n
    O(n)
     */
    private static Map<Integer, List<Integer>> buildSongDurationAndIndexMap(List<Integer> songDuration) {
        Map<Integer, List<Integer>> map = new HashMap<>();
        // We are building the hashmap
        for (int i = 0; i < songDuration.size(); i++) {
            Integer duration = songDuration.get(i);
            if (map.containsKey(duration)) {
                map.get(duration).add(i);
            } else {
                List<Integer> list = new ArrayList<>();
                list.add(i);
                map.put(duration, list);
            }
        }
        return map;
    }

}

/*
Valid pairs will be those the duration sum to rideduration -30
 */
class Pair implements Comparable {
    int songDuration1;
    int songDuration2;
    int index1;
    int index2;
/*
2) select the longest duration pair
3) If two songs have the same duration then select the option with the lowest index
 */
    // compare method returns <0 is the first item < second item , >0 else ,  if equal it will return 0;
    @Override
    public int compareTo(Object o) {
        Pair p1 = (Pair) o;
        int compareValue = Integer.compare(Math.max(songDuration1, songDuration2), Math.max(p1.songDuration1, p1.songDuration2));
        if (compareValue == 0) {
            return Integer.compare(Math.min(index1, index2), Math.min(p1.index1, p1.index2));
        } else {
            return compareValue;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return songDuration1 == pair.songDuration1 &&
                songDuration2 == pair.songDuration2 &&
                ((index1 == pair.index1 &&
                index2 == pair.index2) || (index1 == pair.index2 &&
                        index2 == pair.index1));
    }

    @Override
    public int hashCode() {
        return Objects.hash(songDuration1, songDuration2, index1, index2);
    }
}
