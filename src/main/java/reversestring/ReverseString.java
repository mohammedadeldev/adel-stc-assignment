package reversestring;

import java.util.Stack;

public class ReverseString {

    public static String reverse(String s) {
        Stack<Character> stack = new Stack<>();
        StringBuilder result = new StringBuilder();

        for (char ch : s.toCharArray()) {
            if (ch == ')') {
                StringBuilder temp = new StringBuilder();
                while (!stack.isEmpty() && stack.peek() != '(') {
                    temp.append(stack.pop());
                }
                if (!stack.isEmpty() && stack.peek() == '(') {
                    stack.pop(); // Remove the opening '('
                }
                // Push reversed substring back into stack
                for (char c : temp.toString().toCharArray()) {
                    stack.push(c);
                }
            } else {
                stack.push(ch);
            }
        }

        // Build the final string
        while (!stack.isEmpty()) {
            result.append(stack.pop());
        }

        return result.reverse().toString();
    }

    public static void main(String[] args) {
        String[] testCases = {
                "abd(jnb)asdf",
                "abdjnbasdf",
                "dd(df)a(ghhh)",
                "(abcd)",
                "a(bc(de)f)g"
        };

        for (String testCase : testCases) {
            System.out.println("Input: " + testCase);
            System.out.println("Output: " + reverse(testCase));
        }
    }
}
