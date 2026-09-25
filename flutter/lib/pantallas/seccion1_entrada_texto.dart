import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import '../estado.dart';
import '../secciones.dart';
import '../widgets/comunes.dart';

const _estados = [
  'Aguascalientes', 'Baja California', 'Baja California Sur', 'Campeche', 'Chiapas',
  'Chihuahua', 'Ciudad de México', 'Coahuila', 'Colima', 'Durango', 'Estado de México',
  'Guanajuato', 'Guerrero', 'Hidalgo', 'Jalisco', 'Michoacán', 'Morelos', 'Nayarit',
  'Nuevo León', 'Oaxaca', 'Puebla', 'Querétaro', 'Quintana Roo', 'San Luis Potosí',
  'Sinaloa', 'Sonora', 'Tabasco', 'Tamaulipas', 'Tlaxcala', 'Veracruz', 'Yucatán', 'Zacatecas',
];

const _componentes = [
  'Botón', 'Casilla de verificación', 'Campo de texto', 'Chip', 'Deslizador', 'Diálogo',
  'Hoja inferior', 'Interruptor', 'Lista', 'Pestañas', 'Selector de fecha', 'Snackbar', 'Tarjeta',
];

class Seccion1EntradaTexto extends StatelessWidget {
  const Seccion1EntradaTexto({super.key});

  @override
  Widget build(BuildContext context) {
    return const PantallaSeccion(
      seccion: Seccion.entradaTexto,
      elementos: [
        ElementoCatalogo(
          nombre: 'Campo de texto simple',
          componente: 'TextField + InputDecoration',
          descripcion: 'Permite escribir una línea de texto. La etiqueta indica qué dato se '
              'espera y el texto de ayuda (hint) sugiere un ejemplo mientras está vacío.',
          demo: _CampoSimple(),
        ),
        ElementoCatalogo(
          nombre: 'Campo con validación',
          componente: 'TextField(decoration: errorText)',
          descripcion: 'Revisa el contenido mientras se escribe y muestra un mensaje de error '
              'debajo del campo cuando el formato no es correcto, antes de enviar el formulario.',
          demo: _CampoValidacion(),
        ),
        ElementoCatalogo(
          nombre: 'Campo de contraseña',
          componente: 'TextField(obscureText) + IconButton',
          descripcion: 'Oculta los caracteres escritos para proteger información sensible. '
              'El ícono del ojo permite mostrar u ocultar el contenido para verificarlo.',
          demo: _CampoContrasena(),
        ),
        ElementoCatalogo(
          nombre: 'Tipos de teclado',
          componente: 'TextField(keyboardType)',
          descripcion: 'Cada campo solicita al sistema el teclado más adecuado para el dato: '
              'solo números, con arroba para correos o con el marcador telefónico.',
          demo: _TiposTeclado(),
        ),
        ElementoCatalogo(
          nombre: 'Campo multilínea',
          componente: 'TextField(minLines, maxLines, maxLength)',
          descripcion: 'Admite varias líneas para textos largos como comentarios o notas. '
              'Crece conforme se escribe y un contador indica cuántos caracteres quedan.',
          demo: _CampoMultilinea(),
        ),
        ElementoCatalogo(
          nombre: 'Campo con sugerencias',
          componente: 'Autocomplete<String>',
          descripcion: 'Muestra opciones que coinciden con lo que se va escribiendo, de modo '
              'que el usuario puede completar el dato con un toque y evitar errores de captura.',
          demo: _CampoSugerencias(),
        ),
        ElementoCatalogo(
          nombre: 'Barra de búsqueda',
          componente: 'SearchAnchor + SearchBar',
          descripcion: 'Campo especializado para buscar contenido. Al activarse despliega '
              'resultados filtrados en tiempo real y ofrece un botón para limpiar la consulta.',
          demo: _BarraBusqueda(),
        ),
        ElementoCatalogo(
          nombre: 'Conexión con la Sección 4',
          componente: 'TextField + FilledButton + ChangeNotifier compartido',
          descripcion: 'Lo que captures aquí se agrega al inicio de la lista vertical de la '
              'Sección 4. Ambas pantallas comparten el mismo estado (CatalogoEstado).',
          demo: _AgregarALista(),
        ),
      ],
    );
  }
}

