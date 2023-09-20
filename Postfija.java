package paquetin;

import java.util.Stack;

public class Postfija {
    public static String convertirAPostfija(String infija) {
        StringBuilder postfija = new StringBuilder();
        Stack<Character> pila = new Stack<>();

        int n = infija.length();
        for (int i = 0; i < n; i++) {
            char c = infija.charAt(i);

            if (Character.isLetterOrDigit(c)) {
                postfija.append(c);
            }
            else if (c == '(') {
                pila.push(c);
            }
            else if (c == ')') {
                while (!pila.isEmpty() && pila.peek() != '(') {
                    postfija.append(pila.pop());
                }
                if (!pila.isEmpty() && pila.peek() == '(') {
                    pila.pop();
                }
            }
            else if (c == '^') {
                while (!pila.isEmpty() && precedencia(pila.peek()) > precedencia(c)) {
                    postfija.append(pila.pop());
                }
                pila.push(c);
            }
            else {
                while (!pila.isEmpty() && precedencia(pila.peek()) >= precedencia(c)) {
                    postfija.append(pila.pop());
                }
                pila.push(c);
            }
        }
        while (!pila.isEmpty()) {
            postfija.append(pila.pop());
        }
        return postfija.toString();
    }

    public static int precedencia(char operador) {
        switch(operador) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '^':
                return 3;
            default:
                return -1;
        }
    }
}