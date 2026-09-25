import 'package:flutter/material.dart';

import '../estado.dart';
import '../secciones.dart';
import '../widgets/comunes.dart';

const _lenguajes = ['Kotlin', 'Dart', 'Java', 'Swift', 'C#', 'JavaScript'];
const _intereses = ['Música', 'Deportes', 'Tecnología', 'Viajes', 'Cine', 'Arte'];

class Seccion3Seleccion extends StatelessWidget {
  const Seccion3Seleccion({super.key});

  @override
  Widget build(BuildContext context) {
    return const PantallaSeccion(
      seccion: Seccion.seleccion,
      elementos: [
        ElementoCatalogo(
          nombre: 'Casilla de verificación',
          componente: 'Checkbox(tristate: true) / CheckboxListTile',
          descripcion: 'Permite marcar varias opciones de forma independiente. La casilla '
              'principal muestra un estado indeterminado cuando solo algunas opciones están marcadas.',
          demo: _Casillas(),
        ),
        ElementoCatalogo(
          nombre: 'Botones de opción (conectado con la Sección 5)',
          componente: 'RadioGroup + RadioListTile',
          descripcion: 'Permiten elegir una sola opción de un grupo. La opción elegida aquí '
              'define el tamaño de los textos de ejemplo que se muestran en la Sección 5.',
          demo: _BotonesOpcion(),
        ),
        ElementoCatalogo(
          nombre: 'Interruptor',
          componente: 'Switch / SwitchListTile',
          descripcion: 'Activa o desactiva una opción de forma inmediata, sin necesidad de '
              'confirmar. Es ideal para ajustes que se aplican al momento.',
          demo: _Interruptores(),
        ),
        ElementoCatalogo(
          nombre: 'Deslizador de valor único y de rango',
          componente: 'Slider / RangeSlider',
          descripcion: 'Permiten elegir un valor numérico arrastrando un control sobre una '
              'pista. El de rango tiene dos controles para definir un mínimo y un máximo.',
          demo: _Deslizadores(),
        ),
        ElementoCatalogo(
          nombre: 'Lista desplegable',
          componente: 'DropdownMenu<String>',
          descripcion: 'Muestra la opción seleccionada y, al tocarla, despliega un menú con '
              'todas las alternativas. Ahorra espacio cuando hay muchas opciones.',
          demo: _ListaDesplegable(),
        ),
        ElementoCatalogo(
          nombre: 'Selector de fecha y de hora',
          componente: 'showDatePicker / showTimePicker',
          descripcion: 'Diálogos que muestran un calendario o un reloj para elegir una fecha '
              'o una hora sin tener que escribirla, evitando errores de formato.',
          demo: _SelectoresFechaHora(),
        ),
        ElementoCatalogo(
          nombre: 'Chips de filtro',
          componente: 'FilterChip',
          descripcion: 'Etiquetas compactas que se activan o desactivan para filtrar contenido. '
              'Se pueden seleccionar varias a la vez y muestran una marca cuando están activas.',
          demo: _ChipsFiltro(),
        ),
      ],
    );
  }
}

class _Casillas extends StatefulWidget {
  const _Casillas();

  @override
  State<_Casillas> createState() => _CasillasState();
}

class _CasillasState extends State<_Casillas> {
  static const _ingredientes = ['Queso', 'Jamón', 'Champiñones'];
  final _marcados = [true, false, false];

  /// true = todas, false = ninguna, null = indeterminado.
  bool? get _general {
    if (_marcados.every((m) => m)) return true;
    if (_marcados.every((m) => !m)) return false;
    return null;
  }

  @override
  Widget build(BuildContext context) {
    final general = _general;
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        CheckboxListTile(
          contentPadding: EdgeInsets.zero,
          controlAffinity: ListTileControlAffinity.leading,
          tristate: true,
          value: general,
          title: const Text('Todos los ingredientes'),
          onChanged: (_) => setState(() {
            final nuevo = general != true;
            for (var i = 0; i < _marcados.length; i++) {
              _marcados[i] = nuevo;
            }
          }),
        ),
        for (var i = 0; i < _ingredientes.length; i++)
          CheckboxListTile(
            contentPadding: const EdgeInsets.only(left: 24),
            controlAffinity: ListTileControlAffinity.leading,
            dense: true,
            value: _marcados[i],
            title: Text(_ingredientes[i]),
            onChanged: (v) => setState(() => _marcados[i] = v ?? false),
          ),
        TextoResultado(
          'Estado de la casilla principal: ${general == null ? 'indeterminado' : general ? 'marcado' : 'desmarcado'}',
        ),
      ],
    );
  }
}

class _BotonesOpcion extends StatelessWidget {
  const _BotonesOpcion();

  @override
  Widget build(BuildContext context) {
    final estado = EstadoScope.of(context);
    final base = Theme.of(context).textTheme.bodyLarge!;
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        RadioGroup<TamanoTexto>(
          groupValue: estado.tamanoTexto,
          onChanged: (v) {
            if (v != null) estado.tamanoTexto = v;
          },
          child: Column(
            children: [
              for (final t in TamanoTexto.values)
                RadioListTile<TamanoTexto>(
                  contentPadding: EdgeInsets.zero,
                  dense: true,
                  value: t,
                  title: Text(t.etiqueta),
                ),
            ],
          ),
        ),
        Text('Vista previa del texto', style: base.copyWith(fontSize: base.fontSize! * estado.tamanoTexto.escala)),
        espacio,
        TextoResultado('Tamaño elegido: ${estado.tamanoTexto.etiqueta}. Revisa la Sección 5.'),
      ],
    );
  }
}

class _Interruptores extends StatefulWidget {
  const _Interruptores();

