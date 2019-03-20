package arithmetic;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Stack;

public class Postfix {

    private String input;

    private String result = "";

    private Tree tree = null;

    public Postfix(String input) throws Exception {
        this.input = input;
        try {
            parse();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public String view()
    {
        return this.result;
    }

    public BigDecimal calculate() {
        Tree node = tree();
        return calculate(node);
    }

    public BigDecimal calculate(Tree node) {

        BigDecimal left, right, result = BigDecimal.valueOf(0);

        if (isNumber(node.getValue())) {
            result = new BigDecimal(node.getValue());
        } else {
            left = calculate(node.getLeft());
            right = calculate(node.getRight());

            if (node.getValue().equals("+")) {
                result = left.add(right);
            }

            if (node.getValue().equals("-")) {
                result = left.subtract(right);
            }

            if (node.getValue().equals("*")) {
                result = left.multiply(right);
            }

            if (node.getValue().equals("/")) {
                result = left.divide(right);
            }
        }

        return result;
    }

    public Tree tree() {

        if(null != tree) {
            return tree;
        }

        Tree current = new Tree();
        Character token;

        String subject = result.replace(',', '.');
        for (int i = subject.length() - 1; i >= 0; i--) {
            token = subject.charAt(i);
            if (token.equals(' ') || token.equals('\0')) {
                continue;
            }

            if (isOperator(token)) {
                current.setValue(token.toString());
                Tree node = new Tree();
                current.insert(node);
                current = node;
                continue;
            }

            String number = subject.substring(i, i+1);
            while (i > 0 && ' ' != subject.charAt(--i)) {
                number = subject.charAt(i) + number;
            }

            current.setValue(number);
            do {
                current = current.getParent();
            } while (current.hasParent() && current.completed());

            Tree node = new Tree();
            current.insert(node);
            current = node;
        }

        tree = current.getRoot();
        return tree;
    }

    private void parse() throws Exception {
        Stack<Character> opStack = new Stack<>();
        HashMap<Character, Integer> op = new HashMap<>();
        op.put('*', 3);
        op.put('/', 3);
        op.put('+', 2);
        op.put('-', 2);
        op.put('(', 1);
        op.put('=', 0);

        Character buff;
        for (int i = 0; i < this.input.length(); i++) {
            buff = this.input.charAt(i);

            if (' ' == buff) {
                continue;
            }

            if (op.containsKey(buff)) {

                if ((!opStack.empty()) && (op.get(buff) <= op.get(opStack.peek())) && (buff != '(')) {
                    while ((!opStack.empty()) && (op.get(buff) <= op.get(opStack.peek()))) {
                        result += opStack.peek().toString() + ' ';
                        opStack.pop();
                    }
                }

                opStack.push(buff);
                continue;
            }

            if (isDigit(buff)) {
                while (true) {
                    result += buff;
                    if (i + 1 >= input.length() || !isDigit(input.charAt(i + 1))) {
                        break;
                    }
                    buff = input.charAt(++i);
                }
                result += ' ';
                continue;
            }

            if (buff == ')') {

                while ((!opStack.empty()) && (opStack.peek() != '(')) {
                    result += opStack.peek() + ' ';
                    opStack.pop();
                }

                if (opStack.empty()) {
                    throw new Exception("не правильно расставлены скобки");
                }

                opStack.pop();
                continue;
            }
            throw new Exception("недопустимый символ");
        }

        while (!opStack.empty()){
            if (opStack.peek() == '(')
                throw new Exception("не правильно расставлены скобки");
            result += opStack.peek().toString() + ' ';
            opStack.pop();
        }

        if (result.isEmpty())
            throw new Exception("нет данных");
    }

    private boolean isNumber(String str) {

        final String Digits = "(\\p{Digit}+)";
        final String HexDigits = "(\\p{XDigit}+)";

        final String Exp = "[eE][+-]?"+Digits;
        final String fpRegex = (
            "[\\x00-\\x20]*" +
            "[+-]?(" +
            "NaN|" +
            "Infinity|" +

            "((("+Digits+"(\\.)?("+Digits+"?)("+Exp+")?)|"+

            "(\\.("+Digits+")("+Exp+")?)|"+

            "((" +
            "(0[xX]" + HexDigits + "(\\.)?)|" +

            "(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" +

            ")[pP][+-]?" + Digits + "))" +
            "[fFdD]?))" +
            "[\\x00-\\x20]*"
        );

        return str.matches(fpRegex);
    }

    private boolean isDigit(Character chr)
    {
        return Character.isDigit(chr) || chr.equals(',') || chr.equals('.');
    }

    private boolean isOperator(Character chr)
    {
        return  chr.equals('+') || chr.equals('-') || chr.equals('*') || chr.equals('/');
    }

}
