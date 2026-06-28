import javax.swing.UIManager;

import Vista.VentanaPrincipal;
import Vista.EstilosUI;
import Logica.Cafeteria;
import Logica.GestorClientesCSV;
import Modelo.Cliente;

public class Main {
    public static void main(String[] args) {

    // --- MEJORA VISUAL: Swing usa el aspecto nativo de Windows (botones, menús, etc.) ---
    try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
        // Si falla, Swing usa su estilo por defecto
    }

    // --- MEJORA VISUAL: calcula escala según el monitor y carga posición/tamaño de ventana ---
    EstilosUI.inicializarEscala();

    // 1. Creamos la Cafetería ACÁ, una sola vez para todo el programa
    Cafeteria miCafeteria = new Cafeteria("Diomande");

    // --- PERSISTENCIA: cargamos clientes guardados en clientes.csv (si existe) ---
    for (Cliente cliente : GestorClientesCSV.cargarClientes()) {
        miCafeteria.registrarCliente(cliente);
    }

    miCafeteria.precargarProductos();
    miCafeteria.precargarMesas();

    // 2. Abrimos la ventana de registro (los usuarios ya cargados del CSV pueden ir al Login)
    VentanaPrincipal ventana = new VentanaPrincipal(miCafeteria);
    ventana.setVisible(true);
    }
    
}