  @override
  State<_Interruptores> createState() => _InterruptoresState();
}

class _InterruptoresState extends State<_Interruptores> {
  bool _avion = false;
  bool _wifi = true;

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Row(
          children: [
            const Icon(Icons.airplanemode_active),
            const SizedBox(width: 12),
            const Expanded(child: Text('Modo avión')),
            Switch(
              value: _avion,
              thumbIcon: WidgetStateProperty.resolveWith(
                (estados) => Icon(estados.contains(WidgetState.selected) ? Icons.check : Icons.close),
              ),
              onChanged: (v) => setState(() {
                _avion = v;
                if (v) _wifi = false;
              }),
            ),
          ],
        ),
        SwitchListTile(
          contentPadding: EdgeInsets.zero,
          title: const Text('Wi-Fi'),
          value: _wifi,
          onChanged: _avion ? null : (v) => setState(() => _wifi = v),
        ),
        TextoResultado(
          _avion ? 'Modo avión activado: conexiones desactivadas' : 'Wi-Fi ${_wifi ? 'encendido' : 'apagado'}',
        ),
      ],
    );
  }
}

class _Deslizadores extends StatefulWidget {
  const _Deslizadores();

  @override
  State<_Deslizadores> createState() => _DeslizadoresState();
}

class _DeslizadoresState extends State<_Deslizadores> {
  double _volumen = 40;
  RangeValues _precio = const RangeValues(200, 800);

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text('Volumen: ${_volumen.round()} %'),
        Slider(
          value: _volumen,
          max: 100,
          label: '${_volumen.round()} %',
          onChanged: (v) => setState(() => _volumen = v),
        ),
        Text('Precio: \$${_precio.start.round()} – \$${_precio.end.round()} MXN'),
        RangeSlider(
          values: _precio,
          max: 1000,
          divisions: 20,
          labels: RangeLabels('\$${_precio.start.round()}', '\$${_precio.end.round()}'),
          onChanged: (v) => setState(() => _precio = v),
        ),
      ],
    );
  }
}

class _ListaDesplegable extends StatefulWidget {
  const _ListaDesplegable();

  @override
  State<_ListaDesplegable> createState() => _ListaDesplegableState();
}

class _ListaDesplegableState extends State<_ListaDesplegable> {
  String _lenguaje = _lenguajes.first;

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        DropdownMenu<String>(
          initialSelection: _lenguaje,
          label: const Text('Lenguaje favorito'),
          expandedInsets: EdgeInsets.zero,
          requestFocusOnTap: false,
          onSelected: (v) {
            if (v != null) setState(() => _lenguaje = v);
          },
          dropdownMenuEntries: [
            for (final l in _lenguajes) DropdownMenuEntry(value: l, label: l),
          ],
        ),
        espacio,
        TextoResultado('Elegiste: $_lenguaje'),
      ],
    );
  }
}

class _SelectoresFechaHora extends StatefulWidget {
  const _SelectoresFechaHora();

  @override
  State<_SelectoresFechaHora> createState() => _SelectoresFechaHoraState();
}

class _SelectoresFechaHoraState extends State<_SelectoresFechaHora> {
  DateTime? _fecha;
  TimeOfDay? _hora;

  Future<void> _elegirFecha() async {
    final hoy = DateTime.now();
    final elegida = await showDatePicker(
      context: context,
      initialDate: _fecha ?? hoy,
      firstDate: DateTime(hoy.year - 50),
      lastDate: DateTime(hoy.year + 10),
    );
    if (elegida != null) setState(() => _fecha = elegida);
  }

  Future<void> _elegirHora() async {
    final elegida = await showTimePicker(
      context: context,
      initialTime: _hora ?? TimeOfDay.now(),
      helpText: 'Selecciona la hora',
      builder: (context, hijo) =>
          MediaQuery(data: MediaQuery.of(context).copyWith(alwaysUse24HourFormat: true), child: hijo!),
    );
    if (elegida != null) setState(() => _hora = elegida);
  }

  @override
  Widget build(BuildContext context) {
    final loc = MaterialLocalizations.of(context);
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Wrap(
          spacing: 8,
          runSpacing: 8,
          children: [
            OutlinedButton.icon(
              onPressed: _elegirFecha,
              icon: const Icon(Icons.calendar_month),
              label: const Text('Elegir fecha'),
            ),
            OutlinedButton.icon(
              onPressed: _elegirHora,
              icon: const Icon(Icons.schedule),
              label: const Text('Elegir hora'),
            ),
          ],
        ),
        espacio,
        TextoResultado('Fecha: ${_fecha == null ? 'sin elegir' : loc.formatFullDate(_fecha!)}'),
        TextoResultado('Hora: ${_hora == null ? 'sin elegir' : loc.formatTimeOfDay(_hora!, alwaysUse24HourFormat: true)}'),
      ],
    );
  }
}

class _ChipsFiltro extends StatefulWidget {
  const _ChipsFiltro();

  @override
  State<_ChipsFiltro> createState() => _ChipsFiltroState();
}

class _ChipsFiltroState extends State<_ChipsFiltro> {
  final _seleccion = <String>{'Tecnología'};

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Wrap(
          spacing: 8,
          runSpacing: 4,
          children: [
            for (final interes in _intereses)
              FilterChip(
                label: Text(interes),
                selected: _seleccion.contains(interes),
                onSelected: (v) => setState(() => v ? _seleccion.add(interes) : _seleccion.remove(interes)),
              ),
          ],
        ),
        espacio,
        TextoResultado(
          _seleccion.isEmpty
              ? 'Sin filtros activos'
              : 'Filtros activos (${_seleccion.length}): ${_intereses.where(_seleccion.contains).join(', ')}',
        ),
      ],
    );
  }
}
