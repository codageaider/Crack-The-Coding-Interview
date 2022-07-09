package toptal.q2.java;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
Interview Question
1. HashMap
2. DFS - Depth First Search

 */
/*
Idea
- you can generate the final string, from any single letter in the string
-
Example : P>E , E>R, R>U

- To generate a hash map, scan through the rule
 map1 : P->E, E->R, R->U
 - Generate a new map that is the reverse of this hash map
  map2: E->P, R->E, U->R

 select a letter in the string say E
 ouput = E
 find the value corresponding to this letter E in map1
 ouput=ER (append R to the right side)
 find the value corresponding to this letter R in map1
 ouput=ERU
 ...
 .
 For building the left side of letter E we will use the map2
 output= PERU
  find the value corresponding to this letter P in map2
map2 doesn't contain the letter P as a key.
So we stop

- Generated all the letters that come after the letter E using map1
- Generated all the letters that come before the letter E using map2

Time Complexity = O(n) where n is the length of the string
Space Complexity = O(n)
 */

public class FindWords {
    public static void main(String[] args) {
        System.out.println(findWords(Arrays.asList("P>E","E>R","R>U")));
        System.out.println(findWords(Arrays.asList("W>I", "R>L", "T>Z", "Z>E", "S>W", "E>R", "L>A",
                "A>N", "N>D", "I>T")));
        // SWITZERLAND
    }

    public static String findWords(List<String> rules) {
        Map<String, String> map1 = new HashMap<>();
        Map<String, String> map2 = new HashMap<>();
        for (String rule : rules) {
            String key = rule.split(">")[0];
            String value = rule.split(">")[1];
            map1.put(key, value);
            map2.put(value, key); // reverse hashmap
        }

        String rule = rules.get(0);
        String leftMostChar = rule.split(">")[0];
        String result = rule.split(">")[0] + rule.split(">")[1];
        String rightKey = rule.split(">")[1];
        // fills up the string to the right
        while (map1.containsKey(rightKey)) {
            result += map1.get(rightKey);
            rightKey = map1.get(rightKey);
        }
        // fills up the string to the left
        while (map2.containsKey(leftMostChar)) {
            result = map2.get(leftMostChar) + result;
            leftMostChar = map2.get(leftMostChar);
        }
        return result;
    }
}
