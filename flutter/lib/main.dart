import 'package:flutter/material.dart';
import 'package:flutter_localizations/flutter_localizations.dart';

import 'estado.dart';
import 'pantallas/inicio.dart';
import 'pantallas/seccion1_entrada_texto.dart';
import 'secciones.dart';

void main() {
  runApp(CatalogoApp(estado: CatalogoEstado()));
}

const _colorSemilla = Color(0xFF1E5AA8);

ThemeData _tema(Brightness brillo) {
  return ThemeData(
    colorScheme: ColorScheme.fromSeed(seedColor: _colorSemilla, brightness: brillo),
    useMaterial3: true,
    // Activa el diseño Material 3 actual de indicadores y deslizadores.
    // ignore: deprecated_member_use
    progressIndicatorTheme: const ProgressIndicatorThemeData(year2023: false),
    // ignore: deprecated_member_use
    sliderTheme: const SliderThemeData(year2023: false),
  );
}

class CatalogoApp extends StatelessWidget {
  const CatalogoApp({super.key, required this.estado});

  final CatalogoEstado estado;

  @override
  Widget build(BuildContext context) {
    return EstadoScope(
      estado: estado,
      child: MaterialApp(
        title: 'Catálogo UI Flutter',
        debugShowCheckedModeBanner: false,
        // Tema claro y oscuro: se elige automáticamente según el modo del sistema.
        theme: _tema(Brightness.light),
        darkTheme: _tema(Brightness.dark),
        themeMode: ThemeMode.system,
        // Idioma español para los textos propios de Material (fechas, botones de diálogos…).
        locale: const Locale('es', 'MX'),
        supportedLocales: const [Locale('es', 'MX'), Locale('es')],
        localizationsDelegates: GlobalMaterialLocalizations.delegates,
        initialRoute: rutaInicio,
        routes: {
          rutaInicio: (_) => const PantallaInicio(),
          for (final s in Seccion.values) s.ruta: (_) => _pantallaDe(s),
        },
      ),
    );
  }
}

Widget _pantallaDe(Seccion seccion) {
  return switch (seccion) {
    Seccion.entradaTexto => const Seccion1EntradaTexto(),
    _ => _EnConstruccion(seccion: seccion),
  };
}

class _EnConstruccion extends StatelessWidget {
  const _EnConstruccion({required this.seccion});

  final Seccion seccion;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text(seccion.titulo)),
      body: const Center(child: Text('Sección en construcción')),
    );
  }
}
