package Modelo;

public class Comida extends Producto {
    private boolean esAptoCeliaco;
    private boolean esSalado;


    public Comida(String nombre, double precioBase, boolean esAptoCeliaco, boolean esSalado){
        super(nombre, precioBase);
        this.esAptoCeliaco = esAptoCeliaco;
        this.esSalado = esSalado;
    }


    //Polimorfismo
    @Override
    public double calcularPrecio(){
        double precioFinal = getPrecioBase();


        if(esAptoCeliaco){
            precioFinal = Math.round (precioFinal * 1.15); // Agregamos un 15% al precio final si es apto para celíacos
        }
        return precioFinal;
    }


    public boolean isEsAptoCeliaco() {
        return this.esAptoCeliaco;
    }

    public boolean isEsSalado() {
        return this.esSalado;
    }
}