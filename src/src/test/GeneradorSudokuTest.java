package sudoku;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GeneradorSudokuTest {

    private GeneradorSudoku generador;

    @BeforeEach
    void setUp() {
        generador = new GeneradorSudoku();
    }

    @Test
    void testGenerarTableroFacil() {
        generador.generarTablero(Dificultad.FACIL);
        int[][] tablero = generador.getTablero();
        int count = contarCeldasNoVacias(tablero);
        assertTrue(count >= 50 && count <= 52, "En dificultad FÁCIL deberían quedar alrededor de 51 celdas visibles");
    }

    @Test
    void testGenerarTableroMedio() {
        generador.generarTablero(Dificultad.MEDIO);
        int[][] tablero = generador.getTablero();
        int count = contarCeldasNoVacias(tablero);
        assertTrue(count >= 40 && count <= 42, "En dificultad MEDIA deberían quedar alrededor de 41 celdas visibles");
    }

    @Test
    void testGenerarTableroDificil() {
        generador.generarTablero(Dificultad.DIFICIL);
        int[][] tablero = generador.getTablero();
        int count = contarCeldasNoVacias(tablero);
        assertTrue(count >= 30 && count <= 32, "En dificultad DIFÍCIL deberían quedar alrededor de 31 celdas visibles");
    }

    @Test
    void testCeldasFijasCoincidenConTablero() {
        generador.generarTablero(Dificultad.FACIL);
        int[][] tablero = generador.getTablero();
        boolean[][] fijas = generador.getCeldasFijas();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (tablero[i][j] != 0) {
                    assertTrue(fijas[i][j], "La celda fija debe ser verdadera si hay un número en el tablero");
                } else {
                    assertFalse(fijas[i][j], "La celda fija debe ser falsa si está vacía");
                }
            }
        }
    }

    @Test
    void testGetSolucionTieneTodosLosValores() {
        generador.generarTablero(Dificultad.MEDIO);
        int[][] solucion = generador.getSolucion();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                assertTrue(solucion[i][j] >= 1 && solucion[i][j] <= 9,
                        "La solución debe tener solo valores entre 1 y 9");
            }
        }
    }

    @Test
    void testEsMovimientoValidoDevuelveFalseParaRepetidos() {
        generador.generarTablero(Dificultad.FACIL);
        int[][] tablero = generador.getTablero();

        tablero[0][0] = 5;
        tablero[0][1] = 5;

        assertFalse(generador.esMovimientoValido(0, 2, 5), "No debe permitir repetir el mismo número en la fila");
    }

    private int contarCeldasNoVacias(int[][] tablero) {
        int count = 0;
        for (int[] fila : tablero) {
            for (int val : fila) {
                if (val != 0) count++;
            }
        }
        return count;
    }
}
