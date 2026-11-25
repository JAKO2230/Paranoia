package View;

import util.Recursos;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JButton;
import javax.swing.JPanel;

public class PanelMenu extends JPanel {

    private JButton botonUnJugador;
    private JButton botonDosJugadores;
    private JButton botonSalir;
    private Image fondo;
    private Image titulo;

    public PanelMenu() {
        setPreferredSize(new Dimension(800, 600));
        setLayout(null);
        setBackground(new Color(15, 15, 30));

        fondo = Recursos.cargarImagen("assets/ui/fondo_menu.png");
        titulo = Recursos.cargarImagen("assets/ui/titulo.png");

        botonUnJugador = new JButton("1 JUGADOR");
        botonUnJugador.setBounds(250, 300, 300, 50);
        botonUnJugador.setFont(new Font("Arial", Font.BOLD, 18));
        botonUnJugador.setFocusPainted(false);
        add(botonUnJugador);

        botonDosJugadores = new JButton("2 JUGADORES");
        botonDosJugadores.setBounds(250, 365, 300, 50);
        botonDosJugadores.setFont(new Font("Arial", Font.BOLD, 18));
        botonDosJugadores.setFocusPainted(false);
        add(botonDosJugadores);

        botonSalir = new JButton("SALIR");
        botonSalir.setBounds(250, 430, 300, 50);
        botonSalir.setFont(new Font("Arial", Font.BOLD, 18));
        botonSalir.setFocusPainted(false);
        add(botonSalir);
    }

    public JButton getBotonUnJugador() {
        return botonUnJugador;
    }

    public JButton getBotonDosJugadores() {
        return botonDosJugadores;
    }

    public JButton getBotonSalir() {
        return botonSalir;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // dibujo el fondo del menú
        if (fondo != null) {
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(new Color(15, 15, 35));
            g.fillRect(0, 0, getWidth(), getHeight());

            // luna y niebla hechas con figuras básicas
            g.setColor(new Color(220, 220, 190));
            g.fillOval(620, 50, 90, 90);
            g.setColor(new Color(15, 15, 35));
            g.fillOval(650, 35, 90, 90);

            g.setColor(new Color(25, 25, 50));
            g.fillRect(0, 500, 800, 100);
            g.setColor(new Color(35, 35, 65));
            g.fillRect(0, 540, 800, 60);
        }

        // dibujo el título
        if (titulo != null) {
            g.drawImage(titulo, 100, 40, 600, 140, this);
        } else {
            g.setColor(new Color(240, 210, 120));
            g.setFont(new Font("Arial", Font.BOLD, 48));
            FontMetrics medidas = g.getFontMetrics();
            String texto = "Cofres y Sombras";
            int ancho = medidas.stringWidth(texto);
            g.drawString(texto, (getWidth() - ancho) / 2, 125);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 15));
        g.drawString("Jugador 1 (humano): W A S D", 285, 520);
        g.drawString("Jugador 2 (monstruo): flechas", 285, 542);
        g.drawString("El humano abre el cofre y escapa por la salida.", 245, 564);
        g.drawString("En modo 1 jugador, el monstruo lo controla la computadora.", 205, 586);
    }
}
