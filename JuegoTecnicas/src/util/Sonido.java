package util;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sonido {

    private static Clip musica;
    private static String musicaActual = "";

    // la musica se carga en un hilo para no congelar la ventana
    public static synchronized void reproducirMusica(final String archivo) {
        if (archivo == null) {
            return;
        }

        if (archivo.equals(musicaActual) && musica != null && musica.isRunning()) {
            return;
        }

        detenerMusica();
        musicaActual = archivo;

        Thread hiloSonido = new Thread(new Runnable() {
            public void run() {
                Clip clip = null;
                try {
                    URL url = Sonido.class.getResource("/sounds/music/" + archivo);
                    if (url == null) {
                        return;
                    }

                    AudioInputStream audio = AudioSystem.getAudioInputStream(url);
                    clip = AudioSystem.getClip();
                    clip.open(audio);
                    audio.close();

                    synchronized (Sonido.class) {
                        if (!archivo.equals(musicaActual)) {
                            clip.close();
                            return;
                        }
                        musica = clip;
                    }

                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                    clip.start();
                } catch (Exception e) {
                    // si falta el archivo o no se puede reproducir, no hago nada
                    if (clip != null) {
                        try {
                            clip.close();
                        } catch (Exception ex) {
                            // nada
                        }
                    }
                }
            }
        });
        hiloSonido.start();
    }

    public static synchronized void detenerMusica() {
        if (musica != null) {
            musica.stop();
            musica.close();
            musica = null;
        }
        musicaActual = "";
    }

    // los efectos también van en un hilo aparte
    public static void reproducirEfecto(final String archivo) {
        Thread hiloSonido = new Thread(new Runnable() {
            public void run() {
                Clip clip = null;
                try {
                    URL url = Sonido.class.getResource("/sounds/sfx/" + archivo);
                    if (url == null) {
                        return;
                    }

                    AudioInputStream audio = AudioSystem.getAudioInputStream(url);
                    clip = AudioSystem.getClip();
                    clip.open(audio);
                    audio.close();
                    clip.start();

                    long milisegundos = clip.getMicrosecondLength() / 1000;
                    if (milisegundos > 0) {
                        Thread.sleep(milisegundos);
                    }
                    clip.close();
                } catch (Exception e) {
                    // si falta el archivo o no se puede reproducir, no hago nada
                    if (clip != null) {
                        try {
                            clip.close();
                        } catch (Exception ex) {
                            // nada
                        }
                    }
                }
            }
        });
        hiloSonido.start();
    }
}
