# Catálogo de Elementos de Interfaz de Usuario

Aplicación móvil que funciona como un **catálogo interactivo de componentes de interfaz**. La misma aplicación se implementó en tres tecnologías (Android con Views y XML, Android con Jetpack Compose y Flutter) para identificar los componentes básicos de una interfaz móvil, sus equivalencias entre plataformas y las diferencias entre los enfoques de construcción de interfaces.

Cada versión tiene una pantalla principal y seis secciones. Cada elemento del catálogo muestra su **nombre**, el **componente** usado para construirlo, una **explicación breve** y una **demostración interactiva**.

## Datos de identificación

| Campo | Valor |
|---|---|
| Nombre completo | Javier de Jesús Gamez Rosas |
| Número de boleta | 2022630007 |
| Grupo | 7CV4 |
| Materia | Aplicaciones Móviles |

## Tecnologías utilizadas

| Versión | Carpeta | Lenguaje | Tecnología de interfaz | Herramientas |
|---|---|---|---|---|
| Views y XML | [`android-views/`](android-views) | Kotlin 2.3 | Layouts XML, Material Components 1.13, Navigation Component (Fragments), RecyclerView, ConstraintLayout | AGP 9.3, Gradle 9.5, compileSdk 36 |
| Jetpack Compose | [`android-compose/`](android-compose) | Kotlin 2.3 | Funciones composable, Material 3 (Compose BOM 2026.06.01), Navigation Compose | AGP 9.3, Gradle 9.5, compileSdk 36 |
| Flutter | [`flutter/`](flutter) | Dart 3.13 | Widgets de Material 3, rutas con nombre, `ChangeNotifier` | Flutter 3.47 |