class _CampoSimple extends StatefulWidget {
  const _CampoSimple();

  @override
  State<_CampoSimple> createState() => _CampoSimpleState();
}

class _CampoSimpleState extends State<_CampoSimple> {
  String _nombre = '';

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        TextField(
          onChanged: (v) => setState(() => _nombre = v),
          decoration: const InputDecoration(
            labelText: 'Nombre',
            hintText: 'Ej. Ana López',
            prefixIcon: Icon(Icons.person),
            border: OutlineInputBorder(),
          ),
        ),
        espacio,
        TextField(
          onChanged: (v) => setState(() => _nombre = v),
          decoration: const InputDecoration(labelText: 'Variante rellena', filled: true),
        ),
        espacio,
        Align(
          alignment: Alignment.centerLeft,
          child: TextoResultado(_nombre.trim().isEmpty ? 'Aún no has escrito nada.' : '¡Hola, ${_nombre.trim()}!'),
        ),
      ],
    );
  }
}

class _CampoValidacion extends StatefulWidget {
  const _CampoValidacion();

  @override
  State<_CampoValidacion> createState() => _CampoValidacionState();
}

class _CampoValidacionState extends State<_CampoValidacion> {
  static final _patron = RegExp(r'^[\w.+-]+@[\w-]+(\.[\w-]+)+$');
  String _correo = '';

  @override
  Widget build(BuildContext context) {
    final valido = _patron.hasMatch(_correo);
    final conError = _correo.isNotEmpty && !valido;
    return TextField(
      keyboardType: TextInputType.emailAddress,
      onChanged: (v) => setState(() => _correo = v.trim()),
      decoration: InputDecoration(
        labelText: 'Correo electrónico *',
        border: const OutlineInputBorder(),
        errorText: conError ? 'Formato inválido. Ejemplo: nombre@dominio.com' : null,
        helperText: valido ? 'Correo válido' : 'Campo obligatorio',
        suffixIcon: conError
            ? const Icon(Icons.error)
            : valido
                ? Icon(Icons.check_circle, color: Theme.of(context).colorScheme.primary)
                : null,
      ),
    );
  }
}

class _CampoContrasena extends StatefulWidget {
  const _CampoContrasena();

  @override
  State<_CampoContrasena> createState() => _CampoContrasenaState();
}

class _CampoContrasenaState extends State<_CampoContrasena> {
  String _clave = '';
  bool _visible = false;

  String get _nivel {
    if (_clave.isEmpty) return 'Escribe al menos 8 caracteres';
    if (_clave.length < 8) return 'Débil (${_clave.length} caracteres)';
    final tieneNumero = _clave.contains(RegExp(r'\d'));
    final tieneMayuscula = _clave.contains(RegExp(r'[A-ZÁÉÍÓÚÑ]'));
    return tieneNumero && tieneMayuscula ? 'Segura' : 'Aceptable: agrega números y mayúsculas';
  }

  @override
  Widget build(BuildContext context) {
    return TextField(
      obscureText: !_visible,
      keyboardType: TextInputType.visiblePassword,
      onChanged: (v) => setState(() => _clave = v),
      decoration: InputDecoration(
        labelText: 'Contraseña',
        border: const OutlineInputBorder(),
        prefixIcon: const Icon(Icons.lock),
        helperText: _nivel,
        suffixIcon: IconButton(
          icon: Icon(_visible ? Icons.visibility_off : Icons.visibility),
          tooltip: _visible ? 'Ocultar contraseña' : 'Mostrar contraseña',
          onPressed: () => setState(() => _visible = !_visible),
        ),
      ),
    );
  }
}

