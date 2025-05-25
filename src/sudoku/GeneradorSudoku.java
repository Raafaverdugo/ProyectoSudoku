package sudoku;

import java.util.Random;

public class GeneradorSudoku implements IGeneradorSudoku {

    private int[][] tablero;
    private int[][] solucion;
    private boolean[][] celdasFijas;

    public GeneradorSudoku() {
        this.tablero = new int[9][9];
        this.solucion = new int[9][9];
        this.celdasFijas = new boolean[9][9];
    }

    public void generarTablero(Dificultad dificultad) {
        // Llenar completamente el tablero con una solución válida
        llenarTablero();

        // Copiar solución antes de borrar celdas
        for (int i = 0; i < 9; i++) {
            System.arraycopy(tablero[i], 0, solucion[i], 0, 9);
        }

        // Eliminar celdas según dificultad
        eliminarCeldas(dificultad);
    }

    private boolean llenarTablero() {
        return llenarTableroRecursivo(0, 0);
    }

    private boolean llenarTableroRecursivo(int fila, int columna) {
        if (fila == 9) return true;
        if (columna == 9) return llenarTableroRecursivo(fila + 1, 0);

        if (tablero[fila][columna] != 0)
            return llenarTableroRecursivo(fila, columna + 1);

        int[] numeros = generarNumerosAleatorios();
        for (int num : numeros) {
            if (esMovimientoValido(fila, columna, num)) {
                tablero[fila][columna] = num;
                if (llenarTableroRecursivo(fila, columna + 1)) return true;
                tablero[fila][columna] = 0;
            }
        }

        return false;
    }

    private int[] generarNumerosAleatorios() {
        int[] nums = new int[9];
        for (int i = 0; i < 9; i++) nums[i] = i + 1;

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
            case Dificultad.FACIL: celdasVisibles = 51; break;
            case Dificultad.MEDIO: celdasVisibles = 41; break;
            case Dificultad.DIFICIL: celdasVisibles = 31; break;
            default: celdasVisibles = 41;
        }

        int celdasAEliminar = 81 - celdasVisibles;
        Random rand = new Random();

        // Inicializar celdasFijas en false
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                celdasFijas[i][j] = true;
            }
        }

        while (celdasAEliminar > 0) {
            int fila = rand.nextInt(9);
            int columna = rand.nextInt(9);

            if (tablero[fila][columna] != 0) {
                tablero[fila][columna] = 0;
                celdasFijas[fila][columna] = false;
                celdasAEliminar--;
            }
        }
    }

    public boolean esMovimientoValido(int fila, int columna, int valor) {
        for (int i = 0; i < 9; i++) {
            if (tablero[fila][i] == valor || tablero[i][columna] == valor) {
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

    public int[][] getTablero() {
        return tablero;
    }

    public boolean[][] getCeldasFijas() {
        return celdasFijas;
    }

    public int[][] getSolucion() {
        return solucion;
    }
}
