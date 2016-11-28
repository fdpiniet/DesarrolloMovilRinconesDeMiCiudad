Desarrollo Móvil: Rincones de Mi Ciudad
=======================================

Diseñado para curso *Desarrollo Móvil*.

Descripción
-----------

### Propósito

Permite a personas residentes de alguna ciudad subir fotos públicamente de distintos rincones o esquinas de la ciudad.

Mantiene un registro separado de las fotos subidas para ser vistas o descargadas en el futuro, cuando el usuario lo desee.

### Audiencia

Todo tipo de persona.

Personas menores de edad deben contar con la autorización de sus padres para el uso de la aplicación.

### Permisos

La aplicación requiere permisos para usar la **cámara** y para escribir en **almacenamiento externo** para matener copias de las fotos tomadas dentro de la aplicación.

En *Android 6.0 y superior* es posible ver fotos publicadas por otras personas, pero se requieren permisos de uso de **cámara** y de escritura en **almacenamiento externo** si busca tomar y subir fotos, o ver fotos tomadas por el usuario de la aplicación.

### Privacidad

La aplicación genera un identificador único a la hora de instalar la aplicación y un nuevo identificador es creado al reinstalar la aplicación

La aplicación no cuenta con suficiente información para identificar únicamente o personalmente a los usuarios reales de la apliación y no tiene ninguna necesidad de hacerlo.

Esta aplicación no hace uso de los *metadatos* de las imágenes mostradas y no agrega metadatos a las fotos publicadas por usuarios. La aplicación no remueve metadatos existentes de fotos recibidas, y las fotos publicadas pueden o no pueden contener metadatos agregados por Android.

El desarrollador de la aplicación no se hace responsable por el contenido enviado en los servidores usados por la aplicación, incluyendo las fotos, sus (meta-)datos, sus descripciones y toda otra información transmitida a travez del Internet.

Desarrollo
----------

### Progreso

#### Completado

La fecha y hora de estos cambios es visible en los commits (pereza.) Ordedos por fecha, de más antiguo (arriba) a más reciente (abajo).

* Creado proyecto base y repositorio.
* Creadas opciones principales de menú: Explorar Rincones y Mis Rincones.
* Creado y configurado Floating Action Button para tomar fotos.
* Implementado manejo de permisos en Android 6.0+, necesita ser probado.
* La aplicación detecta la presencia y ausencia de la cámara en el dispositivo.
* La aplicación no toma las fotos directamente; las toma usando cualquier aplicación "*cámara*" registrada en el dispositivo. Generalmente es la aplicación cámara incluida en el sistema, pero puede ser otra. La aplicación es capáz de detectar la ausencia de dicha aplicación.
* Se inicia la captura de fotos al presionar el Floating Action Button de la "*cámara*".
* ExplorarRinconesActivity es capáz de recibir imágenes de fotos tomadas por la aplicación cámara. Informa al usuario si la captura de foto fue cancelada.
* La aplicación crea un UUID que identifica la instalación de la aplicación (no al usuario) al iniciar la aplicación y persiste a travéz de sesiones/ejecuciones/lanzamientos de aplicación.
* Si por cualquier razón mencionada anteriormente la aplicación no es capáz de tomar fotos, entonces el FloatingActionButton de la cámara para tomar fotos no aparecerá.
* Separada la funcionalidad común entre todas las Activity en una clase base: AplicacionBaseActivity. Las Activity existentes heredan de ella.
* UI de PublicarFotoActivity terminada. La Activity recibe y muestra una foto enviada desde otras Activity y propiamente maneja condiciones de error mostrando mensajes al usuario.
* Realizada limpieza sobre interfáz de usuario innecesaria en PublicarFotoActivity.
* PublicarFotoActivity es capáz de guardar las fotos tomadas en almacenamiento interno sin neceidad de pedir pemisos de escritura (es espacio de almacenamiento privado despuñes de todo.)
* La aplicación mantiene un registro SQLite en donde guarda los datos de las fotos, incluyendo descripciones y otros.
* La activity PublicarFotoActivity fue modificada de tal manera que ahora entrega un resultado directamente a ExplorarRinconesActivity de la misma manera en que la cámara de Android regresa resultados.
* Las fotos son guardadas en formato JPEG con calidad igual a 50 para no desperdiciar tanto espacio en el servidor.
* Funciona en Android 6 y superior.
* Muestra de fotos tomadas por usuario en un listado con sus respectivas fechas y descripciones.

#### Por hacer

* Implementar código HTTP (y JSON en caso que el servidor de respuestas JSON).
* Hacer UI mas "tolerable"
* Documentar!
* Enviar trabajo; compartir enlace a repositorio.

#### Screenshots

##### Explorar Rincones

![Captura de pantalla: Activity Explorar Rincones](https://dl.dropboxusercontent.com/u/92267203/Static/uni/Desarrollo%20M%C3%B3vil/RinconesDeMiCiudad/screenshots/2016-11-28-explorar-rincones.png)

##### Captura de Foto

![Captura de pantalla: Pantalla de Captura de Foto](https://dl.dropboxusercontent.com/u/92267203/Static/uni/Desarrollo%20M%C3%B3vil/RinconesDeMiCiudad/screenshots/2016-10-30-captura-de-foto.png)

##### Confirmación de Captura de Foto

![Captura de pantalla: Pantalla de Confirmaci'on de Captura de Foto](https://dl.dropboxusercontent.com/u/92267203/Static/uni/Desarrollo%20M%C3%B3vil/RinconesDeMiCiudad/screenshots/2016-10-30-confirmacion-foto.png)

##### Publicar Foto

![Captura de pantalla: Pantalla de Publicar Foto](https://dl.dropboxusercontent.com/u/92267203/Static/uni/Desarrollo%20M%C3%B3vil/RinconesDeMiCiudad/screenshots/2016-11-6-publicar-foto.png)

### Wireframes

![Wireframes de aplicación](https://dl.dropboxusercontent.com/u/92267203/Static/uni/Desarrollo%20M%C3%B3vil/RinconesDeMiCiudad/wireframes/2016-10-30-wireframe.png)