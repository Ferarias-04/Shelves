# Shelves 📚

**Shelves** es una aplicación Android para llevar el registro de todos los libros que lees. Es una especie de *Letterboxd* pero para libros: puedes apuntar lo que estás leyendo, lo que ya has terminado y lo que quieres leer, valorarlo y escribir una reseña.

Tiene una parte social sencilla y sin ruido: solo te puede seguir quien conoce tu nombre de usuario, y no hay muro ni publicaciones.

## Autoras

- **Adriana Vallespí**
- **Fernanda Arias**

Proyecto del ciclo de **Desarrollo de Aplicaciones Multiplataforma (DAM)** del Institut TIC de Barcelona.

## Estado del proyecto

Esta versión corresponde a la **Actividad 05 – Interfaz del prototipo**. Contiene únicamente el **diseño de las pantallas en XML**, a partir del prototipo de baja fidelidad de la Actividad 04. La lógica, la navegación entre fragments y los menús se programarán en la Actividad 06.

## Estructura de la app

La app funciona con una activity contenedora y fragments:

- **`MainActivity`**: contiene la cabecera, el contenedor de fragments y la barra de navegación inferior (Inicio · Biblioteca · Perfil).
- **Fragments**: cada pantalla principal se carga dentro de la `MainActivity`.
- **Login y Registro**: activities independientes, porque se muestran antes de entrar en la app.

### Pantallas

| Pantalla | Archivo | Descripción |
|---|---|---|
| Inicio de sesión | `activity_login.xml` | Acceso con nombre de usuario y contraseña |
| Registro | `activity_registre.xml` | Alta de un nuevo usuario |
| Contenedor | `activity_main.xml` | Cabecera, fragments y barra inferior |
| Inicio | `fragment_home.xml` | Racha de lectura, lectura actual, frase del mes y libros más leídos |
| Perfil | `fragment_user.xml` | Datos del usuario, racha, objetivo de lectura y reseñas |
| Menú | `fragment_menu.xml` | Acceso a preferencias, ayuda y contacto |
| Biblioteca | `fragment_biblioteca.xml` | Listas *Leyendo*, *Leídos* y *Quiero leer* con buscador |
| Fila de libro | `item_libro.xml` | Plantilla de cada libro de la lista (`RecyclerView`) |
| Editar libro | `fragment_editar_libro.xml` | Página actual, valoración, reseña y cambio de lista |
| Barra de edición | `barra_edicion.xml` | Mover, añadir y eliminar libros desde la Biblioteca |
| Preferencias | `fragment_preferencias.xml` | Modo oscuro, alto contraste, tamaño del texto, idioma y privacidad |
| Ayuda | `fragment_ayuda.xml` | Explicación de cada apartado de la app |

## Diseño

### Prototipo en Figma

El diseño visual de la app ha sido creado por **Adriana Vallespí** en Figma, a partir del prototipo de baja fidelidad de la Actividad 04. Las pantallas de este proyecto se han maquetado siguiendo ese diseño.

🎨 **[Ver el diseño de Shelves en Figma](https://www.figma.com/design/OnKBkMrcMR1xTcMjwMCeNa/shelves?node-id=0-1&p=f)**

### Recursos de la app

- **Tema propio** basado en Material 3 (`Theme.Shelves`), con **modo claro y oscuro** (`values/colors.xml` y `values-night/colors.xml`).
- **Paleta de colores**:
    - Midnight violet `#3C222D`: texto y bordes
    - Verde `#397367`: color principal (botones, progreso)
    - Icy blue `#B0E0FD`: acentos y fondos
    - Crema `#FFF6E3`: fondo
- **Estilos reutilizables** en `styles.xml`: tarjetas con borde, botones principal, secundario y tipo pastilla, campos de texto y títulos en serif.
- **Drawables** propios: iconos vectoriales, fondos redondeados, logo e ilustraciones.
- **Layouts** utilizados: `ConstraintLayout`, `LinearLayout` (con `weight`), `ScrollView`, `RecyclerView`, `MaterialCardView`, `ChipGroup`, `MaterialSwitch`, `Slider`, `RatingBar`, entre otros.
- **Textos** separados en archivos de strings por pantalla.

## Accesibilidad y usabilidad

- Zonas táctiles de **48dp como mínimo** en botones y controles.
- `contentDescription` en todos los elementos interactivos. Los iconos decorativos se marcan como `importantForAccessibility="no"` para que TalkBack no los lea dos veces.
- Títulos marcados como **encabezados** (`accessibilityHeading`).
- Colores de texto con **contraste suficiente** sobre su fondo, también en modo oscuro.
- Teclados adecuados según el campo (numérico, texto multilínea…).

## Cómo abrir el proyecto

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/Ferarias-04/Shelves.git
   ```
2. Abrir la carpeta `Shelves` en **Android Studio**.
3. Esperar a que sincronice Gradle.
4. Abrir cualquier archivo de `res/layout` y ver el diseño en la pestaña **Split** o **Design**.

**Requisitos:** Android Studio reciente · `minSdk` 24 (Android 7.0) · Kotlin.