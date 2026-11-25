package Model;
public class Humano {

    private int x;
    private int y;
    private String direccion;

    public Humano(int x, int y) {
        this.x = x;
        this.y = y;
        this.direccion = "abajo";
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
