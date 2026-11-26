package Model;
public class Juego {

    private Laberinto laberinto;
    private Humano humano;
    private Monstruo monstruo;
    private Cofre cofre;
    private Puzzle puzzle;

    private int nivelActual;
    private int salidaX;
    private int salidaY;

    private boolean cofreAbierto;
    private boolean puzzlePendiente;
    private boolean humanoAtrapado;
    private boolean nivelCompletado;
    private boolean juegoGanado;

    public Juego(Laberinto laberinto, Humano humano, Monstruo monstruo, Cofre cofre, int nivelActual) {
        this.laberinto = laberinto;
        this.humano = humano;
        this.monstruo = monstruo;
        this.cofre = cofre;
        this.nivelActual = nivelActual;
        this.cofreAbierto = false;
        this.puzzlePendiente = false;
        this.humanoAtrapado = false;
        this.nivelCompletado = false;
        this.juegoGanado = false;

        Puzzle[] puzzles = Puzzle.crearPuzzles();
        if (nivelActual >= 1 && nivelActual <= 6) {
            this.puzzle = puzzles[nivelActual - 1];
        } else {
            this.puzzle = puzzles[0];
        }
    }

    public void setSalida(int x, int y) {
        this.salidaX = x;
        this.salidaY = y;
    }

    // muevo al humano y reviso paredes, cofre, salida y monstruo
    public synchronized boolean moverHumano(int dx, int dy) {
        if (humanoAtrapado || nivelCompletado || juegoGanado) {
            return false;
        }

        int nuevaX = humano.getX() + dx;
        int nuevaY = humano.getY() + dy;

        // reviso si choca con la pared
        if (laberinto.esPared(nuevaX, nuevaY)) {
            return false;
        }

        humano.setX(nuevaX);
        humano.setY(nuevaY);

        if (dx == 1) {
            humano.setDireccion("derecha");
        } else if (dx == -1) {
            humano.setDireccion("izquierda");
        } else if (dy == 1) {
            humano.setDireccion("abajo");
        } else if (dy == -1) {
            humano.setDireccion("arriba");
        }

        // si llegó al cofre y sigue cerrado, activo el puzzle
        if (nuevaX == cofre.getX() && nuevaY == cofre.getY() && !cofreAbierto) {
            puzzlePendiente = true;
        }

        revisarChoque();

        // si el monstruo lo tocó, pierde
        if (humanoAtrapado) {
            return true;
        }

        // la salida solo funciona cuando el cofre está abierto
        if (nuevaX == salidaX && nuevaY == salidaY && cofreAbierto) {
            nivelCompletado = true;
            if (nivelActual == 6) {
                juegoGanado = true;
            }
        }

        return true;
    }

    // muevo al monstruo con las flechas del jugador 2
    public synchronized boolean moverMonstruo(int dx, int dy) {
        if (humanoAtrapado || nivelCompletado || juegoGanado) {
            return false;
        }

        int nuevaX = monstruo.getX() + dx;
        int nuevaY = monstruo.getY() + dy;

        // reviso si choca con la pared
        if (laberinto.esPared(nuevaX, nuevaY)) {
            return false;
        }

        monstruo.setX(nuevaX);
        monstruo.setY(nuevaY);

        if (dx == 1) {
            monstruo.setDireccion("derecha");
        } else if (dx == -1) {
            monstruo.setDireccion("izquierda");
        } else if (dy == 1) {
            monstruo.setDireccion("abajo");
        } else if (dy == -1) {
            monstruo.setDireccion("arriba");
        }

        revisarChoque();
        return true;
    }

    // el monstruo de la computadora sigue al humano por el laberinto
    public synchronized void moverMonstruoAuto() {
        if (humanoAtrapado || nivelCompletado || juegoGanado) {
            return;
        }

        // busco el siguiente paso del camino más corto hasta el humano
        int[] paso = buscarSiguientePaso();
        if (paso != null) {
            int pasoX = paso[0] - monstruo.getX();
            int pasoY = paso[1] - monstruo.getY();
            moverMonstruo(pasoX, pasoY);
        }
    }

