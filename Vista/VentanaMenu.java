package Vista;

import javax.swing.JFrame;       
import javax.swing.JLabel;       
import javax.swing.JButton;      
import javax.swing.JOptionPane;  
import java.awt.event.ActionListener;
import java.util.Map;
import java.awt.event.ActionEvent;    

import Logica.Cafeteria; 
import Modelo.Cliente;
import Modelo.Comida;
import Modelo.Producto;

import Vista.EstilosUI.Tipo;
import Vista.EstilosUI.VentanaEscalable;

public class VentanaMenu extends JFrame{
    private Cafeteria miCafeteria;    
    private Cliente clienteActivo; //Variable nueva para poder refernirnos al cliente que esta actualmente operando. private JButton btnVerCarta;      
    private JButton btnVerCarta;
    private JButton btnHacerPedido;    
    private JButton btnCerrarSesion;
    private JButton btnIrAPagar;

    public VentanaMenu(Cafeteria miCafeteria, Cliente clienteLogueado){
        //Inicializamos como en todas las ventanas. 
        this.miCafeteria = miCafeteria; 
        this.clienteActivo = clienteLogueado;

        //Usamos la config que venimos usando para todas las interfaces. 
        setTitle("Cafetería Diomande - Menú Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        
        //Cartel Bienvenida
        JLabel lblBienvenida = new JLabel(); 
        lblBienvenida.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        if (this.clienteActivo != null) {
            // Si entramos por el Login normal, muestra tu nombre
            lblBienvenida.setText("¡Bienvenido a Diomande, " + this.clienteActivo.getNombre() + "!");
        } else {
            // Si volvimos de pagar de forma anónima, pone este texto genérico y NO se rompe
            lblBienvenida.setText("¡Bienvenido a Diomande!");
        }

        //Carta
        btnVerCarta = new JButton("Ver Carta de Productos");

        //Hacer pedido
        btnHacerPedido = new JButton("Realizar un Pedido");

        //Ir a pagar
        btnIrAPagar = new JButton("Pagar / Liberar Mesas");

        //Cerrar sesion
        btnCerrarSesion = new JButton("Cerrar Sesión");

        // --- LAYOUT ADAPTABLE ---
        VentanaEscalable layout = EstilosUI.crearVentana(this, 450, 400);
        layout.agregar(lblBienvenida, 30, 20, 390, 25, Tipo.TITULO);
        layout.agregar(btnVerCarta, 115, 100, 250, 35, Tipo.BOTON);
        layout.agregar(btnHacerPedido, 115, 160, 250, 35, Tipo.BOTON);
        layout.agregar(btnIrAPagar, 115, 220, 250, 35, Tipo.BOTON);
        layout.agregar(btnCerrarSesion, 115, 280, 250, 35, Tipo.BOTON);
        layout.activar();

        //Ahora que hicimos la Precarga de productos vamos a mostrarlos. 
        btnVerCarta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // A. Pedimos el mapa de productos a la lógica
                Map<String, Producto> listaProductos = miCafeteria.getProductos(); //Le ponemos un get

                StringBuilder cartaTexto = new StringBuilder(); //Usamos un StringBuilder para crear cartaTexto
                cartaTexto.append("======= CARTA DE PRODUCTOS =======\n\n"); //a cartaTexto le hacemos una interfaz mas amigable
                
                cartaTexto.append("--- BEBIDAS ---\n"); //Comenzamos con bebidas

                for (Producto producto : listaProductos.values()) { //Bucle for each. con .values que va hacer que me pase solo el obj, es decir, espresso, latte, chipa, etc. 
                    if (!(producto instanceof Comida)) { //instanceof compara si el producto actual se creo en comida. Como tenemos el !, quiere decir si el producto actual no se creo en comida, es decir, es una bebida. 
                        cartaTexto.append("• ").append(producto.getNombre())
                                .append(" -> $").append(producto.getPrecioBase()).append("\n");
                    }
                }
                cartaTexto.append("\n--- PANADERÍA Y COCINA ---\n"); //Ahora vamos con la comida 
                
                for (Producto producto : listaProductos.values()) {
                    if (producto instanceof Comida) { //Hacemos lo mismo, si producto se creo en comida.
                        Comida comida = (Comida) producto; //Hacemos esto para acceder a todos sus atributos
                        
                        cartaTexto.append("• ").append(comida.getNombre())
                                .append(" -> $").append(comida.getPrecioBase());
                    
                

                        if (comida.isEsAptoCeliaco()) {
                                    cartaTexto.append(" [Apto Celíacos(+15% Recargo)]");
                        }
                        if (comida.isEsSalado()) {
                                    cartaTexto.append(" [Salado]");
                        } else {
                                    cartaTexto.append(" [Dulce]");
                        }
                        cartaTexto.append("\n"); // Salto de línea para el siguiente producto
                            
                    }    
                }    
            
                cartaTexto.append("\n=================================");
                JOptionPane.showMessageDialog(null, cartaTexto.toString(), "Carta Diomande", JOptionPane.INFORMATION_MESSAGE);
                //Cartel flotante con la cartaTexto
            }   
        });        
        
        //Ventana de pago
        btnIrAPagar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Guardamos geometría antes de ir a pagar
                EstilosUI.guardarGeometria(VentanaMenu.this);
                Vista.VentanaPago pantallaPago = new Vista.VentanaPago(miCafeteria);
                pantallaPago.setVisible(true);
                dispose(); 
            }
        
        });

        //Cerrar sesion
        btnCerrarSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Guardamos geometría y volvemos al login
                EstilosUI.guardarGeometria(VentanaMenu.this);
                VentanaLogin pantallaLogin = new VentanaLogin(miCafeteria);
                
                // 2. Volvemos a hacer visible el Login
                pantallaLogin.setVisible(true);
                
                
                dispose();
            }
        });

        //Puente hacia Pedido

        btnHacerPedido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // Guardamos geometría y abrimos pedido
                EstilosUI.guardarGeometria(VentanaMenu.this);
                VentanaPedido pantallaPedido = new VentanaPedido(miCafeteria, clienteActivo);
                
            
                pantallaPedido.setVisible(true);
                
    
                dispose(); 
            }
        });
    }
}
