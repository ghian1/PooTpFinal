package Logica;

import Modelo.Producto; //como esta en otra carpeta, hay que importarla para poder usarla
import java.util.List;  //Avisa a java que vamos a usar la clase List, que es una interfaz para manejar listas de objetos
import java.util.ArrayList; // Avisa a java que vamos a usar la clase ArrayList, que es una implementación concreta de la interfaz List, que permite crear listas dinámicas de objetos

public class Pedido {

    private int numero;
    private String fecha;
    private String estado;    
    private int numeroMesa;
    private List<Producto> items = new ArrayList<>(); //creamos una lista llamada 'items' que va a contener objetos de tipo Producto. La inicializamos como un ArrayList vacío. 

    public Pedido(int numero, String fecha, int numeroMesa) { //No pusimos aca el estado porque lo queremos que se asigne automaticamente como "Pendiente" cada vez que se cree un nuevo pedido.
        this.numero = numero;
        this.fecha = fecha;
        this.estado = "Pendiente"; //por defecto, el estado del pedido es "Pendiente"
    }


    //Metodos

    public void agregarProducto(Producto producto) {

        if (producto == null) { //si el producto es nulo, no se puede agregar a la lista
            System.out.println("No se puede agregar un producto nulo al pedido.");
            return; //salimos del metodo
        } else{
        items.add(producto); //agrega un producto a la lista de items
        }    
    }

    public double calcularTotal() {
        double total = 0.0; 
        for (Producto producto : this.items) { //Usamos for each: recorremos la lista de items(this.items) y metemos los productos en 'producto'. Producto es el tipo de dato. 
            total += producto.calcularPrecio(); //Aca aplicamos el metodo calcularPrecio() de cada producto, que devuelve el precio del producto, y lo sumamos al total. Esto demuestra lo importante del polimorfismo.
        }
        return total; 
    }

    public void mostrarDetalleProductos() {
        System.out.println("Productos pedidos:");
        for (Producto productos : this.items) {
            System.out.println("  - " + productos.getNombre() + " : $" + productos.calcularPrecio());
        }
    }

    // Getters y Setters
    public int getNumero() { return this.numero; }
    public String getFecha() { return this.fecha; }
    public String getEstado() { return this.estado; }
    public List<Producto> getItems() { return this.items; }
    public void setEstado(String estado) { this.estado = estado; }

    public int getNumeroMesa() { return this.numeroMesa; }


}
