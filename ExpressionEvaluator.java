package cs2110;
import java.util.Scanner;

public class ExpressionEvaluator {

     // TODO: Update these specs as you build out the functionality of the `evaluate()` method.
    /**
     * Evaluates the given well-formed mathematical expression `expr` and returns its value.
     * Currently, the `evaluate()` method supports:
     * - Single-digit int literals
     * - Addition
     * - Multiplication
     * - Subtraction
     * - Parentheses
     */
    private enum Prev { NONE, NUMBER, RPAREN, OP }
    public static int evaluate(String expr) throws MalformedExpressionException{
        Stack<Integer> operands = new LinkedStack<>();
        Stack<Character> operators = new LinkedStack<>(); // invariant: contains only '(', '+', '-' and '*'
        if (expr .equals("")) throw new MalformedExpressionException("the string sould not be empty");
        boolean expectingOperator = false; // in infix notation, the first operand comes before an operator
        boolean isNum = false;
        boolean isSpace = false;
//        boolean unary = false;
        int unarySign = 1;
        Prev prev = Prev.OP;
            for (char c : expr.toCharArray()) {// arrays are Iterable, so can be used in enhanced-for loops
            if (c == ' '){
                isSpace = true;
                continue;
            }
            else if (c == '(') {
                if (prev == Prev.NUMBER || prev == Prev.RPAREN){
                    pushImplicitMultiply(operands, operators);
                }
                if (unarySign == -1){
                    operands.push(-1);
                    operators.push('*');
                    unarySign = 1;
                }
                // if (expectingOperator) throw new MalformedExpressionException("'(' cannot follow an operand");
                // assert !expectingOperator : "'(' cannot follow an operand";
                expectingOperator = false;
                isSpace = false;
                isNum = false;
                operators.push('(');
                prev = Prev.OP;
            } else if (c == '*') {
                if (unarySign == -1)
                    throw new MalformedExpressionException("'-' must follow an operand, not an operator");
                if (!expectingOperator) throw new MalformedExpressionException("'*' must follow an operand, not an operator");
//                assert expectingOperator : "'*' must follow an operand, not an operator";
                while (!operators.isEmpty() && operators.peek() == '*') {
                    oneStepSimplify(operands, operators);
                }
                operators.push('*');
                expectingOperator = false;
                isNum = false;
                isSpace = false;
                prev = Prev.OP;
            } else if (c == '+') {
                if (unarySign == -1) throw new MalformedExpressionException("'-' must follow an operand, not an operator");
                if (!expectingOperator) throw new MalformedExpressionException("'+' must follow an operand, not an operator");
//                assert expectingOperator : "'+' must follow an operand, not an operator";
                while (!operators.isEmpty() && (operators.peek() == '*'
                        || operators.peek() == '+' || operators.peek() == '-')) {
                    oneStepSimplify(operands, operators);
                }
                operators.push('+');
                expectingOperator = false;
                isNum = false;
                isSpace = false;
                prev = Prev.OP;
            } else if (c == '-'){
                if (!expectingOperator) {
                    unarySign *= -1;
                    isNum = false;
                    isSpace = false;
                    prev = Prev.OP;
                    continue;
                }
                while (!operators.isEmpty()
                        && (operators.peek() == '*' || operators.peek() == '+'|| operators.peek() == '-')) {
                    oneStepSimplify(operands, operators);
                }
                operators.push('-');
                //throw new MalformedExpressionException("'-' must follow an operand, not an operator");
                //assert expectingOperator : "'-' must follow an operand, not an operator";
                expectingOperator = false;
                isNum = false;
                isSpace = false;
                prev = Prev.OP;
            }else if (c == ')') {
                if (unarySign == -1) throw new MalformedExpressionException("'-' must follow an operand, not an operator");
                if (!expectingOperator) throw new MalformedExpressionException("')' must follow an operand, not an operator");
//                assert expectingOperator : "')' must follow an operand, not an operator";
                if (operators.isEmpty()) throw new MalformedExpressionException("mismatched parentheses, extra ')'");
//                assert !operators.isEmpty() : "mismatched parentheses, extra ')'";
                while (operators.peek() != '(') {
                    oneStepSimplify(operands, operators);
                    if (operators.isEmpty()) throw new MalformedExpressionException("mismatched parentheses, extra ')'");
//                    assert !operators.isEmpty() : "mismatched parentheses, extra ')'";
                }
                operators.pop(); // remove '('
                prev = Prev.RPAREN;
                expectingOperator = true;
                isNum = false;
                isSpace = false;

            } else { // c is a digit
                if (prev == Prev.RPAREN) {
                    pushImplicitMultiply(operands, operators);
                }
                if (!(c >= '0' && c <= '9')) throw new MalformedExpressionException("expression contains an illegal character");
//                assert c >= '0' && c <= '9' : "expression contains an illegal character";
                //when the previous operator is '-', means -1 * the later operand
                if (unarySign == -1) {
                    operands.push(-1);
                    operators.push('*');
                    unarySign = 1;
                }

                if(isNum){
                    if(isSpace) throw new MalformedExpressionException("expression contains space between numbers");
                    operands.push((c - '0')+operands.pop()*10);
                }
                else operands.push(c - '0'); // convert c to an int and auto-box
                expectingOperator = true;
                isNum = true;
                isSpace = false;
                prev = Prev.NUMBER;
            }
        }
        if (!expectingOperator) throw new MalformedExpressionException("expression must end with an operand, not an operator");
//        assert expectingOperator : "expression must end with an operand, not an operator";
        while (!operators.isEmpty()) {
            if (operators.peek() == '(') throw new MalformedExpressionException("mismatched parentheses, extra '('");
//            assert operators.peek() != '(' : "mismatched parentheses, extra '('";
            oneStepSimplify(operands, operators);
        }

        // If the above assertions pass, the operands stack should include exactly one value,
        // the return value. We'll include two assertions to verify this as a sanity check.
        assert !operands.isEmpty();
        int result = operands.pop();

        assert operands.isEmpty();
        return result;
    }

    /**
     * Helper method that partially simplifies the expression by `pop()`ping one operator from the
     * `operators` stack, `pop()`ping its two operands from the `operands` stack, evaluating the
     * operator, and then `push()`ing its result onto the `operands` stack. Requires that
     * `opererators.peek()` is '+' or '*' and `operands` includes at least two elements.
     */
    private static void oneStepSimplify(Stack<Integer> operands, Stack<Character> operators) {
        char op = operators.pop();
        assert op == '+' || op == '*' || op == '-';

        int o2 = operands.pop(); // second operand is higher on stack
//        System.out.println(o2+"is o2");o2
        int o1 = operands.pop();
        if (op == '+')
            operands.push(o1 + o2);
        else if(op == '*')
            operands.push(o1 * o2);
        else
            operands.push(o1 - o2);
    }
     private static void pushImplicitMultiply(Stack<Integer> operands, Stack<Character> operators){
         while (!operators.isEmpty() && operators.peek() == '*') {
             oneStepSimplify(operands, operators);
         }
         operators.push('*');
    }

    /**
     * A very basic calculator application.
     */
    public static void main(String[] args) throws Exception {
        try (Scanner in = new Scanner(System.in)) {
            while (true) { // repeat indefinitely
                System.out.print("Enter an expression, or enter \"q\" to quit: ");
                String expr = in.nextLine();
                if (expr.equals("q")) {
                    break; // exit loop
                }
                System.out.println("= " + evaluate(expr));
            }
        }
    }
}
