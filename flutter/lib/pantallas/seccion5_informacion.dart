import 'package:flutter/material.dart';

import '../estado.dart';
import '../secciones.dart';
import '../widgets/comunes.dart';

const urlImagen = 'https://picsum.photos/id/1018/800/500';

const _modosEscalado = [
  ('Recortar', BoxFit.cover),
  ('Ajustar', BoxFit.contain),
  ('Estirar', BoxFit.fill),
  ('Original', BoxFit.none),
];

class Seccion5Informacion extends StatelessWidget {
  const Seccion5Informacion({super.key});

  @override
  Widget build(BuildContext context) {
    return const PantallaSeccion(
      seccion: Seccion.informacion,
      elementos: [
        ElementoCatalogo(
          nombre: 'Textos con distintos estilos',
          componente: 'Text + TextTheme + Text.rich(TextSpan)',
          descripcion: 'La tipografía establece jerarquía: títulos grandes, cuerpo legible y '
              'etiquetas pequeñas. El tamaño de estos ejemplos depende de la opción elegida en la Sección 3.',
          demo: _Textos(),
        ),
        ElementoCatalogo(
          nombre: 'Imagen local e imagen desde URL',
          componente: 'Image.asset / Image.network',
          descripcion: 'La primera imagen está incluida en la aplicación y la segunda se descarga '
              'de internet. El modo de escalado define cómo se adapta la imagen a su contenedor.',
          demo: _Imagenes(),
        ),
        ElementoCatalogo(
          nombre: 'Indicadores de progreso',
          componente: 'LinearProgressIndicator / CircularProgressIndicator',
          descripcion: 'Informan que una tarea está en curso. El modo determinado muestra el '
              'porcentaje avanzado; el indeterminado solo indica que se está trabajando.',
          demo: _Progreso(),
        ),
        ElementoCatalogo(
          nombre: 'Toast y Snackbar',
          componente: 'OverlayEntry (toast propio) / SnackBar',
          descripcion: 'El toast es un aviso breve que desaparece solo; Flutter no lo incluye y se '
              'construyó con un Overlay. El snackbar aparece abajo y puede incluir una acción.',
          demo: _ToastSnackbar(),
        ),
        ElementoCatalogo(
          nombre: 'Diálogo de confirmación',
          componente: 'showDialog + AlertDialog',
          descripcion: 'Interrumpe al usuario para confirmar una acción importante o irreversible. '
              'Presenta un título, una explicación y botones para aceptar o cancelar.',
          demo: _Dialogo(),
        ),
        ElementoCatalogo(
          nombre: 'Hoja inferior',
          componente: 'showModalBottomSheet',
          descripcion: 'Panel que se desliza desde la parte inferior con opciones adicionales '
              'relacionadas con el contenido. Se cierra deslizándola hacia abajo o tocando fuera.',
          demo: _HojaInferior(),
        ),
        ElementoCatalogo(
          nombre: 'Tarjeta, separador y distintivo',
          componente: 'Card.outlined / Divider / Badge',
          descripcion: 'La tarjeta agrupa información relacionada, el separador divide contenido '
              'visualmente y el distintivo (badge) muestra un contador sobre un ícono.',
          demo: _TarjetaBadge(),
        ),
      ],
    );
  }
}

class _Textos extends StatefulWidget {
  const _Textos();

  @override
  State<_Textos> createState() => _TextosState();
}

class _TextosState extends State<_Textos> {
  bool _negrita = false, _cursiva = false, _subrayado = false;

