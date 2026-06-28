package Logica;

import Modelo.Cliente;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ARCHIVO NUEVO — Persistencia de clientes en CSV.
 *
 * Guarda y carga los usuarios registrados para que no se pierdan al cerrar la app.
 * Formato de clientes.csv: nombre,email (una línea por cliente).
 *
 * Lo usa:
 * - Main.java → cargarClientes() al iniciar
 * - VentanaPrincipal.java → guardarCliente() al registrarse
 */
public class GestorClientesCSV {

    // Se crea solo en la carpeta del proyecto cuando alguien se registra
    private static final String ARCHIVO_CLIENTES = "clientes.csv";

    /**
     * Lee clientes.csv y devuelve una lista de Cliente.
     * Si el archivo no existe (primera vez), devuelve lista vacía.
     */
    public static List<Cliente> cargarClientes() {
        List<Cliente> clientes = new ArrayList<>();
        File archivo = new File(ARCHIVO_CLIENTES);

        if (!archivo.exists()) {
            return clientes;
        }

        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) {
                    continue;
                }

                // split(",", 2) separa solo en la primera coma (por si el nombre tiene coma)
                String[] partes = linea.split(",", 2);
                if (partes.length == 2) {
                    clientes.add(new Cliente(partes[0].trim(), partes[1].trim()));
                }
            }
        } catch (IOException e) {
            System.out.println("No se pudieron cargar los clientes: " + e.getMessage());
        }

        return clientes;
    }

    /**
     * Agrega un cliente nuevo al final del CSV.
     * Se llama después de registrarCliente() en la cafetería.
     */
    public static void guardarCliente(Cliente cliente) {
        try (FileWriter escritor = new FileWriter(ARCHIVO_CLIENTES, true)) {
            escritor.write(cliente.getNombre() + "," + cliente.getEmail() + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("No se pudo guardar el cliente: " + e.getMessage());
        }
    }
}
