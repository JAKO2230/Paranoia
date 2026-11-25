package View;

import util.Recursos;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JButton;
import javax.swing.JPanel;

public class PanelMenu extends JPanel {

    private static final String PANTALLA_BIENVENIDA = "BIENVENIDA";
    private static final String PANTALLA_INSTRUCCIONES = "INSTRUCCIONES";
    private static final String PANTALLA_MODO = "MODO";

    private static final String NOMBRE_JUEGO = "COFRES Y SOMBRAS";
    private static final String MATERIA = "Técnicas de Programación";
    private static final String INTEGRANTES = "Sergio Benavides  ·  JAKO";
    private static final String UNIVERSIDAD = "Universidad Autónoma de Manizales";

    private JButton botonIniciar;
    private JButton botonInstrucciones;
    private JButton botonSalir;
    private JButton botonUnJugador;
    private JButton botonDosJugadores;
    private JButton botonVolver;
    private JButton botonInicio;

    private Image fondo;
    private Image logoUam;

    private String pantallaActual;

    public PanelMenu() {
        setPreferredSize(new Dimension(800, 600));
        setLayout(null);
        setBackground(new Color(10, 10, 20));

        // si las imágenes no existen, la vista dibuja un fondo y un logo de respaldo
        fondo = Recursos.cargarImagen("assets/ui/fondo_menu.png");
        logoUam = Recursos.cargarImagen("assets/ui/logo-uam.png");

        botonIniciar = crearBoton("INICIAR", 300, 345, 200, 44);
        botonInstrucciones = crearBoton("INSTRUCCIONES", 300, 400, 200, 44);
        botonSalir = crearBoton("SALIR", 300, 455, 200, 44);

        botonUnJugador = crearBoton("1 JUGADOR", 300, 220, 200, 44);
        botonDosJugadores = crearBoton("2 JUGADORES", 300, 285, 200, 44);

        botonVolver = crearBoton("VOLVER ATRÁS", 180, 515, 200, 44);
        botonInicio = crearBoton("REGRESAR AL INICIO", 420, 515, 200, 44);

        botonIniciar.setToolTipText("Comenzar una partida");
        botonInstrucciones.setToolTipText("Ver los controles y las reglas");
        botonSalir.setToolTipText("Cerrar el juego");
        botonUnJugador.setToolTipText("El monstruo lo controla la computadora");
        botonDosJugadores.setToolTipText("El segundo jugador controla al monstruo");
        botonVolver.setToolTipText("Regresar a la pantalla anterior");
        botonInicio.setToolTipText("Regresar a la bienvenida");

        mostrarBienvenida();
    }

    private JButton crearBoton(String texto, int x, int y, int ancho, int alto) {
        JButton boton = new BotonMenu(texto);
        boton.setBounds(x, y, ancho, alto);
        add(boton);
        return boton;
    }

    // botón dibujado a mano para que se vea igual en cualquier sistema operativo
    private class BotonMenu extends JButton {

