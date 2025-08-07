<h1 align="center" style="display: block; font-size: 2.5em; font-weight: bold; margin-block-start: 1em; margin-block-end: 1em;">
<a name="logo"><img align="center" src="https://github.com/GuillermoX/GuillermoX/blob/main/.github/img/PseudoTest/PseudoTestBanner.png" alt="" style="width:100%;height:100%"/></a>
  <br /><br /><strong>PseudoTest</strong>
</h1>

*PseudoTest - a simple PseudoCode to C translator made to help URV students.*

\* This README is written in spanish since most of the users (if not all) are students from Universitat Rovira i Virgili.

---
<!-- markdownlint-disable -->

## Descargar ejecutable ⬇️
Escoje el archivo ZIP correspondiente a tu sistema.
| Plataforma  | Última versión   |
|-----------|---------------|
| Windows   | [beta.1-v.1.1.1](https://github.com/GuillermoX/PseudoTest/releases/tag/v1.1.1-beta.1) |
| Linux     | [beta.1-v.1.1.1](https://github.com/GuillermoX/PseudoTest/releases/tag/v1.1.1-beta.1)|

> [!NOTE]
> La versión para Mac estará disponible pronto.

### Cómo instalar
1. Descarga la versión para tu sistema operativo de uno de los enlaces de la tabla anterior.
2. Descomprime el archivo ZIP.
3. Ejecuta la aplicación que se encuentra dentro de la carpeta PseudoTest.
> [!TIP]
> Si usas Windows puede ser que Windows Defender te bloquee la ejecución del programa. Cuando esto ocurra presiona "Más opciones" -> "Ejecutar de todos modos".

> [!CAUTION]
> No muevas la aplicación fuera de la carpeta. Si quieres añadirla al escritorio utiliza un acceso directo.

---

## Introducción y Objetivo 🎯

**PseudoTest** es una aplicación de escritorio que tiene el objetivo de ayudar a los alumnos de la asignatura de Fundamentos de Programación 1 de la Universidad Rovira i Virgiili.

El objetivo de PseudoTest es facilitar la corrección y depuración de los ejercicios llevados a cabo utilizando el pseudocódigo dado en la asignatura. Para conseguir esto PseudoTest traduce el pseudocódigo a lenguaje C para poder ser ejecutado.
> [!NOTE]
> Siempre recomiendo que la mejor manera de depurar y corregir código es mediante el análisis de este, no con "el prueba y error".
> 
> La aplicación no está destinada a ser un traductor de pseudocódigo a C para "ahorrarse" traducir el pseudocódigo en las prácticas de la asignatura. No me hago responsable del mal uso de PseudoTest.


---

## Motivación 💡
Cómo estudiante de Ingeniería Informática de la URV tuve que cursar la materia de Fundamentos de Programación 1 en mi primer año de universidad. Uno de mis mayores inconvenientes era el no poder comprobar el correcto funcionamiento de los ejercicios que realizaba hasta que no se corregían en clase. Realmente podía traducir estos ejercicios de pseudocódigo a C para testearlos, pero sentía que esto consumía demasiado tiempo.

Ahora que he acabado el 2o curso me he dado cuenta que una herramienta capaz de traducir del pseudocódigo dado en clase a C permitiría ejecutar y comprobar el funcionamiento de los ejercicios, ayudando así a muchos estudiantes como yo en su momento. Considero que PseudoTest es una herramienta que se debe de usar una vez se está convencido de que el ejercicio está acabado y funcional. De esta manera, uno no tiene que esperar a que el profesor dé la solución a un ejercicio, ya que uno mismo puede comprobar si el resultado es el esperado.

---
## Funcionalidades ✅
- Escribir, importar, modificar y guardar pseudocódigo a modo de IDE muy simplista.
- Traducir el pseudocódigo a código C.
- Uso de toda la sintaxis del pseudocódigo dada en la asignatura (a excepción de la indicada en el apartado de limitaciones).
- Implementación de las funciones de manipulación de listas y matrices. En la asignatura se dan unas funciones "ya implementadas" para utilizar en los ejercicios. En caso de ser utilizadas en PseudoTest, este añade la implementaciones de estas funciones para que se pueda ejecutar el código en C.
 A continación un ejemplo:
  ```
  algorisme
    $ Declaració variables
    var     
	t: taula[10] d'enters
	max, n_elems: enter
    fvar
	max <- 20
	
	obtenir_dades_vector(t, max, n_elems)

  falgorisme
  ```
  Este pseudocódigo se traduce al siguiente código en C:
  ```
  void obtenir_dades_vector(int dades[], int max, int* n_elems);
  int main()
  {
  	/*  -- Variable definitions  -- */
  		int t[10];
  		int max, n_elems;
  	/* ----------------------------- */
  	max = 20;
  	obtenir_dades_vector(t,  max, &( n_elems));
  }
  
  /* ------ Auxiliar Functions (DO NOT MODIFY) ------ */
  
  void obtenir_dades_vector(int dades[], int max, int* n_elems){
      *n_elems = max / 2;
      for(int i = 0; i < n_elems; i++) dades[i] = i;
  }
  /* ------------------------------------------------ */
  ```
  Se puede observar como se ha añadido la implementación de la función para que se pueda compilar y ejecutar el código C.
> [!CAUTION]
> Ya está indicado, pero cabe recalcar que no se debe modificar el código de las implementaciones de rutinas auxiliares.

  ### Atajos de teclado
  | Atajo | Acción |
  |-------|--------|
  | Ctrl + Z | Deshacer cambios |
  | Ctrl + Y | Rehacer cambios |
  | Ctrl + S | Guardar Pseudocódigo |


## Limitaciones ❌
PseudoTest está lejos de ser una herramienta perfecta, a continuación dejo la lista de limitaciones con las que cuenta.
- Hay símbolos de comparación y operación utilizados en el pseudocódigo de la asignatura que son incomodos de escribir con teclado, por lo tanto en PseudoTest se utilizan los usado en el lenguaje C:
  | Símbolo oficial | PseudoTest |
  |-----------------|------------|
  | ≠ | != |
  | ≥ | >= |
  | ≤ | <= |
  | × | * |
  
  Por lo demás se siguen utilizando los símbolos marcados por el pseudocódigo de la asignatura.
- No se pueden inicializar tablas con valores iniciales. A continuación varios ejemplos de lo que NO se puede hacer:
  ```
  taula_de_prova: taula[3] d'enters <- {3, 4, 5}
  matriu_de_prova: taula[2][2] de reals <- {{2, 4},
                                            {3, 5}}
  ```
> [!NOTE]
> Si se necesita realizar ese tipo de inicialización de tablas, se puede añadir manualmente luego de realizar la traducción a C.

- La función de escribir por pantalla sólo puede recibir valores directos de variables. A continuación dos ejemplos de lo que NO se puede hacer:
  ```
  escriure ("Total de fruites:", num_fruite + 1)
  escriure ("Num persones máxim: ", PERSONES_MAX)  $ Donde PERSONES_MAX es una constant
  ```
  Los valores no pueden ser ni operaciones ni constantes, tienen que ser un valor de una variable directo.
  
  Ejemplos de lo que SÍ se puede hacer:
  ```
  escriure ("Nombre de pomes: ", num_pomes, ", Nombre peres: ", num_peres)
  escriure ("Coordenades poble: ", latitud, longitud)
  ```

- NO se pueden escribir varias instrucciones en una misma linea utilizando ";" como separador. A continuación un ejemplo de lo que NO se puede hacer, y a continuación a su corrección:
  ```
  var1: enter; var2: real      $ Incorrecto
  $----------------------------------------
  var1: enter                  $ Correcto
  var2: real
  ```

- Para pseudocódigo que manipula ficheros NO se ha incluido una función de comprobación de la apertura del fichero, tal i como indica el pseudocódigo de la asignatura. Se ha optado por comparar el fichero virtual con NULL, imitando al código en C.

  A continuación un ejemplo de como se deben manipular los ficheros:
  ```
  variable_arxiu: arxiu
  variable_arxiu = obrir_arxiu_per_llegir("arxiu_exemple.txt")    $ Al igual que en la asignatura.
  si (variable_arxiu == NULL) llavors                             $ Diferente a la asignatura.
    escriure ("Error apertura arxiu")
  sino
    $ Continua el codi
  fsi
  ```
---
  
## Cómo generar el binario 🧑‍💻
En caso de no querer o no poder ejecutar la versión compilada para cada SO de PseudoTest, tienes la opción de ejecutar el binario .jar que se encuentra en la carpeta ``` input/ ``` del código. Para ello necesitas tener instalado el JRE (Java Runtime Enviroment) que puedes descargar de la web oficial de Java: https://www.java.com/en/download/manual.jsp

Si de todas formas lo que buscas es generar el binario a partir del código puedes ejecutar lo siguiente desde la raíz del proyecto:
```
javac -d out $(find src -name "*.java")
jar cfm PseudoTest.jar Manifest.mf -C out .
```
Esto generará el binario .jar que podrás ejecutar si tienes el JRE instalado.

---

## Muchas gracias 🙏

Quiero agradecer a todas las personas que decidan utilizar esta herramienta para mejorar en sus estudios :)

Si surge cualquier duda, no dudeis en contactar conmigo a través de Github, Linkedin o correo:\
💻 [Github](https://github.com/GuillermoX) \
🚀 [Linkedin](https://es.linkedin.com/in/guillermo-pinte%C3%B1o-cabello-435760308) \
📥 guillermo.pinteno@estudiants.urv.cat


<!-- markdownlint-enable -->