class _TiposTeclado extends StatelessWidget {
  const _TiposTeclado();

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        TextField(
          keyboardType: TextInputType.number,
          inputFormatters: [FilteringTextInputFormatter.digitsOnly, LengthLimitingTextInputFormatter(3)],
          decoration: const InputDecoration(
            labelText: 'Edad (numérico)',
            prefixIcon: Icon(Icons.cake),
            border: OutlineInputBorder(),
          ),
        ),
        espacio,
        const TextField(
          keyboardType: TextInputType.emailAddress,
          decoration: InputDecoration(
            labelText: 'Correo (teclado de correo)',
            prefixIcon: Icon(Icons.email),
            border: OutlineInputBorder(),
          ),
        ),
        espacio,
        TextField(
          keyboardType: TextInputType.phone,
          inputFormatters: [
            FilteringTextInputFormatter.allow(RegExp(r'[0-9+\- ]')),
            LengthLimitingTextInputFormatter(15),
          ],
          decoration: const InputDecoration(
            labelText: 'Teléfono (marcador)',
            prefixIcon: Icon(Icons.phone),
            border: OutlineInputBorder(),
          ),
        ),
      ],
    );
  }
}

class _CampoMultilinea extends StatelessWidget {
  const _CampoMultilinea();

  @override
  Widget build(BuildContext context) {
    return TextField(
      minLines: 3,
      maxLines: 6,
      maxLength: 200,
      keyboardType: TextInputType.multiline,
      buildCounter: (context, {required currentLength, required isFocused, maxLength}) =>
          Text('$currentLength / $maxLength caracteres'),
      decoration: const InputDecoration(
        labelText: 'Comentarios',
        alignLabelWithHint: true,
        border: OutlineInputBorder(),
      ),
    );
  }
}

class _CampoSugerencias extends StatefulWidget {
  const _CampoSugerencias();

  @override
  State<_CampoSugerencias> createState() => _CampoSugerenciasState();
}

class _CampoSugerenciasState extends State<_CampoSugerencias> {
  String _texto = '';

  @override
  Widget build(BuildContext context) {
    final coincidencias = _estados.where((e) => e.toLowerCase().contains(_texto.toLowerCase())).length;
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        LayoutBuilder(
          builder: (context, restricciones) => Autocomplete<String>(
            optionsBuilder: (valor) {
              if (valor.text.isEmpty) return const Iterable<String>.empty();
              return _estados.where((e) => e.toLowerCase().contains(valor.text.toLowerCase())).take(6);
            },
            onSelected: (v) => setState(() => _texto = v),
            optionsViewBuilder: (context, alSeleccionar, opciones) => Align(
              alignment: Alignment.topLeft,
              child: Material(
                elevation: 4,
                borderRadius: BorderRadius.circular(8),
                child: SizedBox(
                  width: restricciones.maxWidth,
                  child: ListView(
                    padding: EdgeInsets.zero,
                    shrinkWrap: true,
                    children: [
                      for (final o in opciones) ListTile(title: Text(o), onTap: () => alSeleccionar(o)),
                    ],
                  ),
                ),
              ),
            ),
            fieldViewBuilder: (context, controlador, foco, alEnviar) => TextField(
              controller: controlador,
              focusNode: foco,
              onChanged: (v) => setState(() => _texto = v),
              decoration: const InputDecoration(
                labelText: 'Estado de la República',
                suffixIcon: Icon(Icons.arrow_drop_down),
                border: OutlineInputBorder(),
              ),
            ),
          ),
        ),
        espacio,
        TextoResultado(_estados.contains(_texto) ? 'Seleccionaste: $_texto' : '$coincidencias sugerencias disponibles'),
      ],
    );
  }
}

class _BarraBusqueda extends StatefulWidget {
  const _BarraBusqueda();

