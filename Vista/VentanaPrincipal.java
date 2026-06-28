//JLabel cartel fijo. Solo muestra info. lbl para acordarnos que es un texto
//JTextField casillero en blanco para que el usuario escriba. Si van con txt es que tienen texto dentro
//Jbutton boton que se puede presionar. btn para acordarnos que es un boton.
//SetBounds para posicionar los ejes(x, y, ancho, alto)
//JOption pane sirve para carteles flotantes
//
// CAMBIOS RECIENTES EN ESTA VENTANA:
// - VentanaEscalable reemplaza setSize/setBounds (ver bloque LAYOUT ADAPTABLE)
// - GestorClientesCSV guarda el registro en clientes.csv
// - guardarGeometria() antes de ir al Login (misma posición/tamaño de ventana)
package Vista;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener; //Sensores
import java.awt.event.ActionEvent; //Sensores

import Logica.Cafeteria;
import Logica.GestorClientesCSV;
import Modelo.Cliente;

import Vista.EstilosUI.Tipo;
import Vista.EstilosUI.VentanaEscalable;

public class VentanaPrincipal extends JFrame {
    // Variables globales de la ventana (Solo las que usamos)
    private Cafeteria miCafeteria;
    private JLabel lblNombre;
    private JLabel lblEmail;
    private JTextField txtNombre;
    private JTextField txtEmail;
    private JButton btnRegistrar;

    // El constructor ahora recibe la cafetería compartida desde el Main
    public VentanaPrincipal(Cafeteria miCafeteria) {
        this.miCafeteria = miCafeteria;
        
        //Config de la ventana
        setTitle("Cafetería Diomande - Sistema de Gestión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Si cerras la ventana, se termina el programa.

        //Titulo (Con tus coordenadas que quedaron perfectas)
        JLabel lblTitulo = new JLabel("REGISTRAR CLIENTE NUEVO");
        
        //Cartel de nombre
        lblNombre = new JLabel("Nombre:");
    
        //Caja para escribir el nombre
        txtNombre = new JTextField(); // Caja blanca para escribir
    
        //Cartel Email
        lblEmail = new JLabel("Email:");

        //Caja para escribir el Email
        txtEmail = new JTextField(); 
        
        // Botón de Acción para registrar
        btnRegistrar = new JButton("Registrar Cliente");
        
        // Botón para saltar al Login
        JButton btnIrALogin = new JButton("¿Ya tenés cuenta? Iniciá Sesión");

        // --- LAYOUT ADAPTABLE: reemplaza setSize/setBounds + aplicarEstilo sueltos ---
        // 450x400 = tamaño de diseño base. EstilosUI escala según pantalla y al redimensionar.
        VentanaEscalable layout = EstilosUI.crearVentana(this, 450, 400);
        layout.agregar(lblTitulo, 155, 30, 310, 25, Tipo.TITULO);
        layout.agregar(lblNombre, 60, 90, 80, 25, Tipo.LABEL);
        layout.agregar(txtNombre, 130, 90, 220, 25, Tipo.CAMPO);
        layout.agregar(lblEmail, 60, 140, 80, 25, Tipo.LABEL);
        layout.agregar(txtEmail, 130, 140, 220, 25, Tipo.CAMPO);
        layout.agregar(btnRegistrar, 130, 200, 220, 35, Tipo.BOTON);
        layout.agregar(btnIrALogin, 115, 270, 250, 30, Tipo.BOTON_SECUNDARIO);
        layout.activar();

        //Registrar cliente
        
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { //Esto solo se ejecuta cuando hacemos el clic
                String nombreTipeado = txtNombre.getText(); //Guardamos lo que ingreso el usuario
                String emailTipeado = txtEmail.getText(); //Guardamos lo que ingreso el usuario

                //Validacion de campos vacíos
                if (nombreTipeado.isEmpty() || emailTipeado.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, completa todos los campos.");
                    return; 
                }
                
                // Creamos el cliente con los datos tipeados
                Cliente nuevoCliente = new Cliente(nombreTipeado, emailTipeado);
                miCafeteria.registrarCliente(nuevoCliente);
                // --- PERSISTENCIA: guardamos también en clientes.csv para no perder el registro ---
                GestorClientesCSV.guardarCliente(nuevoCliente);

                JOptionPane.showMessageDialog(null, "¡Cliente " + nombreTipeado + " registrado con éxito!");

                // Limpiamos las cajas de texto para el próximo registro
                txtNombre.setText("");
                txtEmail.setText(""); 
            }
        }); 

        // Puente hacia el Login
        
        btnIrALogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                // Guardamos tamaño/posición antes de abrir la otra pantalla
                EstilosUI.guardarGeometria(VentanaPrincipal.this);
                VentanaLogin pantallaLogin = new VentanaLogin(miCafeteria);
                pantallaLogin.setVisible(true); // La mostramos
                dispose(); 
            }
        });
    }
}
