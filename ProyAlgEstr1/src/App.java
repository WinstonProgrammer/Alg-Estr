import java.util.Stack;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label; 
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Esta clase denominada App hereda de la clase Application y modela una calculadora
 * con interfaz gráfica que muestra la notación postija del ejercicio ingresado y su
 * solución.
 * @author Diego Ferrada
 * @author Javier Huanca
 * @version 1.0/2023
 */
public class App extends Application {

    /**
     * El metodo start se llama cuando se inicia la aplicacion JavaFX.
     * @param primaryStage El escenario principal de la aplicacion.
     */
    @Override
    public void start(Stage primaryStage) {
        // Se crea el pane y se determinan sus cualidades.
        GridPane mainPane = new GridPane();
        mainPane.setAlignment(Pos.TOP_CENTER);
        mainPane.setPadding(new Insets(3, 3, 3, 3));
        mainPane.setHgap(4);
        mainPane.setVgap(4);
        mainPane.setStyle("-fx-background-color: lightblue;");

        // Se añaden elementos del pane.
        Label etiqueta1 = new Label("Ingrese un ejercicio aritmético:");
        mainPane.add(etiqueta1, 0, 0);
        TextField campoInfija = new TextField();
        mainPane.add(campoInfija, 0, 1);
        Button btCalcular = new Button("Calcular");
        mainPane.add(btCalcular, 0, 2);
        Label etiqueta2 = new Label("Notación Postfija:");
        mainPane.add(etiqueta2, 0, 3);
        TextField campoPostfija = new TextField();
        mainPane.add(campoPostfija, 0, 4);
        Label etiqueta3 = new Label("Solución:");
        mainPane.add(etiqueta3, 0, 5);
        TextField campoSolucion = new TextField();
        mainPane.add(campoSolucion, 0, 6);
        Button btLimpiar = new Button("Limpiar");
        mainPane.add(btLimpiar, 0, 7);
        btLimpiar.setTextFill(Color.BLUE);
        Button btSalir = new Button("Salir");
        mainPane.add(btSalir, 0, 8);
        GridPane.setHalignment(btSalir, HPos.CENTER);
        btSalir.setTextFill(Color.RED);

        /**
         * Se configura el evento de accion para el boton "Salir".
         */
        btSalir.setOnAction(new EventHandler<ActionEvent>() {
            /**
             * Maneja el evento de accion cuando se hace click en el boton "Salir".
             * @param e El evento de accion que desencadena el boton "Salir".
             */
            @Override
            public void handle(ActionEvent e){
                primaryStage.close();
            }

        });
        /**
         * Se configura el evento de accion para el boton "Limpiar".
         */
        btLimpiar.setOnAction(new EventHandler<ActionEvent>() {
            /**
             * Maneja el evento de accion cuando se hace click en el boton "Limpiar".
             * @param e El evento de accion que desencadena el boton "Limpiar".
             */
            @Override
            public void handle(ActionEvent e){
                campoPostfija.setText("");
                campoSolucion.setText("");
                campoInfija.setText("");
            }
        });

        /**
         * Configura el evento de accion para el boton "Calcular".
         */
        btCalcular.setOnAction(new EventHandler<ActionEvent>() {
            /**
             * Maneja el evento de accion cuando se hace click en el boton "Calcular".
             * @param e El evento de accion que desencadena el boton "Calcular".
             */
            @Override
            public void handle(ActionEvent e) {
                // Obtiene la expresion en notacion infija desde el campo de texto.
                String infija = campoInfija.getText();
                // Convierte la expresion infija en notacion postfija.
                String postfija = convertirAPostfija(infija);
                // Muestra la expresion postfija en el campo de texto correspondiente.
                campoPostfija.setText(postfija);
                // Calcula el resultado de la expresion postfija y lo muestra en el campo de solucion.
                campoSolucion.setText(calcularPostfija(postfija));
            }
        });
        
        Scene mainScene = new Scene(mainPane, 300, 250);
        primaryStage.setTitle("Calculadora");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }
    /**
     * Método que convierte un ejercicio aritmético de notación infija a una de
     * notación postfija.
     * @param infija Parámetro que representa el ejercicio en notación infija.
     * @return El ejercicio original en notación postfija.
     */
    public static String convertirAPostfija(String infija) {
        // Se crea el string que representa el ejercicio en notación postfija.
        StringBuilder postfija = new StringBuilder();
        // Se crea una pila que almacena los carácteres del string postfija.
        Stack<Character> pila = new Stack<>();

        int n = infija.length();
        for (int i = 0; i < n; i++) {
            char c = infija.charAt(i);
            // Si c es una letra o dígito se añade al string postfija
            if (Character.isLetterOrDigit(c)) {
                postfija.append(c);
            }
            // Si c es una parentesis se añaden caractéres a la pila
            // hasta que se halle su parentesis de cierre correspondiente
            // En ese caso se añaden los elementos de la pila al string postfija
            // hasta que la parentesis inicial quede en la posición superior
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
            // Si c es un operador se revisa si la pila no está vacía y si su precedencia
            // es mayor or igual a la precedencia de otro caracter
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
    /**
     * Método que determina la precedencia de una operación en el ejercicio.
     * @param operador Parámetro que representa el operador a evaluar.
     * @return El valor de precedencia del operador evaluado.
     */
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

    /**
     * Metodo que calcula el resultado de una expresion en notacion postfija.
     * @param postfija La expresion en notacion postfija a calcular.
     * @return El resultado de la expresion.
     */
    public static String calcularPostfija(String postfija) {
        // Creamos una pila para almacenar los operandos.
        Stack<Integer> pila = new Stack<>();

        int n = postfija.length();
        for (int i = 0; i < n; i++) {
            char c = postfija.charAt(i);
            
            // Si el caracter es un digito, lo convertimos a un entero y lo agregamos a la pila.
            if (Character.getNumericValue(c) != -1) {
                pila.push(Character.getNumericValue(c));
            }
            // Si el caracter es un operador de suma, realizamos la suma de los dos operandos superiores de la pila.
            else if (c == '+') {
                int operando2 = pila.pop();
                int operando1 = pila.pop();
                pila.push(operando1 + operando2);
            }
            // Si el caracter es un operador de resta, realizamos la resta de los operandos superiores de la pila.
            else if (c == '-') {
                int operando2 = pila.pop();
                int operando1 = pila.pop();
                pila.push(operando1 - operando2);
            }
            // Si el caracter es un operador de multiplicacion, realizamos la multiplicacion de los operandos superiores de la pila.
            else if (c == '*') {
                int operando2 = pila.pop();
                int operando1 = pila.pop();
                pila.push((operando1 * operando2));
            }
            // Si el caracter es un operador de division, realizamos la division de los operandos superiores de la pila.
            else if (c == '/') {
                int operando2 = pila.pop();
                int operando1 = pila.pop();
                pila.push(operando1 / operando2);
            }
            // Si el caracter es un operador de exponenciacion, realizamos la exponenciacion de los operandos superiores de la pila.
            else if (c == '^') {
                int operando2 = pila.pop();
                int operando1 = pila.pop();
                pila.push((int)Math.pow(operando1, operando2));
            } 
        }
        // El resultado final estara en la cima de la pila.
        return pila.pop().toString();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}