  @override
  Widget build(BuildContext context) {
    final estado = EstadoScope.of(context);
    final escala = estado.tamanoTexto.escala;
    final t = Theme.of(context).textTheme;
    final esquema = Theme.of(context).colorScheme;

    TextStyle escalar(TextStyle? estilo) => estilo!.copyWith(fontSize: estilo.fontSize! * escala);

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text('Titular grande', style: escalar(t.headlineMedium)),
        Text('Título de sección', style: escalar(t.titleLarge)),
        Text('Texto de cuerpo para párrafos largos.', style: escalar(t.bodyLarge)),
        Text('ETIQUETA PEQUEÑA', style: escalar(t.labelSmall)),
        const SizedBox(height: 4),
        Text.rich(
          TextSpan(
            style: escalar(t.bodyMedium),
            children: [
              const TextSpan(text: 'Texto con '),
              const TextSpan(text: 'negrita', style: TextStyle(fontWeight: FontWeight.bold)),
              const TextSpan(text: ', '),
              const TextSpan(text: 'cursiva', style: TextStyle(fontStyle: FontStyle.italic)),
              const TextSpan(text: ', '),
              TextSpan(text: 'color', style: TextStyle(color: esquema.primary)),
              const TextSpan(text: ', '),
              const TextSpan(text: 'tachado', style: TextStyle(decoration: TextDecoration.lineThrough)),
              const TextSpan(text: ' y '),
              const TextSpan(text: 'monoespaciado', style: TextStyle(fontFamily: 'monospace')),
              const TextSpan(text: '.'),
            ],
          ),
        ),
        const Divider(height: 24),
        Text('Aplica énfasis a la frase de ejemplo:', style: t.labelLarge),
        const SizedBox(height: 8),
        Wrap(
          spacing: 8,
          children: [
            FilterChip(label: const Text('Negrita'), selected: _negrita, onSelected: (v) => setState(() => _negrita = v)),
            FilterChip(label: const Text('Cursiva'), selected: _cursiva, onSelected: (v) => setState(() => _cursiva = v)),
            FilterChip(
                label: const Text('Subrayado'), selected: _subrayado, onSelected: (v) => setState(() => _subrayado = v)),
          ],
        ),
        const SizedBox(height: 8),
        Text(
          'El diseño es cómo funciona.',
          style: escalar(t.titleMedium).copyWith(
            fontWeight: _negrita ? FontWeight.bold : FontWeight.normal,
            fontStyle: _cursiva ? FontStyle.italic : FontStyle.normal,
            decoration: _subrayado ? TextDecoration.underline : TextDecoration.none,
          ),
        ),
        Row(
          children: [
            Expanded(child: TextoResultado('Tamaño actual: ${estado.tamanoTexto.etiqueta}')),
            TextButton(
              onPressed: () => irASeccion(context, Seccion.seleccion.ruta),
              child: const Text('Cambiar en Sección 3'),
            ),
          ],
        ),
      ],
    );
  }
}

class _Imagenes extends StatefulWidget {
  const _Imagenes();

  @override
  State<_Imagenes> createState() => _ImagenesState();
}

class _ImagenesState extends State<_Imagenes> {
  int _modo = 0;

  @override
  Widget build(BuildContext context) {
    final ajuste = _modosEscalado[_modo].$2;
    final etiqueta = Theme.of(context).textTheme.labelMedium;
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        SizedBox(
          width: double.infinity,
          child: SegmentedButton<int>(
            showSelectedIcon: false,
            segments: [
              for (var i = 0; i < _modosEscalado.length; i++)
                ButtonSegment(value: i, label: Text(_modosEscalado[i].$1, style: etiqueta)),
            ],
            selected: {_modo},
            onSelectionChanged: (s) => setState(() => _modo = s.first),
          ),
        ),
        espacio,
        Row(
          children: [
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  _MarcoImagen(
                    child: Image.asset(
                      'assets/paisaje.png',
                      fit: ajuste,
                      semanticLabel: 'Paisaje de montañas incluido en la app',
                    ),
                  ),
                  Text('Local (asset)', style: etiqueta),
                ],
              ),
            ),
            const SizedBox(width: 8),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  _MarcoImagen(
                    child: Image.network(
                      urlImagen,
                      fit: ajuste,
                      semanticLabel: 'Fotografía descargada de internet',
                      loadingBuilder: (context, hijo, progreso) => progreso == null
                          ? hijo
                          : const Center(child: SizedBox(width: 32, height: 32, child: CircularProgressIndicator())),
                      errorBuilder: (context, error, pila) => const Column(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [Icon(Icons.broken_image), Text('Sin conexión')],
                      ),
                    ),
                  ),
                  Text('Desde URL (red)', style: etiqueta),
                ],
              ),
            ),
          ],
        ),
        espacio,
        TextoResultado('Modo de escalado: ${_modosEscalado[_modo].$1}'),
      ],
    );
  }
}