        public BotonMenu(String texto) {
            super(texto);
            setFont(new Font("Arial", Font.BOLD, 15));
            setForeground(new Color(245, 220, 150));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setRolloverEnabled(true);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            Color relleno = new Color(22, 28, 48);
            if (getModel().isPressed()) {
                relleno = new Color(12, 15, 28);
            } else if (getModel().isRollover()) {
                relleno = new Color(42, 52, 86);
            }

            g2.setColor(relleno);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 14, 14));
            g2.setColor(new Color(190, 160, 80));
            g2.setStroke(new BasicStroke(2));
            g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 14, 14));

            g2.setFont(getFont());
            g2.setColor(getForeground());
            FontMetrics medidas = g2.getFontMetrics();
            int textoX = (getWidth() - medidas.stringWidth(getText())) / 2;
            int textoY = (getHeight() - medidas.getHeight()) / 2 + medidas.getAscent();
            g2.drawString(getText(), textoX, textoY);

            g2.dispose();
        }
    }

    public void mostrarBienvenida() {
        pantallaActual = PANTALLA_BIENVENIDA;

        botonIniciar.setVisible(true);
        botonInstrucciones.setVisible(true);
        botonSalir.setVisible(true);

        botonUnJugador.setVisible(false);
        botonDosJugadores.setVisible(false);
        botonVolver.setVisible(false);
        botonInicio.setVisible(false);

        repaint();
    }

    public void mostrarInstrucciones() {
        pantallaActual = PANTALLA_INSTRUCCIONES;

        botonIniciar.setVisible(false);
        botonInstrucciones.setVisible(false);
        botonSalir.setVisible(false);

        botonUnJugador.setVisible(false);
        botonDosJugadores.setVisible(false);
        botonVolver.setVisible(true);
        botonInicio.setVisible(true);

        repaint();
    }

    public void mostrarModoJuego() {
        pantallaActual = PANTALLA_MODO;

        botonIniciar.setVisible(false);
        botonInstrucciones.setVisible(false);
        botonSalir.setVisible(false);

        botonUnJugador.setVisible(true);
        botonDosJugadores.setVisible(true);
        botonVolver.setVisible(true);
        botonInicio.setVisible(true);

        repaint();
    }

    public void volverAtras() {
        if (PANTALLA_INSTRUCCIONES.equals(pantallaActual) || PANTALLA_MODO.equals(pantallaActual)) {
            mostrarBienvenida();
        }
    }

    public JButton getBotonIniciar() {
        return botonIniciar;
    }

    public JButton getBotonInstrucciones() {
        return botonInstrucciones;
    }

    public JButton getBotonSalir() {
        return botonSalir;
    }

    public JButton getBotonUnJugador() {
        return botonUnJugador;
    }

    public JButton getBotonDosJugadores() {
        return botonDosJugadores;
    }

    public JButton getBotonVolver() {
        return botonVolver;
    }

    public JButton getBotonInicio() {
        return botonInicio;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        dibujarFondo(g2);
        dibujarLogo(g2);

        if (PANTALLA_INSTRUCCIONES.equals(pantallaActual)) {
            dibujarInstrucciones(g2);
        } else if (PANTALLA_MODO.equals(pantallaActual)) {
            dibujarModoJuego(g2);
        } else {
            dibujarBienvenida(g2);
        }

        dibujarPie(g2);
    }

    private void dibujarFondo(Graphics2D g) {
        if (fondo != null) {
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        } else {
            // fondo de respaldo: cielo nocturno, luna, castillo y niebla
            GradientPaint cielo = new GradientPaint(
                    0, 0, new Color(18, 20, 45),
                    0, getHeight(), new Color(5, 5, 12));
            g.setPaint(cielo);
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(new Color(235, 225, 180, 35));
            g.fillOval(575, 25, 170, 170);
            g.setColor(new Color(225, 220, 185, 210));
            g.fillOval(615, 55, 90, 90);

            g.setColor(new Color(8, 8, 18));
            g.fillRect(0, 430, getWidth(), 170);
            g.fillRect(70, 350, 70, 110);
            g.fillRect(95, 315, 20, 45);
            g.fillRect(180, 385, 55, 75);
            g.fillRect(560, 370, 70, 90);
            g.fillRect(590, 330, 18, 50);
            g.fillRect(670, 395, 50, 65);

            g.setColor(new Color(40, 42, 70, 90));
            g.fillRect(0, 500, getWidth(), 35);
            g.setColor(new Color(55, 58, 90, 70));
            g.fillRect(0, 535, getWidth(), 30);

            g.setColor(new Color(90, 80, 55, 45));
            g.fillRect(0, 585, getWidth(), 15);
        }

        // oscurezco un poco para que los textos se lean bien
        g.setColor(new Color(0, 0, 0, 75));
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    private void dibujarLogo(Graphics2D g) {
        int x = 28;
        int y = 22;
        int ancho = 100;
        int alto = 50;

        // tarjeta blanca para que el logo negro se vea sobre el fondo oscuro
        g.setColor(new Color(0, 0, 0, 235));
        g.fill(new RoundRectangle2D.Float(x - 9, y - 7, ancho + 18, alto + 14, 16, 16));
        g.setColor(new Color(190, 160, 80, 220));
        g.setStroke(new BasicStroke(2));
        g.draw(new RoundRectangle2D.Float(x - 9, y - 7, ancho + 18, alto + 14, 16, 16));

        if (logoUam != null) {
            g.drawImage(logoUam, x, y, ancho, alto, this);
        } else {
            // logo de respaldo dibujado con texto
            g.setColor(new Color(20, 20, 20));
            g.setFont(new Font("Arial", Font.BOLD, 32));
            g.drawString("UAM", x + 42, y + 42);
            g.setFont(new Font("Arial", Font.PLAIN, 9));
            g.drawString("Casa abierta al tiempo", x + 14, y + 62);
        }
    }

    private void dibujarBienvenida(Graphics2D g) {
        dibujarTextoCentrado(g, NOMBRE_JUEGO, 150,
                new Font("Arial", Font.BOLD, 42), new Color(245, 215, 125));
        dibujarTextoCentrado(g, "Una aventura entre cofres, laberintos y sombras", 178,
                new Font("Arial", Font.ITALIC, 15), new Color(220, 220, 230));

        // tarjeta de datos escolares
        g.setColor(new Color(8, 10, 22, 185));
        g.fill(new RoundRectangle2D.Float(185, 200, 430, 105, 18, 18));
        g.setColor(new Color(190, 160, 80, 170));
        g.setStroke(new BasicStroke(1.5f));
        g.draw(new RoundRectangle2D.Float(185, 200, 430, 105, 18, 18));

        dibujarTextoCentrado(g, "Materia: " + MATERIA, 232,
                new Font("Arial", Font.BOLD, 16), Color.WHITE);
        dibujarTextoCentrado(g, "Integrantes: " + INTEGRANTES, 260,
                new Font("Arial", Font.PLAIN, 15), new Color(235, 235, 240));
        dibujarTextoCentrado(g, UNIVERSIDAD, 286,
                new Font("Arial", Font.PLAIN, 13), new Color(200, 200, 210));

        dibujarTextoCentrado(g, "Presiona INICIAR para elegir el modo de juego", 325,
                new Font("Arial", Font.PLAIN, 14), new Color(205, 205, 215));
    }

    private void dibujarInstrucciones(Graphics2D g) {
        dibujarTextoCentrado(g, "INSTRUCCIONES", 78,
                new Font("Arial", Font.BOLD, 36), new Color(245, 215, 125));

        g.setColor(new Color(8, 10, 22, 190));
        g.fill(new RoundRectangle2D.Float(70, 100, 660, 385, 20, 20));
        g.setColor(new Color(190, 160, 80, 170));
        g.setStroke(new BasicStroke(1.5f));
        g.draw(new RoundRectangle2D.Float(70, 100, 660, 385, 20, 20));

        int x = 105;
        g.setFont(new Font("Arial", Font.BOLD, 17));
        g.setColor(new Color(245, 215, 125));
        g.drawString("OBJETIVO", x, 138);

        g.setFont(new Font("Arial", Font.PLAIN, 15));
        g.setColor(Color.WHITE);
        g.drawString("Abre el cofre del nivel resolviendo el puzzle y llega a la salida.", x, 166);

        g.setFont(new Font("Arial", Font.BOLD, 17));
        g.setColor(new Color(245, 215, 125));
        g.drawString("CONTROLES", x, 205);

        g.setFont(new Font("Arial", Font.PLAIN, 15));
        g.setColor(Color.WHITE);
        g.drawString("Jugador 1 (humano): W A S D", x, 233);
        g.drawString("Jugador 2 (monstruo): flechas del teclado", x, 259);
        g.drawString("Modo 1 jugador: el monstruo lo controla la computadora.", x, 285);
        g.drawString("ESC: volver al menú principal.", x, 311);

        g.setFont(new Font("Arial", Font.BOLD, 17));
        g.setColor(new Color(245, 215, 125));
        g.drawString("REGLAS", x, 350);

        g.setFont(new Font("Arial", Font.PLAIN, 15));
        g.setColor(Color.WHITE);
        g.drawString("La salida permanece cerrada hasta abrir el cofre.", x, 378);
        g.drawString("Si el monstruo te toca, pierdes y vuelves al inicio.", x, 404);
        g.drawString("Hay 6 niveles y cada uno tiene un puzzle diferente.", x, 430);
        g.drawString("Si fallas, aléjate del cofre y vuelve a intentarlo.", x, 456);
    }

    private void dibujarModoJuego(Graphics2D g) {
        dibujarTextoCentrado(g, "SELECCIONA EL MODO DE JUEGO", 145,
                new Font("Arial", Font.BOLD, 32), new Color(245, 215, 125));
        dibujarTextoCentrado(g, "¿Quién controlará al monstruo?", 175,
                new Font("Arial", Font.PLAIN, 16), new Color(220, 220, 230));

        g.setColor(new Color(8, 10, 22, 185));
        g.fill(new RoundRectangle2D.Float(180, 350, 440, 105, 18, 18));
        g.setColor(new Color(190, 160, 80, 170));
        g.setStroke(new BasicStroke(1.5f));
        g.draw(new RoundRectangle2D.Float(180, 350, 440, 105, 18, 18));

        dibujarTextoCentrado(g, "1 JUGADOR: el monstruo te persigue automáticamente.", 382,
                new Font("Arial", Font.PLAIN, 14), Color.WHITE);
        dibujarTextoCentrado(g, "2 JUGADORES: el segundo jugador mueve al monstruo.", 410,
                new Font("Arial", Font.PLAIN, 14), Color.WHITE);
        dibujarTextoCentrado(g, "El humano siempre usa W A S D.", 438,
                new Font("Arial", Font.PLAIN, 14), new Color(210, 210, 220));
    }

    private void dibujarPie(Graphics2D g) {
        dibujarTextoCentrado(g, "Proyecto escolar · UAM", 580,
                new Font("Arial", Font.PLAIN, 12), new Color(170, 170, 180));
    }

    private void dibujarTextoCentrado(Graphics g, String texto, int y, Font fuente, Color color) {
        g.setFont(fuente);
        g.setColor(color);
        FontMetrics medidas = g.getFontMetrics();
        int x = (getWidth() - medidas.stringWidth(texto)) / 2;
        g.drawString(texto, x, y);
    }
}
