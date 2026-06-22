package Modelo;

public abstract class Producto {
    private String nombre;
    private double precioBase;

    public Producto(String nombre, double precioBase){
        this.nombre = nombre;
        this.precioBase = precioBase;
    }
    //Metodos

    public abstract double calcularPrecio(); // Método abstracto para calcular el precio final del producto, no se usan {}
    
    
    public String getNombre() {
        return this.nombre;
    }

    public double getPrecioBase() {
        return this.precioBase;
    }
}
















    

