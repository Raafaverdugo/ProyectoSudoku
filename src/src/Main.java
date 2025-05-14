import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Sudoku sudoku = new Sudoku();

        // Generar tablero
        sudoku.generarTablero(Dificultad.MEDIO);

        System.out.println("Tablero generado:");
        sudoku.mostrarTablero();

        // Pedir datos al usuario
        System.out.print("\nIntroduce la fila (0-8): ");
        int fila = scanner.nextInt();

        System.out.print("Introduce la columna (0-8): ");
        int columna = scanner.nextInt();

        System.out.print("Introduce el valor (1-9): ");
        int valor = scanner.nextInt();

        // Intentar colocar el número
        System.out.println("\nIntentando colocar " + valor + " en [" + fila + "," + columna + "]...");
        if (sudoku.colocarNumero(fila, columna, valor)) {
            System.out.println("Movimiento válido.");
        } else {
            System.out.println("Movimiento inválido.");
        }

        // Mostrar el tablero actualizado
        System.out.println("\nTablero después del intento:");
        sudoku.mostrarTablero();
    }
}
