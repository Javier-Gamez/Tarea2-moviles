import 'package:flutter/material.dart';

import '../estado.dart';
import '../secciones.dart';
import '../widgets/comunes.dart';

/// Pantalla principal: presentación de la app y acceso a las seis secciones.
class PantallaInicio extends StatelessWidget {
  const PantallaInicio({super.key});

  @override
  Widget build(BuildContext context) {
    final esquema = Theme.of(context).colorScheme;
    final texto = Theme.of(context).textTheme;
    final estado = EstadoScope.of(context);

    return Scaffold(
      appBar: AppBar(title: const Text('Catálogo de UI')),
      drawer: const MenuLateral(rutaActual: rutaInicio),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          Card(
            margin: EdgeInsets.zero,
            color: esquema.primaryContainer,
            elevation: 0,
            child: Padding(
              padding: const EdgeInsets.all(20),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text('Catálogo de elementos de interfaz',
                      style: texto.headlineSmall?.copyWith(color: esquema.onPrimaryContainer)),
                  const SizedBox(height: 8),
                  Text('Versión: Flutter',
                      style: texto.labelLarge?.copyWith(fontWeight: FontWeight.bold, color: esquema.onPrimaryContainer)),
                  const SizedBox(height: 8),
                  Text(
                    'Explora los componentes básicos de una interfaz móvil. Cada elemento incluye '
                    'su nombre, una breve explicación y una demostración interactiva.',
                    style: texto.bodyMedium?.copyWith(color: esquema.onPrimaryContainer),
                  ),
                ],
              ),
            ),
          ),
          const SizedBox(height: 20),
          Text('Secciones', style: texto.titleMedium),
          const SizedBox(height: 12),
          for (final s in Seccion.values) ...[
            Card(
              margin: EdgeInsets.zero,
              clipBehavior: Clip.antiAlias,
              child: InkWell(
                onTap: () => irASeccion(context, s.ruta),
                child: Padding(
                  padding: const EdgeInsets.all(16),
                  child: Row(
                    children: [
                      CircleAvatar(
                        radius: 22,
                        backgroundColor: esquema.secondaryContainer,
                        child: Icon(s.icono, color: esquema.onSecondaryContainer),
                      ),
                      const SizedBox(width: 16),
                      Expanded(
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text('Sección ${s.numero}', style: texto.labelMedium?.copyWith(color: esquema.primary)),
                            Text(s.titulo, style: texto.titleMedium),
                            Text(s.resumen, style: texto.bodySmall?.copyWith(color: esquema.onSurfaceVariant)),
                          ],
                        ),
                      ),
                      const Icon(Icons.arrow_forward),
                    ],
                  ),
                ),
              ),
            ),
            const SizedBox(height: 12),
          ],
          Card(
            margin: const EdgeInsets.only(top: 8),
            color: esquema.tertiaryContainer,
            elevation: 0,
            child: Padding(
              padding: const EdgeInsets.all(16),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Row(children: [
                    Icon(Icons.link, color: esquema.onTertiaryContainer),
                    const SizedBox(width: 8),
                    Text('Conexiones entre secciones',
                        style: texto.titleSmall?.copyWith(color: esquema.onTertiaryContainer)),
                  ]),
                  const SizedBox(height: 6),
                  Text(
                    '• Sección 1 → 4: el texto capturado se agrega a la lista vertical '
                    '(actualmente hay ${estado.elementos.length} elementos).',
                    style: texto.bodySmall?.copyWith(color: esquema.onTertiaryContainer),
                  ),
                  Text(
                    '• Sección 3 → 5: el tamaño de texto elegido (${estado.tamanoTexto.etiqueta}) '
                    'cambia los ejemplos de tipografía.',
                    style: texto.bodySmall?.copyWith(color: esquema.onTertiaryContainer),
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }
}
