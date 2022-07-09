package amazon;

import java.util.HashSet;
import java.util.Set;

/*

https://github.com/codageaider/Crack-The-Coding-Interview/blob/main/src/amazon/OA.mp4

 good -> 16
 g->{1} , go, goo, good, o, oo,ood, o, od,d
 (i,j) -> pair basically pair indicies
 Each substring contributes a number = number of distinct characters in that substring.
 g->{1} , go->{2}, goo->{2}, good->{3], o->{1}, oo-{1},ood->{2}, o->{1}, od->{2},d->{1}
 Total strength or the value to be returned =  1+2+2+3+1+1+2+1+2+1=16
 good -> 16
 test -> 19
 abc -> 10

a , ab, abc, b, bc, c = 1 + 2 + 3 + 1 + 2 + 1 = 10

(i) How many substrings are there for a given string of length n?
  Ans: n*(n+1)/2
  a1 a2 a3.....an
  a1, a1a2, a1a2a3,....,a1a2a3..an --> n substrings that start with a1
  substrings that start with a2 = n-1
  a2, a2a3, a2a3a4,,,...

  (i,j) i and j are indices 0<=i<=j => will give a substring of the given string.
  n *(n+1)/2

(ii)  Enumerate all substrings of the given string (i,j) pair will give a substring
  and calculate it's strength and then take sum of all the values.
n*(n+1)/2 substring -> O(n^3) approach, O(n^2) if you use some kind of memoization.

(iii) strength(password,n) -> we are only considering first n characters of password string.
strength(password,n+1) = strength(n) + strenght(lastchar), strength(last 2 char), strenght(last 3 char)+..
..
T(n) = T(n-1) + n -> T(n) = O(n^2)

(iv) O(n)
     - string only contains small characters between a and z
     Take a character a. What is the strength that this character contributes to the password.
     strength contributed by any character = number of substrings in which this character occurs.
     A character can only contribute 0 or 1 to a substring
   - Main step of the O(n) Solution
     Total = numOfSubstringInWhichOccurs('a')+numOfSubstringInWhichOccurs('b')+numOfSubstringInWhichOccurs('c')+
     numOfSubstringInWhichOccurs('d')+numOfSubstringInWhichOccurs('e')+numOfSubstringInWhichOccurs('f')+...
     .....+numOfSubstringInWhichOccurs('z')
   - numOfSubstringInWhichOccurs('a') = total number of substrings - number of substrings in which 'a' doesnot occur.
                                      = n * (n+1)/2 - number of substrings in which 'a' doesnot occur
         .....a...a.........a.......a........... if the given password is of this form.
         Any substring that doesn't contain the letter a must lie in the gap.
         So the substring must consists of letter other than a
         k1=5,k2=3,k4=...kp kp represents the number of dots in the p th gap of the substring.
         k1 dots or there k1 chars that are not a
         so the number of substrings that can be formed from this k1 dots = k1*(k1+1)/2

         - For each letter we will be doing one pass -> O(n) for each character
         - and since number of chars are limited = 26  -> 26*n -> O(n)

  TotalStrength(good)=strength(g) + strenght(o) + strength(d)
  {g,o,d}
  n = 4, total number of substrings = 4*(4+1)/2 = 10
 (i) g... = 10 - 3*(3+1)/2 = 10 - 6 = 4  <--- strength("g","good");
  (ii).oo. = 10 - 2 = 8
  (iii)...d = 10 - 6 = 4
  4 + 8 + 4 = 16

good -> 16
test -> 19
abc -> 10

totalStrength(password){
long total=0;
for(ch in 'a' to 'z'):
     total+=strength(ch,password);
return total
}
strength(ch,password){

n = password.length;
long numOfStringsThatDoesnotContainsCharch=0;
int lastIndex =-1; // last time this ch was detected while scanning through the string password
for( pc in password.toCharArray()){
     if(pc==ch){
         currIndex-lastIndex
         // currIndex has ch and lastIndex has ch character   from
         // currIndex+1 to lastIndex-1  we don't have ch lastIndex-1-currIndex+1+1 = lastIndex-currIndex+1
         long lengthOfGap = lastIndex-currIndex+1;
         numOfStringsThatDoesnotContainsCharch=lengthOfGap*(lengthOfGap+1)/2;
         lastIndex=currenIndex;
     }

}
numOfStringsThatDoesnotContainsCharch = (n-1-lastIndex+1+1) *(...+1)/2;
return n*(n+1)/2 - numOfStringsThatDoesnotContainsCharch;
}

 */
public class OA {

    public static void main(String[] args) {
        System.out.println(findPasswordStrength("good")==16);
        System.out.println(findPasswordStrength("test") == 19);
        System.out.println(findPasswordStrength("abc") == 10);
    }

    private static long findPasswordStrength(String password) {
        long result=0;
        Set<Character> set = new HashSet<>();
        for(Character ch:password.toCharArray())
            set.add(ch);
        for(Character ch: set){
            result+=strengthContribution(ch,password);
        }
        return result;
    }
//.....a...a.........a.......a...........
    private static long strengthContribution(char c, String password) {
        long numOfStringsWithoutChar=0;
        if(password.indexOf(c)==-1)
            return 0;
        int count=0; // count of the currentgap
        // maintain the count = number of chars in the gap or number of chars that doesn't contain the char c
        for(int i=0;i<password.length();i++){
            if(password.charAt(i)==c){
                numOfStringsWithoutChar+= (count*(count+1))/2;
                count=0;
            }
            else {
                count++;
            }
        }
        numOfStringsWithoutChar+=(count*(count+1))/2;
        return (password.length()*(password.length()+1))/2-numOfStringsWithoutChar;
    }
}
// In the optimized approach 26 * n
// Instead of scanning for all the characters scan only those characyers that are there in the password

// (ii) optimization.
/*
Maintain a map for each character that stores the count value.
maintain a map for each character that stores the number of substrings without this character.
26 * n
 */