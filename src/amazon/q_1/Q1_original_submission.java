package amazon.q_1;
//15/15 test cases passed
import java.util.*;

public class Q1_original_submission {
    public static void main(String[] args) {

        System.out.println(findSongs(250, Arrays.asList(100, 180, 40, 120, 10)).equals(Arrays.asList(1,2)));
        System.out.println(findSongs(90, Arrays.asList(1,10,25,35,60)).equals(Arrays.asList(2,3)));

    }

    public static List<Integer> findSongs(int rideDuration, List<Integer> songDurations) {
        Map<Integer, Integer> map = new HashMap<>();
        Map<Integer, List<Integer>> map2 = new HashMap<>();
        Integer pair1 = null, pair2 = null;
        Integer index1 = null, index2 = null;
        for (int i = 0; i < songDurations.size(); i++) {
            Integer dur = songDurations.get(i);
            if (!map.containsKey(dur)) {
                map.put(dur, i);
                List<Integer> arr = new ArrayList<>();
                arr.add(i);
                map2.put(dur, arr);
            } else {
                List<Integer> arr2 = map2.get(dur);
                arr2.add(i);
                map2.put(dur, arr2);
            }

        }
        for (int i = 0; i < songDurations.size(); i++) {
            int a = songDurations.get(i);
            int b = rideDuration - a - 30;
            if (map.containsKey(b)) {
                if (a == b) {
                    if (map2.get(a).size() > 1) {
                        if (Math.max(a, b) > Math.max(pair1, pair2)) {
                            pair1 = a;
                            pair2 = b;
                            index1 = map2.get(a).get(0);
                            index2 = map2.get(a).get(1);
                        } else if (a == pair1 && b == pair2) {
                            if (Math.min(map2.get(a).get(0), map2.get(a).get(1)) < Math.min(index1, index2)) {
                                pair1 = a;
                                pair2 = b;
                                index1 = map2.get(a).get(0);
                                index2 = map2.get(a).get(1);
                            }
                        }

                    }
                } else if (pair1 == null) {
                    pair1 = a;
                    pair2 = b;
                    index1 = i;
                    index2 = map.get(b);
                } else {
                    if (Math.max(a, b) > Math.max(pair1, pair2)) {
                        pair1 = a;
                        pair2 = b;
                        index1 = map.get(a);
                        index2 = map.get(b);
                    } else if (a == pair1 && b == pair2) {
                        if (Math.min(map.get(a), map.get(b)) < Math.min(index1, index2)) {
                            pair1 = a;
                            pair2 = b;
                            index1 = map.get(a);
                            index2 = map.get(b);
                        }
                    }
                }
            }
        }
        List<Integer> songIndex = new ArrayList<>();
        if (index1 != null) {
            songIndex.add(index1);
            songIndex.add(index2);
            songIndex.sort(Integer::compareTo);
        } else {
            songIndex.add(-1);
            songIndex.add(-1);
        }
        return songIndex;
    }
}
