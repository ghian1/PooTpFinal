//JLabel cartel fijo. Solo muestra info. lbl para acordarnos que es un texto
//JTextField casillero en blanco para que el usuario escriba. Si van con txt es que tienen texto dentro
//Jbutton boton que se puede presionar. btn para acordarnos que es un boton.
//SetBounds para posicionar los ejes(x, y, ancho, alto)
//JOption pane sirve para carteles flotantes
package Vista;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener; //Sensores
import java.awt.event.ActionEvent; //Sensores

import Logica.Cafeteria;
import Modelo.Cliente;

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
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Si cerras la ventana, se termina el programa.
        setLocationRelativeTo(null); // Centra la ventana automáticamente en tu monitor
        
        //Desactivamos el acomodador autmatico(Layout null). Hacemos esto para poder posicionar cada componente con coordenadas fijas (X, Y)
        setLayout(null);

        //Titulo (Con tus coordenadas que quedaron perfectas)
        JLabel lblTitulo = new JLabel("REGISTRAR CLIENTE NUEVO");
        lblTitulo.setBounds(155, 30, 310, 25);
        add(lblTitulo);
        
        //Cartel de nombre
        lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(60, 90, 80, 25);
        add(lblNombre);
    
        //Caja para escribir el nombre
        txtNombre = new JTextField(); // Caja blanca para escribir
        txtNombre.setBounds(130, 90, 220, 25);
        add(txtNombre);
    
        //Cartel Email
        lblEmail = new JLabel("Email:");
        lblEmail.setBounds(60, 140, 80, 25);
        add(lblEmail);

        //Caja para escribir el Email
        txtEmail = new JTextField(); 
        txtEmail.setBounds(130, 140, 220, 25);
        add(txtEmail);
        
        // Botón de Acción para registrar
        btnRegistrar = new JButton("Registrar Cliente");
        btnRegistrar.setBounds(130, 200, 220, 35);
        add(btnRegistrar);
        
        // Botón para saltar al Login
        JButton btnIrALogin = new JButton("¿Ya tenés cuenta? Iniciá Sesión");
        btnIrALogin.setBounds(115, 270, 250, 30);
        btnIrALogin.setBorderPainted(false); 
        add(btnIrALogin);

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
                
                VentanaLogin pantallaLogin = new VentanaLogin(miCafeteria);
                pantallaLogin.setVisible(true); // La mostramos
                dispose(); 
            }
        });
    }
}