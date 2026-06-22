import Vista.VentanaPrincipal;
import Logica.Cafeteria;



public class Main {
    public static void main(String[] args) {
    // 1. Creamos la Cafetería ACÁ, una sola vez para todo el programa
    Cafeteria miCafeteria = new Cafeteria("Diomande");
    miCafeteria.precargarProductos();
    miCafeteria.precargarMesas();

    // 2. Abrimos la ventana pasándole la cafetería entre los paréntesis
    VentanaPrincipal ventana = new VentanaPrincipal(miCafeteria);
    ventana.setVisible(true);
    }    
    
}
