package Vista;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import Logica.Cafeteria;

import Vista.EstilosUI.Tipo;
import Vista.EstilosUI.VentanaEscalable;

public class VentanaPago extends JFrame {
    private Cafeteria miCafeteria;
    private JComboBox<String> comboMesas;
    private JButton btnPagar;
    private JButton btnVolver;

    public VentanaPago(Cafeteria miCafeteria) {
        this.miCafeteria = miCafeteria;

        // Configuración básica de la ventana
        setTitle("Cerrar Mesa / Registrar Pago");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Cartel
        JLabel lblMesa = new JLabel("Seleccione la Mesa a Cobrar:");

         //Lista Desplegable con las mesas a pagar
        comboMesas = new JComboBox<>();

        // Cargar las mesas en el combo
        for (Modelo.Mesa mesa : miCafeteria.getMesas()) {
            comboMesas.addItem("Mesa " + mesa.getNumeroMesa());
        }

        // Botón Pagar
        btnPagar = new JButton("Consultar Saldo y Pagar");

        // Botón Volver
        btnVolver = new JButton("Volver al Menú");

        // --- LAYOUT ADAPTABLE (diseño base 400x300) ---
        VentanaEscalable layout = EstilosUI.crearVentana(this, 400, 300);
        layout.agregar(lblMesa, 30, 40, 200, 25, Tipo.LABEL);
        layout.agregar(comboMesas, 30, 70, 320, 25, Tipo.COMBO);
        layout.agregar(btnPagar, 30, 130, 320, 35, Tipo.BOTON);
        layout.agregar(btnVolver, 30, 180, 320, 35, Tipo.BOTON);
        layout.activar();

        //Boton Volver
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EstilosUI.guardarGeometria(VentanaPago.this);
                Vista.VentanaMenu menu = new Vista.VentanaMenu(miCafeteria, null);
                menu.setVisible(true);
                dispose();
            }
        });

        //Accion de pagar
        btnPagar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int indiceMesaSeleccionada = comboMesas.getSelectedIndex();
                Modelo.Mesa mesaElegida = miCafeteria.getMesas().get(indiceMesaSeleccionada);

                if (!mesaElegida.isEstaOcupada()) {
                    JOptionPane.showMessageDialog(null, 
                        "La Mesa " + mesaElegida.getNumeroMesa() + " no está ocupada por ningún cliente.", 
                        "Mesa Libre", 
                        JOptionPane.WARNING_MESSAGE);
                    return; 
                }

                
                Logica.Pedido pedidoEncontrado = null;

                // Recorremos la lista central de pedidos de la cafetería
                for (Logica.Pedido producto : miCafeteria.getPedidos()) {
                    // Buscamos un pedido que sea de ESTA mesa y que todavía esté "Pendiente"
                    
                    if (producto.getEstado().equalsIgnoreCase("Pendiente")) {
                        pedidoEncontrado = producto;
                        break; // Ya lo encontramos, salimos del bucle
                    }
                }

                
                if (pedidoEncontrado != null) {
                    double totalACobrar = pedidoEncontrado.calcularTotal();
                    
                    // Cambiamos los estados en el sistema
                    mesaElegida.liberar();                     
                    pedidoEncontrado.setEstado("Finalizado");   

                    // Mostramos el cartelazo de éxito con el total limpio
                    String totalLindo = String.format("%.2f", totalACobrar);
                    JOptionPane.showMessageDialog(null, 
                        "¡Pago Registrado con Éxito!\n" +
                        "Mesa " + mesaElegida.getNumeroMesa() + " liberada.\n" +
                        "Total cobrado: $" + totalLindo, 
                        "Mesa Cerrada", 
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    
                    mesaElegida.liberar(); 
                    JOptionPane.showMessageDialog(null, "Mesa liberada por el sistema.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                }
                EstilosUI.guardarGeometria(VentanaPago.this);
                Vista.VentanaMenu menu = new Vista.VentanaMenu(miCafeteria, null);
                menu.setVisible(true);
            
                dispose(); 
            
            }
        });
    }
}
