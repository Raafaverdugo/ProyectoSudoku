package sudoku;

public interface ISudoku {

    // Método para generar el tablero con una dificultad específica
    void generarTablero(Dificultad dificultad);

    // Método para validar si un movimiento es válido
    boolean esMovimientoValido(int fila, int columna, int valor);

    // Método para colocar un número en el tablero si el movimiento es válido
    boolean colocarNumero(int fila, int columna, int valor);

    // Método para verificar si el tablero está resuelto correctamente
    boolean estaResuelto();

    //Método para dar una pista
    int[] darPista();

    //Getters
    int[][] getTablero();
    boolean[][] getCeldasFijas();
}

