package Controller;

import Model.Juego;
import Model.Puzzle;
import util.CargadorNivel;
import util.Sonido;
import View.DialogoPuzzle;
import View.PanelJuego;
import View.PanelMenu;
import View.VentanaPrincipal;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.SwingUtilities;

public class JuegoController {

    private VentanaPrincipal ventana;
    private PanelJuego panelJuego;
    private TecladoListener teclado;

    private volatile Juego juego;
    private volatile String estado;
    private volatile boolean puzzleAbierto;
    private volatile boolean puzzlePendienteAbrir;
    private volatile boolean transicionando;
    private volatile boolean modoUnJugador;
    private volatile int segundos;

    private volatile boolean corriendo;
    private volatile boolean cronometroActivo;
    private volatile boolean monstruoActivo;

    public JuegoController(VentanaPrincipal ventana) {
        this.ventana = ventana;
        this.panelJuego = ventana.getPanelJuego();
        this.teclado = new TecladoListener(this);
        this.estado = "MENU";
        this.puzzleAbierto = false;
        this.puzzlePendienteAbrir = false;
        this.transicionando = false;
        this.modoUnJugador = true;
        this.segundos = 0;
        this.corriendo = true;
        this.cronometroActivo = true;
        this.monstruoActivo = false;

        panelJuego.addKeyListener(teclado);

        configurarBotonesMenu();
        iniciarHilos();
        configurarCierreVentana();
    }

