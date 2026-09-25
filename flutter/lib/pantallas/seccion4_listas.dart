import 'package:flutter/material.dart';

import '../estado.dart';
import '../secciones.dart';
import '../widgets/comunes.dart';

const _coloresCuadricula = [
  Color(0xFFE57373), Color(0xFFF06292), Color(0xFFBA68C8), Color(0xFF9575CD),
  Color(0xFF7986CB), Color(0xFF64B5F6), Color(0xFF4DD0E1), Color(0xFF4DB6AC),
  Color(0xFF81C784), Color(0xFFDCE775), Color(0xFFFFD54F), Color(0xFFFF8A65),
];

const _correos = [
  'Recordatorio de clase', 'Entrega de la práctica', 'Boletín semanal',
  'Invitación a conferencia', 'Confirmación de registro', 'Promoción de temporada',
];

/// Filas de la lista con encabezados: dos tipos distintos.
sealed class _Fila {
  const _Fila();
}

class _Contacto extends _Fila {
  const _Contacto(this.nombre, this.detalle);
  final String nombre;
  final String detalle;
}

class _Anuncio extends _Fila {
  const _Anuncio(this.texto);
  final String texto;
}

const _grupos = <(String, List<_Fila>)>[
  ('Favoritos', [
    _Contacto('Ana Martínez', 'Diseñadora'),
    _Contacto('Bruno Díaz', 'Desarrollador'),
    _Anuncio('Tus favoritos se sincronizan en todos tus dispositivos.'),
  ]),
  ('Compañeros', [
    _Contacto('Carla Núñez', 'Grupo 7CM1'),
    _Contacto('Diego Ramos', 'Grupo 7CM1'),
    _Contacto('Elena Soto', 'Grupo 7CM2'),
    _Anuncio('Invita a tus compañeros a probar la aplicación.'),
  ]),
  ('Profesores', [
    _Contacto('Fernando Gil', 'Aplicaciones Móviles'),
    _Contacto('Gabriela Ortiz', 'Ingeniería de Software'),
  ]),
];

class Seccion4Listas extends StatelessWidget {
  const Seccion4Listas({super.key});

  @override
  Widget build(BuildContext context) {
    return const PantallaSeccion(
      seccion: Seccion.listas,
      elementos: [
        ElementoCatalogo(
          nombre: 'Lista vertical y detalle de elemento',
          componente: 'ListView.separated + Navigator.pushNamed',
          descripcion: 'Muestra muchos elementos de forma eficiente: solo se construyen los visibles. '
              'Toca un elemento para abrir su detalle. Los que agregues en la Sección 1 aparecen aquí.',
          demo: _ListaVertical(),
        ),
        ElementoCatalogo(
          nombre: 'Cuadrícula de elementos',
          componente: 'GridView.count(crossAxisCount: 3)',
          descripcion: 'Organiza los elementos en filas y columnas, útil para galerías de fotos '
              'o catálogos. Toca las celdas para seleccionarlas.',
          demo: _Cuadricula(),
        ),
        ElementoCatalogo(
          nombre: 'Lista con encabezados de sección',
          componente: 'CustomScrollView + SliverMainAxisGroup + PinnedHeaderSliver',
          descripcion: 'Agrupa los elementos bajo encabezados que permanecen fijos al desplazarse. '
              'Combina dos tipos de fila: contactos y avisos con un diseño distinto.',
          demo: _ListaAgrupada(),
        ),
        ElementoCatalogo(
          nombre: 'Deslizar para eliminar',
          componente: 'Dismissible',
          descripcion: 'Desliza un correo hacia cualquier lado para eliminarlo. Se muestra un fondo '
              'rojo con un ícono de papelera y un snackbar permite deshacer la acción.',
          demo: _DeslizarEliminar(),
        ),
        ElementoCatalogo(
          nombre: 'Actualizar arrastrando hacia abajo',
          componente: 'RefreshIndicator',
          descripcion: 'Al arrastrar la lista hacia abajo desde el inicio aparece un indicador de '
              'carga y se obtienen elementos nuevos. Es el gesto estándar para refrescar.',
          demo: _ActualizarArrastrando(),
        ),
        ElementoCatalogo(
          nombre: 'Estado vacío',
          componente: 'Column + Icon + Text (composición propia)',
          descripcion: 'Cuando no hay elementos se muestra una ilustración con un mensaje que '
              'explica la situación y una acción para salir de ella, en lugar de una pantalla en blanco.',
          demo: _EstadoVacio(),
        ),
        ElementoCatalogo(
          nombre: 'Pestañas con contenido deslizable',
          componente: 'TabBar + TabBarView',
          descripcion: 'Dividen el contenido en categorías del mismo nivel. Se puede cambiar de '
              'pestaña tocándola o deslizando el contenido hacia los lados.',
          demo: _Pestanas(),
        ),
      ],
    );
  }
}

