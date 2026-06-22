package Vista;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import Logica.Cafeteria; 
import Modelo.Cliente;

public class VentanaPedido extends JFrame{
    private Cafeteria miCafeteria;
    private Cliente clienteActivo;

    // Componentes visuales
    private JComboBox<String> comboMesas;      
    private JComboBox<String> comboProductos;  
    private JTextArea txtResumen;              
    private JButton btnAgregarItem;            
    private JButton btnConfirmarPedido;        
    private JButton btnVolver;
    private java.util.List<Modelo.Producto> carrito = new java.util.ArrayList<>(); // Arraylist importatisima para ir guardando los productos en el carrito. 
    private JComboBox<String> comboOpcionesCafe; // Este combo sirve para Intensidad (Espresso) o Leche (Latte)

    public VentanaPedido(Cafeteria miCafeteria, Cliente clienteLogueado) {  
        this.miCafeteria = miCafeteria;
        this.clienteActivo = clienteLogueado; 
        
        //Config que venimos usando. 
        setTitle("Realizar Nuevo Pedido - Diomande");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Solo cierra ESTA ventana, no todo el programa
        setLocationRelativeTo(null); // La centra en la pantalla
        setLayout(null);

        //Cartel
        JLabel lblMesa = new JLabel("Seleccione la Mesa:"); 
        lblMesa.setBounds(30, 30, 150, 25);
        add(lblMesa);

        //Lista desplegable con las mesas
        comboMesas = new JComboBox<>(); 
        comboMesas.setBounds(180, 30, 260, 25);
        add(comboMesas);

        //Cartel
        JLabel lblProducto = new JLabel("Seleccione Producto:");
        lblProducto.setBounds(30, 80, 150, 25);
        add(lblProducto);

        //Lista Desplegable con productos
        comboProductos = new JComboBox<>();
        comboProductos.setBounds(180, 80, 260, 25);
        add(comboProductos);

        // Botón para agregar el ítem seleccionado al carrito
        btnAgregarItem = new JButton("Agregar al Pedido +");
        btnAgregarItem.setBounds(180, 160, 260, 30);
        add(btnAgregarItem);

        //Cartel
        JLabel lblResumen = new JLabel("Resumen del Pedido:");
        lblResumen.setBounds(30, 210, 150, 25);
        add(lblResumen);

        // JTextArea es un cuadro de texto gigante. Lo seteamos para que no se pueda escribir a mano
        txtResumen = new JTextArea();
        txtResumen.setEditable(false);

        // JScrollPane es una caja contenedora donde almacenamos los productos
        JScrollPane scrollPane = new JScrollPane(txtResumen);
        scrollPane.setBounds(30, 240, 410, 140);
        add(scrollPane);

        //Agregamos la intensidad para que la pueda manejar el usuario y no sea hardcodeada. 

        // Cartel 
        JLabel lblOpcionesCafe = new JLabel("Opciones de Personalización:");
        lblOpcionesCafe.setBounds(30, 120, 180, 25);
        add(lblOpcionesCafe);

        // Inicializamos el combo 
        comboOpcionesCafe = new JComboBox<>();
        comboOpcionesCafe.setBounds(210, 120, 230, 25);
        add(comboOpcionesCafe);

        //Boton para confirmar
        btnConfirmarPedido = new JButton("Confirmar Pedido");
        btnConfirmarPedido.setBounds(30, 400, 200, 35);
        add(btnConfirmarPedido);

        //Boton para volver al Menu
        btnVolver = new JButton("Volver al Menú");
        btnVolver.setBounds(240, 400, 200, 35);
        add(btnVolver);
    
        //Traemos la info

        for (Modelo.Mesa mesa : miCafeteria.getMesas()) { //trae las mesas que ''Hardcodeamos' gracias al miCafeteria.getMesas
        String textoMesa = "Mesa " + mesa.getNumeroMesa() + " (Capacidad: " + mesa.getCapacidad() + ")"; //Fabricamos el texto, diciendo el numero y capacidad. 
        comboMesas.addItem(textoMesa); //Agregamos
        }
        
        for (Modelo.Producto producto : miCafeteria.getProductos().values()) { //Hacemos lo mismo con los productos, gracias al .values
        String nombreProducto = producto.getNombre(); //Armamos el texto. 
        comboProductos.addItem(nombreProducto); //Agregamos
        }

        //Escuchador de la lista comboProductos (productos con personalizacion)
        comboProductos.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
                String seleccionado = (String) comboProductos.getSelectedItem(); //capturamos el texto(cafe latte por ejemplo)
                comboOpcionesCafe.removeAllItems();//limpiamos
                
                if (seleccionado != null) {
                    if (seleccionado.equalsIgnoreCase("Café Espresso")) {
                        comboOpcionesCafe.setEnabled(true);
                        comboOpcionesCafe.addItem("1 - Suave");
                        comboOpcionesCafe.addItem("2 - Intermedio");
                        comboOpcionesCafe.addItem("3 - Fuerte");
                    } 
                    else if (seleccionado.equalsIgnoreCase("Café Latte")) {
                        comboOpcionesCafe.setEnabled(true);
                        comboOpcionesCafe.addItem("Leche Entera");
                        comboOpcionesCafe.addItem("Leche de Almendras (+ $400)");
                    } 
                    else {
                        comboOpcionesCafe.addItem("No requiere adicionales");
                        comboOpcionesCafe.setEnabled(false);
                    }
                }
            }
        });

        //Carrito + personalizacion si eligio latte o expresso
        btnAgregarItem.addActionListener(new ActionListener() { //esto es una vez agregamos el item o producto
            @Override
            public void actionPerformed(ActionEvent e) {

                // Captura el renglón seleccionado por el usuario en la lista desplegable.
                // Como getSelectedItem() devuelve un Object genérico, usamos String para transformarlo en texto.
                String nombreSeleccionado = (String) comboProductos.getSelectedItem(); 

                
                //Buscamos el producto
                Modelo.Producto productoElegido = miCafeteria.getProductos().get(nombreSeleccionado); 

                if (productoElegido != null) { 
                    // Creamos una variable temporal que por defecto apunta al producto base
                    Modelo.Producto productoAFacturar = productoElegido;

                    //Si el usuario elegio expresso
                    if (nombreSeleccionado.equalsIgnoreCase("Café Espresso")) {
                        int intensidadElegida = comboOpcionesCafe.getSelectedIndex() + 1; // +1 porque empezamos en 0. Si el usario elige intensidad 1, el valor esta en 0+1= intensidad1
                        productoAFacturar = new Modelo.Espresso("Café Espresso", 1500.0, "Chico", intensidadElegida);
                    }
                    //Si selecciono latte
                    else if (nombreSeleccionado.equalsIgnoreCase("Café Latte")) {
                        String lecheSeleccionada = "Entera";
                        if (comboOpcionesCafe.getSelectedIndex() == 1) { //Si elegio
                            lecheSeleccionada = "Almendras";
                        }
                        productoAFacturar = new Modelo.Latte("Café Latte", 1800.0, "Medio", lecheSeleccionada);
                    }

                    
                    carrito.add(productoAFacturar); //Agregamos al producto al carrito pero ya personalizado
                
                    //Redibujamos el resumen visual aplicando polimorfismo puro (.calcularPrecio())
                    StringBuilder resumenTexto = new StringBuilder();
                    resumenTexto.append("--- PRODUCTOS AGREGADOS ---\n");
    
                    double totalActual = 0.0;

                    //Un for para ir recorriendo los productos y agregarlos 
                    for (Modelo.Producto producto : carrito) {
                        resumenTexto.append("• ").append(producto.getNombre())
                                    .append(" : $").append(producto.calcularPrecio()).append("\n");
                        
                        totalActual += producto.calcularPrecio();
                    }
                    resumenTexto.append("\n-----------------------------------");
                    resumenTexto.append("\n TOTAL ACTUAL: $").append(totalActual);
                    txtResumen.setText(resumenTexto.toString());
                }    
            }    
        });
        
        //Confirmar pedido
        btnConfirmarPedido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Validamos que el carrito no este vacio
                if (carrito.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "El carrito está vacío. Agregá productos antes de confirmar.", "Error", JOptionPane.ERROR_MESSAGE);
                    return; 
                }

                //Veemos que mesa quedo seleccionada
                int indiceMesaSeleccionada = comboMesas.getSelectedIndex();
                
                // Con ese índice, vamos a la lista de la cafetería a buscar el objeto Mesa real
                Modelo.Mesa mesaElegida = miCafeteria.getMesas().get(indiceMesaSeleccionada);

                //Validamos que la mesa este libre
                if (mesaElegida.isEstaOcupada()) {
                    JOptionPane.showMessageDialog(null, "La mesa elegida ya está ocupada. Elegí otra.", "Mesa Ocupada", JOptionPane.WARNING_MESSAGE);
                    return; 
                }

                //
                int numeroPedido = (int) (Math.random() * 1000) + 1; //Usamos un numero de pedido al azar con random
                String fechaHoy = "20/06/2026"; //Hardcodeamos la fecha
                Logica.Pedido nuevoPedido = new Logica.Pedido(numeroPedido, fechaHoy, mesaElegida.getNumeroMesa());   //Le paso el numero mesa tambien.

                // Traspasamos los productos del carrito temporal al pedido definitivo del sistema
                for (Modelo.Producto producto : carrito) {
                    nuevoPedido.agregarProducto(producto);
                }

                //Cambiamos el estado de la Mesa a ocupada y guardamos el pedido en el sistema central
                mesaElegida.ocupar();
                miCafeteria.guardarPedido(nuevoPedido); // Guarda el pedido en la lista global de Cafeteria

                //Enviamos el mensaje.
                String mensajeExito = "¡Pedido #" + nuevoPedido.getNumero() + " Confirmado con Éxito!\n"
                    + "Mesa: " + mesaElegida.getNumeroMesa() + "\n"
                    + "Total a pagar: $" + nuevoPedido.calcularTotal(); // Mediante el Polimorfismo, al llamar a producto.calcularPrecio(), se ejecuta de forma
                     // automática la fórmula correspondiente
                
                JOptionPane.showMessageDialog(null, mensajeExito, "Compra Exitosa", JOptionPane.INFORMATION_MESSAGE);

                //Limpiamos las variables temporales y volvemos al menú principal
                carrito.clear(); 
                txtResumen.setText(""); 
                
                Vista.VentanaMenu menu = new Vista.VentanaMenu(miCafeteria, clienteActivo);
                menu.setVisible(true);
                dispose(); 
            }
        });

        //Boton para volver al Menu
        btnVolver.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
            
                Vista.VentanaMenu menu = new Vista.VentanaMenu(miCafeteria, clienteActivo);
                menu.setVisible(true);
                
                dispose();
            }
        });

        comboProductos.setSelectedItem("Chipá de Queso"); //Obligo a que empiece con chipa de queso
    }       
}
