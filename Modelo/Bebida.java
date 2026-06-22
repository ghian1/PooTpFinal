package Modelo;

public abstract class Bebida extends Producto{
    private String tamaño;

    public Bebida(String nombre, double precioBase, String tamaño){
        super(nombre, precioBase);
        this.tamaño = tamaño;
    }

    public String getTamaño() {
        return this.tamaño;
    }
}
