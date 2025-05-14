public class Sudoku implements ISudoku {
    private int[][] tablero;            // Matriz del tablero de Sudoku
    private boolean[][] celdasFijas;    // Celdas que no se pueden modificar (fijas)
    private GeneradorSudoku generador;  // Generador de Sudoku para crear tableros iniciales

    // Constructor
    public Sudoku() {
        this.tablero = new int[9][9];
        this.celdasFijas = new boolean[9][9];
        this.generador = new GeneradorSudoku();
    }

    // Genera un tablero de Sudoku de acuerdo con la dificultad
    public void generarTablero(Dificultad dificultad) {
        generador.generarTablero(dificultad);
        this.tablero = generador.getTablero();  // Obtener el tablero generado
        this.celdasFijas = generador.getCeldasFijas(); // Obtener las celdas fijas
    }

    // Valida si el movimiento del usuario es válido en el tablero
    public boolean esMovimientoValido(int fila, int columna, int valor) {
        // Verificar fila
        for (int i = 0; i < 9; i++) {
            if (tablero[fila][i] == valor) {
                return false;
            }
        }

        // Verificar columna
        for (int i = 0; i < 9; i++) {
            if (tablero[i][columna] == valor) {
                return false;
            }
        }

        // Verificar bloque 3x3
        int subFila = (fila / 3) * 3;
        int subColumna = (columna / 3) * 3;
        for (int i = subFila; i < subFila + 3; i++) {
            for (int j = subColumna; j < subColumna + 3; j++) {
                if (tablero[i][j] == valor) {
                    return false;
                }
            }
        }

        return true;
    }

    // Coloca un número en el tablero si el movimiento es válido
    public boolean colocarNumero(int fila, int columna, int valor) {
        if (celdasFijas[fila][columna]) {
            System.out.println("No puedes modificar una celda fija.");
            return false;
        }

        if (esMovimientoValido(fila, columna, valor)) {
            tablero[fila][columna] = valor;
            return true;
        } else {
            System.out.println("Movimiento no válido.");
            return false;
        }
    }

    // Verifica si el tablero está resuelto correctamente
    public boolean estaResuelto() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (tablero[i][j] == 0) {
                    return false; // Si alguna celda está vacía, no está resuelto
                }
                if (!esMovimientoValido(i, j, tablero[i][j])) {
                    return false; // Si alguna celda es inválida, el tablero no está resuelto
                }
            }
        }
        return true;
    }

    // Muestra el tablero en consola
    public void mostrarTablero() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(tablero[i][j] + " ");
            }
            System.out.println();
        }
    }
}