class Avatar extends StatelessWidget {
  const Avatar(this.texto, {super.key, this.radio = 20});

  final String texto;
  final double radio;

  @override
  Widget build(BuildContext context) {
    final esquema = Theme.of(context).colorScheme;
    return CircleAvatar(
      radius: radio,
      backgroundColor: esquema.primaryContainer,
      child: Text(
        texto.characters.first.toUpperCase(),
        style: (radio > 24 ? Theme.of(context).textTheme.headlineMedium : Theme.of(context).textTheme.titleMedium)
            ?.copyWith(color: esquema.onPrimaryContainer),
      ),
    );
  }
}

/// Contenedor con bordes redondeados para las listas embebidas en las tarjetas.
class _MarcoLista extends StatelessWidget {
  const _MarcoLista({required this.alto, required this.child});

  final double? alto;
  final Widget child;

  @override
  Widget build(BuildContext context) {
    return ClipRRect(
      borderRadius: BorderRadius.circular(12),
      child: Container(
        height: alto,
        color: Theme.of(context).colorScheme.surfaceContainerLowest,
        child: child,
      ),
    );
  }
}

class _ListaVertical extends StatelessWidget {
  const _ListaVertical();

  @override
  Widget build(BuildContext context) {
    final esquema = Theme.of(context).colorScheme;
    final elementos = EstadoScope.of(context).elementos;
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text('${elementos.length} elementos', style: Theme.of(context).textTheme.labelLarge),
        const SizedBox(height: 8),
        _MarcoLista(
          alto: 360,
          child: ListView.separated(
            itemCount: elementos.length,
            separatorBuilder: (_, _) => const Divider(height: 1),
            itemBuilder: (context, i) {
              final e = elementos[i];
              return ListTile(
                key: ValueKey(e.id),
                leading: Avatar(e.titulo),
                title: Text(e.titulo),
                subtitle: Text(e.descripcion, maxLines: 1, overflow: TextOverflow.ellipsis),
                trailing: Row(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    if (e.deUsuario)
                      Container(
                        padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 2),
                        decoration: BoxDecoration(
                          color: esquema.tertiaryContainer,
                          borderRadius: BorderRadius.circular(8),
                        ),
                        child: Text('Nuevo',
                            style: Theme.of(context).textTheme.labelSmall?.copyWith(color: esquema.onTertiaryContainer)),
                      ),
                    const Icon(Icons.chevron_right),
                  ],
                ),
                onTap: () => Navigator.of(context).pushNamed(rutaDetalle, arguments: e.id),
              );
            },
          ),
        ),
      ],
    );
  }
}

class _Cuadricula extends StatefulWidget {
  const _Cuadricula();

  @override
  State<_Cuadricula> createState() => _CuadriculaState();
}

class _CuadriculaState extends State<_Cuadricula> {
  final _seleccionadas = <int>{};

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        GridView.count(
          crossAxisCount: 3,
          mainAxisSpacing: 8,
          crossAxisSpacing: 8,
          childAspectRatio: 1.5,
          padding: EdgeInsets.zero,
          shrinkWrap: true,
          physics: const NeverScrollableScrollPhysics(),
          children: [
            for (var i = 0; i < _coloresCuadricula.length; i++)
              Material(
                color: _coloresCuadricula[i],
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(12),
                  side: _seleccionadas.contains(i)
                      ? BorderSide(color: Theme.of(context).colorScheme.onSurface, width: 3)
                      : BorderSide.none,
                ),
                clipBehavior: Clip.antiAlias,
                child: InkWell(
                  onTap: () => setState(() => _seleccionadas.contains(i) ? _seleccionadas.remove(i) : _seleccionadas.add(i)),
                  child: Center(
                    child: _seleccionadas.contains(i)
                        ? const Icon(Icons.check_circle, color: Colors.white)
                        : Text('${i + 1}', style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold)),
                  ),
                ),
              ),
          ],
        ),
        espacio,
        TextoResultado('Celdas seleccionadas: ${_seleccionadas.length} de ${_coloresCuadricula.length}'),
      ],
    );
  }
}

