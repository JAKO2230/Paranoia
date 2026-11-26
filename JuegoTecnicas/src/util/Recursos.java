package util;

import java.awt.Image;
import java.net.URL;
import javax.imageio.ImageIO;

public class Recursos {

    // si la imagen no existe devuelvo null y la vista dibuja una figura
    public static Image cargarImagen(String ruta) {
        try {
            URL url = Recursos.class.getResource("/Resources/" + ruta);
            if (url == null) {
                return null;
            }
            return ImageIO.read(url);
        } catch (Exception e) {
            return null;
        }
    }
}