class _MarcoImagen extends StatelessWidget {
  const _MarcoImagen({required this.child});

  final Widget child;

  @override
  Widget build(BuildContext context) {
    return ClipRRect(
      borderRadius: BorderRadius.circular(12),
      child: Container(
        height: 120,
        width: double.infinity,
        color: Theme.of(context).colorScheme.surfaceContainerHighest,
        child: child,
      ),
    );
  }
}

class _Progreso extends StatefulWidget {
  const _Progreso();

  @override
  State<_Progreso> createState() => _ProgresoState();
}

class _ProgresoState extends State<_Progreso> with SingleTickerProviderStateMixin {
  late final AnimationController _animacion = AnimationController(vsync: this, duration: const Duration(seconds: 3))
    ..addListener(() => setState(() {}))
    ..addStatusListener((estado) {
      if (estado == AnimationStatus.completed && mounted) {
        mostrarSnackbar(context, 'Descarga simulada completada');
      }
    });
  bool _indeterminado = true;

  @override
  void dispose() {
    _animacion.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final valor = _animacion.value;
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text('Determinado: ${(valor * 100).toInt()} %', style: Theme.of(context).textTheme.labelLarge),
        espacio,
        Row(
          children: [
            Expanded(child: LinearProgressIndicator(value: valor)),
            const SizedBox(width: 16),
            CircularProgressIndicator(value: valor),
          ],
        ),
        espacio,
        Wrap(
          spacing: 8,
          children: [
            FilledButton(
              onPressed: _animacion.isAnimating ? null : () => _animacion.forward(from: 0),
              child: const Text('Iniciar'),
            ),
            OutlinedButton(onPressed: () => _animacion.reset(), child: const Text('Reiniciar')),
          ],
        ),
        const Divider(height: 24),
        SwitchListTile(
          contentPadding: EdgeInsets.zero,
          title: const Text('Mostrar indeterminados'),
          value: _indeterminado,
          onChanged: (v) => setState(() => _indeterminado = v),
        ),
        if (_indeterminado)
          const Row(
            children: [
              Expanded(child: LinearProgressIndicator()),
              SizedBox(width: 16),
              CircularProgressIndicator(),
            ],
          ),
      ],
    );
  }
}

class _ToastSnackbar extends StatefulWidget {
  const _ToastSnackbar();

  @override
  State<_ToastSnackbar> createState() => _ToastSnackbarState();
}

class _ToastSnackbarState extends State<_ToastSnackbar> {
  bool _archivado = false;

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Wrap(
          spacing: 8,
          runSpacing: 8,
          children: [
            OutlinedButton(
              onPressed: () => mostrarToast(context, 'Esto es un toast'),
              child: const Text('Mostrar toast'),
            ),
            FilledButton(
              onPressed: () {
                setState(() => _archivado = true);
                mostrarSnackbar(context, 'Conversación archivada', accion: 'Deshacer', alAccion: () {
                  if (mounted) setState(() => _archivado = false);
                });
              },
              child: const Text('Mostrar snackbar'),
            ),
          ],
        ),
        espacio,
        TextoResultado(_archivado ? 'Estado: archivada (pulsa Deshacer en el snackbar)' : 'Estado: en la bandeja'),
      ],
    );
  }
}

class _Dialogo extends StatefulWidget {
  const _Dialogo();