class _ListaAgrupada extends StatelessWidget {
  const _ListaAgrupada();

  @override
  Widget build(BuildContext context) {
    final esquema = Theme.of(context).colorScheme;
    return _MarcoLista(
      alto: 320,
      child: CustomScrollView(
        slivers: [
          for (final (grupo, filas) in _grupos)
            SliverMainAxisGroup(
              slivers: [
                PinnedHeaderSliver(
                  child: Container(
                    width: double.infinity,
                    color: esquema.primaryContainer,
                    padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                    child: Text(grupo,
                        style: Theme.of(context).textTheme.labelLarge?.copyWith(color: esquema.onPrimaryContainer)),
                  ),
                ),
                SliverList.list(
                  children: [
                    for (final fila in filas)
                      switch (fila) {
                        _Contacto(:final nombre, :final detalle) => ListTile(
                            leading: Avatar(nombre),
                            title: Text(nombre),
                            subtitle: Text(detalle),
                            onTap: () => mostrarToast(context, 'Contacto: $nombre'),
                          ),
                        _Anuncio(:final texto) => Card(
                            margin: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
                            color: esquema.tertiaryContainer,
                            elevation: 0,
                            child: Padding(
                              padding: const EdgeInsets.all(12),
                              child: Row(
                                children: [
                                  Icon(Icons.campaign, color: esquema.onTertiaryContainer),
                                  const SizedBox(width: 12),
                                  Expanded(
                                    child: Text(texto,
                                        style: Theme.of(context)
                                            .textTheme
                                            .bodySmall
                                            ?.copyWith(color: esquema.onTertiaryContainer)),
                                  ),
                                ],
                              ),
                            ),
                          ),
                      },
                  ],
                ),
              ],
            ),
        ],
      ),
    );
  }
}

class _DeslizarEliminar extends StatefulWidget {
  const _DeslizarEliminar();

  @override
  State<_DeslizarEliminar> createState() => _DeslizarEliminarState();
}

class _DeslizarEliminarState extends State<_DeslizarEliminar> {
  final _lista = List<String>.of(_correos);

  void _eliminar(String correo) {
    final indice = _lista.indexOf(correo);
    setState(() => _lista.remove(correo));
    mostrarSnackbar(context, 'Correo eliminado', accion: 'Deshacer', alAccion: () {
      if (mounted && !_lista.contains(correo)) {
        setState(() => _lista.insert(indice.clamp(0, _lista.length), correo));
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    final esquema = Theme.of(context).colorScheme;
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        _MarcoLista(
          alto: null,
          child: Column(
            children: [
              if (_lista.isEmpty)
                Padding(
                  padding: const EdgeInsets.all(16),
                  child: Text('Bandeja vacía', style: TextStyle(color: esquema.onSurfaceVariant)),
                ),
              for (final correo in _lista)
                Dismissible(
                  key: ValueKey(correo),
                  onDismissed: (_) => _eliminar(correo),
                  background: _FondoEliminar(alineacion: Alignment.centerLeft),
                  secondaryBackground: _FondoEliminar(alineacion: Alignment.centerRight),
                  child: ListTile(
                    leading: const Icon(Icons.email),
                    title: Text(correo),
                    subtitle: const Text('Desliza para eliminar'),
                  ),
                ),
            ],
          ),
        ),
        espacio,
        OutlinedButton(
          onPressed: () => setState(() => _lista
            ..clear()
            ..addAll(_correos)),
          child: const Text('Restaurar correos'),
        ),
      ],
    );
  }
}

class _FondoEliminar extends StatelessWidget {
  const _FondoEliminar({required this.alineacion});

