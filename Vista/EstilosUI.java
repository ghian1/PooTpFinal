package Vista;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 * ARCHIVO NUEVO — Estilos visuales y layout adaptable.
 *
 * Centraliza colores, fuentes y el escalado de la interfaz.
 * Todas las ventanas usan VentanaEscalable en vez de setBounds() sueltos.
 *
 * Ver también: REGISTRO_CAMBIOS.md en la raíz del proyecto.
 */
public class EstilosUI {

    // --- Colores del tema cafetería (se usan en todos los métodos aplicarEstilo*) ---
    public static final Color FONDO = new Color(0xF5, 0xF0, 0xE8);
    public static final Color COLOR_BOTON = new Color(0x6F, 0x4E, 0x37);
    public static final Color COLOR_TITULO = new Color(0x4A, 0x2C, 0x1A);
    public static final Color COLOR_LABEL = new Color(0x3D, 0x3D, 0x3D);
    public static final Color COLOR_SECUNDARIO = new Color(0x6F, 0x4E, 0x37);
    public static final Color FONDO_CAMPO = Color.WHITE;
    public static final Color BORDE_CAMPO = new Color(0xD4, 0xC4, 0xB0);

    private static final int REF_ANCHO = 1366;
    private static final int REF_ALTO = 768;
    // Guarda la última posición/tamaño de ventana (archivo ventana.properties)
    private static final String ARCHIVO_GEOMETRIA = "ventana.properties";

    private static double escalaPantalla = 1.0; // factor según tamaño del monitor
    private static double escalaFuente = 1.0;   // factor según tamaño actual de la ventana
    private static GeometriaVentana geometriaGuardada = null;

    // Indica qué estilo aplicar a cada componente al registrarlo en VentanaEscalable
    public enum Tipo {
        TITULO,
        LABEL,
        CAMPO,
        BOTON,
        BOTON_SECUNDARIO,
        COMBO,
        AREA,
        SOLO_BOUNDS
    }

    private static class GeometriaVentana {
        int x;
        int y;
        int ancho;
        int alto;

        GeometriaVentana(int x, int y, int ancho, int alto) {
            this.x = x;
            this.y = y;
            this.ancho = ancho;
            this.alto = alto;
        }
    }

    /** Llamar una sola vez desde Main antes de abrir ventanas. */
    public static void inicializarEscala() {
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        escalaPantalla = Math.min(pantalla.width / (double) REF_ANCHO, pantalla.height / (double) REF_ALTO);
        escalaPantalla = Math.max(0.75, Math.min(escalaPantalla, 1.4));
        cargarGeometriaDesdeArchivo();
    }

    /**
     * Guarda dónde y qué tamaño tenía la ventana.
     * Se llama al mover/redimensionar y antes de cambiar de pantalla.
     */
    public static void guardarGeometria(JFrame ventana) {
        if (ventana.getWidth() <= 0 || ventana.getHeight() <= 0) {
            return;
        }
        geometriaGuardada = new GeometriaVentana(
                ventana.getX(),
                ventana.getY(),
                ventana.getWidth(),
                ventana.getHeight());
        guardarGeometriaEnArchivo();
    }

    private static void cargarGeometriaDesdeArchivo() {
        File archivo = new File(ARCHIVO_GEOMETRIA);
        if (!archivo.exists()) {
            return;
        }

        int x = -1;
        int y = -1;
        int ancho = -1;
        int alto = -1;

        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                String[] partes = linea.split("=", 2);
                if (partes.length != 2) {
                    continue;
                }
                String clave = partes[0].trim();
                String valor = partes[1].trim();
                switch (clave) {
                    case "x" -> x = Integer.parseInt(valor);
                    case "y" -> y = Integer.parseInt(valor);
                    case "ancho" -> ancho = Integer.parseInt(valor);
                    case "alto" -> alto = Integer.parseInt(valor);
                    default -> { /* ignorar */ }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("No se pudo cargar la geometría de ventana: " + e.getMessage());
            return;
        }

        if (x >= 0 && y >= 0 && ancho > 0 && alto > 0) {
            geometriaGuardada = new GeometriaVentana(x, y, ancho, alto);
        }
    }

