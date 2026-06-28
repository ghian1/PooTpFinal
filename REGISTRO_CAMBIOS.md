# Registro de cambios — Cafetería Diomande

Documento para que el equipo vea **qué se modificó**, **por qué** y **cómo explicarlo**.

Cada entrada nueva va **arriba de todo** (la más reciente primero).

### Guía rápida para leer el código nuevo

| Qué buscar | Dónde | Comentario clave en el código |
|------------|-------|-------------------------------|
| Estilos y ventanas adaptables | `Vista/EstilosUI.java` | `ARCHIVO NUEVO`, `VentanaEscalable` |
| Guardar clientes en CSV | `Logica/GestorClientesCSV.java` | `ARCHIVO NUEVO`, `PERSISTENCIA` |
| Carga al iniciar | `Main.java` | `MEJORA VISUAL`, `PERSISTENCIA` |
| Layout en cada pantalla | `Vista/Ventana*.java` | `LAYOUT ADAPTABLE` |
| Guardar posición al cambiar pantalla | `Vista/Ventana*.java` | `guardarGeometria` |

---

## [27/06/2026] — Recordar posición y tamaño de ventana

**Autor:** Mariano  
**Motivo:** Al cambiar de pantalla o reabrir la app, la ventana volvía a un tamaño/posición fijos. Ahora recuerda dónde y cómo la dejaste.

### Archivos tocados

| Archivo | Tipo de cambio |
|---------|----------------|
| `Vista/EstilosUI.java` | Guarda/restaura geometría (x, y, ancho, alto) |
| `Vista/VentanaPrincipal.java` | Guarda geometría antes de navegar |
| `Vista/VentanaLogin.java` | Idem |
| `Vista/VentanaMenu.java` | Idem |
| `Vista/VentanaPedido.java` | Idem |
| `Vista/VentanaPago.java` | Idem |

### Archivo que se crea solo al usar la app

| Archivo | Contenido |
|---------|-----------|
| `ventana.properties` | Última posición y tamaño de la ventana |

### Cómo funciona

1. Al **mover o redimensionar** cualquier ventana → se guarda la geometría en memoria y en `ventana.properties`.
2. Al **cambiar de pantalla** (registro → login → menú, etc.) → la nueva ventana abre con el mismo tamaño y posición.
3. Al **reabrir la app** → se carga `ventana.properties` y se restaura la última geometría.

### Cómo probarlo

1. Abrí la app y mové/redimensioná la ventana donde quieras.
2. Andá a otra pantalla (ej. Login) → debería abrir en el mismo lugar y tamaño.
3. Cerrá la app y volvé a abrirla → debería recordar la última posición.

### Para la defensa

- *"Guardamos x, y, ancho y alto en un archivo de propiedades simple."*
- *"Todas las pantallas comparten la misma geometría para que se sienta como una sola app."*

---

## [27/06/2026] — UI adaptable al tamaño de pantalla

**Autor:** Mariano  
**Motivo:** Las ventanas tenían tamaño y fuentes fijos. Ahora se adaptan al monitor al abrir y también cuando el usuario redimensiona la ventana.

### Archivos tocados

| Archivo | Tipo de cambio |
|---------|----------------|
| `Vista/EstilosUI.java` | Escala de pantalla, fuentes dinámicas, clase `VentanaEscalable` |
| `Main.java` | Llama a `EstilosUI.inicializarEscala()` al iniciar |
| `Vista/VentanaPrincipal.java` | Usa `VentanaEscalable` (base 450×400) |
| `Vista/VentanaLogin.java` | Idem (450×400) |
| `Vista/VentanaMenu.java` | Idem (450×400) |
| `Vista/VentanaPedido.java` | Idem (500×500) |
| `Vista/VentanaPago.java` | Idem (400×300) |

### Cómo funciona

1. **Al iniciar** → se calcula un factor según la resolución del monitor (referencia 1366×768).
2. **Cada ventana** → guarda coordenadas de diseño base y las escala al tamaño actual.
3. **Al redimensionar** → un `ComponentListener` recalcula posición, tamaño y fuente de cada componente.
4. **Centralizado en `EstilosUI.VentanaEscalable`** → las 5 ventanas usan el mismo patrón.

### Ejemplo de uso en una ventana