  @override
  State<_BarraBusqueda> createState() => _BarraBusquedaState();
}

class _BarraBusquedaState extends State<_BarraBusqueda> {
  final _controlador = SearchController();
  String _elegido = '';

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

  List<String> get _resultados =>
      _componentes.where((c) => c.toLowerCase().contains(_controlador.text.toLowerCase())).toList();

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        SearchAnchor(
          searchController: _controlador,
          isFullScreen: false,
          viewHintText: 'Buscar componente…',
          builder: (context, controlador) => SearchBar(
            controller: controlador,
            hintText: 'Buscar componente…',
            leading: const Icon(Icons.search),
            onTap: controlador.openView,
            onChanged: (_) => controlador.openView(),
            trailing: [
              if (controlador.text.isNotEmpty)
                IconButton(
                  icon: const Icon(Icons.clear),
                  tooltip: 'Limpiar búsqueda',
                  onPressed: () => setState(() {
                    controlador.clear();
                    _elegido = '';
                  }),
                ),
            ],
          ),
          suggestionsBuilder: (context, controlador) {
            final resultados =
                _componentes.where((c) => c.toLowerCase().contains(controlador.text.toLowerCase())).toList();
            if (resultados.isEmpty) {
              return [ListTile(title: Text('Sin resultados para “${controlador.text}”'))];
            }
            return resultados.map((r) => ListTile(
                  leading: const Icon(Icons.search),
                  title: Text(r),
                  onTap: () {
                    controlador.closeView(r);
                    setState(() => _elegido = r);
                  },
                ));
          },
        ),
        espacio,
        TextoResultado(_elegido.isEmpty
            ? '${_resultados.length} de ${_componentes.length} componentes coinciden'
            : 'Abriste el resultado: $_elegido'),
      ],
    );
  }
}

class _AgregarALista extends StatefulWidget {
  const _AgregarALista();

  @override
  State<_AgregarALista> createState() => _AgregarAListaState();
}

class _AgregarAListaState extends State<_AgregarALista> {
  final _titulo = TextEditingController();
  final _descripcion = TextEditingController();

  @override
  void dispose() {
    _titulo.dispose();
    _descripcion.dispose();
    super.dispose();
  }

  void _agregar() {
    final titulo = _titulo.text.trim();
    if (titulo.isEmpty) return;
    EstadoScope.of(context).agregar(titulo, _descripcion.text);
    // Se guarda el Navigator ahora: cuando se pulse "Ver" este widget podría ya no existir.
    final navegador = Navigator.of(context);
    mostrarSnackbar(
      context,
      '“$titulo” se agregó a la Sección 4',
      accion: 'Ver',
      alAccion: () => navegador.pushNamedAndRemoveUntil(Seccion.listas.ruta, (r) => r.isFirst),
    );
    _titulo.clear();
    _descripcion.clear();
    FocusScope.of(context).unfocus();
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    final total = EstadoScope.of(context).elementos.length;
    return Column(
      children: [
        TextField(
          controller: _titulo,
          onChanged: (_) => setState(() {}),
          decoration: const InputDecoration(labelText: 'Título del elemento *', border: OutlineInputBorder()),
        ),
        espacio,
        TextField(
          controller: _descripcion,
          textInputAction: TextInputAction.done,
          onSubmitted: (_) => _agregar(),
          decoration: const InputDecoration(labelText: 'Descripción (opcional)', border: OutlineInputBorder()),
        ),
        espacio,
        Row(
          children: [
            FilledButton.icon(
              onPressed: _titulo.text.trim().isEmpty ? null : _agregar,
              icon: const Icon(Icons.playlist_add),
              label: const Text('Agregar a la lista'),
            ),
            const SizedBox(width: 12),
            Text('$total elementos', style: Theme.of(context).textTheme.bodySmall),
          ],
        ),
      ],
    );
  }
}
