import 'dart:async';

import 'package:flutter/material.dart';

import '../secciones.dart';
import '../widgets/comunes.dart';

class Seccion2Botones extends StatelessWidget {
  const Seccion2Botones({super.key});

  @override
  Widget build(BuildContext context) {
    return const PantallaSeccion(
      seccion: Seccion.botones,
      elementos: [
        ElementoCatalogo(
          nombre: 'Botón relleno, con contorno y de texto',
          componente: 'FilledButton / OutlinedButton / TextButton',
          descripcion: 'Los tres niveles de énfasis de un botón. El relleno se usa para la acción '
              'principal, el de contorno para acciones secundarias y el de texto para las menos importantes.',
          demo: _BotonesBasicos(),
        ),
        ElementoCatalogo(
          nombre: 'Botones con ícono',
          componente: 'IconButton / IconButton.filled / FilledButton.icon',
          descripcion: 'Un ícono comunica la acción de forma compacta. Los botones de solo ícono '
              'ahorran espacio y los que combinan ícono y texto son más claros.',
          demo: _BotonesIcono(),
        ),
        ElementoCatalogo(
          nombre: 'Botón de acción flotante',
          componente: 'FloatingActionButton / FloatingActionButton.extended',
          descripcion: 'Representa la acción más importante de una pantalla y flota sobre el '
              'contenido. La versión extendida agrega una etiqueta y puede contraerse al desplazarse.',
          demo: _BotonesFlotantes(),
        ),
        ElementoCatalogo(
          nombre: 'Botón de alternancia y selector segmentado',
          componente: 'IconButton(isSelected) / SegmentedButton',
          descripcion: 'Permiten activar o desactivar una opción o elegir una entre varias '
              'relacionadas. El estado seleccionado queda resaltado visualmente.',
          demo: _Alternancia(),
        ),
        ElementoCatalogo(
          nombre: 'Botón deshabilitado y en estado de carga',
          componente: 'onPressed: null + CircularProgressIndicator',
          descripcion: 'Un botón deshabilitado indica que la acción aún no está disponible. '
              'El estado de carga evita pulsaciones repetidas mientras se completa una operación.',
          demo: _DeshabilitadoYCarga(),
        ),
      ],
    );
  }
}

class _BotonesBasicos extends StatefulWidget {
  const _BotonesBasicos();

  @override
  State<_BotonesBasicos> createState() => _BotonesBasicosState();
}

class _BotonesBasicosState extends State<_BotonesBasicos> {
  int _relleno = 0, _contorno = 0, _texto = 0;

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Wrap(
          spacing: 8,
          runSpacing: 8,
          children: [
            FilledButton(onPressed: () => setState(() => _relleno++), child: const Text('Relleno')),
            OutlinedButton(onPressed: () => setState(() => _contorno++), child: const Text('Contorno')),
            TextButton(onPressed: () => setState(() => _texto++), child: const Text('Texto')),
          ],
        ),
        espacio,
        TextoResultado('Pulsaciones → relleno: $_relleno · contorno: $_contorno · texto: $_texto'),
      ],
    );
  }
}

class _BotonesIcono extends StatefulWidget {
  const _BotonesIcono();

  @override
  State<_BotonesIcono> createState() => _BotonesIconoState();
}

class _BotonesIconoState extends State<_BotonesIcono> {
  bool _favorito = false;
  String _resultado = 'Pulsa alguno de los botones.';

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Row(
          children: [
            IconButton(
              tooltip: 'Favorito',
              icon: Icon(
                _favorito ? Icons.favorite : Icons.favorite_border,
                color: _favorito ? Theme.of(context).colorScheme.error : null,
              ),
              onPressed: () => setState(() {
                _favorito = !_favorito;
                _resultado = _favorito ? 'Agregado a favoritos' : 'Quitado de favoritos';
              }),
            ),
            const SizedBox(width: 8),
            IconButton.filled(
              tooltip: 'Compartir',
              icon: const Icon(Icons.share),
              onPressed: () {
                setState(() => _resultado = 'Compartir');
                mostrarToast(context, 'Compartiendo contenido…');
              },
            ),
            const SizedBox(width: 8),
            FilledButton.icon(
              onPressed: () {
                setState(() => _resultado = 'Mensaje enviado');
                mostrarSnackbar(context, 'Mensaje enviado correctamente');
              },
              icon: const Icon(Icons.send),
              label: const Text('Enviar'),
            ),
          ],
        ),
        espacio,
        TextoResultado(_resultado),
      ],
    );
  }
}

class _BotonesFlotantes extends StatefulWidget {
  const _BotonesFlotantes();

  @override
  State<_BotonesFlotantes> createState() => _BotonesFlotantesState();
}

