package Controller;

import View.VentanaPrincipal;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                VentanaPrincipal ventana = new VentanaPrincipal();
                JuegoController controlador = new JuegoController(ventana);
                ventana.setVisible(true);
                controlador.volverAlMenu();
            }
        });
    }
}
