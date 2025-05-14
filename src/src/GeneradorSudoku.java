import java.util.Random;

public class GeneradorSudoku implements IGeneradorSudoku {

    private int[][] tablero;
    private boolean[][] celdasFijas;

    public GeneradorSudoku() {
        this.tablero = new int[9][9];
        this.celdasFijas = new boolean[9][9];
    }

    public void generarTablero(Dificultad dificultad) {
        // Llenar el tablero con un patrón válido
        llenarTablero();

        // Eliminar celdas dependiendo de la dificultad
        eliminarCeldas(dificultad);
    }

    private boolean llenarTablero() {
        return llenarTableroRecursivo(0, 0);
    }

    private boolean llenarTableroRecursivo(int fila, int columna) {
        if (fila == 9) {
            return true;
        }

        if (columna == 9) {
            return llenarTableroRecursivo(fila + 1, 0);
        }

        if (tablero[fila][columna] != 0) {
            return llenarTableroRecursivo(fila, columna + 1);
        }

        Random rand = new Random();
        int[] numeros = generarNumerosAleatorios();

        for (int num : numeros) {
            if (esMovimientoValido(fila, columna, num)) {
                tablero[fila][columna] = num;
                if (llenarTableroRecursivo(fila, columna + 1)) {
                    return true;
                }
                tablero[fila][columna] = 0;
            }
        }

        return false;
    }

    private int[] generarNumerosAleatorios() {
        int[] nums = new int[9];
        for (int i = 0; i < 9; i++) {
            nums[i] = i + 1;
        }

        Random rand = new Random();
        for (int i = 0; i < nums.length; i++) {
            int j = rand.nextInt(9);
            int temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;
        }
        return nums;
    }

    private void eliminarCeldas(Dificultad dificultad) {
        int celdasVisibles;
        switch (dificultad) {
            case FACIL: celdasVisibles = 51; break; // 81 - 30 celdas vacías
            case MEDIO: celdasVisibles = 41; break; // 81 - 40 celdas vacías
            case DIFICIL: celdasVisibles = 31; break; // 81 - 50 celdas vacías
            default: celdasVisibles = 41;
        }

        Random rand = new Random();
        int celdasAEliminar = 81 - celdasVisibles;

        while (celdasAEliminar > 0) {
            int fila = rand.nextInt(9);
            int columna = rand.nextInt(9);

            if (tablero[fila][columna] != 0) {
                celdasFijas[fila][columna] = true; // Marcar la celda como fija
                tablero[fila][columna] = 0;
                celdasAEliminar--;
            }
        }
    }

    // Método para obtener el tablero
    public int[][] getTablero() {
        return tablero;
    }

    // Método para obtener las celdas fijas
    public boolean[][] getCeldasFijas() {
        return celdasFijas;
    }

    // Método para verificar si el movimiento es válido
    public boolean esMovimientoValido(int fila, int columna, int valor) {
        for (int i = 0; i < 9; i++) {
            if (tablero[fila][i] == valor) {
                return false;
            }
        }

        for (int i = 0; i < 9; i++) {
            if (tablero[i][columna] == valor) {
                return false;
            }
        }

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
}