    private static void guardarGeometriaEnArchivo() {
        if (geometriaGuardada == null) {
            return;
        }

        try (FileWriter escritor = new FileWriter(ARCHIVO_GEOMETRIA)) {
            escritor.write("x=" + geometriaGuardada.x + System.lineSeparator());
            escritor.write("y=" + geometriaGuardada.y + System.lineSeparator());
            escritor.write("ancho=" + geometriaGuardada.ancho + System.lineSeparator());
            escritor.write("alto=" + geometriaGuardada.alto + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("No se pudo guardar la geometría de ventana: " + e.getMessage());
        }
    }

    private static void aplicarGeometriaGuardada(JFrame ventana, int anchoDefault, int altoDefault) {
        if (geometriaGuardada != null) {
            Rectangle areaVisible = calcularAreaVisible();
            int ancho = Math.max(ventana.getMinimumSize().width, geometriaGuardada.ancho);
            int alto = Math.max(ventana.getMinimumSize().height, geometriaGuardada.alto);
            int x = geometriaGuardada.x;
            int y = geometriaGuardada.y;

            if (x + ancho > areaVisible.x + areaVisible.width) {
                x = areaVisible.x + areaVisible.width - ancho;
            }
            if (y + alto > areaVisible.y + areaVisible.height) {
                y = areaVisible.y + areaVisible.height - alto;
            }
            x = Math.max(areaVisible.x, x);
            y = Math.max(areaVisible.y, y);

            ventana.setBounds(x, y, ancho, alto);
        } else {
            ventana.setSize(anchoDefault, altoDefault);
            ventana.setLocationRelativeTo(null);
        }
    }

    private static Rectangle calcularAreaVisible() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
    }

    public static VentanaEscalable crearVentana(JFrame ventana, int anchoBase, int altoBase) {
        return new VentanaEscalable(ventana, anchoBase, altoBase);
    }

    /**
     * Helper para armar ventanas con escalado automático.
     *
     * Uso típico en cada Vista:
     *   VentanaEscalable layout = EstilosUI.crearVentana(this, 450, 400);
     *   layout.agregar(btnX, x, y, ancho, alto, Tipo.BOTON);
     *   layout.activar();
     *
     * Los números x,y,ancho,alto son el "diseño base"; se escalan solos al redimensionar.
     */
    public static class VentanaEscalable {
        private final JFrame ventana;
        private final int anchoBase;
        private final int altoBase;
        private final List<EntradaLayout> layout = new ArrayList<>();
        private final List<EntradaEstilo> estilosExtra = new ArrayList<>();

        private static class EntradaLayout {
            JComponent componente;
            int x, y, w, h;
            Tipo tipo;
        }

        private static class EntradaEstilo {
            Component componente;
            Tipo tipo;
        }

        public VentanaEscalable(JFrame ventana, int anchoBase, int altoBase) {
            this.ventana = ventana;
            this.anchoBase = anchoBase;
            this.altoBase = altoBase;
        }

        /** Registra componente, lo agrega a la ventana y le aplica estilo al redimensionar. */
        public void agregar(JComponent componente, int x, int y, int w, int h, Tipo tipo) {
            ventana.add(componente);
            EntradaLayout entrada = new EntradaLayout();
            entrada.componente = componente;
            entrada.x = x;
            entrada.y = y;
            entrada.w = w;
            entrada.h = h;
            entrada.tipo = tipo;
            layout.add(entrada);
        }

        /** Para componentes dentro de contenedores (ej. JTextArea dentro de JScrollPane). */
        public void registrarEstilo(Component componente, Tipo tipo) {
            EntradaEstilo entrada = new EntradaEstilo();
            entrada.componente = componente;
            entrada.tipo = tipo;
            estilosExtra.add(entrada);
        }

        /** Activa layout, estilos, tamaño inicial y listeners de resize/movimiento. */
        public void activar() {
            ventana.setLayout(null);
            aplicarEstiloVentana(ventana);

            int anchoInicial = (int) Math.round(anchoBase * escalaPantalla);
            int altoInicial = (int) Math.round(altoBase * escalaPantalla);
            ventana.setMinimumSize(new Dimension(
                    (int) Math.round(anchoBase * 0.8 * escalaPantalla),
                    (int) Math.round(altoBase * 0.8 * escalaPantalla)));

            aplicarGeometriaGuardada(ventana, anchoInicial, altoInicial);

            ComponentAdapter adaptador = new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    redimensionar();
                    guardarGeometria(ventana);
                }

                @Override
                public void componentMoved(ComponentEvent e) {
                    guardarGeometria(ventana);
                }
            };
            ventana.addComponentListener(adaptador);

