package cs2110;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class EvaluatorTest {

    //should here be MalformedExpression for every single test?
    @DisplayName("WHEN we evaluate an expression containing only a single digit, THEN that digit "
            + "is returned.")
    @Test
    public void testDigit() throws MalformedExpressionException{
        assertEquals(0, ExpressionEvaluator.evaluate("0"));
        assertEquals(1, ExpressionEvaluator.evaluate("1"));
        assertEquals(5, ExpressionEvaluator.evaluate("5"));
    }

    @DisplayName("WHEN we evaluate an expression containing only a single digit within "
            + "parentheses, THEN that digit is returned.")
    @Test
    public void testParenthesizedDigit() throws MalformedExpressionException{
        assertEquals(0, ExpressionEvaluator.evaluate("(0)"));
        assertEquals(2, ExpressionEvaluator.evaluate("(2)"));
        assertEquals(4, ExpressionEvaluator.evaluate("((4))"));
    }

    @DisplayName("WHEN we evaluate an expression containing one addition operation applied to two "
            + "single-digit operands, THEN the correct result is returned.")
    @Test
    public void testOneAddition() throws MalformedExpressionException{
        assertEquals(3, ExpressionEvaluator.evaluate("1+2"));
        assertEquals(11, ExpressionEvaluator.evaluate("4+7"));
        assertEquals(9, ExpressionEvaluator.evaluate("9+0"));
    }
    @DisplayName("WHEN we evaluate an expression containing one multiplication operation applied to "
            + "two single-digit operands, THEN the correct result is returned.")
    @Test
    public void testOneMultiplication() throws MalformedExpressionException{
        assertEquals(2, ExpressionEvaluator.evaluate("1*2"));
        assertEquals(28, ExpressionEvaluator.evaluate("4*7"));
        assertEquals(0, ExpressionEvaluator.evaluate("9*0"));
    }

    @DisplayName("WHEN we evaluate an expression containing one addition operation applied to two "
            + "single-digit operands with additional parentheses, THEN the correct result is "
            + "returned.")
    @Test
    public void testOneOperatorParentheses() throws MalformedExpressionException{
        assertEquals(3, ExpressionEvaluator.evaluate("(1+2)"));
        assertEquals(3, ExpressionEvaluator.evaluate("(1)+2"));
        assertEquals(3, ExpressionEvaluator.evaluate("1+(2)"));
        assertEquals(3, ExpressionEvaluator.evaluate("(1)+(2)"));
        assertEquals(3, ExpressionEvaluator.evaluate("((1)+2)"));
        assertEquals(3, ExpressionEvaluator.evaluate("(1+(2))"));
        assertEquals(3, ExpressionEvaluator.evaluate("((1)+(2))"));
    }

    @DisplayName("WHEN an expression contains multiple of the same operator, THEN "
            + "it is correctly evaluated")
    @Test
    public void testOneOperatorMultipleTimes() throws MalformedExpressionException{
        assertEquals(6, ExpressionEvaluator.evaluate("1+2+3"));
        assertEquals(21, ExpressionEvaluator.evaluate("4+8+9"));
        assertEquals(28, ExpressionEvaluator.evaluate("1+2+3+4+5+6+7"));
        assertEquals(84, ExpressionEvaluator.evaluate("4*7*3"));
        assertEquals(180, ExpressionEvaluator.evaluate("5*6*2*3"));
    }

    @DisplayName("WHEN an expression contains both addition and multiplication but no "
            + "parentheses, THEN the order of operations is respected.")
    @Test
    public void testBothOperators() throws MalformedExpressionException{
        assertEquals(7, ExpressionEvaluator.evaluate("1+2*3"));
        assertEquals(5, ExpressionEvaluator.evaluate("1*2+3"));
        assertEquals(15, ExpressionEvaluator.evaluate("1+2+3*4"));
        assertEquals(11, ExpressionEvaluator.evaluate("1+2*3+4"));
        assertEquals(14, ExpressionEvaluator.evaluate("1*2+3*4"));
        assertEquals(25, ExpressionEvaluator.evaluate("1+2*3*4"));
        assertEquals(10, ExpressionEvaluator.evaluate("1*2*3+4"));
    }

    @DisplayName("WHEN an expression contains both addition and multiplication and "
            + "non-nested parentheses, THEN the order of operations is respected.")
    @Test
    public void testBothOperatorsParentheses() throws MalformedExpressionException{
        assertEquals(14, ExpressionEvaluator.evaluate("2+(3*4)"));
        assertEquals(20, ExpressionEvaluator.evaluate("(2+3)*4"));
        assertEquals(10, ExpressionEvaluator.evaluate("(2*3)+4"));
        assertEquals(14, ExpressionEvaluator.evaluate("2*(3+4)"));
        assertEquals(45, ExpressionEvaluator.evaluate("(2+3)*(4+5)"));
        assertEquals(70, ExpressionEvaluator.evaluate("2*(3+4)*5"));
    }

    @DisplayName("WHEN an expression contains both addition and multiplication and "
            + "nested parentheses, THEN the order of operations is respected.")
    @Test
    public void testBothOperatorsNestedParentheses() throws MalformedExpressionException{
        assertEquals(94, ExpressionEvaluator.evaluate("2*(3+4*(5+6))"));
    }

    // TODO: Add unit testing for all of the features that you add to the ExpressionEvaluator
    //  over the course of the assignment. Be sure that your tests have descriptive method names
    //  and @DisplayNames. Your tests will be evaluated for their correctness and coverage.

    @DisplayName("WHEN an expression contains both multi-digit addition , THEN the order of "
            + "operations is respected.")
    @Test
    public void testMultiDigitComputing() throws MalformedExpressionException{
        assertEquals(912, ExpressionEvaluator.evaluate("2*456"));
        assertEquals(246, ExpressionEvaluator.evaluate("12+234"));
        assertEquals(25000, ExpressionEvaluator.evaluate("25*1000"));
    }

    @DisplayName("WHEN an expression contains both multi-digit addition "
            + "nested parentheses, THEN the order of operations is respected.")
    @Test
    public void testMultiDigitParenthesisComputing() throws MalformedExpressionException{
        assertEquals(420, ExpressionEvaluator.evaluate("12*(34+1)"));
        assertEquals(1230, ExpressionEvaluator.evaluate("((12+9)*2)+99*12"));
    }

    @DisplayName("WHEN there should be an exception thrown from the method")
    @Test
    public void testExceptionThrowing() throws MalformedExpressionException{
        assertThrows(MalformedExpressionException.class, () -> ExpressionEvaluator.evaluate(""));
        assertEquals(1230, ExpressionEvaluator.evaluate("((12+9)*2)+99*12"));
    }

    @DisplayName("Multi-digit: mixed with precedence and nested parentheses")
    @Test
    public void testMultiDigitMixed() throws MalformedExpressionException {
        assertEquals(579, ExpressionEvaluator.evaluate("123+456"));
        assertEquals(123 * (4 + 5) + 6, ExpressionEvaluator.evaluate("123*(4+5)+6"));
        assertEquals(15, ExpressionEvaluator.evaluate("007+008")); // leading zeros ok
    }

    @DisplayName("Empty string is malformed")
    @Test
    public void testEmptyStringMalformed() {
        assertThrows(MalformedExpressionException.class,
                () -> ExpressionEvaluator.evaluate(""));
    }

    @DisplayName("Illegal character in expression is malformed")
    @Test
    public void testIllegalCharacterMalformed() {
        assertThrows(MalformedExpressionException.class,
                () -> ExpressionEvaluator.evaluate("2+3a"));
        assertThrows(MalformedExpressionException.class,
                () -> ExpressionEvaluator.evaluate("1$2"));
    }

    @DisplayName("Mismatched parentheses: extra closing")
    @Test
    public void testMismatchedParensExtraClosing() {
        assertThrows(MalformedExpressionException.class,
                () -> ExpressionEvaluator.evaluate("3+4)"));
        assertThrows(MalformedExpressionException.class,
                () -> ExpressionEvaluator.evaluate("(3+4))"));
    }

    @DisplayName("Mismatched parentheses: extra opening")
    @Test
    public void testMismatchedParensExtraOpening() {
        assertThrows(MalformedExpressionException.class,
                () -> ExpressionEvaluator.evaluate("(3+4"));
        assertThrows(MalformedExpressionException.class,
                () -> ExpressionEvaluator.evaluate("((3+4)"));
    }

    @DisplayName("Expected operand, got operator")
    @Test
    public void testExpectedOperandGotOperator() {
        // consecutive operators
        assertThrows(MalformedExpressionException.class,
                () -> ExpressionEvaluator.evaluate("3++4"));
        // operator immediately after '('
        assertThrows(MalformedExpressionException.class,
                () -> ExpressionEvaluator.evaluate("(*3)"));
        // unary plus not supported (and '+' where operand expected)
        assertThrows(MalformedExpressionException.class,
                () -> ExpressionEvaluator.evaluate("3*(+2)"));
        // expression starts with operator
        assertThrows(MalformedExpressionException.class,
                () -> ExpressionEvaluator.evaluate("+6"));
    }

    @DisplayName("Expression must not end with operator")
    @Test
    public void testEndsWithOperator() {
        assertThrows(MalformedExpressionException.class,
                () -> ExpressionEvaluator.evaluate("3+"));
        assertThrows(MalformedExpressionException.class,
                () -> ExpressionEvaluator.evaluate("5*"));
    }

    @DisplayName("Whitespace between digits inside a number is malformed")
    @Test
    public void testWhitespaceInsideNumber() {
        assertThrows(MalformedExpressionException.class,
                 () -> ExpressionEvaluator.evaluate("1 2+3"));
    }

    @DisplayName("Whitespace elsewhere is ignored")
    @Test
    public void testWhitespaceIgnored() throws MalformedExpressionException {
        assertEquals(15, ExpressionEvaluator.evaluate(" 3 +  4 * (  2 + 1 ) "));
    }

    @DisplayName("Binary subtraction: simple cases")
    @Test
    public void testSubtractionSimple() throws MalformedExpressionException {
        assertEquals(2,  ExpressionEvaluator.evaluate("5-3"));
        assertEquals(5,  ExpressionEvaluator.evaluate("5-0"));
        assertEquals(-5, ExpressionEvaluator.evaluate("0-5")); // negative result from binary '-'
    }

    @DisplayName("Binary subtraction: chaining is left-associative")
    @Test
    public void testSubtractionChainingLeftAssociative() throws MalformedExpressionException {
        assertEquals(7,  ExpressionEvaluator.evaluate("10-2-1"));       // (10-2)-1
        assertEquals(40, ExpressionEvaluator.evaluate("100-30-20-10")); // ((100-30)-20)-10
    }

    @DisplayName("Mixing + and - with equal precedence, evaluated left-to-right")
    @Test
    public void testAddSubMixLeftToRight() throws MalformedExpressionException {
        assertEquals(9,  ExpressionEvaluator.evaluate("10-2+1"));       // (10-2)+1
        assertEquals(5,  ExpressionEvaluator.evaluate("1+2-3+4-5+6"));  // ((((1+2)-3)+4)-5)+6
    }

    @DisplayName("Precedence with * vs -")
    @Test
    public void testSubtractionTimesPrecedence() throws MalformedExpressionException {
        assertEquals(17, ExpressionEvaluator.evaluate("2*10-3"));   // 20-3
        assertEquals(4,  ExpressionEvaluator.evaluate("10-2*3"));   // 10-(2*3)
        assertEquals(14, ExpressionEvaluator.evaluate("2*(10-3)")); // 2*7
        assertEquals(4,  ExpressionEvaluator.evaluate("10-(2*3)")); // 10-6
    }

    @DisplayName("Parentheses with subtraction")
    @Test
    public void testSubtractionParentheses() throws MalformedExpressionException {
        assertEquals(9,  ExpressionEvaluator.evaluate("(10-2)+1"));
        assertEquals(7,  ExpressionEvaluator.evaluate("10-(2+1)"));
        assertEquals(21, ExpressionEvaluator.evaluate("123-(45+6)*2"));
    }

    @DisplayName("Multi-digit and whitespace with subtraction (integration with 2.4)")
    @Test
    public void testSubtractionMultiDigitWhitespace() throws MalformedExpressionException {
        assertEquals(13, ExpressionEvaluator.evaluate(" 12 - 3 + 4 ")); // whitespace should be ignored
    }

    @DisplayName("Unary negation: simple literals")
    @Test
    public void testUnarySimple() throws MalformedExpressionException {
        assertEquals(-5, ExpressionEvaluator.evaluate("-5"));
        assertEquals(0,  ExpressionEvaluator.evaluate("-0"));
    }

    @DisplayName("Unary negation with parentheses")
    @Test
    public void testUnaryWithParens() throws MalformedExpressionException {
        assertEquals(-5, ExpressionEvaluator.evaluate("(-5)"));
        assertEquals(-5, ExpressionEvaluator.evaluate("-(3+2)"));
        assertEquals(-6, ExpressionEvaluator.evaluate("-(2*3)"));
        assertEquals(6,  ExpressionEvaluator.evaluate("--(2*3)"));
    }

    @DisplayName("Unary negation binds tighter than * and +")
    @Test
    public void testUnaryPrecedence() throws MalformedExpressionException {
        // (-2)*3 and 2*(-3)
        assertEquals(-6, ExpressionEvaluator.evaluate("-2*3"));
        assertEquals(-6, ExpressionEvaluator.evaluate("2*-3"));
        // plus with unary minus
        assertEquals(-2, ExpressionEvaluator.evaluate("3+-5"));
        // multiply by -1 pattern
        assertEquals(-5, ExpressionEvaluator.evaluate("5*-1"));
    }

    @DisplayName("Multiple consecutive unary minuses")
    @Test
    public void testMultipleUnary() throws MalformedExpressionException {
        assertEquals(5,   ExpressionEvaluator.evaluate("--5"));     // --5 = 5
        assertEquals(-5,  ExpressionEvaluator.evaluate("---5"));    // ---5 = -5
        assertEquals(8,   ExpressionEvaluator.evaluate("5--3"));    // 5 - (-3)
        assertEquals(2,   ExpressionEvaluator.evaluate("5---3"));   // 5 - (+3)
        assertEquals(5,  ExpressionEvaluator.evaluate("10---5"));  // 10 - (5)
        assertEquals(15,   ExpressionEvaluator.evaluate("10----5")); // 10 - (-5)
    }

    @DisplayName("Whitespace with unary negation")
    @Test
    public void testUnaryWhitespace() throws MalformedExpressionException {
        assertEquals(-12, ExpressionEvaluator.evaluate("- 12"));
        assertEquals(-5,  ExpressionEvaluator.evaluate("- ( 3 + 2 )"));
        assertEquals(-5,  ExpressionEvaluator.evaluate("5* - 1"));
    }

    @DisplayName("Unary negation: malformed inputs remain malformed")
    @Test
    public void testUnaryMalformedStillThrows() {
        assertThrows(MalformedExpressionException.class, () -> ExpressionEvaluator.evaluate("-)"));
        assertThrows(MalformedExpressionException.class, () -> ExpressionEvaluator.evaluate("3*-"));
        assertThrows(MalformedExpressionException.class, () -> ExpressionEvaluator.evaluate("-(3+)"));
    }

    @DisplayName("Implicit multiplication: ')(' between parenthesized groups")
    @Test
    public void testImplicitMult_ParenParen() throws MalformedExpressionException {
        assertEquals(12, ExpressionEvaluator.evaluate("(3)(4)"));
        assertEquals(21, ExpressionEvaluator.evaluate("(2+1)(3+4)"));
    }

    @DisplayName("Implicit multiplication: number followed by '('")
    @Test
    public void testImplicitMult_NumberBeforeParen() throws MalformedExpressionException {
        assertEquals(25, ExpressionEvaluator.evaluate("5(2+3)"));
        assertEquals(36, ExpressionEvaluator.evaluate("12(3)"));
    }

    @DisplayName("Implicit multiplication: ')' followed by number")
    @Test
    public void testImplicitMult_ParenBeforeNumber() throws MalformedExpressionException {
        assertEquals(9,  ExpressionEvaluator.evaluate("(1+2)3"));
        assertEquals(50, ExpressionEvaluator.evaluate("(2+3)10"));
    }

    @DisplayName("Implicit multiplication mixes with explicit '*' and respects precedence")
    @Test
    public void testImplicitMult_MixedWithExplicit() throws MalformedExpressionException {
        // Same as 2*(3+4)*5 from earlier tests
        assertEquals(70, ExpressionEvaluator.evaluate("2(3+4)5"));
        // Same as (2+3)*(4+5)
        assertEquals(45, ExpressionEvaluator.evaluate("(2+3)(4+5)"));
        // Addition should not bind tighter than implicit '*'
        assertEquals(14, ExpressionEvaluator.evaluate("2+3(4)")); // 2 + (3*4)
    }

    @DisplayName("Implicit multiplication with unary negation (from 2.6)")
    @Test
    public void testImplicitMult_WithUnary() throws MalformedExpressionException {
        assertEquals(-20, ExpressionEvaluator.evaluate("-(2+3)4")); // (-1)*(2+3)*4
        assertEquals(6,   ExpressionEvaluator.evaluate("(-2)(-3)"));
        assertEquals(-6,  ExpressionEvaluator.evaluate("-2(3)"));
    }

    @DisplayName("Implicit multiplication with whitespace (integration with 2.4)")
    @Test
    public void testImplicitMult_WithWhitespace() throws MalformedExpressionException {
        assertEquals(14, ExpressionEvaluator.evaluate("2 + 3 ( 4 )"));     // 2 + 3*4
        assertEquals(70, ExpressionEvaluator.evaluate(" 2 ( 3 + 4 ) 5 ")); // 2*(3+4)*5
    }

}
