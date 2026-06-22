package Modelo;

public class Cliente {
    private String nombre; 
    private String email;

    public Cliente(String nombre, String email) { 
        this.nombre = nombre;
        this.email = email;
    }

    public String getNombre() {
        return this.nombre;
    }

    public String getEmail() {
        return this.email;
    }

    
}