            ventana.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    guardarGeometria(ventana);
                }
            });

            redimensionar();
        }

        /** Recalcula posición/tamaño/fuente de todos los componentes según el tamaño actual. */
        private void redimensionar() {
            int ancho = ventana.getContentPane().getWidth();
            int alto = ventana.getContentPane().getHeight();
            if (ancho <= 0 || alto <= 0) {
                ancho = ventana.getWidth();
                alto = ventana.getHeight();
            }
            if (ancho <= 0 || alto <= 0) {
                ancho = (int) Math.round(anchoBase * escalaPantalla);
                alto = (int) Math.round(altoBase * escalaPantalla);
            }

            double escalaX = ancho / (double) anchoBase;
            double escalaY = alto / (double) altoBase;
            escalaFuente = Math.min(escalaX, escalaY);

            for (EntradaLayout entrada : layout) {
                entrada.componente.setBounds(
                        (int) Math.round(entrada.x * escalaX),
                        (int) Math.round(entrada.y * escalaY),
                        (int) Math.round(entrada.w * escalaX),
                        (int) Math.round(entrada.h * escalaY));
                aplicarEstiloPorTipo(entrada.componente, entrada.tipo);
            }

            for (EntradaEstilo entrada : estilosExtra) {
                aplicarEstiloPorTipo(entrada.componente, entrada.tipo);
            }
        }
    }

    // --- Métodos de estilo (colores y fuentes). Los llama VentanaEscalable automáticamente ---

    public static void aplicarEstiloVentana(JFrame ventana) {
        ventana.getContentPane().setBackground(FONDO);
    }

    public static void aplicarEstiloTitulo(JLabel lbl) {
        lbl.setFont(fuente(Font.BOLD, 16));
        lbl.setForeground(COLOR_TITULO);
    }

    public static void aplicarEstiloLabel(JLabel lbl) {
        lbl.setFont(fuente(Font.PLAIN, 13));
        lbl.setForeground(COLOR_LABEL);
    }

    public static void aplicarEstiloBoton(JButton btn) {
        btn.setFont(fuente(Font.BOLD, 13));
        btn.setBackground(COLOR_BOTON);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
    }

    public static void aplicarEstiloBotonSecundario(JButton btn) {
        btn.setFont(fuente(Font.PLAIN, 12));
        btn.setForeground(COLOR_SECUNDARIO);
        btn.setBackground(FONDO);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
    }

    public static void aplicarEstiloCampo(JTextField txt) {
        txt.setFont(fuente(Font.PLAIN, 13));
        txt.setBackground(FONDO_CAMPO);
        txt.setBorder(bordeCampo());
    }

    public static void aplicarEstiloCombo(JComboBox<?> combo) {
        combo.setFont(fuente(Font.PLAIN, 13));
        combo.setBackground(FONDO_CAMPO);
    }

    public static void aplicarEstiloArea(JTextArea area) {
        area.setFont(fuente(Font.PLAIN, 13));
        area.setBackground(FONDO_CAMPO);
        area.setBorder(bordeCampo());
    }

    private static void aplicarEstiloPorTipo(Component componente, Tipo tipo) {
        switch (tipo) {
            case TITULO -> aplicarEstiloTitulo((JLabel) componente);
            case LABEL -> aplicarEstiloLabel((JLabel) componente);
            case CAMPO -> aplicarEstiloCampo((JTextField) componente);
            case BOTON -> aplicarEstiloBoton((JButton) componente);
            case BOTON_SECUNDARIO -> aplicarEstiloBotonSecundario((JButton) componente);
            case COMBO -> aplicarEstiloCombo((JComboBox<?>) componente);
            case AREA -> aplicarEstiloArea((JTextArea) componente);
            case SOLO_BOUNDS -> { /* solo posición y tamaño */ }
        }
    }

    private static Font fuente(int estilo, int tamanoBase) {
        int tamano = Math.max(9, (int) Math.round(tamanoBase * escalaFuente));
        return new Font("SansSerif", estilo, tamano);
    }

    private static Border bordeCampo() {
        int padding = Math.max(2, (int) Math.round(4 * escalaFuente));
        int paddingH = Math.max(4, (int) Math.round(6 * escalaFuente));
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE_CAMPO),
                BorderFactory.createEmptyBorder(padding, paddingH, padding, paddingH));
    }
}
