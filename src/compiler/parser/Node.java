package compiler.parser;

import compiler.lexer.Punctuator;

/**
 * Created by troy on 15/11/14.
 */
public abstract class Node {

    public abstract int evaluate();

    public static class IntNode extends Node {
        private int value;

        public IntNode(int value) {
            this.value = value;
        }

        public int evaluate() {
            return value;
        }

        @Override
        public String toString() {
            return Integer.toString(value);
        }
    }

    public static class AddNode extends Node {
        private Node lhs, rhs;

        public AddNode(Node lhs, Node rhs) {
            this.lhs = lhs;
            this.rhs = rhs;
        }

        public int evaluate() {
            return lhs.evaluate() + rhs.evaluate();
        }


        @Override
        public String toString() {
            return "(" + lhs.toString() + " + " + rhs.toString() + ")";
        }
    }

    public static class MinusNode extends Node {
        private Node lhs, rhs;

        public MinusNode(Node lhs, Node rhs) {
            this.lhs = lhs;
            this.rhs = rhs;
        }

        public int evaluate() {
            return lhs.evaluate() - rhs.evaluate();
        }


        @Override
        public String toString() {
            return "(" + lhs.toString() + " - " + rhs.toString() + ")";
        }
    }

    public static class MultNode extends Node {
        private Node lhs, rhs;

        public MultNode(Node lhs, Node rhs) {
            this.lhs = lhs;
            this.rhs = rhs;
        }

        public int evaluate() {
            return lhs.evaluate() * rhs.evaluate();
        }

        @Override
        public String toString() {
            return "(" + lhs.toString() + " * " + rhs.toString() + ")";
        }
    }

    public static class DivideNode extends Node {
        private Node lhs, rhs;

        public DivideNode(Node lhs, Node rhs) {
            this.lhs = lhs;
            this.rhs = rhs;
        }

        public int evaluate() {
            return lhs.evaluate() / rhs.evaluate();
        }

        @Override
        public String toString() {
            return "(" + lhs.toString() + " / " + rhs.toString() + ")";
        }
    }

    public static class OperatorNode extends Node {
        private Node lhs, rhs;
        private Punctuator punc;

        public OperatorNode(Node lhs, Node rhs, Punctuator punc) {
            this.lhs = lhs;
            this.rhs = rhs;
            this.punc = punc;
        }

        public int evaluate() {
            int lhsR = lhs.evaluate();
            int rhsR = rhs.evaluate();

            switch (punc) {
                case Plus: return lhsR + rhsR;
                case Minus: return lhsR - rhsR;
                case Asterisk: return lhsR * rhsR;
                case Slash: return lhsR / rhsR;
                default: throw new RuntimeException("shouldn't reach this");
            }
        }

    }
}


