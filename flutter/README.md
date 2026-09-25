# Catálogo UI: versión Flutter

Versión en Flutter del catálogo interactivo de elementos de interfaz. La descripción completa, la tabla de equivalencias y las capturas están en el [README principal](../README.md).

## Ejecución

```bash
flutter pub get
flutter run
```

## Estructura

```
lib/
  main.dart                 Tema claro/oscuro, localización en español y rutas
  estado.dart               Estado compartido entre secciones (ChangeNotifier)
  secciones.dart            Definición de las seis secciones
  widgets/comunes.dart      Menú lateral, tarjeta de documentación, toast y snackbar
  pantallas/                Pantalla principal, seis secciones y detalle
test/widget_test.dart       Pruebas de la pantalla principal y del estado
```