    // búsqueda sencilla para saber cuál celda debe pisar el monstruo
    private synchronized int[] buscarSiguientePaso() {
        int filas = laberinto.getFilas();
        int columnas = laberinto.getColumnas();

        boolean[][] visitado = new boolean[filas][columnas];
        int[][] primerX = new int[filas][columnas];
        int[][] primerY = new int[filas][columnas];

        int[] colaX = new int[filas * columnas];
        int[] colaY = new int[filas * columnas];
        int inicio = 0;
        int fin = 0;

        int monstruoX = monstruo.getX();
        int monstruoY = monstruo.getY();
        int humanoX = humano.getX();
        int humanoY = humano.getY();

        colaX[fin] = monstruoX;
        colaY[fin] = monstruoY;
        fin = fin + 1;
        visitado[monstruoY][monstruoX] = true;

        int[] moverX = {1, -1, 0, 0};
        int[] moverY = {0, 0, 1, -1};

        while (inicio < fin) {
            int x = colaX[inicio];
            int y = colaY[inicio];
            inicio = inicio + 1;

            if (x == humanoX && y == humanoY) {
                return new int[]{primerX[y][x], primerY[y][x]};
            }

            for (int i = 0; i < 4; i++) {
                int nuevaX = x + moverX[i];
                int nuevaY = y + moverY[i];

                if (nuevaX < 0 || nuevaX >= columnas || nuevaY < 0 || nuevaY >= filas) {
                    continue;
                }
                if (visitado[nuevaY][nuevaX]) {
                    continue;
                }
                if (laberinto.esPared(nuevaX, nuevaY)) {
                    continue;
                }

                visitado[nuevaY][nuevaX] = true;

                // guardo el primer paso que se dio desde el monstruo
                if (x == monstruoX && y == monstruoY) {
                    primerX[nuevaY][nuevaX] = nuevaX;
                    primerY[nuevaY][nuevaX] = nuevaY;
                } else {
                    primerX[nuevaY][nuevaX] = primerX[y][x];
                    primerY[nuevaY][nuevaX] = primerY[y][x];
                }

                colaX[fin] = nuevaX;
                colaY[fin] = nuevaY;
                fin = fin + 1;
            }
        }

        return null;
    }

    private synchronized void revisarChoque() {
        if (humano.getX() == monstruo.getX() && humano.getY() == monstruo.getY()) {
            humanoAtrapado = true;
        }
    }

    public synchronized void abrirCofre() {
        cofre.abrir();
        cofreAbierto = true;
        puzzlePendiente = false;
    }

    // el controlador avisa que ya va a mostrar el puzzle
    public synchronized boolean consumirPuzzlePendiente() {
        if (!puzzlePendiente) {
            return false;
        }
        puzzlePendiente = false;
        return true;
    }

    public synchronized boolean isCofreAbierto() {
        return cofreAbierto;
    }

    public synchronized boolean isPuzzlePendiente() {
        return puzzlePendiente;
    }

    public synchronized boolean isHumanoAtrapado() {
        return humanoAtrapado;
    }

    public synchronized boolean isNivelCompletado() {
        return nivelCompletado;
    }

    public synchronized boolean isJuegoGanado() {
        return juegoGanado;
    }

    public synchronized int getNivelActual() {
        return nivelActual;
    }

    public synchronized int getHumanoX() {
        return humano.getX();
    }

    public synchronized int getHumanoY() {
        return humano.getY();
    }

    public synchronized String getDireccionHumano() {
        return humano.getDireccion();
    }

    public synchronized int getMonstruoX() {
        return monstruo.getX();
    }

    public synchronized int getMonstruoY() {
        return monstruo.getY();
    }

    public synchronized String getDireccionMonstruo() {
        return monstruo.getDireccion();
    }

    public synchronized int getCofreX() {
        return cofre.getX();
    }

    public synchronized int getCofreY() {
        return cofre.getY();
    }

    public synchronized int getSalidaX() {
        return salidaX;
    }

    public synchronized int getSalidaY() {
        return salidaY;
    }

    public synchronized Puzzle getPuzzle() {
        return puzzle;
    }

    public Laberinto getLaberinto() {
        return laberinto;
    }
}
