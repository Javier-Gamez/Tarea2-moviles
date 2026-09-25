import 'package:flutter/material.dart';

const rutaInicio = '/';
const rutaDetalle = '/detalle';

/// Las seis secciones del catálogo; cada una es una ruta con nombre.
enum Seccion {
  entradaTexto('/entrada-texto', 1, 'Entrada de texto', 'Campos para capturar información escrita.', Icons.text_fields),
  botones('/botones', 2, 'Botones y acciones', 'Controles que ejecutan acciones al pulsarse.', Icons.smart_button),
  seleccion('/seleccion', 3, 'Elementos de selección', 'Controles para elegir opciones y valores.', Icons.check_box),
  listas('/listas', 4, 'Listas y colecciones', 'Formas de mostrar conjuntos de elementos.', Icons.list),
  informacion('/informacion', 5, 'Información y retroalimentación', 'Elementos que comunican estados y resultados.', Icons.info),
  contenedores('/contenedores', 6, 'Contenedores y estructura', 'Distribuciones que organizan la pantalla.', Icons.dashboard);

  const Seccion(this.ruta, this.numero, this.titulo, this.resumen, this.icono);

  final String ruta;
  final int numero;
  final String titulo;
  final String resumen;
  final IconData icono;
}
