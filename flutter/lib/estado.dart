import 'package:flutter/widgets.dart';

class ElementoLista {
  const ElementoLista({
    required this.id,
    required this.titulo,
    required this.descripcion,
    this.deUsuario = false,
  });

  final int id;
  final String titulo;
  final String descripcion;
  final bool deUsuario;
}

/// Preferencia elegida en la Sección 3 que modifica los textos de la Sección 5.
enum TamanoTexto {
  pequeno('Pequeño', 0.85),
  mediano('Mediano', 1.0),
  grande('Grande', 1.25);

  const TamanoTexto(this.etiqueta, this.escala);

  final String etiqueta;
  final double escala;
}

const _ciudades = [
  ('Ciudad de México', 'la Ciudad de México'),
  ('Guadalajara', 'Jalisco'),
  ('Monterrey', 'Nuevo León'),
  ('Puebla', 'Puebla'),
  ('Querétaro', 'Querétaro'),
  ('Mérida', 'Yucatán'),
  ('Oaxaca', 'Oaxaca'),
  ('Guanajuato', 'Guanajuato'),
  ('Morelia', 'Michoacán'),
  ('Veracruz', 'Veracruz'),
  ('Tijuana', 'Baja California'),
  ('León', 'Guanajuato'),
  ('Chihuahua', 'Chihuahua'),
  ('Zacatecas', 'Zacatecas'),
  ('San Luis Potosí', 'San Luis Potosí'),
  ('Toluca', 'el Estado de México'),
];

/// Estado compartido entre secciones. Lo que se captura en una sección
/// queda disponible en las demás mientras la app esté abierta.
class CatalogoEstado extends ChangeNotifier {
  CatalogoEstado() {
    for (final (ciudad, estado) in _ciudades) {
      _elementos.add(ElementoLista(
        id: _siguienteId++,
        titulo: ciudad,
        descripcion: 'Ciudad ubicada en $estado.',
      ));
    }
  }

  int _siguienteId = 1;
  final List<ElementoLista> _elementos = [];
  TamanoTexto _tamanoTexto = TamanoTexto.mediano;

  List<ElementoLista> get elementos => List.unmodifiable(_elementos);

  TamanoTexto get tamanoTexto => _tamanoTexto;

  set tamanoTexto(TamanoTexto valor) {
    _tamanoTexto = valor;
    notifyListeners();
  }

  /// Conexión Sección 1 → Sección 4: agrega un elemento capturado por el usuario.
  void agregar(String titulo, String descripcion) {
    final texto = descripcion.trim().isEmpty ? 'Elemento agregado desde la Sección 1.' : descripcion.trim();
    _elementos.insert(
      0,
      ElementoLista(id: _siguienteId++, titulo: titulo.trim(), descripcion: texto, deUsuario: true),
    );
    notifyListeners();
  }

  ElementoLista? buscar(int id) {
    for (final e in _elementos) {
      if (e.id == id) return e;
    }
    return null;
  }

  void eliminar(int id) {
    _elementos.removeWhere((e) => e.id == id);
    notifyListeners();
  }
}

/// Hace accesible el [CatalogoEstado] a todo el árbol de widgets y
/// reconstruye a quienes lo usan cuando cambia.
class EstadoScope extends InheritedNotifier<CatalogoEstado> {
  const EstadoScope({super.key, required CatalogoEstado estado, required super.child})
      : super(notifier: estado);

  static CatalogoEstado of(BuildContext context) =>
      context.dependOnInheritedWidgetOfExactType<EstadoScope>()!.notifier!;
}