Las tres versiones comparten la misma paleta de colores (Material 3 con semilla azul `#1E5AA8`), la misma estructura de pantallas y los mismos textos, para que la comparación sea directa. Las dos versiones de Android usan [Coil](https://coil-kt.github.io/coil/) para cargar la imagen desde internet. Flutter no necesita bibliotecas externas.

## Estructura del repositorio

```
android-views/     Versión con Views y XML (Kotlin + layouts XML)
android-compose/   Versión con Jetpack Compose (Kotlin + funciones composable)
flutter/           Versión con Flutter (Dart)
docs/              Capturas de pantalla de cada versión
apks/              APK de release de las tres versiones
README.md          Este documento
```

## Funcionalidades comunes

- **Navegación:** menú lateral con la pantalla principal y las seis secciones, más tarjetas de acceso en la pantalla de inicio. Cada sección tiene en la barra superior un botón para volver al inicio, y el botón de regresar del sistema también lleva al inicio.
  - Views: `DrawerLayout` + `NavigationView` + Navigation Component (un `Fragment` por sección).
  - Compose: `ModalNavigationDrawer` + `NavHost` (un destino composable por sección).
  - Flutter: `Drawer` (`NavigationDrawer`) + rutas con nombre de `MaterialApp`.
- **Tema claro y oscuro:** las tres versiones siguen el modo del sistema (`Theme.Material3.DayNight`, `isSystemInDarkTheme()` y `ThemeMode.system`).
- **Idioma:** todos los textos están en español. Además, se fuerza la configuración regional `es-MX` para que los componentes del sistema (selectores de fecha y hora, botones de diálogos) también aparezcan en español.
- **Conexión entre secciones** (dos funcionalidades):
  1. **Sección 1 → Sección 4:** el título y la descripción capturados en la Sección 1 se agregan al inicio de la lista vertical de la Sección 4, marcados como "Nuevo". El snackbar de confirmación tiene la acción "Ver", que abre directamente la Sección 4.
  2. **Sección 3 → Sección 5:** el grupo de botones de opción de la Sección 3 define el tamaño (pequeño, mediano o grande) de los textos de ejemplo de la Sección 5.

  El estado compartido vive en un `ViewModel` con `LiveData` (Views), en un `ViewModel` con estado de Compose (Compose) y en un `ChangeNotifier` expuesto con `InheritedNotifier` (Flutter).

## Compilación y ejecución

Requisitos generales: Android SDK con la plataforma 36, un dispositivo o emulador con Android 8.0 (API 26) o superior y JDK 17 o posterior (el que incluye Android Studio funciona).

### Android con Views y XML

```bash
cd android-views
./gradlew assembleDebug          # APK de depuración: app/build/outputs/apk/debug/
./gradlew installDebug           # Instala en el dispositivo conectado
./gradlew assembleRelease        # APK de release (firmado con la llave de depuración)
```

También se puede abrir la carpeta `android-views` en Android Studio y ejecutar la configuración `app`.

### Android con Jetpack Compose

```bash
cd android-compose
./gradlew assembleDebug
./gradlew installDebug
./gradlew assembleRelease
```

### Flutter

```bash
cd flutter
flutter pub get
flutter run                      # Ejecuta en el dispositivo o emulador conectado
flutter build apk --release      # APK: build/app/outputs/flutter-apk/app-release.apk
flutter test                     # Pruebas de widgets y del estado compartido
```

### APK listos para instalar

La carpeta [`apks/`](apks) contiene los APK de release de las tres versiones. Cada una tiene un identificador de aplicación distinto, así que se pueden instalar las tres a la vez en el mismo dispositivo.

| Versión | Archivo | Tamaño aproximado |
|---|---|---|
| Views y XML | [`catalogo-ui-views.apk`](apks/catalogo-ui-views.apk) | 2.5 MB |
| Jetpack Compose | [`catalogo-ui-compose.apk`](apks/catalogo-ui-compose.apk) | 2.2 MB |
| Flutter | [`catalogo-ui-flutter.apk`](apks/catalogo-ui-flutter.apk) | 55 MB (universal: incluye el motor para arm, arm64 y x86_64) |

## Tabla de equivalencias

Componente principal usado para cada elemento del catálogo en cada tecnología. La columna "Notas" explica cómo se resolvió cuando no hay un equivalente directo.

### Sección 1: Entrada de texto

| Elemento | Views / XML | Jetpack Compose | Flutter | Notas |
|---|---|---|---|---|
| Campo de texto simple | `TextInputLayout` + `TextInputEditText` | `OutlinedTextField` / `TextField` | `TextField` + `InputDecoration` | — |
| Campo con validación y error | `TextInputLayout.error` / `helperText` | `OutlinedTextField(isError, supportingText)` | `InputDecoration(errorText, helperText)` | — |
| Contraseña mostrar/ocultar | `TextInputLayout(endIconMode="password_toggle")` | `PasswordVisualTransformation` + `IconButton` | `TextField(obscureText)` + `IconButton` | Views lo trae integrado; en Compose y Flutter el botón del ojo se programa a mano. |
| Teclado numérico, correo y teléfono | `android:inputType` (`number`, `textEmailAddress`, `phone`) | `KeyboardOptions(keyboardType)` | `TextField(keyboardType)` + `inputFormatters` | — |
| Campo multilínea | `inputType="textMultiLine"` + `counterEnabled` | `OutlinedTextField(minLines, maxLines)` | `TextField(minLines, maxLines, maxLength)` | — |
| Sugerencias automáticas | `MaterialAutoCompleteTextView` + `ArrayAdapter` | `ExposedDropdownMenuBox` (editable) | `Autocomplete<String>` | — |
| Barra de búsqueda | `androidx.appcompat.widget.SearchView` | `DockedSearchBar` | `SearchAnchor` + `SearchBar` | El `SearchBar` de Material para Views necesita un `CoordinatorLayout` y abre una vista a pantalla completa, por lo que dentro de una tarjeta se usó el `SearchView` de AppCompat y los resultados se muestran debajo. |

### Sección 2: Botones y acciones

| Elemento | Views / XML | Jetpack Compose | Flutter | Notas |
|---|---|---|---|---|
| Botón relleno | `MaterialButton` | `Button` | `FilledButton` | — |
| Botón con contorno | `MaterialButton` (estilo `OutlinedButton`) | `OutlinedButton` | `OutlinedButton` | — |
| Botón de solo texto | `MaterialButton` (estilo `TextButton`) | `TextButton` | `TextButton` | — |
| Botón de solo ícono | `MaterialButton` (estilo `IconButton` / `IconButton.Filled`) | `IconButton` / `FilledIconButton` | `IconButton` / `IconButton.filled` | — |
| Botón con ícono y texto | `MaterialButton` + `app:icon` | `Button { Icon(); Text() }` | `FilledButton.icon` | — |
| Botón de acción flotante | `FloatingActionButton` | `FloatingActionButton` | `FloatingActionButton` | — |
| FAB extendido | `ExtendedFloatingActionButton` (`extend()` / `shrink()`) | `ExtendedFloatingActionButton(expanded)` | `FloatingActionButton.extended(isExtended)` | — |
| Selector segmentado | `MaterialButtonToggleGroup` | `SingleChoiceSegmentedButtonRow` + `SegmentedButton` | `SegmentedButton` | — |
| Botón de alternancia | `MaterialButton(checkable)` | `IconToggleButton` | `IconButton(isSelected, selectedIcon)` | — |
| Botón deshabilitado | `android:enabled="false"` | `Button(enabled = false)` | `onPressed: null` | — |
| Botón en estado de carga | `IndeterminateDrawable` como ícono del botón | `CircularProgressIndicator` dentro del `Button` | `CircularProgressIndicator` como ícono | Ninguna tecnología tiene un botón "cargando" propio: se compone un indicador de progreso dentro del botón. |

### Sección 3: Elementos de selección

| Elemento | Views / XML | Jetpack Compose | Flutter | Notas |
|---|---|---|---|---|
| Casilla de verificación | `MaterialCheckBox` | `Checkbox` | `Checkbox` / `CheckboxListTile` | — |
| Casilla con estado indeterminado | `MaterialCheckBox.checkedState = STATE_INDETERMINATE` | `TriStateCheckbox` + `ToggleableState` | `Checkbox(tristate: true, value: null)` | El `CheckBox` clásico de Android no tiene tercer estado; se usó `MaterialCheckBox`, que lo agrega. |
| Botones de opción | `RadioGroup` + `MaterialRadioButton` | `RadioButton` + `Modifier.selectableGroup()` | `RadioGroup` + `RadioListTile` | En Compose la exclusión mutua se maneja con el estado: no existe un contenedor "grupo". |
| Interruptor | `MaterialSwitch` | `Switch` | `Switch` / `SwitchListTile` | — |
| Deslizador de valor único | `Slider` | `Slider` | `Slider` | — |
| Deslizador de rango | `RangeSlider` | `RangeSlider` | `RangeSlider` | — |
| Lista desplegable | `TextInputLayout` (ExposedDropdownMenu) + `MaterialAutoCompleteTextView` | `ExposedDropdownMenuBox` (solo lectura) | `DropdownMenu<String>` | En Views también existe `Spinner`, pero el menú expuesto de Material es el recomendado actualmente. |
| Selector de fecha | `MaterialDatePicker` | `DatePickerDialog` + `DatePicker` | `showDatePicker` | — |
| Selector de hora | `MaterialTimePicker` | `TimePicker` dentro de un `AlertDialog` | `showTimePicker` | En Compose, `TimePicker` es un componente en línea; para mostrarlo como diálogo se colocó dentro de un `AlertDialog`. |
| Chips de filtro | `ChipGroup` + `Chip` (estilo `Filter`) | `FilterChip` + `FlowRow` | `FilterChip` + `Wrap` | — |

### Sección 4: Listas y colecciones

| Elemento | Views / XML | Jetpack Compose | Flutter | Notas |
|---|---|---|---|---|
| Lista vertical (16 elementos) | `RecyclerView` + `ListAdapter` + `DiffUtil` | `LazyColumn` | `ListView.separated` | — |
| Cuadrícula | `RecyclerView` + `GridLayoutManager` | `LazyVerticalGrid` | `GridView.count` | — |
| Lista con encabezados y dos tipos de elemento | `RecyclerView` + `getItemViewType` (3 tipos de fila) | `LazyColumn` + `stickyHeader` | `CustomScrollView` + `SliverMainAxisGroup` + `PinnedHeaderSliver` | Los encabezados fijos no existen de forma nativa en `RecyclerView` (requieren un `ItemDecoration` propio), así que en Views los encabezados se desplazan con la lista. |
| Selección que abre el detalle | `OnClickListener` + `NavController.navigate` con argumento | `Modifier.clickable` + `NavController.navigate("detalle/{id}")` | `onTap` + `Navigator.pushNamed(arguments)` | — |
| Deslizar para eliminar | `ItemTouchHelper.SimpleCallback` | `SwipeToDismissBox` | `Dismissible` | En Views el fondo rojo y la papelera se dibujan a mano en `onChildDraw`. |
| Actualizar arrastrando | `SwipeRefreshLayout` | `PullToRefreshBox` | `RefreshIndicator` | — |
| Estado vacío | Vistas alternadas con `visibility` | Composición condicional (`if`) | Widget condicional | No es un componente: es un patrón de diseño en las tres tecnologías. |
| Pestañas deslizables | `TabLayout` + `ViewPager2` + `TabLayoutMediator` | `PrimaryTabRow` + `HorizontalPager` | `TabBar` + `TabBarView` | — |

### Sección 5: Información y retroalimentación

| Elemento | Views / XML | Jetpack Compose | Flutter | Notas |
|---|---|---|---|---|
| Textos con estilos | `TextView` + `textAppearance` + `SpannableString` | `Text` + `MaterialTheme.typography` + `AnnotatedString` | `Text` + `TextTheme` + `Text.rich` | — |
| Imagen local | `ImageView` / `ShapeableImageView` | `Image(painterResource)` | `Image.asset` | — |
| Imagen desde URL | `ImageView` + Coil (`load`) | `SubcomposeAsyncImage` (Coil) | `Image.network` | Android no carga imágenes de red por sí solo; se usó la biblioteca Coil. Flutter lo trae integrado. |
| Modos de escalado | `scaleType` (`centerCrop`, `fitCenter`, `fitXY`, `center`) | `ContentScale` (`Crop`, `Fit`, `FillBounds`, `None`) | `BoxFit` (`cover`, `contain`, `fill`, `none`) | — |
| Progreso lineal | `LinearProgressIndicator` | `LinearProgressIndicator` | `LinearProgressIndicator` | — |
| Progreso circular | `CircularProgressIndicator` | `CircularProgressIndicator` | `CircularProgressIndicator` | — |
| Mensaje breve (toast) | `Toast` | `Toast` (API de Android) | `OverlayEntry` (implementación propia) | Compose no tiene un toast propio, pero puede usar el de Android. **Flutter no tiene toast:** se construyó con un `OverlayEntry` que se retira solo a los 2 segundos. |
| Snackbar con acción | `Snackbar.setAction` | `SnackbarHostState.showSnackbar(actionLabel)` | `SnackBar(action: SnackBarAction)` | — |
| Diálogo de confirmación | `MaterialAlertDialogBuilder` | `AlertDialog` | `showDialog` + `AlertDialog` | — |
| Hoja inferior | `BottomSheetDialog` | `ModalBottomSheet` | `showModalBottomSheet` | — |
| Tarjeta | `MaterialCardView` | `ElevatedCard` / `OutlinedCard` | `Card` / `Card.outlined` | — |
| Separador | `MaterialDivider` | `HorizontalDivider` / `VerticalDivider` | `Divider` / `VerticalDivider` | — |
| Distintivo numérico | `BadgeDrawable` + `BadgeUtils` | `BadgedBox` + `Badge` | `Badge` | — |

### Sección 6: Contenedores y estructura

| Elemento | Views / XML | Jetpack Compose | Flutter | Notas |
|---|---|---|---|---|
| Distribución en fila | `LinearLayout` (horizontal) + `gravity` | `Row` + `horizontalArrangement` | `Row` + `MainAxisAlignment` | `LinearLayout` no tiene "espacio entre" como Compose y Flutter; la opción "Repartir" usa pesos iguales. |
| Distribución en columna | `LinearLayout` (vertical) + `gravity` | `Column` + `horizontalAlignment` | `Column` + `CrossAxisAlignment` | — |
| Distribución superpuesta | `FrameLayout` + `layout_gravity` | `Box` + `Modifier.align` | `Stack` + `Align` | — |
| Contenedor con desplazamiento | `NestedScrollView` | `Modifier.verticalScroll` | `SingleChildScrollView` | — |
| Barra superior con acciones | `MaterialToolbar` + menú XML | `TopAppBar` + `DropdownMenu` | `AppBar` + `PopupMenuButton` | — |
| Navegación inferior | `BottomNavigationView` + menú XML | `NavigationBar` | `NavigationBar` | — |
| Menú lateral (navegación de la app) | `DrawerLayout` + `NavigationView` | `ModalNavigationDrawer` | `Drawer` / `NavigationDrawer` | — |
| Restricciones / pesos | `ConstraintLayout` (cadena con `layout_constraintHorizontal_weight`) | `Row` + `Modifier.weight` | `Row` + `Expanded(flex)` | `ConstraintLayout` es propio de Views; para Compose existe como biblioteca aparte y Flutter no lo tiene, por eso ahí se demostraron pesos proporcionales. |

### Infraestructura

| Concepto | Views / XML | Jetpack Compose | Flutter |
|---|---|---|---|
| Pantalla de sección | `Fragment` + layout XML + ViewBinding | Función `@Composable` (destino de `NavHost`) | `StatelessWidget` (ruta con nombre) |
| Tarjeta de documentación reutilizable | Vista personalizada `ElementoCatalogoView` (extiende `MaterialCardView`) | Composable `ElementoCatalogo` | Widget `ElementoCatalogo` |
| Estado compartido entre secciones | `ViewModel` + `LiveData` (`activityViewModels`) | `ViewModel` + `mutableStateOf` / `mutableStateListOf` | `ChangeNotifier` + `InheritedNotifier` |
| Tema claro/oscuro | `Theme.Material3.DayNight` + `values-night/` | `MaterialTheme` + `isSystemInDarkTheme()` | `theme` + `darkTheme` + `ThemeMode.system` |

## Capturas de pantalla

Todas las capturas se tomaron de los APK de release incluidos en `apks/`, instalados en un Samsung Galaxy S21 Ultra.

### Pantalla principal

| Views / XML | Jetpack Compose | Flutter |
|:---:|:---:|:---:|
| <img src="docs/views/inicio.png" width="230"> | <img src="docs/compose/inicio.png" width="230"> | <img src="docs/flutter/inicio.png" width="230"> |

### Sección 1: Entrada de texto

| Views / XML | Jetpack Compose | Flutter |
|:---:|:---:|:---:|
| <img src="docs/views/seccion1.png" width="230"> | <img src="docs/compose/seccion1.png" width="230"> | <img src="docs/flutter/seccion1.png" width="230"> |

### Sección 2: Botones y acciones

| Views / XML | Jetpack Compose | Flutter |
|:---:|:---:|:---:|
| <img src="docs/views/seccion2.png" width="230"> | <img src="docs/compose/seccion2.png" width="230"> | <img src="docs/flutter/seccion2.png" width="230"> |

### Sección 3: Elementos de selección

| Views / XML | Jetpack Compose | Flutter |
|:---:|:---:|:---:|
| <img src="docs/views/seccion3.png" width="230"> | <img src="docs/compose/seccion3.png" width="230"> | <img src="docs/flutter/seccion3.png" width="230"> |

### Sección 4: Listas y colecciones

| Views / XML | Jetpack Compose | Flutter |
|:---:|:---:|:---:|
| <img src="docs/views/seccion4.png" width="230"> | <img src="docs/compose/seccion4.png" width="230"> | <img src="docs/flutter/seccion4.png" width="230"> |

### Sección 5: Información y retroalimentación

| Views / XML | Jetpack Compose | Flutter |
|:---:|:---:|:---:|
| <img src="docs/views/seccion5.png" width="230"> | <img src="docs/compose/seccion5.png" width="230"> | <img src="docs/flutter/seccion5.png" width="230"> |

### Sección 6: Contenedores y estructura

| Views / XML | Jetpack Compose | Flutter |
|:---:|:---:|:---:|
| <img src="docs/views/seccion6.png" width="230"> | <img src="docs/compose/seccion6.png" width="230"> | <img src="docs/flutter/seccion6.png" width="230"> |

### Tema oscuro

| Views / XML | Jetpack Compose | Flutter |
|:---:|:---:|:---:|
| <img src="docs/views/oscuro-inicio.png" width="230"> | <img src="docs/compose/oscuro-inicio.png" width="230"> | <img src="docs/flutter/oscuro-inicio.png" width="230"> |
| <img src="docs/views/oscuro-seccion3.png" width="230"> | <img src="docs/compose/oscuro-seccion3.png" width="230"> | <img src="docs/flutter/oscuro-seccion3.png" width="230"> |

## Reflexión final

<!-- Revisa y reescribe esta sección con tu propia experiencia antes de entregar. -->

**Tamaño del código.** Para la misma funcionalidad, la versión con Views necesitó unas 1,500 líneas de Kotlin más unas 2,900 de XML repartidas en más de 30 archivos. La versión con Compose quedó en unas 3,000 líneas de Kotlin en 12 archivos y la de Flutter en unas 3,400 líneas de Dart en 11 archivos.

**¿Cuál fue más rápida de construir?** Jetpack Compose. Declarar la interfaz y su estado en el mismo lugar evitó ir y venir entre el layout y el código, y la mayoría de los componentes de Material 3 ya existen con el comportamiento esperado. Flutter fue casi igual de rápido; su ventaja es que la recarga en caliente y un SDK autocontenido (imágenes de red, localización y selectores incluidos) evitan dependencias extra. Views fue la más lenta: cada elemento requiere el layout XML, los ids, el ViewBinding y los listeners por separado, y listas como la de "deslizar para eliminar" requieren un adaptador y dibujar el fondo a mano.

**¿Cuál generó código más legible?** Compose. Cada demostración cabe en una sola función, con su estado (`remember`) al lado de la vista que lo usa. En Flutter el código es igual de declarativo, pero cada demostración con estado necesita dos clases (`StatefulWidget` y `State`) y el anidamiento de widgets crece rápido. En Views la estructura visual es fácil de leer en el XML, pero el comportamiento queda separado en el Fragment y hay que seguir los ids entre archivos.

**Dificultades encontradas.**
- *Views:* conectar el `DrawerLayout` con Navigation Component y la barra superior, manejar los márgenes del sistema con edge-to-edge (la barra inferior de ejemplo agregaba un relleno inesperado), la falta de encabezados fijos en `RecyclerView` y la casilla de tres estados, que requiere `MaterialCheckBox`.
- *Compose:* algunas APIs de Material 3 siguen siendo experimentales (`@OptIn`), cambian de nombre entre versiones (`ExposedDropdownMenuAnchorType`) y el `TimePicker` tuvo que envolverse en un `AlertDialog`. Además, las listas `Lazy*` dentro de otra lista necesitan una altura fija.
- *Flutter:* no existe un toast, así que hubo que construirlo con un `Overlay`. También cambiaron APIs recientes (`RadioGroup`, `persist` en `SnackBar`), y para que las demostraciones conserven su estado al desplazarse se usó `SingleChildScrollView` en lugar de `ListView`, que destruye los widgets que salen de la pantalla.

**¿Con cuál preferiría trabajar?** Para una app solo de Android, con Jetpack Compose, por la velocidad de desarrollo y la legibilidad. Si la app tuviera que llegar también a iOS, Flutter, porque ofrece una experiencia de desarrollo muy parecida y un solo código para ambas plataformas. Views sigue siendo importante para mantener proyectos existentes, pero no lo elegiría para uno nuevo.

## Referencias

- Coil Contributors. (2025). *Coil: Image loading for Android and Compose Multiplatform* [Documentación]. https://coil-kt.github.io/coil/
- Flutter. (s.f.). *Material components widgets*. Flutter Documentation. Recuperado el 24 de septiembre de 2026, de https://docs.flutter.dev/ui/widgets/material
- Flutter. (s.f.). *Internationalizing Flutter apps*. Flutter Documentation. Recuperado el 24 de septiembre de 2026, de https://docs.flutter.dev/ui/accessibility-and-internationalization/internationalization
- Flutter. (s.f.). *Simple app state management*. Flutter Documentation. Recuperado el 24 de septiembre de 2026, de https://docs.flutter.dev/data-and-backend/state-mgmt/simple
- Google. (s.f.). *Material Design 3*. Recuperado el 24 de septiembre de 2026, de https://m3.material.io/
- Google. (s.f.). *Material Components for Android*. GitHub. Recuperado el 24 de septiembre de 2026, de https://github.com/material-components/material-components-android
- Google. (s.f.). *Compose layout basics*. Android Developers. Recuperado el 24 de septiembre de 2026, de https://developer.android.com/develop/ui/compose/layouts/basics
- Google. (s.f.). *Material Design 3 in Compose*. Android Developers. Recuperado el 24 de septiembre de 2026, de https://developer.android.com/develop/ui/compose/designsystems/material3
- Google. (s.f.). *Navigation with Compose*. Android Developers. Recuperado el 24 de septiembre de 2026, de https://developer.android.com/develop/ui/compose/navigation
- Google. (s.f.). *Create dynamic lists with RecyclerView*. Android Developers. Recuperado el 24 de septiembre de 2026, de https://developer.android.com/develop/ui/views/layout/recyclerview
- Google. (s.f.). *Build a responsive UI with ConstraintLayout*. Android Developers. Recuperado el 24 de septiembre de 2026, de https://developer.android.com/develop/ui/views/layout/constraint-layout
- Google. (s.f.). *Get started with the Navigation component*. Android Developers. Recuperado el 24 de septiembre de 2026, de https://developer.android.com/guide/navigation/get-started
- Google. (s.f.). *Display content edge-to-edge in views*. Android Developers. Recuperado el 24 de septiembre de 2026, de https://developer.android.com/develop/ui/views/layout/edge-to-edge
