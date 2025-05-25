package sudoku;

public class Sudoku implements ISudoku {
    private int[][] tablero;
    private boolean[][] celdasFijas;
    private GeneradorSudoku generador;

    private int pistasDisponibles;
    private Dificultad dificultadActual;

    public Sudoku() {
        this.tablero = new int[9][9];
        this.celdasFijas = new boolean[9][9];
        this.generador = new GeneradorSudoku();
    }

    public void generarTablero(Dificultad dificultad) {
        this.dificultadActual = dificultad;

        switch (dificultad) {
            case Dificultad.FACIL: pistasDisponibles = 3; break;
            case Dificultad.MEDIO: pistasDisponibles = 2; break;
            case Dificultad.DIFICIL: pistasDisponibles = 1; break;
            default: pistasDisponibles = 0; break;
        }

        generador.generarTablero(dificultad);
        this.tablero = generador.getTablero();
        this.celdasFijas = generador.getCeldasFijas();
    }

    // Método modificado para validar ignorando la celda actual
    public boolean esMovimientoValido(int fila, int columna, int valor) {
        if (valor == 0) return true;

        for (int i = 0; i < 9; i++) {
            if (i != columna && tablero[fila][i] == valor) {
                return false;
            }
        }

        for (int i = 0; i < 9; i++) {
            if (i != fila && tablero[i][columna] == valor) {
                return false;
            }
        }

        int subFila = (fila / 3) * 3;
        int subColumna = (columna / 3) * 3;
        for (int i = subFila; i < subFila + 3; i++) {
            for (int j = subColumna; j < subColumna + 3; j++) {
                if ((i != fila || j != columna) && tablero[i][j] == valor) {
                    return false;
                }
            }
        }

        return true;
    }

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

    public int[] darPista() {
        if (pistasDisponibles <= 0) {
            System.out.println("No te quedan pistas disponibles.");
            return null;
        }

        int[][] copia = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(tablero[i], 0, copia[i], 0, 9);
        }

        if (!resolverSudoku(copia)) {
            System.out.println("No se puede resolver el tablero.");
            return null;
        }

        for (int fila = 0; fila < 9; fila++) {
            for (int columna = 0; columna < 9; columna++) {
                if (tablero[fila][columna] == 0) {
                    int valorCorrecto = copia[fila][columna];
                    pistasDisponibles--;
                    return new int[]{fila, columna, valorCorrecto};
                }
            }
        }

        return null;
    }

    // Método para resolver el tablero actual (modifica this.tablero)
    public void resolver() {
        resolverSudoku(tablero);
    }

    // Devuelve una matriz con la solución del tablero actual sin modificar this.tablero
    public int[][] getSolucion() {
        int[][] copia = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(tablero[i], 0, copia[i], 0, 9);
        }
        resolverSudoku(copia);
        return copia;
    }

    // Permite actualizar el tablero (útil para la interfaz)
    public void setTablero(int[][] nuevoTablero) {
        for (int i = 0; i < 9; i++) {
            System.arraycopy(nuevoTablero[i], 0, this.tablero[i], 0, 9);
        }
    }

    private boolean resolverSudoku(int[][] tablero) {
        for (int fila = 0; fila < 9; fila++) {
            for (int col = 0; col < 9; col++) {
                if (tablero[fila][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (esMovimientoValidoResuelto(tablero, fila, col, num)) {
                            tablero[fila][col] = num;
                            if (resolverSudoku(tablero)) {
                                return true;
                            }
                            tablero[fila][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private boolean esMovimientoValidoResuelto(int[][] tablero, int fila, int col, int valor) {
        for (int i = 0; i < 9; i++) {
            if (tablero[fila][i] == valor || tablero[i][col] == valor) {
                return false;
            }
        }

        int subFila = (fila / 3) * 3;
        int subCol = (col / 3) * 3;
        for (int i = subFila; i < subFila + 3; i++) {
            for (int j = subCol; j < subCol + 3; j++) {
                if (tablero[i][j] == valor) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean estaResuelto() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (tablero[i][j] == 0 || !esMovimientoValido(i, j, tablero[i][j])) {
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

    public int getPistasDisponibles() {
        return pistasDisponibles;
    }
}
