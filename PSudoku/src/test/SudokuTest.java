package test;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sudoku.Dificultad;
import sudoku.Sudoku;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class SudokuTest {

    private Sudoku sudoku;

    @BeforeEach
    void setUp() {
        sudoku = new Sudoku();
        sudoku.generarTablero(Dificultad.FACIL);
    }

    @Test
    void testGenerarTableroEstablecePistas() {
        sudoku.generarTablero(Dificultad.DIFICIL);
        assertEquals(1, sudoku.getPistasDisponibles());
    }

    @Test
    void testEsMovimientoValido() {
        int[][] tablero = new int[9][9];
        tablero[0][0] = 5;
        sudoku.setTablero(tablero);

        assertFalse(sudoku.esMovimientoValido(0, 1, 5), "No debería permitir duplicados en la misma fila");
        assertFalse(sudoku.esMovimientoValido(1, 0, 5), "No debería permitir duplicados en la misma columna");
        assertFalse(sudoku.esMovimientoValido(1, 1, 5), "No debería permitir duplicados en la misma subcuadrícula");
        assertTrue(sudoku.esMovimientoValido(4, 4, 3), "Debería permitir colocar el número si es válido");
    }

    @Test
    void testColocarNumeroEnCeldaFijaFalla() {
        boolean[][] fijas = sudoku.getCeldasFijas();
        int[][] tablero = sudoku.getTablero();
        outer:
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (fijas[i][j]) {
                    boolean result = sudoku.colocarNumero(i, j, 9);
                    assertFalse(result, "No se debe poder modificar una celda fija");
                    break outer;
                }
            }
        }
    }

    @Test
    void testColocarNumeroValidoEnCeldaLibre() {
        boolean[][] fijas = sudoku.getCeldasFijas();
        int[][] tablero = sudoku.getTablero();

        outer:
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!fijas[i][j]) {
                    for (int num = 1; num <= 9; num++) {
                        if (sudoku.esMovimientoValido(i, j, num)) {
                            assertTrue(sudoku.colocarNumero(i, j, num),
                                    "Debería permitir colocar un número válido en celda libre");
                            break outer;
                        }
                    }
                }
            }
        }
    }


    @Test
    void testDarPistaReduceDisponibles() {
        int antes = sudoku.getPistasDisponibles();
        int[] pista = sudoku.darPista();
        int despues = sudoku.getPistasDisponibles();

        if (pista != null) {
            assertEquals(antes - 1, despues, "Dar pista debería reducir el contador");
            assertEquals(3, pista.length, "La pista debe ser un arreglo de 3 elementos");
        } else {
            assertTrue(antes <= 0, "Si no hay pista, debe ser porque no quedan disponibles o el tablero es inválido");
        }
    }

    @Test
    void testResolverResuelveTablero() {
        sudoku.resolver();
        assertTrue(sudoku.estaResuelto(), "Después de resolver, el tablero debe estar completo y válido");
    }

    @Test
    void testGetSolucionDevuelveTableroResueltoSinModificarOriginal() {
        int[][] antes = copiarTablero(sudoku.getTablero());
        int[][] solucion = sudoku.getSolucion();

        assertTrue(tableroResuelto(solucion), "getSolucion debe devolver un tablero resuelto");
        assertArrayEquals(antes, sudoku.getTablero(), "getSolucion no debe modificar el tablero original");
    }

    private boolean tableroResuelto(int[][] tablero) {
        for (int[] fila : tablero) {
            for (int val : fila) {
                if (val < 1 || val > 9) return false;
            }
        }
        return true;
    }

    private int[][] copiarTablero(int[][] original) {
        int[][] copia = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(original[i], 0, copia[i], 0, 9);
        }
        return copia;
    }
}