  final Alignment alineacion;

  @override
  Widget build(BuildContext context) {
    final esquema = Theme.of(context).colorScheme;
    return Container(
      color: esquema.errorContainer,
      alignment: alineacion,
      padding: const EdgeInsets.symmetric(horizontal: 20),
      child: Icon(Icons.delete, color: esquema.onErrorContainer),
    );
  }
}

class _ActualizarArrastrando extends StatefulWidget {
  const _ActualizarArrastrando();

  @override
  State<_ActualizarArrastrando> createState() => _ActualizarArrastrandoState();
}

class _ActualizarArrastrandoState extends State<_ActualizarArrastrando> {
  final _noticias = ['Noticia 3 · inicial', 'Noticia 2 · inicial', 'Noticia 1 · inicial'];
  int _contador = 3;

  Future<void> _actualizar() async {
    await Future<void>.delayed(const Duration(milliseconds: 1500));
    if (!mounted) return;
    final ahora = TimeOfDay.now();
    final segundos = DateTime.now().second.toString().padLeft(2, '0');
    setState(() {
      _contador++;
      _noticias.insert(0, 'Noticia $_contador · ${ahora.hour.toString().padLeft(2, '0')}:'
          '${ahora.minute.toString().padLeft(2, '0')}:$segundos');
    });
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        _MarcoLista(
          alto: 240,
          child: RefreshIndicator(
            onRefresh: _actualizar,
            child: ListView.builder(
              physics: const AlwaysScrollableScrollPhysics(),
              itemCount: _noticias.length,
              itemBuilder: (_, i) => ListTile(
                key: ValueKey(_noticias[i]),
                leading: const Icon(Icons.newspaper),
                title: Text(_noticias[i]),
              ),
            ),
          ),
        ),
        espacio,
        TextoResultado('Arrastra la lista hacia abajo. Total: ${_noticias.length} noticias'),
      ],
    );
  }
}

class _EstadoVacio extends StatefulWidget {
  const _EstadoVacio();

  @override
  State<_EstadoVacio> createState() => _EstadoVacioState();
}

class _EstadoVacioState extends State<_EstadoVacio> {
  final _tareas = ['Repasar apuntes', 'Subir la tarea'];

  @override
  Widget build(BuildContext context) {
    final esquema = Theme.of(context).colorScheme;
    final texto = Theme.of(context).textTheme;
    if (_tareas.isEmpty) {
      return SizedBox(
        width: double.infinity,
        child: Padding(
          padding: const EdgeInsets.symmetric(vertical: 16),
          child: Column(
            children: [
              CircleAvatar(
                radius: 48,
                backgroundColor: esquema.secondaryContainer,
                child: Icon(Icons.inbox, size: 48, color: esquema.onSecondaryContainer),
              ),
              const SizedBox(height: 8),
              Text('No hay tareas pendientes', style: texto.titleMedium),
              const SizedBox(height: 8),
              Text('Cuando agregues tareas aparecerán aquí.',
                  textAlign: TextAlign.center, style: texto.bodyMedium?.copyWith(color: esquema.onSurfaceVariant)),
              const SizedBox(height: 8),
              FilledButton(
                onPressed: () => setState(() => _tareas.addAll(['Repasar apuntes', 'Subir la tarea', 'Leer capítulo 3'])),
                child: const Text('Agregar tareas de ejemplo'),
              ),
            ],
          ),
        ),
      );
    }
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        for (final t in _tareas)
          ListTile(contentPadding: EdgeInsets.zero, leading: const Icon(Icons.check_circle), title: Text(t)),
        FilledButton(onPressed: () => setState(_tareas.clear), child: const Text('Vaciar lista')),
      ],
    );
  }
}

class _Pestanas extends StatefulWidget {
  const _Pestanas();

  @override
  State<_Pestanas> createState() => _PestanasState();
}

