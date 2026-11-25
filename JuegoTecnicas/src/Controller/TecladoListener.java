package Controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TecladoListener implements KeyListener {

    private JuegoController controlador;

    public TecladoListener(JuegoController controlador) {
        this.controlador = controlador;
    }

    public void keyPressed(KeyEvent e) {
        int tecla = e.getKeyCode();

        if (tecla == KeyEvent.VK_ESCAPE) {
            controlador.volverAlMenu();
            return;
        }

        if (!controlador.isJugando() || controlador.isPuzzleAbierto()) {
            return;
        }

        // controles del humano
        if (tecla == KeyEvent.VK_W) {
            controlador.moverHumano(0, -1);
        } else if (tecla == KeyEvent.VK_S) {
            controlador.moverHumano(0, 1);
        } else if (tecla == KeyEvent.VK_A) {
            controlador.moverHumano(-1, 0);
        } else if (tecla == KeyEvent.VK_D) {
            controlador.moverHumano(1, 0);
        }

        // controles del monstruo cuando son 2 jugadores
        if (controlador.isDosJugadores()) {
            if (tecla == KeyEvent.VK_UP) {
                controlador.moverMonstruo(0, -1);
            } else if (tecla == KeyEvent.VK_DOWN) {
                controlador.moverMonstruo(0, 1);
            } else if (tecla == KeyEvent.VK_LEFT) {
                controlador.moverMonstruo(-1, 0);
            } else if (tecla == KeyEvent.VK_RIGHT) {
                controlador.moverMonstruo(1, 0);
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        // abro el puzzle cuando ya se soltó la tecla del movimiento
        controlador.soltarTecla();
    }

    public void keyTyped(KeyEvent e) {
        // no hace falta hacer nada aquí
    }
}
