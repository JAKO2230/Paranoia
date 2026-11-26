package Model;
public class Laberinto {

    private char[][] celdas;
    private int filas;
    private int columnas;

    public Laberinto(char[][] celdas) {
        this.celdas = celdas;
        this.filas = celdas.length;
        this.columnas = celdas[0].length;
    }

    public synchronized boolean esPared(int x, int y) {
        // tambien cuento los bordes como pared
        if (x < 0 || x >= columnas || y < 0 || y >= filas) {
            return true;
        }
        return celdas[y][x] == '#';
    }

    public synchronized char getCelda(int x, int y) {
        if (x < 0 || x >= columnas || y < 0 || y >= filas) {
            return '#';
        }
        return celdas[y][x];
    }

    public synchronized void setCelda(int x, int y, char valor) {
        if (x >= 0 && x < columnas && y >= 0 && y < filas) {
            celdas[y][x] = valor;
        }
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }
}
