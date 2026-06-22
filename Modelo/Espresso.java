package Modelo;

public class Espresso extends Bebida {
    private int intensidad;
    
    public Espresso(String nombre, double precio, String tamaño, int intensidad) {
        super(nombre, precio, tamaño);
        this.intensidad = intensidad;
    }


    //Polimorfismo
    @Override

    public double calcularPrecio() {
        double precioFinal = getPrecioBase();


        if (this.intensidad > 1) {
            precioFinal += (this.intensidad - 1) * 200; //Hacemos -1 porque el primer nivel de intensidad no tiene costo adicional, y cada nivel adicional cuesta 200.
        }
        return precioFinal;  
    }
    public int getIntensidad() {
        return this.intensidad;
    }
}
