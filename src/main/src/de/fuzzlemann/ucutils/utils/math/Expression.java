package de.fuzzlemann.ucutils.utils.math;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Expression {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###,###.###", DecimalFormatSymbols.getInstance(Locale.GERMAN));
    private static final String[] TO_REPLACE = new String[]{"PI", "E", "ANS"};
    private static final String[] REPLACER = new String[]{String.valueOf(Math.PI), String.valueOf(Math.E), "0"};
    private static double lastResult;
    private double result = Double.NaN;

    static {
        DECIMAL_FORMAT.setMaximumFractionDigits(5);
    }

    private String expression;

    private int pos = -1;
    private int ch;

    public Expression(String expression) {
        this.expression = expression;
    }

    public String parse() {
        if (Double.isNaN(result)) evaluate();

        return DECIMAL_FORMAT.format(result);
    }

    public double evaluate() {
        replaceVariables();
        nextChar();

        double x = parseExpression();
        if (pos < expression.length()) {
            throw new ExpressionException("Unexpected character: " + (char) ch);
        }

        this.result = x;
        lastResult = x;
        return x;
    }

    private void replaceVariables() {
        REPLACER[2] = String.valueOf(lastResult);
        expression = StringUtils.replaceEach(expression, TO_REPLACE, REPLACER);
    }

    private double parseExpression() throws ExpressionException {
        double x = parseTerm();
        while (true) {
            if (eat('+')) x += parseTerm();
            else if (eat('-')) x -= parseTerm();
            else return x;
        }
    }

    private double parseTerm() throws ExpressionException {
        double x = parseFactor();
        while (true) {
            if (eat('*')) x *= parseFactor();
            else if (eat('/')) x /= parseFactor();
            else return x;
        }
    }

    private double parseFactor() throws ExpressionException {
        if (eat('+')) return parseFactor();
        if (eat('-')) return -parseFactor();

        double x;
        int startPos = this.pos;
        if (eat('(')) {
            x = parseExpression();
            eat(')');
        } else if ((ch >= '0' && ch <= '9') || ch == '.') {
            while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
            x = Double.parseDouble(expression.substring(startPos, this.pos));
        } else if (ch >= 'a' && ch <= 'z') {
            while (ch >= 'a' && ch <= 'z') nextChar();
            String func = expression.substring(startPos, this.pos);
            x = parseFactor();
            switch (func) {
                case "sqrt":
                    x = Math.sqrt(x);
                    break;
                case "sin":
                    x = Math.sin(Math.toRadians(x));
                    break;
                case "cos":
                    x = Math.cos(Math.toRadians(x));
                    break;
                case "tan":
                    x = Math.tan(Math.toRadians(x));
                    break;
                default:
                    throw new ExpressionException("Unknown function: " + func);
            }
        } else {
            char wrongChar;

            if (ch == -1) {
                wrongChar = expression.charAt(expression.length() - 1);
            } else {
                wrongChar = (char) ch;
            }

            throw new ExpressionException("Unexpected character: " + wrongChar);
        }

        if (eat('^')) x = Math.pow(x, parseFactor());

        return x;
    }

    private void nextChar() {
        ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
    }

    private boolean eat(int charToEat) {
        while (ch == ' ') nextChar();
        if (ch == charToEat) {
            nextChar();
            return true;
        }
        return false;
    }

    public static class ExpressionException extends RuntimeException {

        ExpressionException(String message) {
            super(message);
        }
    }
}
