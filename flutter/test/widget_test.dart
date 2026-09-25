import 'package:catalogo_ui/estado.dart';
import 'package:catalogo_ui/main.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
  testWidgets('La pantalla principal muestra las seis secciones', (tester) async {
    await tester.pumpWidget(CatalogoApp(estado: CatalogoEstado()));
    expect(find.text('Catálogo de UI'), findsOneWidget);
    expect(find.text('Entrada de texto'), findsOneWidget);
  });

  test('Agregar un elemento lo coloca al inicio de la lista', () {
    final estado = CatalogoEstado();
    final antes = estado.elementos.length;
    estado.agregar('Prueba', '');
    expect(estado.elementos.length, antes + 1);
    expect(estado.elementos.first.titulo, 'Prueba');
    expect(estado.elementos.first.deUsuario, isTrue);
  });
}
