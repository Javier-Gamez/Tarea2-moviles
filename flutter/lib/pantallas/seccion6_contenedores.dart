import 'package:flutter/material.dart';

import '../secciones.dart';
import '../widgets/comunes.dart';

const _colorA = Color(0xFF64B5F6);
const _colorB = Color(0xFF81C784);
const _colorC = Color(0xFFFFB74D);

class Seccion6Contenedores extends StatelessWidget {
  const Seccion6Contenedores({super.key});

  @override
  Widget build(BuildContext context) {
    return const PantallaSeccion(
      seccion: Seccion.contenedores,
      elementos: [
        ElementoCatalogo(
          nombre: 'Distribución en fila',
          componente: 'Row + MainAxisAlignment',
          descripcion: 'Coloca los elementos uno junto a otro de forma horizontal. La '
              'distribución define cómo se reparte el espacio libre entre ellos.',
          demo: _DemoFila(),
        ),
        ElementoCatalogo(
          nombre: 'Distribución en columna',
          componente: 'Column + CrossAxisAlignment',
          descripcion: 'Apila los elementos de arriba hacia abajo. La alineación horizontal '
              'decide si se pegan a la izquierda, al centro o a la derecha.',
          demo: _DemoColumna(),
        ),
        ElementoCatalogo(
          nombre: 'Distribución superpuesta',
          componente: 'Stack + Align',
          descripcion: 'Coloca los elementos uno sobre otro, como capas. Es útil para poner '
              'texto o botones sobre una imagen. Elige dónde colocar la etiqueta.',
          demo: _DemoSuperpuesta(),
        ),
        ElementoCatalogo(
          nombre: 'Contenedor con desplazamiento vertical',
          componente: 'SingleChildScrollView + ScrollController',
          descripcion: 'Permite ver contenido más alto que el espacio disponible desplazándolo '
              'con el dedo. Aquí también se puede saltar al final con un botón.',
          demo: _DemoDesplazamiento(),
        ),
        ElementoCatalogo(
          nombre: 'Barra superior con título y acciones',
          componente: 'AppBar + PopupMenuButton',
          descripcion: 'Muestra el título de la pantalla y las acciones más frecuentes. Las '
              'acciones que no caben se agrupan en un menú de desbordamiento (tres puntos).',
          demo: _DemoBarraSuperior(),
        ),
        ElementoCatalogo(
          nombre: 'Barra de navegación inferior',
          componente: 'NavigationBar + NavigationDestination',
          descripcion: 'Da acceso directo a entre tres y cinco destinos principales. Esta app '
              'usa además un menú lateral (NavigationDrawer) para moverse entre secciones.',
          demo: _DemoNavegacionInferior(),
        ),
        ElementoCatalogo(
          nombre: 'Distribución con pesos proporcionales',
          componente: 'Row + Expanded(flex)',
          descripcion: 'Reparte el ancho disponible en proporción al peso de cada elemento. '
              'Mueve el deslizador para cambiar el peso del bloque A frente al B.',
          demo: _DemoPesos(),
        ),
      ],
    );
  }
}

class _Selector extends StatelessWidget {
  const _Selector({required this.opciones, required this.elegido, required this.alElegir});

  final List<String> opciones;
  final int elegido;
  final ValueChanged<int> alElegir;

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      width: double.infinity,
      child: SegmentedButton<int>(
        showSelectedIcon: false,
        segments: [
          for (var i = 0; i < opciones.length; i++)
            ButtonSegment(value: i, label: Text(opciones[i], style: Theme.of(context).textTheme.labelMedium)),
        ],
        selected: {elegido},
        onSelectionChanged: (s) => alElegir(s.first),
      ),
    );
  }
}

class _Bloque extends StatelessWidget {
  const _Bloque(this.texto, this.color, {this.ancho = 56});

  final String texto;
  final Color color;
  final double ancho;

  @override
  Widget build(BuildContext context) {
    return Container(
      width: ancho,
      height: 48,
      alignment: Alignment.center,
      decoration: BoxDecoration(color: color, borderRadius: BorderRadius.circular(8)),
      child: Text(texto, style: const TextStyle(color: Colors.black, fontWeight: FontWeight.bold)),
    );
  }
}

class _FondoDemo extends StatelessWidget {
  const _FondoDemo({required this.child, this.alto});

  final Widget child;
  final double? alto;

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      height: alto,
      padding: const EdgeInsets.all(8),
      decoration: BoxDecoration(
        color: Theme.of(context).colorScheme.surfaceContainerHighest,
        borderRadius: BorderRadius.circular(12),
      ),
      child: child,
    );
  }
}

class _DemoFila extends StatefulWidget {
  const _DemoFila();

  @override
  State<_DemoFila> createState() => _DemoFilaState();
}

