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
        setSize(450, 400); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setLocationRelativeTo(null);
        setLayout(null);
        
        //Cartel Bienvenida
        JLabel lblBienvenida = new JLabel(); 
        lblBienvenida.setBounds(30, 20, 390, 25); 
        lblBienvenida.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        if (this.clienteActivo != null) {
            // Si entramos por el Login normal, muestra tu nombre
            lblBienvenida.setText("¡Bienvenido a Diomande, " + this.clienteActivo.getNombre() + "!");
        } else {
            // Si volvimos de pagar de forma anónima, pone este texto genérico y NO se rompe
            lblBienvenida.setText("¡Bienvenido a Diomande!");
        }
        add(lblBienvenida);

        //Carta
        btnVerCarta = new JButton("Ver Carta de Productos");
        btnVerCarta.setBounds(115, 100, 250, 35); 
        add(btnVerCarta);

        //Hacer pedido
        btnHacerPedido = new JButton("Realizar un Pedido");
        btnHacerPedido.setBounds(115, 160, 250, 35);
        add(btnHacerPedido);

        //Ir a pagar
        btnIrAPagar = new JButton("Pagar / Liberar Mesas");
        btnIrAPagar.setBounds(115, 220, 250, 35); // Ajustá el eje Y (140) para que no se pise con tus otros botones
        add(btnIrAPagar);

        //Cerrar sesion
        btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setBounds(115, 280, 250, 35);
        add(btnCerrarSesion);

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
                // Creamos la ventana de pago pasándole la cafetería central
                Vista.VentanaPago pantallaPago = new Vista.VentanaPago(miCafeteria);
                pantallaPago.setVisible(true);
                dispose(); 
            }
        
        });

        //Cerrar sesion
        btnCerrarSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Creamos la ventana de Login pasándole el mismo cuaderno (miCafeteria)
                // Esto es para que el Login siga teniendo la lista de clientes intacta.
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

                VentanaPedido pantallaPedido = new VentanaPedido(miCafeteria, clienteActivo);
                
            
                pantallaPedido.setVisible(true);
                
    
                dispose(); 
            }
        });
    }
}