  @override
  State<_Dialogo> createState() => _DialogoState();
}

class _DialogoState extends State<_Dialogo> {
  String _resultado = 'Aún no se ha abierto el diálogo.';

  Future<void> _abrir() async {
    final confirmado = await showDialog<bool>(
      context: context,
      builder: (context) => AlertDialog(
        icon: const Icon(Icons.delete),
        title: const Text('¿Eliminar archivo?'),
        content: const Text('El archivo “reporte.pdf” se eliminará de forma permanente. Esta acción no se puede deshacer.'),
        actions: [
          TextButton(onPressed: () => Navigator.pop(context, false), child: const Text('Cancelar')),
          TextButton(onPressed: () => Navigator.pop(context, true), child: const Text('Eliminar')),
        ],
      ),
    );
    if (!mounted) return;
    setState(() {
      _resultado = switch (confirmado) {
        true => 'Confirmaste: archivo eliminado.',
        false => 'Cancelaste la eliminación.',
        null => 'Diálogo cerrado sin elegir.',
      };
    });
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        FilledButton.tonalIcon(onPressed: _abrir, icon: const Icon(Icons.delete), label: const Text('Eliminar archivo')),
        espacio,
        TextoResultado(_resultado),
      ],
    );
  }
}

class _HojaInferior extends StatefulWidget {
  const _HojaInferior();

  @override
  State<_HojaInferior> createState() => _HojaInferiorState();
}

class _HojaInferiorState extends State<_HojaInferior> {
  String _resultado = 'Ninguna opción elegida.';

  Future<void> _abrir() async {
    final opcion = await showModalBottomSheet<String>(
      context: context,
      showDragHandle: true,
      builder: (context) => SafeArea(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 8),
              child: Text('Compartir foto', style: Theme.of(context).textTheme.titleMedium),
            ),
            for (final (texto, icono) in const [
              ('Compartir', Icons.share),
              ('Copiar enlace', Icons.content_copy),
              ('Descargar', Icons.download),
            ])
              ListTile(
                leading: Icon(icono),
                title: Text(texto),
                onTap: () => Navigator.pop(context, texto),
              ),
            const SizedBox(height: 16),
          ],
        ),
      ),
    );
    if (opcion != null && mounted) setState(() => _resultado = 'Elegiste: $opcion');
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        FilledButton(onPressed: _abrir, child: const Text('Abrir hoja inferior')),
        espacio,
        TextoResultado(_resultado),
      ],
    );
  }
}

class _TarjetaBadge extends StatefulWidget {
  const _TarjetaBadge();

  @override
  State<_TarjetaBadge> createState() => _TarjetaBadgeState();
}

class _TarjetaBadgeState extends State<_TarjetaBadge> {
  int _notificaciones = 3;

  @override
  Widget build(BuildContext context) {
    final texto = Theme.of(context).textTheme;
    return Card.outlined(
      margin: EdgeInsets.zero,
      child: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(16),
            child: IntrinsicHeight(
              child: Row(
                children: [
                  Badge(
                    isLabelVisible: _notificaciones > 0,
                    label: Text(_notificaciones > 99 ? '99+' : '$_notificaciones'),
                    child: const Icon(Icons.notifications, size: 32),
                  ),
                  const VerticalDivider(width: 32),
                  Expanded(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text('Notificaciones', style: texto.titleMedium),
                        Text(
                          _notificaciones == 0 ? 'Estás al día' : 'Tienes $_notificaciones sin leer',
                          style: texto.bodySmall?.copyWith(color: Theme.of(context).colorScheme.onSurfaceVariant),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
            ),
          ),
          const Divider(height: 1),
          Padding(
            padding: const EdgeInsets.all(8),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.end,
              children: [
                TextButton(onPressed: () => setState(() => _notificaciones = 0), child: const Text('Marcar como leídas')),
                TextButton(onPressed: () => setState(() => _notificaciones++), child: const Text('Recibir una')),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
