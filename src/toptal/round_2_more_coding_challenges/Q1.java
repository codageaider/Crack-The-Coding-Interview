package toptal;
/*
Code will be pushed to the repository: https://github.com/codageaider/Crack-The-Coding-Interview/
Quora Space for discussion Q n A: https://codageaider.quora.com/
You can post your questions here and give comments on existing ones.
This could serve as a nice collaborative learning space.

Youtube Channel: https://youtube.com/codageaider/
Contact Directly for further questions on:
WhatsApp: +17604747575
Email: expert@codageaider.com
Telegram: @codageaider

Explanation:
countMissingTags("<app></app></app></app>") == 2);
countMissingTags("<app></app><app></app>") == 0);
countMissingTags("<app></app><app><app>") == 2);
countMissingTags("</app><app><app>") == 3);

</app><app><app> -> </app> -> 1 , <app> -> 2
</app><app>  -> unbalanced , </app> -> 1 <app> -> 1
"<app></app><app></app><app></app>" I need to add 2 xml tags -> 2
<app></app><app></app><app></app>" -> 3 tags to make this xml well balanced.

for every open tag there must be a closing tag after the open tag
and for closing tag there must be an open tag before it.
<app> -> ( , </app> -> )
"<app></app></app></app>" -> ()))  , third char is ) doesn't have a matching open paranthesis , fourth char is ) does not have an opening tag -> 2
"<app></app><app></app>" -> ()()
<app></app><app><app>" -> ()((

WE have to count the open paranthesis that are unmatched and closed parenthesis that are unmatched
unmatched open + unmatched close  parenthesis / tags
()))

o=
c=))
Stack o will be left with unmatched open parenthesis and c will be left with unmatched closed paranthesis
unmatched open + unmatched close  parenthesis / tags = o.size() + c.size() = 2
</app><app><app> -> )((
)((

o=((
c=)
o.size() + c.size()=3

Similar Problem: Balanced Parenthesis
 */


import java.util.Stack;

public class Q1 {
    public static void main(String[] args) {
        System.out.println(countMissingTags2("<app></app></app></app>") == 2);
        System.out.println(countMissingTags2("<app></app><app></app>") == 0);
        System.out.println(countMissingTags2("<app></app><app><app>") == 2);
        System.out.println(countMissingTags2("</app><app><app>") == 3);
        // <app></app>
    }

    private static int countMissingTags(String log) {
        String logWithParenthesis = converXmlToParenthesis(log);
        Stack<Character> openStack = new Stack<>();
        Stack<Character> closedStack = new Stack<>();
        for (Character ch : logWithParenthesis.toCharArray()) {
            if (ch == '(')
                openStack.push('(');
            if (ch == ')') {
                if (!openStack.isEmpty())
                    openStack.pop();
                else
                    closedStack.push('(');
            }
        }
        //unmatched open + unmatched close  parenthesis / tags = o.size() + c.size() = 2
        return openStack.size() + closedStack.size();
    }
/*

How many unmatched open parenthesis are there and how many unmatched closed parenthesis are there
unmatchedOpenParen, unmatchedClosedParen
sum of these is the required answer
"<app></app></app></app>" -> ()))
o=1 , c=

if there is open parentheses o++
if there is a closed parenthesis and o!=0 => o--
if o==0, c++
"<app></app></app></app>" -> ()))
o=0, c=0
o=1, c=0
o=0, c=0
o=0,c=1
o=0, c=2
o+c=2
 */
    private static int countMissingTags2(String log) {
        String logWithParenthesis = converXmlToParenthesis(log);
        int unmatchedOpenParenthesis=0;
        int unmatchedClosedParenthesis=0;
        for(char ch: logWithParenthesis.toCharArray()){
            if(ch=='(')
                unmatchedOpenParenthesis++;
            else {
                if(unmatchedOpenParenthesis>0)
                    unmatchedOpenParenthesis--;
                else
                    unmatchedClosedParenthesis++;
            }
        }
        return unmatchedOpenParenthesis+unmatchedClosedParenthesis;
    }


    private static String converXmlToParenthesis(String log) {
        log = log.replaceAll("<app>", "(");
        log = log.replaceAll("</app>", ")");
        return log;
    }
}
