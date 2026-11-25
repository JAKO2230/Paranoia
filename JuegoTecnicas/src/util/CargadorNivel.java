package util;

import Model.Cofre;
import Model.Humano;
import Model.Juego;
import Model.Laberinto;
import Model.Monstruo;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CargadorNivel {

    private static final int FILAS = 15;
    private static final int COLUMNAS = 25;

    public static Juego cargarNivel(int numero) {
        String ruta = "../Resources/levels/nivel" + numero + ".txt";
        InputStream entrada = null;
        BufferedReader lector = null;
        char[][] mapa = new char[FILAS][COLUMNAS];

        int humanoX = -1;
        int humanoY = -1;
        int monstruoX = -1;
        int monstruoY = -1;
        int cofreX = -1;
        int cofreY = -1;
        int salidaX = -1;
        int salidaY = -1;

        // primero lleno el mapa con paredes por si falta una fila
        for (int y = 0; y < FILAS; y++) {
            for (int x = 0; x < COLUMNAS; x++) {
                mapa[y][x] = '#';
            }
        }

        try {
            entrada = CargadorNivel.class.getResourceAsStream(ruta);
            if (entrada == null) {
                System.out.println("No se encontró el nivel: " + ruta);
                return null;
            }

            lector = new BufferedReader(new InputStreamReader(entrada, "UTF-8"));

            for (int y = 0; y < FILAS; y++) {
                String linea = lector.readLine();
                if (linea == null) {
                    break;
                }

                for (int x = 0; x < COLUMNAS; x++) {
                    if (x < linea.length()) {
                        mapa[y][x] = linea.charAt(x);
                    }
                }
            }

            // busco las letras importantes del mapa
            for (int y = 0; y < FILAS; y++) {
                for (int x = 0; x < COLUMNAS; x++) {
                    char celda = mapa[y][x];
                    if (celda == 'H') {
                        humanoX = x;
                        humanoY = y;
                    } else if (celda == 'M') {
                        monstruoX = x;
                        monstruoY = y;
                    } else if (celda == 'C') {
                        cofreX = x;
                        cofreY = y;
                    } else if (celda == 'S') {
                        salidaX = x;
                        salidaY = y;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (lector != null) {
                try {
                    lector.close();
                } catch (Exception e) {
                    // nada
                }
            } else if (entrada != null) {
                try {
                    entrada.close();
                } catch (Exception e) {
                    // nada
                }
            }
        }

        // reviso que el mapa tenga todo lo necesario
        if (humanoX == -1 || monstruoX == -1 || cofreX == -1 || salidaX == -1) {
            System.out.println("El nivel " + numero + " no tiene H, M, C o S.");
            return null;
        }

        Laberinto laberinto = new Laberinto(mapa);
        Humano humano = new Humano(humanoX, humanoY);
        Monstruo monstruo = new Monstruo(monstruoX, monstruoY);
        Cofre cofre = new Cofre(cofreX, cofreY);

        Juego juego = new Juego(laberinto, humano, monstruo, cofre, numero);
        juego.setSalida(salidaX, salidaY);
        return juego;
    }
}
