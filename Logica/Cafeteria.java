package Logica;

import Modelo.Cliente; //traemos la clase Cliente para poder usarla en la clase Cafeteria
import Modelo.Mesa; //traemos la clase Mesa para poder usarla en la clase Cafeteria

import Modelo.Producto; //Lo traje para crear un HashMap con los productos
import Modelo.Espresso;
import Modelo.Latte;
import Modelo.Comida;

//Traemos las herramientas.
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cafeteria {
    private String nombre;
    private  Map<String, Cliente> clientes = new HashMap<>(); //Creamos una variable llamada clientes. Usamos map, definimos la llave con un String(Email en este caso) y el valor con un objeto de tipo Cliente. Esto nos permite acceder a los clientes de manera rápida usando su email como llave. Lo incializamos como un HashMap vacio. 
    
    private List<Mesa> mesas = new ArrayList<>(); //Creamos una variable llamada mesas. Usamos List para definir una lista de objetos de tipo Mesa. Lo inicializamos como un ArrayList vacio. Esto nos permite manejar las mesas de la cafeteria, agregar nuevas mesas, etc.

    private Map<String, Producto> productos = new HashMap<>(); // Guardamos los productos de la cafetería
    private List<Pedido> pedidos = new ArrayList<>();//Guardamos los pedidos

    

    public Cafeteria(String nombre) {
        this.nombre = nombre;
    }// Las colecciones ya se inicializan vacías arriba en los atributos, por lo que no es necesario inicializarlas en el constructor.

    //metodos

    public void registrarCliente(Cliente nuevoCliente){
        if (nuevoCliente == null) { //si el cliente es nulo, no se puede registrar
            System.out.println("No se puede registrar un cliente nulo.");
            return; //salimos del metodo
        }else{
            this.clientes.put(nuevoCliente.getEmail(), nuevoCliente); //Arriba setteamos la variable clientes como un Map, entonces aca usamos el metodo put para agregar un nuevo cliente al Map. La llave es el email del cliente(nuevoCliente.getEmail()) y el valor es el objeto cliente(nuevoCliente).
        }
    }

    public Cliente buscarCliente(String email){
        if(email == null || email.isEmpty()) { //si el email es nulo o esta vacio, no se puede buscar
            System.out.println("No se puede buscar un cliente con un email nulo o vacio.");
            return null; //salimos del metodo y devolvemos null
        } else{
        return this.clientes.get(email); //Usamos el metodo get del Map para buscar un cliente por su email. Si el cliente existe, devuelve el objeto Cliente, si no existe, devuelve null.
        }
    }   


    public void agregarMesa(Mesa mesa){
        if (mesa == null) { //si la mesa es nula, no se puede agregar
            System.out.println("No se puede agregar una mesa nula.");
            return; //salimos del metodo
        } else{
        this.mesas.add(mesa); //Agregamos una mesa a la lista de mesas usando el metodo add de List.
        }
    }
    public String getNombre() {
        return this.nombre;
    }

    public Mesa buscarMesaDisponible() {
        for (Mesa mesa : this.mesas) { //Usamos for each para recorrer la lista de mesas(this.mesas) y metemos cada mesa en la variable 'mesa'. Mesa es el tipo de dato.
            
            if (!mesa.isEstaOcupada()) { //Usamos el boolean que habiamos creado(isEstaOcupada) para verificar si la mesa esta ocupada o no.
                return mesa; // Te devuelve la mesa libre que encontró
            }
        }
        return null; // Si recorrió todas y no hay ninguna libre, devuelve null
    }
    public Map<String, Producto> getProductos() {
        return this.productos;
    }

    public void precargarProductos() {

        //Comidas
        //Dos comidas aptas celiacos, una dulce y una salada
        this.productos.put("Banana Bread", new Comida("Banana Bread", 1200.0, true, false));
        this.productos.put("Chipá de Queso", new Comida("Chipá de Queso", 800.0, true, true));

        //Dos comidas NO aptas celiacos, una dulce y una salada
        this.productos.put("Medialuna Dulce", new Comida("Medialuna Dulce", 400.0, false, false));
        this.productos.put("Pollo al Verdeo", new Comida("Pollo al Verdeo", 4500.0, false, true));

        this.productos.put("Café Espresso", new Espresso("Café Espresso", 1500.0, "Chico", 4));
        this.productos.put("Café Latte", new Latte("Café Latte", 1800.0, "Medio", "Entera"));
        
    }

    public List<Mesa> getMesas() { //Para ver la lista de mesas completas
        return this.mesas;
    }
    public void precargarMesas() { //Agregamos las mesas manualmente, luego las Precargamos en el Main
        this.mesas.add(new Mesa(1, 2)); // Mesa 1 para 2 personas
        this.mesas.add(new Mesa(2, 4)); // Mesa 2 para 4 personas
        this.mesas.add(new Mesa(3, 2)); // Mesa 3 para 2 personas
        this.mesas.add(new Mesa(4, 6)); // Mesa 4 para 6 personas
    }

    public void guardarPedido(Pedido nuevoPedido) {
        if (nuevoPedido != null) {
            this.pedidos.add(nuevoPedido);
        }
    }
    public List<Pedido> getPedidos() {
        return this.pedidos;
    }
}

