import 'dart:async';

import 'package:flutter/material.dart';

import '../secciones.dart';

/// Abre una sección dejando la pantalla principal debajo, para que
/// el botón de regresar siempre lleve al inicio.
void irASeccion(BuildContext context, String ruta) {
  Navigator.of(context).pushNamedAndRemoveUntil(ruta, (r) => r.isFirst);
}

void irAInicio(BuildContext context) {
  Navigator.of(context).popUntil((r) => r.isFirst);
}

/// Mensaje con acción opcional en la parte inferior de la pantalla.
void mostrarSnackbar(BuildContext context, String mensaje, {String? accion, VoidCallback? alAccion}) {
  final messenger = ScaffoldMessenger.of(context);
  messenger.hideCurrentSnackBar();
  messenger.showSnackBar(SnackBar(
    content: Text(mensaje),
    persist: false,
    action: accion == null ? null : SnackBarAction(label: accion, onPressed: alAccion ?? () {}),
  ));
}

OverlayEntry? _toastActual;

/// Flutter no incluye un "toast" nativo: se construye con un [OverlayEntry]
/// que aparece sobre toda la app y se retira solo después de dos segundos.
void mostrarToast(BuildContext context, String mensaje) {
  _toastActual?.remove();
  final overlay = Overlay.of(context, rootOverlay: true);
  late final OverlayEntry entrada;
  entrada = OverlayEntry(builder: (_) => _Toast(mensaje: mensaje));
  _toastActual = entrada;
  overlay.insert(entrada);
  Timer(const Duration(seconds: 2), () {
    if (_toastActual == entrada) {
      entrada.remove();
      _toastActual = null;
    }
  });
}

class _Toast extends StatelessWidget {
  const _Toast({required this.mensaje});

  final String mensaje;

  @override
  Widget build(BuildContext context) {
    return Positioned(
      left: 24,
      right: 24,
      bottom: 96,
      child: IgnorePointer(
        child: Center(
          child: TweenAnimationBuilder<double>(
            tween: Tween(begin: 0, end: 1),
            duration: const Duration(milliseconds: 200),
            builder: (context, valor, child) => Opacity(opacity: valor, child: child),
            child: Material(
              color: const Color(0xE6323232),
              borderRadius: BorderRadius.circular(24),
              child: Padding(
                padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 12),
                child: Text(mensaje, style: const TextStyle(color: Colors.white, fontSize: 14)),
              ),
            ),
          ),
        ),
      ),
    );
  }
}

/// Menú lateral con la pantalla principal y las seis secciones.
class MenuLateral extends StatelessWidget {
  const MenuLateral({super.key, required this.rutaActual});

  final String rutaActual;

  @override
  Widget build(BuildContext context) {
    final indice = rutaActual == rutaInicio ? 0 : Seccion.values.indexWhere((s) => s.ruta == rutaActual) + 1;
    return NavigationDrawer(
      selectedIndex: indice,
      onDestinationSelected: (i) {
        Navigator.of(context).pop();
        if (i == indice) return;
        if (i == 0) {
          irAInicio(context);
        } else {
          irASeccion(context, Seccion.values[i - 1].ruta);
        }
      },
      children: [
        Padding(
          padding: const EdgeInsets.fromLTRB(28, 24, 16, 16),
          child: Text('Catálogo de UI', style: Theme.of(context).textTheme.titleLarge),
        ),
        const NavigationDrawerDestination(icon: Icon(Icons.home_outlined), selectedIcon: Icon(Icons.home), label: Text('Inicio')),
        const Padding(padding: EdgeInsets.symmetric(horizontal: 28, vertical: 8), child: Divider()),
        for (final s in Seccion.values)
          NavigationDrawerDestination(icon: Icon(s.icono), label: Text('${s.numero}. ${s.titulo}')),
      ],
    );
  }
}

/// Pantalla base de cada sección: barra superior, menú lateral,
/// encabezado y las tarjetas de los elementos.
class PantallaSeccion extends StatelessWidget {
  const PantallaSeccion({super.key, required this.seccion, required this.elementos});

  final Seccion seccion;
  final List<Widget> elementos;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(seccion.titulo, maxLines: 1, overflow: TextOverflow.ellipsis),
        actions: [
          IconButton(
            icon: const Icon(Icons.home),
            tooltip: 'Ir a la pantalla principal',
            onPressed: () => irAInicio(context),
          ),
        ],
      ),
      drawer: MenuLateral(rutaActual: seccion.ruta),
      // Se usa SingleChildScrollView + Column para que todas las demostraciones
      // conserven su estado aunque salgan de la pantalla al desplazarse.
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            EncabezadoSeccion(seccion: seccion),
            for (final e in elementos) ...[const SizedBox(height: 16), e],
            const SizedBox(height: 24),
          ],
        ),
      ),
    );
  }
}

class EncabezadoSeccion extends StatelessWidget {
  const EncabezadoSeccion({super.key, required this.seccion});

  final Seccion seccion;

  @override
  Widget build(BuildContext context) {
    final esquema = Theme.of(context).colorScheme;
    final texto = Theme.of(context).textTheme;
    return Row(
      children: [
        CircleAvatar(
          radius: 24,
          backgroundColor: esquema.primaryContainer,
          child: Icon(seccion.icono, color: esquema.onPrimaryContainer),
        ),
        const SizedBox(width: 16),
        Expanded(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text('Sección ${seccion.numero}', style: texto.labelLarge?.copyWith(color: esquema.primary)),
              Text(seccion.titulo, style: texto.headlineSmall),
              Text(seccion.resumen, style: texto.bodyMedium?.copyWith(color: esquema.onSurfaceVariant)),
            ],
          ),
        ),
      ],
    );
  }
}

/// Tarjeta que documenta un elemento del catálogo: nombre, componente usado,
/// explicación breve y una demostración interactiva.
class ElementoCatalogo extends StatelessWidget {
  const ElementoCatalogo({
    super.key,
    required this.nombre,
    required this.componente,
    required this.descripcion,
    required this.demo,
  });

  final String nombre;
  final String componente;
  final String descripcion;
  final Widget demo;

  @override
  Widget build(BuildContext context) {
    final esquema = Theme.of(context).colorScheme;
    final texto = Theme.of(context).textTheme;
    return Card(
      margin: EdgeInsets.zero,
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(nombre, style: texto.titleMedium?.copyWith(fontWeight: FontWeight.w600)),
            const SizedBox(height: 6),
            Container(
              padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 2),
              decoration: BoxDecoration(
                color: esquema.secondaryContainer,
                borderRadius: BorderRadius.circular(6),
              ),
              child: Text(
                componente,
                style: texto.labelMedium?.copyWith(fontFamily: 'monospace', color: esquema.onSecondaryContainer),
              ),
            ),
            const SizedBox(height: 6),
            Text(descripcion, style: texto.bodyMedium?.copyWith(color: esquema.onSurfaceVariant)),
            const SizedBox(height: 12),
            const Divider(height: 1),
            const SizedBox(height: 12),
            demo,
          ],
        ),
      ),
    );
  }
}

/// Texto que muestra el resultado de interactuar con una demostración.
class TextoResultado extends StatelessWidget {
  const TextoResultado(this.texto, {super.key});

  final String texto;

  @override
  Widget build(BuildContext context) {
    return Text(
      texto,
      style: Theme.of(context).textTheme.bodyMedium?.copyWith(color: Theme.of(context).colorScheme.primary),
    );
  }
}

/// Espacio vertical estándar entre los controles de una demostración.
const espacio = SizedBox(height: 12);
