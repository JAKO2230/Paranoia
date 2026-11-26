package Model;

public class Puzzle {

    private String pregunta;
    private String respuesta;

    public Puzzle(String pregunta, String respuesta) {
        this.pregunta = pregunta;
        this.respuesta = respuesta;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    // reviso la respuesta sin importar mayusculas o minusculas
    public boolean esCorrecta(String intento) {
        if (intento == null) {
            return false;
        }
        return respuesta.equalsIgnoreCase(intento.trim());
    }

    // lista simple con un puzzle para cada nivel
    public static Puzzle[] crearPuzzles() {
        Puzzle[] lista = new Puzzle[6];
        lista[0] = new Puzzle("¿Cuánto es 7 + 8?", "15");
        lista[1] = new Puzzle("Tengo hojas y no soy un árbol. ¿Qué soy?", "libro");
        lista[2] = new Puzzle("2, 4, 8, 16, ¿qué número sigue?", "32");
        lista[3] = new Puzzle("¿Cuánto es 25 - 9?", "16");
        lista[4] = new Puzzle("Tengo teclas pero no abro puertas. ¿Qué soy?", "piano");
        lista[5] = new Puzzle("1, 1, 2, 3, 5, 8, ¿qué número sigue?", "13");
        return lista;
    }
}