class _DemoFilaState extends State<_DemoFila> {
  static const _opciones = [
    ('Inicio', MainAxisAlignment.start),
    ('Centro', MainAxisAlignment.center),
    ('Entre', MainAxisAlignment.spaceBetween),
    ('Alrededor', MainAxisAlignment.spaceEvenly),
  ];
  int _elegido = 0;

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        _Selector(
          opciones: [for (final o in _opciones) o.$1],
          elegido: _elegido,
          alElegir: (i) => setState(() => _elegido = i),
        ),
        espacio,
        _FondoDemo(
          child: Row(
            mainAxisAlignment: _opciones[_elegido].$2,
            children: const [_Bloque('A', _colorA), _Bloque('B', _colorB), _Bloque('C', _colorC)],
          ),
        ),
      ],
    );
  }
}

class _DemoColumna extends StatefulWidget {
  const _DemoColumna();

  @override
  State<_DemoColumna> createState() => _DemoColumnaState();
}

class _DemoColumnaState extends State<_DemoColumna> {
  static const _opciones = [
    ('Izquierda', CrossAxisAlignment.start),
    ('Centro', CrossAxisAlignment.center),
    ('Derecha', CrossAxisAlignment.end),
  ];
  int _elegido = 0;

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        _Selector(
          opciones: [for (final o in _opciones) o.$1],
          elegido: _elegido,
          alElegir: (i) => setState(() => _elegido = i),
        ),
        espacio,
        _FondoDemo(
          child: Column(
            crossAxisAlignment: _opciones[_elegido].$2,
            children: const [
              _Bloque('A', _colorA, ancho: 120),
              SizedBox(height: 6),
              _Bloque('B', _colorB, ancho: 80),
              SizedBox(height: 6),
              _Bloque('C', _colorC, ancho: 160),
            ],
          ),
        ),
      ],
    );
  }
}

class _DemoSuperpuesta extends StatefulWidget {
  const _DemoSuperpuesta();

  @override
  State<_DemoSuperpuesta> createState() => _DemoSuperpuestaState();
}

class _DemoSuperpuestaState extends State<_DemoSuperpuesta> {
  static const _opciones = [
    ('Arriba', Alignment.topLeft),
    ('Centro', Alignment.center),
    ('Abajo', Alignment.bottomRight),
  ];
  int _elegido = 2;

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        _Selector(
          opciones: [for (final o in _opciones) o.$1],
          elegido: _elegido,
          alElegir: (i) => setState(() => _elegido = i),
        ),
        espacio,
        ClipRRect(
          borderRadius: BorderRadius.circular(12),
          child: SizedBox(
            height: 160,
            width: double.infinity,
            child: Stack(
              fit: StackFit.expand,
              children: [
                Image.asset('assets/paisaje.png', fit: BoxFit.cover),
                const DecoratedBox(
                  decoration: BoxDecoration(
                    gradient: LinearGradient(
                      begin: Alignment.topCenter,
                      end: Alignment.bottomCenter,
                      colors: [Colors.transparent, Color(0x99000000)],
                    ),
                  ),
                ),
                Align(
                  alignment: _opciones[_elegido].$2,
                  child: Container(
                    margin: const EdgeInsets.all(12),
                    padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                    decoration: BoxDecoration(color: const Color(0x66000000), borderRadius: BorderRadius.circular(8)),
                    child: const Text('Capa de texto', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold)),
                  ),
                ),
              ],
            ),
          ),
        ),
      ],
    );
  }
}

class _DemoDesplazamiento extends StatefulWidget {
  const _DemoDesplazamiento();

  @override
  State<_DemoDesplazamiento> createState() => _DemoDesplazamientoState();
}

class _DemoDesplazamientoState extends State<_DemoDesplazamiento> {
  final _controlador = ScrollController();

  @override
  void initState() {
    super.initState();
    _controlador.addListener(() => setState(() {}));
  }

  @override
  void dispose() {
    _controlador.dispose();
    super.dispose();
  }

  bool get _alFinal =>
      _controlador.hasClients && _controlador.offset >= _controlador.position.maxScrollExtent - 1;

  @override
  Widget build(BuildContext context) {
    final posicion = _controlador.hasClients ? _controlador.offset.round() : 0;
    return Column(
      children: [
        _FondoDemo(
          alto: 180,
          child: SingleChildScrollView(
            controller: _controlador,
            padding: const EdgeInsets.all(4),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                for (var i = 1; i <= 20; i++)
                  Padding(
                    padding: const EdgeInsets.only(bottom: 8),
                    child: Text('Párrafo $i: contenido que requiere desplazamiento.'),
                  ),
              ],
            ),
          ),
        ),
        espacio,
        Row(
          children: [
            Expanded(child: TextoResultado('Desplazamiento: $posicion px')),
            OutlinedButton(
              onPressed: () => _controlador.animateTo(
                _alFinal ? 0 : _controlador.position.maxScrollExtent,
                duration: const Duration(milliseconds: 500),
                curve: Curves.easeInOut,
              ),
              child: Text(_alFinal ? 'Ir al inicio' : 'Ir al final'),
            ),
          ],
        ),
      ],
    );
  }
}