    private void configurarBotonesMenu() {
        final PanelMenu menu = ventana.getPanelMenu();

        menu.getBotonIniciar().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                menu.mostrarModoJuego();
            }
        });

        menu.getBotonInstrucciones().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                menu.mostrarInstrucciones();
            }
        });

        menu.getBotonUnJugador().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iniciarJuego(true);
            }
        });

        menu.getBotonDosJugadores().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iniciarJuego(false);
            }
        });

        menu.getBotonVolver().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                menu.volverAtras();
            }
        });

        menu.getBotonInicio().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                menu.mostrarBienvenida();
            }
        });

        menu.getBotonSalir().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Sonido.detenerMusica();
                System.exit(0);
            }
        });
    }

    private void configurarCierreVentana() {
        ventana.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                corriendo = false;
                cronometroActivo = false;
                monstruoActivo = false;
                Sonido.detenerMusica();
            }
        });
    }

    private void iniciarHilos() {
        // hilo del juego: repinta el panel cada 16 milisegundos
        Thread hiloJuego = new Thread(new Runnable() {
            public void run() {
                while (corriendo) {
                    try {
                        if (estado.equals("JUGANDO")) {
                            panelJuego.repaint();
                        }
                        Thread.sleep(16);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        hiloJuego.start();

        // hilo del cronómetro: cuenta los segundos de la partida
        Thread hiloCronometro = new Thread(new Runnable() {
            public void run() {
                while (cronometroActivo) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (estado.equals("JUGANDO")) {
                        segundos = segundos + 1;
                        final int tiempoActual = segundos;
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                panelJuego.actualizarTiempo(tiempoActual);
                            }
                        });
                    }
                }
            }
        });
        hiloCronometro.start();

        // hilo del monstruo: solo se mueve en modo 1 jugador
        Thread hiloMonstruo = new Thread(new Runnable() {
            public void run() {
                while (corriendo) {
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (estado.equals("JUGANDO") && modoUnJugador && monstruoActivo
                            && !puzzleAbierto && !transicionando) {
                        final Juego juegoActual = juego;
                        if (juegoActual != null) {
                            juegoActual.moverMonstruoAuto();
                            if (juegoActual.isHumanoAtrapado()) {
                                SwingUtilities.invokeLater(new Runnable() {
                                    public void run() {
                                        revisarEstado();
                                    }
                                });
                            }
                        }
                    }
                }
            }
        });
        hiloMonstruo.start();
    }

    public void iniciarJuego(boolean unJugador) {
        Juego nuevoJuego = CargadorNivel.cargarNivel(1);
        if (nuevoJuego == null) {
            ventana.mostrarMensaje("Error", "No se pudo cargar el nivel 1.");
            return;
        }

        modoUnJugador = unJugador;
        monstruoActivo = unJugador;
        puzzleAbierto = false;
        puzzlePendienteAbrir = false;
        transicionando = false;
        segundos = 0;
        juego = nuevoJuego;
        estado = "JUGANDO";

        panelJuego.setJuego(nuevoJuego);
        panelJuego.actualizarTiempo(0);
        if (unJugador) {
            panelJuego.setModo("Monstruo: CPU");
        } else {
            panelJuego.setModo("Monstruo: flechas");
        }

        ventana.mostrarJuego();
        panelJuego.requestFocusInWindow();

        Sonido.detenerMusica();
        Sonido.reproducirMusica("juego.wav");
    }

    public void volverAlMenu() {
        estado = "MENU";
        puzzleAbierto = false;
        puzzlePendienteAbrir = false;
        transicionando = false;
        monstruoActivo = false;

        ventana.getPanelMenu().mostrarBienvenida();
        ventana.mostrarMenu();
        Sonido.detenerMusica();
        Sonido.reproducirMusica("menu.wav");
    }

    public void moverHumano(int dx, int dy) {
        if (!estado.equals("JUGANDO") || puzzleAbierto || transicionando) {
            return;
        }

        Juego juegoActual = juego;
        if (juegoActual == null) {
            return;
        }

        boolean seMovio = juegoActual.moverHumano(dx, dy);
        if (seMovio) {
            Sonido.reproducirEfecto("paso.wav");
        }
        revisarEstado();
    }

    public void moverMonstruo(int dx, int dy) {
        if (!estado.equals("JUGANDO") || puzzleAbierto || transicionando || modoUnJugador) {
            return;
        }

        Juego juegoActual = juego;
        if (juegoActual == null) {
            return;
        }

        boolean seMovio = juegoActual.moverMonstruo(dx, dy);
        if (seMovio) {
            Sonido.reproducirEfecto("paso.wav");
        }
        revisarEstado();
    }

    private void revisarEstado() {
        if (!estado.equals("JUGANDO") || juego == null || transicionando) {
            return;
        }

        Juego juegoActual = juego;

        if (juegoActual.isHumanoAtrapado()) {
            terminarDerrota();
            return;
        }

        if (juegoActual.isJuegoGanado()) {
            terminarVictoria();
            return;
        }

        if (juegoActual.isNivelCompletado()) {
            pasarNivel();
            return;
        }

        if (juegoActual.isPuzzlePendiente()) {
            boolean abrir = juegoActual.consumirPuzzlePendiente();
            if (abrir) {
                // espero a que el jugador suelte la tecla para abrir el diálogo
                puzzleAbierto = true;
                puzzlePendienteAbrir = true;
            }
        }
    }

    // el teclado avisa cuando se suelta una tecla
    public void soltarTecla() {
        if (puzzlePendienteAbrir && estado.equals("JUGANDO")) {
            puzzlePendienteAbrir = false;
            abrirPuzzle();
        }
    }

    private void abrirPuzzle() {
        puzzleAbierto = true;
        puzzlePendienteAbrir = false;

        Juego juegoActual = juego;
        if (!estado.equals("JUGANDO") || juegoActual == null) {
            puzzleAbierto = false;
            return;
        }

        Puzzle puzzle = juegoActual.getPuzzle();
        DialogoPuzzle dialogo = new DialogoPuzzle(ventana, puzzle.getPregunta());
        String respuesta = dialogo.mostrarDialogo();

        if (respuesta == null) {
            puzzleAbierto = false;
            panelJuego.requestFocusInWindow();
            return;
        }

        if (puzzle.esCorrecta(respuesta)) {
            juegoActual.abrirCofre();
            Sonido.reproducirEfecto("cofre_abrir.wav");
            ventana.mostrarMensaje("Cofre abierto", "¡Correcto! La salida ya está habilitada.");
        } else {
            Sonido.reproducirEfecto("puzzle_error.wav");
            ventana.mostrarMensaje("Respuesta incorrecta", "No era esa. Aléjate del cofre y vuelve a intentarlo.");
        }

        puzzleAbierto = false;
        panelJuego.requestFocusInWindow();
    }

    private void pasarNivel() {
        transicionando = true;

        int nivel = juego.getNivelActual();
        if (nivel >= 6) {
            terminarVictoria();
            return;
        }

        ventana.mostrarMensaje("Nivel superado", "¡Pasaste al nivel " + (nivel + 1) + "!");

        Juego siguiente = CargadorNivel.cargarNivel(nivel + 1);
        if (siguiente == null) {
            transicionando = false;
            ventana.mostrarMensaje("Error", "No se pudo cargar el siguiente nivel.");
            volverAlMenu();
            return;
        }

        juego = siguiente;
        panelJuego.setJuego(siguiente);
        if (modoUnJugador) {
            panelJuego.setModo("Monstruo: CPU");
        } else {
            panelJuego.setModo("Monstruo: flechas");
        }

        transicionando = false;
        panelJuego.requestFocusInWindow();
    }

    private void terminarVictoria() {
        if (!estado.equals("JUGANDO")) {
            return;
        }

        estado = "GANADO";
        monstruoActivo = false;
        Sonido.detenerMusica();
        Sonido.reproducirEfecto("victoria.wav");

        ventana.mostrarMensaje("¡Victoria!", "¡Conseguiste abrir los 6 cofres y escapar! Ganaste.");
        volverAlMenu();
    }

    private void terminarDerrota() {
        if (!estado.equals("JUGANDO")) {
            return;
        }

        estado = "PERDIDO";
        monstruoActivo = false;
        Sonido.detenerMusica();
        Sonido.reproducirEfecto("derrota.wav");

        ventana.mostrarMensaje("Derrota", "El monstruo te atrapó. Vuelve a intentarlo.");
        volverAlMenu();
    }

    public boolean isJugando() {
        return estado.equals("JUGANDO");
    }

    public boolean isPuzzleAbierto() {
        return puzzleAbierto;
    }

    public boolean isDosJugadores() {
        return !modoUnJugador;
    }
}
