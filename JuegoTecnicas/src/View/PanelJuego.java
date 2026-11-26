package View;

import Model.Juego;
import Model.Laberinto;
import util.Recursos;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PanelJuego extends JPanel {

    private static final int TAM = 32;
    private static final int ALTO_HUD = 60;

    private Juego juego;
    private JLabel labelTiempo;
    private String textoModo;

    private Image piso;
    private Image pared;
    private Image cofreCerrado;
    private Image cofreAbierto;
    private Image salida;

    private Image humanoArriba;
    private Image humanoAbajo;
    private Image humanoIzquierda;
    private Image humanoDerecha;

    private Image monstruoArriba;
    private Image monstruoAbajo;
    private Image monstruoIzquierda;
    private Image monstruoDerecha;

    public PanelJuego() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        setLayout(null);
        setFocusable(true);

        textoModo = "";

        labelTiempo = new JLabel("Tiempo 00:00");
        labelTiempo.setForeground(Color.WHITE);
        labelTiempo.setFont(new Font("Arial", Font.BOLD, 15));
        labelTiempo.setBounds(645, 20, 145, 25);
        add(labelTiempo);

        // cargo las imágenes. Si no existen, se dibujan figuras.
        piso = Recursos.cargarImagen("assets/tiles/piso.png");
        pared = Recursos.cargarImagen("assets/tiles/pared.png");
        cofreCerrado = Recursos.cargarImagen("assets/sprites/objetos/cofre_cerrado.png");
        cofreAbierto = Recursos.cargarImagen("assets/sprites/objetos/cofre_abierto.png");
        salida = Recursos.cargarImagen("assets/sprites/objetos/salida.png");

        humanoArriba = Recursos.cargarImagen("assets/sprites/humano/humano_arriba.png");
        humanoAbajo = Recursos.cargarImagen("assets/sprites/humano/humano_abajo.png");
        humanoIzquierda = Recursos.cargarImagen("assets/sprites/humano/humano_izquierda.png");
        humanoDerecha = Recursos.cargarImagen("assets/sprites/humano/humano_derecha.png");

        monstruoArriba = Recursos.cargarImagen("assets/sprites/monstruo/monstruo_arriba.png");
        monstruoAbajo = Recursos.cargarImagen("assets/sprites/monstruo/monstruo_abajo.png");
        monstruoIzquierda = Recursos.cargarImagen("assets/sprites/monstruo/monstruo_izquierda.png");
        monstruoDerecha = Recursos.cargarImagen("assets/sprites/monstruo/monstruo_derecha.png");
    }

    public void setJuego(Juego juego) {
        this.juego = juego;
        repaint();
    }

    public void setModo(String texto) {
        textoModo = texto;
        repaint();
    }

    public void actualizarTiempo(int segundos) {
        int minutos = segundos / 60;
        int resto = segundos % 60;
        labelTiempo.setText("Tiempo " + String.format("%02d:%02d", minutos, resto));
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        if (juego == null) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Cargando nivel...", 300, 300);
            return;
        }

        dibujarHud(g);

        Laberinto laberinto = juego.getLaberinto();
        for (int y = 0; y < laberinto.getFilas(); y++) {
            for (int x = 0; x < laberinto.getColumnas(); x++) {
                char celda = laberinto.getCelda(x, y);
                int px = x * TAM;
                int py = y * TAM + ALTO_HUD;

                if (celda == '#') {
                    dibujarPared(g, px, py, x, y);
                } else {
                    dibujarPiso(g, px, py, x, y);
                }

                if (celda == 'C') {
                    dibujarCofre(g, px, py);
                } else if (celda == 'S') {
                    dibujarSalida(g, px, py);
                }
            }
        }

        dibujarMonstruo(g);
        dibujarHumano(g);
    }

    private void dibujarHud(Graphics g) {
        g.setColor(new Color(12, 12, 22));
        g.fillRect(0, 0, getWidth(), ALTO_HUD);
        g.fillRect(0, 540, getWidth(), 60);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 15));
        g.drawString("Nivel " + juego.getNivelActual() + "/6", 15, 35);

        if (juego.isCofreAbierto()) {
            g.setColor(new Color(120, 255, 120));
            g.drawString("Cofre: ABIERTO", 120, 35);
        } else {
            g.setColor(new Color(255, 180, 90));
            g.drawString("Cofre: CERRADO", 120, 35);
        }

        g.setColor(Color.WHITE);
        g.drawString("Humano: WASD", 285, 35);
        g.drawString(textoModo, 430, 35);

        g.setColor(new Color(200, 200, 200));
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.drawString("Abre el cofre, llega a la salida y escapa. ESC = volver al menú.", 15, 575);
    }

    private void dibujarPiso(Graphics g, int px, int py, int x, int y) {
        if (piso != null) {
            g.drawImage(piso, px, py, TAM, TAM, this);
        } else {
            if ((x + y) % 2 == 0) {
                g.setColor(new Color(35, 32, 30));
            } else {
                g.setColor(new Color(45, 42, 38));
            }
            g.fillRect(px, py, TAM, TAM);
            g.setColor(new Color(60, 55, 50));
            g.drawRect(px, py, TAM - 1, TAM - 1);
        }
    }

    private void dibujarPared(Graphics g, int px, int py, int x, int y) {
        if (pared != null) {
            g.drawImage(pared, px, py, TAM, TAM, this);
        } else {
            g.setColor(new Color(45, 55, 80));
            g.fillRect(px, py, TAM, TAM);
            g.setColor(new Color(80, 95, 130));
            g.drawRect(px + 1, py + 1, TAM - 3, TAM - 3);
            g.drawLine(px + 4, py + TAM / 2, px + TAM - 5, py + TAM / 2);
        }
    }

    private void dibujarCofre(Graphics g, int px, int py) {
        if (juego.isCofreAbierto() && cofreAbierto != null) {
            g.drawImage(cofreAbierto, px, py, TAM, TAM, this);
            return;
        }

        if (!juego.isCofreAbierto() && cofreCerrado != null) {
            g.drawImage(cofreCerrado, px, py, TAM, TAM, this);
            return;
        }

        // cofre dibujado con figuras básicas
        if (juego.isCofreAbierto()) {
            g.setColor(new Color(120, 75, 30));
            g.fillRect(px + 5, py + 15, 22, 13);
            g.setColor(new Color(80, 45, 20));
            g.fillRect(px + 4, py + 5, 24, 7);
            g.setColor(new Color(240, 210, 90));
            g.fillRect(px + 14, py + 17, 4, 6);
        } else {
            g.setColor(new Color(120, 75, 30));
            g.fillRect(px + 5, py + 10, 22, 18);
            g.setColor(new Color(80, 45, 20));
            g.fillRect(px + 4, py + 7, 24, 7);
            g.setColor(new Color(240, 210, 90));
            g.fillRect(px + 14, py + 16, 4, 6);
        }
    }

    private void dibujarSalida(Graphics g, int px, int py) {
        if (salida != null) {
            g.drawImage(salida, px, py, TAM, TAM, this);
        } else {
            g.setColor(new Color(70, 70, 80));
            g.fillRect(px + 3, py + 3, 26, 29);
            g.setColor(new Color(150, 150, 160));
            g.drawRect(px + 5, py + 5, 22, 25);
        }

        if (juego.isCofreAbierto()) {
            g.setColor(new Color(80, 255, 100));
            g.drawRect(px + 1, py + 1, TAM - 3, TAM - 3);
        } else {
            // candado rojo para mostrar que la salida está cerrada
            g.setColor(new Color(180, 30, 30));
            g.fillRect(px + 10, py + 14, 12, 13);
            g.setColor(new Color(230, 200, 70));
            g.fillOval(px + 14, py + 18, 4, 4);
        }
    }

    private void dibujarHumano(Graphics g) {
        int px = juego.getHumanoX() * TAM;
        int py = juego.getHumanoY() * TAM + ALTO_HUD;
        String direccion = juego.getDireccionHumano();
        Image imagen = humanoAbajo;

        if (direccion.equals("arriba")) {
            imagen = humanoArriba;
        } else if (direccion.equals("izquierda")) {
            imagen = humanoIzquierda;
        } else if (direccion.equals("derecha")) {
            imagen = humanoDerecha;
        }

        if (imagen != null) {
            g.drawImage(imagen, px, py, TAM, TAM, this);
        } else {
            // humano dibujado con figuras básicas
            g.setColor(new Color(0, 0, 0, 120));
            g.fillOval(px + 5, py + 24, 22, 7);

            g.setColor(new Color(70, 130, 230));
            g.fillOval(px + 5, py + 5, 22, 24);

            g.setColor(new Color(255, 220, 180));
            g.fillOval(px + 10, py + 7, 12, 12);

            g.setColor(Color.WHITE);
            if (direccion.equals("izquierda")) {
                g.fillOval(px + 10, py + 11, 3, 3);
            } else if (direccion.equals("derecha")) {
                g.fillOval(px + 19, py + 11, 3, 3);
            } else if (direccion.equals("arriba")) {
                g.fillOval(px + 13, py + 9, 3, 3);
                g.fillOval(px + 18, py + 9, 3, 3);
            } else {
                g.fillOval(px + 12, py + 13, 3, 3);
                g.fillOval(px + 17, py + 13, 3, 3);
            }
        }
    }

    private void dibujarMonstruo(Graphics g) {
        int px = juego.getMonstruoX() * TAM;
        int py = juego.getMonstruoY() * TAM + ALTO_HUD;
        String direccion = juego.getDireccionMonstruo();
        Image imagen = monstruoAbajo;

        if (direccion.equals("arriba")) {
            imagen = monstruoArriba;
        } else if (direccion.equals("izquierda")) {
            imagen = monstruoIzquierda;
        } else if (direccion.equals("derecha")) {
            imagen = monstruoDerecha;
        }

        if (imagen != null) {
            g.drawImage(imagen, px, py, TAM, TAM, this);
        } else {
            // monstruo dibujado con figuras básicas
            g.setColor(new Color(0, 0, 0, 120));
            g.fillOval(px + 5, py + 24, 22, 7);

            g.setColor(new Color(150, 30, 150));
            g.fillOval(px + 4, py + 6, 24, 23);

            // cuernos
            g.setColor(new Color(90, 20, 90));
            g.fillRect(px + 7, py + 2, 5, 8);
            g.fillRect(px + 20, py + 2, 5, 8);

            // ojos
            g.setColor(new Color(255, 230, 40));
            g.fillOval(px + 10, py + 13, 5, 5);
            g.fillOval(px + 18, py + 13, 5, 5);
        }
    }
}