class _PestanasState extends State<_Pestanas> with SingleTickerProviderStateMixin {
  static const _pestanas = [
    ('Recientes', Icons.history, 'Archivos abiertos hoy'),
    ('Favoritos', Icons.star, 'Archivos marcados con estrella'),
    ('Archivados', Icons.archive, 'Archivos guardados para después'),
  ];
  late final TabController _controlador = TabController(length: _pestanas.length, vsync: this)
    ..addListener(() => setState(() {}));

  @override
  void dispose() {
    _controlador.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final esquema = Theme.of(context).colorScheme;
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        TabBar(
          controller: _controlador,
          tabs: [for (final (t, i, _) in _pestanas) Tab(text: t, icon: Icon(i))],
        ),
        SizedBox(
          height: 160,
          child: TabBarView(
            controller: _controlador,
            children: [
              for (final (titulo, icono, texto) in _pestanas)
                Container(
                  margin: const EdgeInsets.all(8),
                  decoration: BoxDecoration(
                    color: esquema.secondaryContainer,
                    borderRadius: BorderRadius.circular(12),
                  ),
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Icon(icono, size: 40, color: esquema.onSecondaryContainer),
                      Text(titulo,
                          style: Theme.of(context).textTheme.titleMedium?.copyWith(color: esquema.onSecondaryContainer)),
                      Text(texto,
                          style: Theme.of(context).textTheme.bodySmall?.copyWith(color: esquema.onSecondaryContainer)),
                    ],
                  ),
                ),
            ],
          ),
        ),
        TextoResultado('Pestaña actual: ${_pestanas[_controlador.index].$1}'),
      ],
    );
  }
}

/// Pantalla de detalle de un elemento de la lista vertical.
class PantallaDetalle extends StatelessWidget {
  const PantallaDetalle({super.key});

  @override
  Widget build(BuildContext context) {
    final id = ModalRoute.of(context)!.settings.arguments as int? ?? -1;
    final estado = EstadoScope.of(context);
    final elemento = estado.buscar(id);
    final texto = Theme.of(context).textTheme;

    return Scaffold(
      appBar: AppBar(
        title: const Text('Detalle del elemento'),
        actions: [
          IconButton(
            icon: const Icon(Icons.home),
            tooltip: 'Ir a la pantalla principal',
            onPressed: () => irAInicio(context),
          ),
        ],
      ),
      body: elemento == null
          ? Center(
              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  const Text('El elemento ya no existe.'),
                  const SizedBox(height: 16),
                  FilledButton(onPressed: () => Navigator.of(context).pop(), child: const Text('Regresar')),
                ],
              ),
            )
          : Padding(
              padding: const EdgeInsets.all(24),
              child: Column(
                children: [
                  Avatar(elemento.titulo, radio: 48),
                  const SizedBox(height: 16),
                  Text(elemento.titulo, style: texto.headlineMedium),
                  const SizedBox(height: 16),
                  Text(elemento.descripcion,
                      textAlign: TextAlign.center,
                      style: texto.bodyLarge?.copyWith(color: Theme.of(context).colorScheme.onSurfaceVariant)),
                  const SizedBox(height: 16),
                  SizedBox(
                    width: double.infinity,
                    child: Card.filled(
                      margin: EdgeInsets.zero,
                      child: Padding(
                        padding: const EdgeInsets.all(16),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text('Identificador: ${elemento.id}'),
                            const SizedBox(height: 4),
                            Text('Origen: ${elemento.deUsuario ? 'capturado en la Sección 1' : 'lista predeterminada'}'),
                            const SizedBox(height: 4),
                            Text('Posición en la lista: ${estado.elementos.indexOf(elemento) + 1} de ${estado.elementos.length}'),
                          ],
                        ),
                      ),
                    ),
                  ),
                  const SizedBox(height: 16),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      OutlinedButton(onPressed: () => Navigator.of(context).pop(), child: const Text('Regresar')),
                      const SizedBox(width: 12),
                      FilledButton.icon(
                        onPressed: () {
                          estado.eliminar(elemento.id);
                          mostrarSnackbar(context, 'Se eliminó “${elemento.titulo}”');
                          Navigator.of(context).pop();
                        },
                        icon: const Icon(Icons.delete),
                        label: const Text('Eliminar'),
                      ),
                    ],
                  ),
                ],
              ),
            ),
    );
  }
}
