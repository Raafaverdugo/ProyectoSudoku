package sudoku;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SudokuAppWindow extends JFrame {
    private Sudoku sudoku;               // Modelo del sudoku.Sudoku
    private JTextField[][] celdas;       // Celdas del tablero
    private JLabel mensajeLabel;         // Mostrar mensajes

    public SudokuAppWindow() {
        sudoku = new Sudoku();
        init();
    }

    // Inicializa la interfaz gráfica
    private void init() {
        setTitle("Sudoku");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelTablero = new JPanel(new GridLayout(9, 9));
        celdas = new JTextField[9][9];

        // Crear celdas del tablero con estilo y eventos de teclado
        for (int fila = 0; fila < 9; fila++) {
            for (int col = 0; col < 9; col++) {
                JTextField tf = new JTextField();
                tf.setHorizontalAlignment(JTextField.CENTER);
                tf.setFont(new Font("SansSerif", Font.BOLD, 20));
                tf.setBackground(Color.WHITE);

                // Bordes más gruesos para separar bloques 3x3
                int top = (fila % 3 == 0) ? 3 : 1;
                int left = (col % 3 == 0) ? 3 : 1;
                int bottom = (fila == 8) ? 3 : 1;
                int right = (col == 8) ? 3 : 1;
                tf.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));

                final int f = fila;
                final int c = col;

                // Validar entrada y actualizar sudoku al escribir en celda
                tf.addKeyListener(new KeyAdapter() {
                    public void keyReleased(KeyEvent e) {
                        String text = tf.getText();

                        if (text.length() > 1) {
                            tf.setText(text.substring(0, 1));
                            return;
                        }
                        if (text.isEmpty()) {
                            sudoku.colocarNumero(f, c, 0);
                            mensajeLabel.setText("");
                            return;
                        }
                        char ch = text.charAt(0);
                        if (ch < '1' || ch > '9') {
                            tf.setText("");
                            return;
                        }
                        int valor = ch - '0';
                        if (!sudoku.colocarNumero(f, c, valor)) {
                            mensajeLabel.setText("Movimiento no válido en fila " + (f + 1) + ", columna " + (c + 1));
                            tf.setText("");
                        } else {
                            mensajeLabel.setText("");
                        }
                    }
                });

                celdas[fila][col] = tf;
                panelTablero.add(tf);
            }
        }

        // Panel con botones y etiqueta de mensajes
        JPanel panelInferior = new JPanel();
        JButton btnGenerar = new JButton("Generar");
        JButton btnPista = new JButton("Pista");
        JButton btnVerificar = new JButton("Verificar");
        JButton btnResolver = new JButton("Resolver");
        mensajeLabel = new JLabel(" ");

        // Generar nuevo tablero con dificultad seleccionada
        btnGenerar.addActionListener(e -> {
            String[] opciones = {"FACIL", "MEDIO", "DIFICIL"};
            String seleccion = (String) JOptionPane.showInputDialog(this, "Selecciona dificultad:",
                    "Dificultad", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[1]);

            if (seleccion != null) {
                Dificultad dificultad = Dificultad.valueOf(seleccion);
                sudoku.generarTablero(dificultad);
                actualizarTablero();

                mensajeLabel.setText("Tablero generado (" + seleccion + "). Pistas disponibles: " + sudoku.getPistasDisponibles());
            }
        });

        // Solicitar pista para la siguiente celda vacía válida
        btnPista.addActionListener(e -> {
            if (sudoku.getPistasDisponibles() <= 0) {
                mensajeLabel.setText("No tienes más pistas disponibles.");
                return;
            }

            int[] pista = sudoku.darPista();
            if (pista == null) {
                mensajeLabel.setText("No hay pistas posibles o el tablero está completo.");
                return;
            }

            int fila = pista[0];
            int col = pista[1];
            int valor = pista[2];

            if (!sudoku.colocarNumero(fila, col, valor)) {
                mensajeLabel.setText("La pista coincide con una celda fija.");
                return;
            }

            JTextField tf = celdas[fila][col];
            tf.setText(String.valueOf(valor));
            tf.setForeground(Color.BLUE);
            tf.setBackground(new Color(200, 255, 200));
            tf.setFont(tf.getFont().deriveFont(Font.BOLD));

            mensajeLabel.setText("Pista: fila " + (fila + 1) + ", columna " + (col + 1) +
                    " = " + valor + " | Pistas restantes: " + sudoku.getPistasDisponibles());
        });

        // Verifica si el Sudoku está correctamente resuelto
        btnVerificar.addActionListener(e -> {
            if (sudoku.estaResuelto()) {
                mensajeLabel.setText("¡Felicidades! Sudoku resuelto correctamente.");
            } else {
                mensajeLabel.setText("El Sudoku no está resuelto correctamente.");
            }
        });

        // Resuelve automáticamente el Sudoku y actualiza la interfaz
        btnResolver.addActionListener(e -> {
            int[][] solucion = sudoku.getSolucion();
            sudoku.setTablero(solucion);
            for (int fila = 0; fila < 9; fila++) {
                for (int col = 0; col < 9; col++) {
                    celdas[fila][col].setText(String.valueOf(solucion[fila][col]));
                    celdas[fila][col].setBackground(new Color(230, 255, 250));
                    celdas[fila][col].setEditable(false);
                    celdas[fila][col].setFont(celdas[fila][col].getFont().deriveFont(Font.PLAIN));
                }
            }
            mensajeLabel.setText("Tablero resuelto automáticamente.");
        });

        // Añadimos los botones y etiqueta al panel inferior
        panelInferior.add(btnGenerar);
        panelInferior.add(btnPista);
        panelInferior.add(btnVerificar);
        panelInferior.add(btnResolver);
        panelInferior.add(mensajeLabel);

        // Añadimos los paneles a la ventana principal
        add(panelTablero, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        setSize(620, 720);
        setLocationRelativeTo(null);  // Centrar ventana en pantalla
        setVisible(true);
    }

    // Actualiza las celdas con el estado actual del tablero y fija estilos según sea celda fija o editable
    private void actualizarTablero() {
        int[][] tablero = sudoku.getTablero();
        boolean[][] celdasFijas = sudoku.getCeldasFijas();

        for (int fila = 0; fila < 9; fila++) {
            for (int col = 0; col < 9; col++) {
                JTextField tf = celdas[fila][col];

                if (tablero[fila][col] != 0) {
                    tf.setText(String.valueOf(tablero[fila][col]));
                    tf.setEditable(!celdasFijas[fila][col]);
                    tf.setForeground(Color.BLACK);

                    if (celdasFijas[fila][col]) {
                        tf.setBackground(new Color(200, 200, 200));
                        tf.setFont(tf.getFont().deriveFont(Font.BOLD));
                    } else {
                        tf.setBackground(Color.WHITE);
                        tf.setFont(tf.getFont().deriveFont(Font.PLAIN));
                    }
                } else {
                    tf.setText("");
                    tf.setEditable(true);
                    tf.setForeground(Color.BLACK);
                    tf.setBackground(Color.WHITE);
                    tf.setFont(tf.getFont().deriveFont(Font.PLAIN));
                }
            }
        }
    }
}
