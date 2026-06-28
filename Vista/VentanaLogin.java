package Vista;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import Logica.Cafeteria;
import Modelo.Cliente;

import Vista.EstilosUI.Tipo;
import Vista.EstilosUI.VentanaEscalable;

public class VentanaLogin extends JFrame {
    private Cafeteria miCafeteria;     // Guardo en miCafeteria los datos de Cafeteria, ya que ahi registro clientes(Sirve para el Login).
    private JTextField txtEmailLogin;  // Caja de texto para el Email
    private JButton btnIngresar;       // Boton para entrar
    private JButton btnVolverRegistro; //Boton para regresar

    //Constructor
    public VentanaLogin(Cafeteria miCafeteria) {
        this.miCafeteria = miCafeteria;

        setTitle("Sistema de Cafetería - Iniciar Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Si se cierra la cruz, muere el programa

        //Cartel de Email
        JLabel lblEmail = new JLabel("Email:");

        //Caja para escribir Email
        txtEmailLogin = new JTextField();

        //Boton para ingresar
        btnIngresar = new JButton("Ingresar a la Cafetería");

        //Boton para volver
        btnVolverRegistro = new JButton("¿No tenés cuenta? Registrate acá");

        // --- LAYOUT ADAPTABLE (mismo patrón que VentanaPrincipal) ---
        VentanaEscalable layout = EstilosUI.crearVentana(this, 450, 400);
        layout.agregar(lblEmail, 60, 110, 80, 25, Tipo.LABEL);
        layout.agregar(txtEmailLogin, 130, 110, 220, 25, Tipo.CAMPO);
        layout.agregar(btnIngresar, 130, 180, 220, 35, Tipo.BOTON);
        layout.agregar(btnVolverRegistro, 115, 240, 250, 30, Tipo.BOTON_SECUNDARIO);
        layout.activar();

        //Ahora tenemos que configurar los botones de ingresar y de volver. 

        //Ingresar
        btnIngresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            
                String emailTipeado = txtEmailLogin.getText();

                //Validacion
                if (emailTipeado.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingresa tu email.");
                    return; 
                }

                //Usamos buscarCliente para comparar el emailTipeado.
                Cliente clienteLogueado = miCafeteria.buscarCliente(emailTipeado);

                if (clienteLogueado != null) {
                    
                    // Si no es null, significa que lo encontro
                    JOptionPane.showMessageDialog(null, "¡Bienvenido/a de vuelta, " + clienteLogueado.getNombre() + "!");
                    txtEmailLogin.setText(""); // Limpiamos la caja de texto
                    
                    // Guardamos geometría y pasamos al menú
                    EstilosUI.guardarGeometria(VentanaLogin.this);
                    VentanaMenu pantallaMenu = new VentanaMenu(miCafeteria, clienteLogueado);
                    pantallaMenu.setVisible(true); 
                    dispose();
                
                } else {
                    // Si es null, significa que ese mail no existe en el HashMap
                    JOptionPane.showMessageDialog(null, "El email ingresado no está registrado. Por favor, registrate primero.");
                }
            }
        });

        //Volver
        btnVolverRegistro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Guardamos geometría y volvemos al registro
                EstilosUI.guardarGeometria(VentanaLogin.this);
                VentanaPrincipal pantallaRegistro = new VentanaPrincipal(miCafeteria);
                pantallaRegistro.setVisible(true);
                dispose(); // Destruimos este Login para pasar a la otra pantalla
            }
        });
    }
}
