package Modelo;

public class Mesa {
    private int numeroMesa;
    private int capacidad;
    private boolean estaOcupada;

    public Mesa(int numeroMesa, int capacidad) { //No es necesario poner 'estaOcupada' en el constructor porque siempre va a empezar como 'false' (libre)
        this.numeroMesa = numeroMesa;
        this.capacidad = capacidad;
        this.estaOcupada = false; // Por defecto, la mesa está libre al crearla
    }

    public void ocupar() {
        this.estaOcupada = true; // Cambia el estado de la mesa a ocupada
    }

    public void liberar() {
        this.estaOcupada = false; // Cambia el estado de la mesa a libre
    }

    public int getNumeroMesa() {
        return this.numeroMesa;
    }

    public int getCapacidad() {
        return this.capacidad;
    }

    public boolean isEstaOcupada() {
        return this.estaOcupada;
    }
}