class _BotonesFlotantesState extends State<_BotonesFlotantes> {
  int _creados = 0;
  bool _extendido = true;

  @override
  Widget build(BuildContext context) {
    final esquema = Theme.of(context).colorScheme;
    return Column(
      children: [
        SwitchListTile(
          contentPadding: EdgeInsets.zero,
          title: const Text('Mostrar etiqueta del FAB extendido'),
          value: _extendido,
          onChanged: (v) => setState(() => _extendido = v),
        ),
        Container(
          height: 140,
          decoration: BoxDecoration(
            color: esquema.surfaceContainerHighest,
            borderRadius: BorderRadius.circular(12),
          ),
          child: Stack(
            children: [
              Padding(
                padding: const EdgeInsets.all(16),
                child: Text('Elementos creados: $_creados', style: Theme.of(context).textTheme.bodyLarge),
              ),
              Positioned(
                left: 16,
                bottom: 16,
                child: FloatingActionButton(
                  heroTag: 'fab-normal',
                  tooltip: 'Crear',
                  onPressed: () => setState(() => _creados++),
                  child: const Icon(Icons.add),
                ),
              ),
              Positioned(
                right: 16,
                bottom: 16,
                child: FloatingActionButton.extended(
                  heroTag: 'fab-extendido',
                  isExtended: _extendido,
                  onPressed: () => mostrarSnackbar(context, 'Abriendo el editor…'),
                  icon: const Icon(Icons.edit),
                  label: const Text('Redactar'),
                ),
              ),
            ],
          ),
        ),
      ],
    );
  }
}

enum _Vista { dia, semana, mes }

class _Alternancia extends StatefulWidget {
  const _Alternancia();

  @override
  State<_Alternancia> createState() => _AlternanciaState();
}

class _AlternanciaState extends State<_Alternancia> {
  _Vista _vista = _Vista.dia;
  bool _guardado = false;

  static const _etiquetas = {_Vista.dia: 'Día', _Vista.semana: 'Semana', _Vista.mes: 'Mes'};

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        SizedBox(
          width: double.infinity,
          child: SegmentedButton<_Vista>(
            segments: [
              for (final v in _Vista.values) ButtonSegment(value: v, label: Text(_etiquetas[v]!)),
            ],
            selected: {_vista},
            onSelectionChanged: (s) => setState(() => _vista = s.first),
          ),
        ),
        espacio,
        Row(
          children: [
            IconButton(
              isSelected: _guardado,
              tooltip: 'Guardar',
              icon: const Icon(Icons.bookmark_border),
              selectedIcon: const Icon(Icons.bookmark),
              onPressed: () => setState(() => _guardado = !_guardado),
            ),
            Text(_guardado ? 'Guardado en marcadores' : 'Sin guardar'),
          ],
        ),
        TextoResultado('Vista seleccionada: ${_etiquetas[_vista]}'),
      ],
    );
  }
}

class _DeshabilitadoYCarga extends StatefulWidget {
  const _DeshabilitadoYCarga();

  @override
  State<_DeshabilitadoYCarga> createState() => _DeshabilitadoYCargaState();
}

class _DeshabilitadoYCargaState extends State<_DeshabilitadoYCarga> {
  bool _habilitado = false;
  bool _cargando = false;
  int _completadas = 0;
  Timer? _temporizador;

  @override
  void dispose() {
    _temporizador?.cancel();
    super.dispose();
  }

  void _descargar() {
    setState(() => _cargando = true);
    _temporizador = Timer(const Duration(seconds: 2), () {
      if (!mounted) return;
      setState(() {
        _cargando = false;
        _completadas++;
      });
      mostrarSnackbar(context, 'Operación completada');
    });
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        SwitchListTile(
          contentPadding: EdgeInsets.zero,
          title: const Text('Habilitar botón'),
          value: _habilitado,
          onChanged: (v) => setState(() => _habilitado = v),
        ),
        Wrap(
          spacing: 8,
          runSpacing: 8,
          children: [
            FilledButton(
              onPressed: _habilitado ? () => mostrarToast(context, '¡El botón está habilitado!') : null,
              child: Text(_habilitado ? 'Habilitado' : 'Deshabilitado'),
            ),
            FilledButton.tonalIcon(
              onPressed: _cargando ? null : _descargar,
              icon: _cargando
                  ? const SizedBox(width: 18, height: 18, child: CircularProgressIndicator(strokeWidth: 2))
                  : const Icon(Icons.cloud_download),
              label: Text(_cargando ? 'Cargando…' : 'Descargar'),
            ),
          ],
        ),
        espacio,
        TextoResultado('Descargas completadas: $_completadas'),
      ],
    );
  }
}
