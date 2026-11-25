package View;

import java.awt.CardLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class VentanaPrincipal extends JFrame {

    private PanelMenu panelMenu;
    private PanelJuego panelJuego;
    private JPanel contenedor;
    private CardLayout cardLayout;

    public VentanaPrincipal() {
        setTitle("Cofres y Sombras");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        cardLayout = new CardLayout();
        contenedor = new JPanel(cardLayout);
        contenedor.setPreferredSize(new Dimension(800, 600));

        panelMenu = new PanelMenu();
        panelJuego = new PanelJuego();

        contenedor.add(panelMenu, "MENU");
        contenedor.add(panelJuego, "JUEGO");

        add(contenedor);
        pack();
        setLocationRelativeTo(null);
    }

    public PanelMenu getPanelMenu() {
        return panelMenu;
    }

    public PanelJuego getPanelJuego() {
        return panelJuego;
    }

    public void mostrarMenu() {
        cardLayout.show(contenedor, "MENU");
    }

    public void mostrarJuego() {
        cardLayout.show(contenedor, "JUEGO");
    }

    public void mostrarMensaje(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
    }
}
