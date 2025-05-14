public interface IGeneradorSudoku {

    // Método para generar el tablero con una dificultad específica
    void generarTablero(Dificultad dificultad);

    // Método para obtener el tablero
    int[][] getTablero();

    // Método para obtener las celdas fijas
    boolean[][] getCeldasFijas();

    // Método para verificar si un movimiento es válido
    boolean esMovimientoValido(int fila, int columna, int valor);
}