```java
VentanaEscalable layout = EstilosUI.crearVentana(this, 450, 400);
layout.agregar(btnRegistrar, 130, 200, 220, 35, Tipo.BOTON);
layout.activar();
```

### Qué NO se tocó

- `Modelo/` y `Logica/` sin cambios.
- Los `JOptionPane` no escalan (comportamiento normal de Swing).

### Cómo probarlo

1. Ejecutar `java Main` → la ventana debería verse proporcional a tu pantalla.
2. Arrastrar los bordes de la ventana → botones, campos y textos crecen o achican.
3. Repetir en registro, login, menú, pedido y pago.

### Para la defensa

- *"Calculamos un factor de escala según la resolución del monitor."*
- *"Las coordenadas son de diseño base; al redimensionar se recalculan proporcionalmente."*
- *"La lógica está centralizada en `VentanaEscalable` para no repetir código en 5 ventanas."*

---

## [27/06/2026] — Persistencia de clientes en CSV

**Autor:** Mariano  
**Motivo:** Al cerrar la app se perdían los usuarios registrados. Ahora se guardan en un CSV y se cargan al iniciar, así no hay que registrarse de nuevo.

### Archivos tocados

| Archivo | Tipo de cambio |
|---------|----------------|
| `Logica/GestorClientesCSV.java` | **Nuevo** — lee/escribe `clientes.csv` |
| `Main.java` | Carga clientes del CSV al iniciar |
| `Vista/VentanaPrincipal.java` | Guarda cliente en CSV al registrarse |

### Archivo que se crea solo al usar la app

| Archivo | Contenido |
|---------|-----------|
| `clientes.csv` | Lista de usuarios: `nombre,email` (una línea por cliente) |

### Cómo funciona

1. **Registro** → el cliente se guarda en memoria (HashMap) y se agrega una línea a `clientes.csv`.
2. **Al abrir la app** → `Main` lee el CSV y carga todos los clientes en la cafetería.
3. **Login** → funciona igual que antes; si ya te registraste en una sesión anterior, podés iniciar sesión con tu email sin volver a registrarte.

### Qué NO se tocó

- No se guarda sesión automática: siempre tenés que ir al Login para entrar.
- Pedidos, mesas y productos siguen en memoria (no se persisten).

### Cómo probarlo

```cmd
cd C:\Users\Mariano\Desktop\PooTpFinal-main
javac Main.java Vista\*.java Modelo\*.java Logica\*.java
java Main
```

1. Registrate con nombre y email.
2. Cerrá la app completamente.
3. Volvé a ejecutar `java Main`.
4. Andá a "Iniciá Sesión" e ingresá el mismo email → debería funcionar sin registrarte otra vez.

### Para la defensa

- *"Usamos un CSV para que los clientes registrados persistan entre ejecuciones."*
- *"`GestorClientesCSV` centraliza la lectura y escritura del archivo."*
- *"Al iniciar, `Main` carga el CSV al HashMap de `Cafeteria`."*

---

## [27/06/2026] — Estilos visuales básicos (tema cafetería)

**Autor:** Mariano  
**Motivo:** La app se veía con el estilo gris por defecto de Swing. Se agregó una capa visual simple sin tocar la lógica.

### Archivos tocados

| Archivo | Tipo de cambio |
|---------|----------------|
| `Vista/EstilosUI.java` | **Nuevo** — clase central de estilos |
| `Main.java` | Look & Feel del sistema operativo |
| `Vista/VentanaPrincipal.java` | Aplica estilos |
| `Vista/VentanaLogin.java` | Aplica estilos |
| `Vista/VentanaMenu.java` | Aplica estilos |
| `Vista/VentanaPedido.java` | Aplica estilos |
| `Vista/VentanaPago.java` | Aplica estilos |

### Para la defensa

- *"Los estilos están en `EstilosUI`; Modelo y Logica no conocen colores."*
- *"En `Main` configuramos el Look & Feel nativo de Windows."*

---

## Estructura del proyecto

```
PooTpFinal-main/
├── Main.java
├── clientes.csv          ← se crea al registrar
├── ventana.properties    ← posición/tamaño de la ventana
├── Modelo/
├── Logica/
│   ├── Cafeteria.java
│   ├── Pedido.java
│   └── GestorClientesCSV.java
├── Vista/
└── REGISTRO_CAMBIOS.md
```