class _DemoBarraSuperior extends StatefulWidget {
  const _DemoBarraSuperior();

  @override
  State<_DemoBarraSuperior> createState() => _DemoBarraSuperiorState();
}

class _DemoBarraSuperiorState extends State<_DemoBarraSuperior> {
  String _accion = 'Pulsa un ícono de la barra.';

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        ClipRRect(
          borderRadius: BorderRadius.circular(12),
          child: AppBar(
            primary: false,
            automaticallyImplyLeading: false,
            backgroundColor: Theme.of(context).colorScheme.primaryContainer,
            title: const Text('Mis notas'),
            leading: IconButton(
              icon: const Icon(Icons.menu),
              tooltip: 'Menú',
              onPressed: () => setState(() => _accion = 'Menú de navegación'),
            ),
            actions: [
              IconButton(
                icon: const Icon(Icons.search),
                tooltip: 'Buscar',
                onPressed: () => setState(() => _accion = 'Buscar'),
              ),
              IconButton(
                icon: const Icon(Icons.favorite),
                tooltip: 'Favoritos',
                onPressed: () => setState(() => _accion = 'Favoritos'),
              ),
              PopupMenuButton<String>(
                tooltip: 'Más opciones',
                onSelected: (o) => setState(() => _accion = 'Menú: $o'),
                itemBuilder: (_) => [
                  for (final o in const ['Ajustes', 'Ayuda', 'Acerca de']) PopupMenuItem(value: o, child: Text(o)),
                ],
              ),
            ],
          ),
        ),
        espacio,
        TextoResultado('Acción: $_accion'),
      ],
    );
  }
}

class _DemoNavegacionInferior extends StatefulWidget {
  const _DemoNavegacionInferior();

  @override
  State<_DemoNavegacionInferior> createState() => _DemoNavegacionInferiorState();
}

class _DemoNavegacionInferiorState extends State<_DemoNavegacionInferior> {
  static const _destinos = [
    ('Inicio', Icons.home, 'Contenido de la pantalla de inicio'),
    ('Explorar', Icons.explore, 'Descubre contenido nuevo'),
    ('Perfil', Icons.person, 'Tu información personal'),
  ];
  int _actual = 0;

  @override
  Widget build(BuildContext context) {
    final (_, icono, texto) = _destinos[_actual];
    return ClipRRect(
      borderRadius: BorderRadius.circular(12),
      child: ColoredBox(
        color: Theme.of(context).colorScheme.surfaceContainerLow,
        child: Column(
          children: [
            SizedBox(
              height: 100,
              child: Center(
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [Icon(icono, size: 32), Text(texto)],
                ),
              ),
            ),
            MediaQuery.removePadding(
              context: context,
              removeBottom: true,
              child: NavigationBar(
                selectedIndex: _actual,
                onDestinationSelected: (i) => setState(() => _actual = i),
                destinations: [
                  for (final (etiqueta, ic, _) in _destinos) NavigationDestination(icon: Icon(ic), label: etiqueta),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class _DemoPesos extends StatefulWidget {
  const _DemoPesos();

  @override
  State<_DemoPesos> createState() => _DemoPesosState();
}

class _DemoPesosState extends State<_DemoPesos> {
  double _pesoA = 1;

  @override
  Widget build(BuildContext context) {
    final a = _pesoA.round();
    final porcentaje = (a * 100 / (a + 1)).round();
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        _FondoDemo(
          child: Row(
            children: [
              Expanded(
                flex: a,
                child: Container(
                  height: 56,
                  alignment: Alignment.center,
                  decoration: BoxDecoration(color: _colorA, borderRadius: BorderRadius.circular(8)),
                  child: Text('A · $a', style: const TextStyle(color: Colors.black)),
                ),
              ),
              const SizedBox(width: 6),
              Expanded(
                child: Container(
                  height: 56,
                  alignment: Alignment.center,
                  decoration: BoxDecoration(color: _colorB, borderRadius: BorderRadius.circular(8)),
                  child: const Text('B · 1', style: TextStyle(color: Colors.black)),
                ),
              ),
            ],
          ),
        ),
        Slider(value: _pesoA, min: 1, max: 5, divisions: 4, label: '$a', onChanged: (v) => setState(() => _pesoA = v)),
        TextoResultado('A ocupa $porcentaje % del ancho y B el ${100 - porcentaje} %'),
      ],
    );
  }
}
