
1. [Introducción](#introducción)
2. [Componentes principales del sistema](#componentes-principales-del-sistema)
3. [Capa de Infreaestructura](#capa-de-infreaestructura)
   1. [Modulo de API](#modulo-de-api)
   2. [Modulo Adapter](#modulo-adapter)
   3. [Modulo Integration](#modulo-integration)
4. [Capa de Modelo](#capa-de-modelo)
      1. [Infonut.java](#infonutjava)
   1. [Item.java](#itemjava)
   2. [Layout.java](#layoutjava)
   3. [Log.java](#logjava)
   4. [Modulo Structures](#modulo-structures)
5. [Capa Domain](#capa-domain)
   1. [Modulo Model](#modulo-model)
   2. [**Modulo Handler**](#modulo-handler)
   3. [**Modulo Service**](#modulo-service)
      1. [**DataExtractionService**](#dataextractionservice)
      2. [**DataTransformationService**](#datatransformationservice)
      3. [**DataLoadingService**](#dataloadingservice)
      4. [**ScaleQueueService**](#scalequeueservice)
      5. [**ImagesTransferService**](#imagestransferservice)
      6. [**ScaleDataReaderService**](#scaledatareaderservice)
      7. [**ProductComparisonService**](#productcomparisonservice)
      8. [**WriteDeleteFileService**](#writedeletefileservice)
      9. [**DeleteScaleProductService**](#deletescaleproductservice)
6. [Capa Config](#capa-config)
7. [Capa Application](#capa-application)
   1. [ScalesNetworkController](#scalesnetworkcontroller)
   2. [ProcessQueuesController](#processqueuescontroller)
   3. [DeleteProductsController](#deleteproductscontroller)
8. [Utils](#utils)
   1. [ConnectionTest](#connectiontest)
   2. [FileReaderUtil](#filereaderutil)
   3. [FileUtils](#fileutils)
   4. [GlobalStore](#globalstore)
   5. [NotesForWalmart](#notesforwalmart)
   6. [NoteWriter](#notewriter)
   7. [OperationTypeConverter](#operationtypeconverter)
   8. [ProgessEventFactory](#progesseventfactory)
   9. [ProgressResult](#progressresult)
   10. [TablaNutricionalCondition](#tablanutricionalcondition)
9. [MainClass](#mainclass)
10. [Primeros Pasos](#primeros-pasos)
    1. [⚠️ Requisitos mínimos](#️-requisitos-mínimos)
    2. [⚙️ Instalación y configuración](#️-instalación-y-configuración)
       1. [Clona el repositorio](#clona-el-repositorio)
       2. [Carga el proyecto en tu IDE (IntelliJ, Eclipse, VS Code)](#carga-el-proyecto-en-tu-ide-intellij-eclipse-vs-code)
       3. [Configura el archivo application.properties](#configura-el-archivo-applicationproperties)
       4. [Compila el proyecto con Maven](#compila-el-proyecto-con-maven)
       5. [Ejecuta la aplicación](#ejecuta-la-aplicación)


# Introducción
La aplicación de carga automática de datos desarrollada para Walmart Chile es una solución diseñada para facilitar y optimizar la sincronización de datos entre los servicios de Walmart (API) y las balanzas HPRT instaladas en tienda. Su principal objetivo es que las balanzas estén siempre actualizadas con la información más reciente de productos, precios, imágenes, entre otros elementos.

Este sistema permite automatizar la extracción de datos desde fuentes externas como son los servicios web de Walmart, tranformarlos al formato requerido por las balanzas HPRT, y realizar la carga de manera programada, garantizando consistencia y calidad en los datos de las balanzas.

El desarrollo de esta aplicación responde a la necesidad de Walmart de contar con un sistema que gestione de manera automática la información de las balanzas, reduciendo la intervención manual y manteniendo la información siempre actualizada.

# Componentes principales del sistema
La aplicación está organizada en una arquitectura de capas, diseñada para separar las responsabilidades y facilitar el mantenimiento y escalabilidad del sistema. A continuación, se describen los principales componentes y su función dentro del sistema.

# Capa de Infreaestructura
## Modulo de API
Este módulo está ubicado en la capa de `infraestructura` y es fundamental para la carga de datos, ya que encapsula todas las interacciones con los servicios externos proporcionados por Walmart. Su principal responsabilidad es ofrecer una capa de acceso simplificada para realizar las solicitudes HTTP necesarias, gestionar la autenticación, y devolver los datos en un formato usable para las demás capas de la aplicación.

Sus clases principales son:

| Clase         | Descripción                                      |
| ------------- | ----------------------------------------------- |
| `ApiService`  | Clase base que centraliza la configuración general de las solicitudes HTTP. Incluye métodos para construir las solicitudes, procesar las respuestas y gestionar errores comunes. |
| `AuthService`  | Gestiona la autenticación con los servicios de Walmart, obteniendo los tokens necesarios para realizar las peticiones autorizadas. |
| `ProductService` | Implementa las solicitudes para obtener información de productos, incluyendo detalles como códigos, descripciones, precios y atributos relacionados.  |
| `InfonutService` | Encargada de obtener la información nutricional de los productos, tales como ingredientes, valores energéticos, grasas, y otros nutrientes. |
| `LayoutService` | Permite obtener las imágenes asociadas a los productos, necesarios para la obtención de productos para balanzas de autoservicio. |
| `LogService` | Permite registrar los logs en los servicios de Walmart tras la carga exitosa o fallida de la información de la balanza. |
| `ScaleService` | Se encarga de consultar la información de las balanzas registradas en el sistema de Walmart. |

> **Nota:** Las URL de los endpoints, token de acceso y credenciales se configuran en el archivo de propiedades del sistema

## Modulo Adapter
El módulo **Adapter** es un componente de la capa de infraestructura, cuya principal función es actuar como un puente entre la aplicación y el SDK para la comunicación con las balanzas. Este módulo encapsula las interacciones con la librería ``SyncSDK``, facilitando el envío de datos, la gestión de errores, y el manejo de eventos de progreso durante las operaciones de sincronización.

Sus clases principales son:

| Clase         | Descripción                                      |
| ------------- | ----------------------------------------------- |
| `SyncManager`  | Clase principal que centraliza el acceso al SDK, asegurando la inicialización y finalización controlada de la librería nativa. |
| `SyncSDKDefine`  | Clase de utilidades que define constantes, tipos de datos, códigos de error y formatos utilizados por el SDK. |
| `SyncSDKIntf` | Es una interfáz de Java que define los métodos de la librería nativa `SyncSDK` (SDK). Esta interfáz expone las funciones necesarias para interactuar directamente con las balanzas.  |
| `SyncSDKImpl` | Es una implementación vacía de la interfaz `SyncSDKIntf`. Su principal propósito es actuar como stub o estructura base para representar las funciones del SDK. |
| `TSDKDeviceInfo` | Es una estructura de datos utilizada para representar la información de una balanza HPRT. Esta clase facilita la interacción con la librería nativa mediante JNA, ya que mapea de forma directa la estructura de datos que utiliza el SDK en C/C++ a una representación en Java. |
| `TSDKOnProgressEvent` | Es una interfaz que define un callback que es necesario durante las operaciones realizadas por el SDK. |
| `ErrorTranslator` | Es una clase de utilidad que traduce los códigos de errores numéricos devueltos por el SDK a mensajes de texto comprensibles para los usuarios y desarrolladores. |

## Modulo Integration
El módulo **Integration** forma parte de la capa de infraestructura y es responsable de las operaciones de **sincronización** de datos entre la aplicación y las balanzas. Su objetivo principal es orquestar la descarga y carga de archivos, como productos, notas, etiquetas y configuraciones mediante el uso del SDK.

Sus clases principales son:

| Clase         | Descripción                                      |
| ------------- | ----------------------------------------------- |
| `SyncDataDownloader`  | Gestiona la descarga de datos **desde la balanza hacia el sistema**, como archivos PLU, Notas, Códigos de Barra, Etiquetas y Parámetros del Sistema. Realiza la conversión de IP, gestiona el progreso de la descarga y maneja errores de conexión.|
| `SyncDataLoader`  | Gestinoa la **carga de datos desde el sistema a la balanza**, incluyendo PLU, Notas, Códigos de Barra, y otros datos. También permite la eliminación de archivos PLU en la balanza.|

# Capa de Modelo
La **capa de modelo** define las estructuras de datos y entidades que representan los objetos manejados por el sistema. Estas clases permiten mapear la información obtenida desde las fuentes externas en objetos de negocio que pueden ser procesados, transformados y cargados en las balanzas.

### Infonut.java
La clase `Infonut` representa la información nutricional asociada a un producto. Esta clase toma sus valores a partir de la información extraída de los servicios de Walmart (`InfonutService`) y contiene información nutricional, ingredientes, condiciones de mantención, entre otros datos.
>**Nota:** Existe el campo `imagenSellos`, este campo indica la combinación de sellos nutricionales que lleva el producto (1-14) o si el producto no lleva sello nutricional (0).

## Item.java
`Item` es el modelo principal que representa un producto o item del catálogo. Es fundamental para la generación de archivos PLU y para la sincronización de datos de productos en las balanzas. Se construye a partir de los datos extraidos por `ProductService`.

## Layout.java
`Layout` define la estructura visual de un producto en la balanza. Se utiliza para obtener la lista de imagenes de productos del servicio web de Walmart (`LayoutService`) y sirve para hacer un match entre los productos y aquellos productos con imagen, los cuales corresponden a los productos de una balanza de autoservicio.
>**Nota:** Los productos a cargar en una balanza de autoservicio son un match entre la lista de productos y la lista de layouts para una tienda y departamento.

## Log.java
`Log` representa un registro de evento del sistema. Se utiliza para auditar los procesos, registrar errores, advertencias o mensajes informativos generados durante la ejecución de la aplicación y la sincronización de las balanzas.
>**Nota:** Permite generar reportes de los procesos de carga y registrar estos eventos en los servicios de Walmart. 

## Modulo Structures
El módulo `structures` contiene definiciones y estructuras utilizadas por el sistema para estandarizar la generación de archivos de datos para las balanzas. Este módulo define las **cabeceras (headers) estándar** que deben utilizar los archivos de carga, asegurando la compatibilidad con el formato requerido por el SDK de HPRT.

La clase `HeadersFilesHPRT` define constantes que representan las cabeceras de los distintos archivos generados para la carga de datos en la balanza.

Las principales cabeceras definidas son:

| Método         | Descripción                                      |
| ------------- | ----------------------------------------------- |
| `PLUHeader`  | Define la cabecera con los campos requeridos para el archivo PLU. Estos campos definen los atributos completos de los productos cargados en la balanza.|
| `NoteHeader`  | Define la cabecera para los archivos de notas (Nota 1, Nota 2, Nota 3 y Nota 4).|
| `PluDeleteHeader`  | Define la cabecera del archivo para la eliminación de PLUs.|

# Capa Domain

## Modulo Model
El módulo `Model` en la capa Domain define las entidades de negocio del sistema, modelando los datos que representas las **balanzas, productos (PLU) y notas**. Además, incluye validaciones y lógica propia para asegurar que los datos cumplan las restricciones de la balanza HPRT.

| Clase         | Descripción                                      |
| ------------- | ----------------------------------------------- |
| `Notes`  | Define la estructura de una **Nota** para la balanza HPRT. Incluye validaciones para el rango de `LFCode` y normaliza el texto de `Value` eliminando acentos, caracteres especiales y asegurando que no supere los 1000 caracteres|
| `PLU`  | Representa un producto que puede cargarse en la balanza. Incluye todos los atributos requeridos por el archivo `PLU.txt` que se utiliza para cargar productos en la balanza. Además, se encarga de validar los campos segun las restricciones técnicas de la balanza.|
| `Scale`  | Modelo que representa una balanza en el sistema. La clase implementa la interfaz `Comparable` para permitir la ordenación de balanzas según la última actualización.|

## **Modulo Handler**
El módulo `Handler` implementa una serie de manejadores de lógica condicional utilizando el patrón **Chain of Responsability**.
Permite procesar y asignar valores a atributos específicos de los objetos del dominio de forma modular, secuencial y flexible.

Cada clase define una lógica particular para validar, transformar o asignar valores a los objetos según ciertas reglas del negocio relacionadas con:
* Codigos de barra.
* Fechas de perecibilidad.
* Sellos nutricionales (formatos de etiqueta).

| Clase         | Descripción                                      |
| ------------- | ----------------------------------------------- |
| `DatesHandler`  | Clase abstracta que actúa como base para la cadena de manejo de fechas. Define el metodo abstracto `handleDate` para aplicar la lógica de asignación de fechas a los objetos `PLU` e `Infonut`|
| `PerishabilityHandler`  | Implementación concreta de `DatesHandler`. Asigna valores específicos a los campos de `ProducedDateF` y `ValidDatef` del `PLU` según la restricción para la impresión de fechas de Walmart.|
| `LabelHandler`  | Clase abstracta para definir la estructura de los manejadores que se usan para asignar los formatos de etiquetas a cada producto.|
| `GraphicsHandler`  | Implementación de `LabelHandler`. Asigna valores a `label1` en función del campo `imagenSellos` de `Infonut`.|


> **Nota:** Walmart utiliza los valores `998` y `999` en el campo `diasPerecibilidad` (de `Infonut`) para controlar las fechas que se deben imprimir en la etiqueta.
> * El valor `998` indica que no debe imprimirse ninguna fecha.
> * El valor `999` indica que solo debe imprimirse fecha de producción.

>**Nota:** Walmart utiliza un identificador (id) para representar las combinaciones de sellos nutricionales (del 1 al 15), donde cada valor corresponde a un formato de etiqueta con los sellos correctos. El valor `0` indica un formato de etiqueta sin sellos nutricionales.

## **Modulo Service**
El módulo **Service** es el **núcleo funcional del sistema** y se encarga de orquestar todos los procesos críticos de la sincronización de datos entre los servicios de Walmart, las fuentes de datos externas y las balanzas HPRT.

Implementa la lógica de negocio principal, agrupando las responsabilidades en servicios especializados que cumplen tareas como la extracción, transformación, carga, comparación y eliminación de datos.

Este módulo actúa como una **capa de aplicación**, donde se coordinan las interacciones entre la infraestructura y el dominio.

**📌 Principales responsabilidades del módulo**
* ✅ **Extracción de datos desde fuentes externas** (API de productos, infonut, imágenes, etc.).
* ✅ **Transformación y validación de los datos** extraídos para cumplir con el formato requerido por las balanzas HPRT.
* ✅ **Carga de datos en las balanzas**, incluyendo archivos PLU, Notas, modelos de IA, etiquetas y otros elementos.
* ✅ **Comparación y análisis de datos** para detectar diferencias entre el sistema de origen y las balanzas.
* ✅ **Gestión de colas** para controlar el flujo de carga hacia las balanzas, priorizando según políticas definidas.
* ✅ **Eliminación de datos obsoletos** en las balanzas, asegurando la sincronización completa.
* ✅ **Transferencia de imágenes y modelos** para el sistema de clasificación inteligente y etiquetas gráficas.

### **DataExtractionService**
**📌 Descripción General**

Es el servicio responsable de extraer los datos necesarios para el proceso de sincronización, conectándose con las fuentes externas (API). 

Este servicio centraliza la lógica de extracción, garantizando que los datos necesarios estén disponibles para los procesos de **transformación** y **carga** en las balanzas.

**📌 Funcionalidades Principales**
* ✅ **Obtener información de productos** (``Item``) desde la API de productos (``ProductService``).
* ✅ **Obtener información nutricional** (``Infonut``) desde la API de información nutricional (``InfonutService``).
* ✅ **Obtener layouts** (``Layout``) de productos para autoservicio desde la API de layouts (``LayoutService``).
* ✅ **Obtener la lista de balanzas** (``Scale``) disponibles desde la API de balanzas (``ScaleService``).
* ✅ Filtrar productos para autoservicio mediante la combinación de layouts y productos (``getAutoservicioItemsDept``).
* ✅ Retornar datos en **listas de objetos Java**, transformando las respuestas JSON mediante ``Gson``.

**📌 Sus métodos son:**

| Método         | Descripción                                      | Retorno |
| ------------- | ----------------------------------------------- |-----------|
| `getInfonut(int storeNbr, int deptNbr)`  | Obtiene la lista de información nutricional para los productos de una tienda y departamento específicos. | `List<Infonut>` |
| `getItemsDept(int storeNbr, int deptNbr)`  | Obtiene los productos para una tienda y departamento específicos.| `List<Item>` |
| `getLayout(int storeNbr, int deptNbr)`  | Obtiene los layouts para los productos de una tienda y departamento específicos.| `List<Layout>` |
| `getScales(String marca)`  | Obtiene la lista de balanzas disponibles filtradas por marca.| `List<Scale>` |
| `getItems(int storeNbr, int deptNbr, boolean isAutoservicio)`  | Obtiene los productos, considerando si la balanza es de autoservicio o no. Usa `getAutoservicioItemsDept` si es autoservicio.| `List<Item>` |
| `getAutoservicioItemsDept(int storeNbr, int deptNbr)`  | Devuelve solo los productos que tienen layout asignado, usando la combinación de layout y productos.| `List<Item>` |

>**Nota:** Este servicio utiliza `ProductService`, `InfonutService`, `LayoutService` y `ScaleService`.

### **DataTransformationService**
**📌 Descripción General**

``DataTransformationService`` es el servicio responsable de transformar los datos extraídos desde las APIs de Walmart en archivos y estructuras compatibles con las balanzas HPRT.

Este servicio procesa los datos crudos (productos, información nutricional, layouts) y los convierte en archivos ``.txt`` específicos para la carga en las balanzas: archivos de productos (``PLU``) y notas (``Nota1``, ``Nota2``, ``Nota3``, ``Nota4``).

Además, aplica las reglas de negocio específicas de Walmart, como el manejo de sellos, fechas de perecibilidad y configuraciones de etiquetas.

**📌 Funcionalidades Clave**
* ✅ **Generación de archivos de notas** (``Note1``, ``Note2``, ``Note3``, ``Note4``), transformando la información nutricional (``Infonut``) en textos que serán cargados en la balanza.
* ✅ **Generación del archivo PLU**:
  * Integra la información de productos (``Item``) y su respectiva información nutricional (``Infonut``).

  * Aplica reglas de negocio mediante **handlers** (Chain of Responsibility) para asignar valores de ``Label1`` (gráficos y sellos) y fechas (``PerishabilityHandler``).
* ✅ Uso de ``BufferedWriter`` para escribir los archivos en la carpeta de pendientes (``directory.pendings``).
* ✅ Asignación dinámica de campos especiales como ``BarcodeType``, ``WeightUnit``, ``ValidDays`` y ``ProducedDateTime`` de acuerdo a las políticas de Walmart y las reglas del sistema.

**📌 Sus métodos son:**

| Método         | Descripción                                      | Retorno |
| ------------- | ----------------------------------------------- |-----------|
| `transformDataNotes(Scale scale)`  | Genera los archivos de Notas para una balanza específica, a partir de datos de `Infonut`. Aplica formatos de texto específicos definidos en `NotesForWalmart`. | `void` |
| `transformDataPLUs(Scale scale)`  | Genera el archivo PLU para una balanza específica. Combina datos de `Item` y `Infonut`, aplica lógica de etiquetas (`LabelHandler`), fechas (`DatesHandler`) y escribe el archivo final con los headers definidos en `HeadersFilesHPRT`.| `void` |
| `initializeLabelHandlerChain()`  | Crea la cadena de handlers para procesar `Label1`. Actualmente incluye solo `GraphicHandler`.| `LabelHandler` |
| `initializeDatesHandlerChain()`  | Crea la cadena de handlers para procesar las fechas (``PerishabilityHandler``).| `DatesHandler` |
| `createPLU(Item, Infonut)`  | Crea y construye un objeto ``PLU`` a partir de un ``Item`` y su correspondiente ``Infonut``. Aplica conversiones y asigna campos específicos como ``BarcodeType`` y ``WeightUnit``.| `PLU` |

**📌 Integración con otros módulos**
* ✅ Usa ``DataExtractionService`` para obtener los datos crudos desde las APIs de Walmart.
* ✅ Aplica lógica de negocio encapsulada en los **handlers**:

  * ``LabelHandler`` (con ``GraphicsHandler`` y ``NotesInfoHandler``).

  * ``DatesHandler`` (con ``PerishabilityHandler``).
* ✅ Usa ``NotesForWalmart`` para generar los textos de las notas.
* ✅ Usa ``NoteWriter`` para escribir los archivos ``.txt``.

### **DataLoadingService**
**📌 Descripción General**

``DataLoadingService`` es el servicio responsable de **cargar los archivos generados** (``PLU`` y ``Notas``) en las balanzas HPRT.

Este servicio utiliza el módulo de integración (``SyncDataLoader``) para enviar los archivos ``.txt`` generados a cada balanza mediante la red, además de registrar logs de las operaciones realizadas para su seguimiento y trazabilidad en los servicios de Walmart.

También gestiona el formateo de la fecha y hora de cada operación y la contabilización de líneas de archivo (productos cargados) para registrar correctamente los eventos.

**📌 Funcionalidades Clave**

* ✅ **Carga de archivos ``PLU``** (plu.txt) en la balanza, usando la IP de la balanza y la información del archivo generado previamente.
* ✅ **Carga de archivos de ``Notas``** (Note1.txt, Note2.txt, Note3.txt, Note4.txt), cada uno asociado a diferentes tipos de contenido (resolución, ingredientes, tabla nutricional).
* ✅ **Registro de logs de carga** en el sistema mediante ``LogService``, incluyendo detalles como cantidad de registros cargados, estado (Success), y la IP de la balanza.
* ✅ Uso de ``FileUtils`` para contar líneas de archivo y obtener métricas precisas.
* ✅ Uso de ``LocalDateTime`` y ``DateTimeFormatter`` para registrar la fecha y hora de cada carga.

**📌 Sus métodos son:**

| Método         | Descripción                                      | Retorno |
| ------------- | ----------------------------------------------- |-----------|
| `loadPlu(Scale scale)`  | Carga el archivo PLU generado (plu.txt) en la balanza correspondiente. Si la carga es exitosa, registra un log de la operación, incluyendo la cantidad de líneas del archivo. | `void` |
| `loadNotes(Scale scale)`  | Carga los archivos de notas (``Note1``, ``Note2``, ``Note3``, ``Note4``) en la balanza. Genera un log independiente para cada archivo cargado, con detalles de la cantidad de líneas y el estado.| `void` |

**📌 Colaboración con otros módulos**
* ✅ Utiliza ``SyncDataLoader`` (``infraestructura``) para cargar archivos en las balanzas.
* ✅ Utiliza ``LogService`` para registrar las operaciones de carga y actualizar el estado de la balanza.
* ✅ Utiliza ``FileUtils`` para contar las líneas de los archivos cargados.
* ✅ Usa ``DataExtractionService`` (inyectado, pero no utilizado directamente en los métodos actuales).
* ✅ Usa ``ImagesTransferService`` (inyectado, posiblemente para futuras funciones de carga de imágenes).

### **ScaleQueueService**
**📌 Descripción General**

``ScaleQueueService`` es el servicio responsable de **gestionar las colas de procesamiento de balanzas**, organizando y controlando la ejecución de cargas de datos (PLUs, Notas, Imágenes, etc.) según su prioridad.

Utiliza estructuras de control (colas y mapas) almacenadas en ``GlobalStore`` para garantizar la sincronización correcta de las balanzas y evitar duplicados o conflictos.

Este servicio orquesta las llamadas a otros servicios del sistema, como ``DataTransformationService``, ``DataLoadingService``, ``ImagesTransferService`` y ``ScaleService``.

**📌 Funcionalidades Clave**
* ✅ Añadir balanzas a **colas de prioridad** para procesar cambios detectados.
* ✅ Añadir balanzas a **colas de carga forzada** para actualizaciones completas (ej., cuando se necesita forzar la carga de datos e imágenes).
* ✅ Procesar las colas (prioridad y forzada) ejecutando los flujos de carga:

  * Transformar datos (``PLUs``, ``Notas``).

  * Cargar archivos (``PLUs``, ``Notas``).

  * Cargar imágenes (``Layouts``).
* ✅ Validar la **conexión con la balanza** antes de procesar (ping).
* ✅ Mantener un **control de duplicados** mediante mapas y sets en ``GlobalStore``.
* ✅ Registrar el estado del procesamiento y manejar errores.

**📌 Sus métodos son:**

| Método         | Descripción                                      | Retorno |
| ------------- | ----------------------------------------------- |-----------|
| `addScaleToPriorityQueue(Scale scale)`  | Añade una balanza a la cola de prioridad si no está ya presente (según ``id`` y ``lastUpdate``). Verifica la conectividad mediante ping. | `void` |
| `addScaleToForcedUpdateQueue(Scale scale)`  | Añade una balanza a la cola de carga forzada si no está ya en el set de control. Verifica la conectividad mediante ping.| `void` |
| `processPriorityQueue()`  | Procesa las balanzas de la cola de prioridad: transforma datos (``PLU``, ``Notas``), carga archivos, y elimina referencias del mapa de control de duplicados.| `void` |
| `processForcedUpdateQueue()`  | Procesa las balanzas de la cola de carga forzada: transforma datos, carga archivos, transfiere imágenes (si aplica), actualiza estados de carga (``CargaMaestra``, ``CargaLayout``), y elimina del set de duplicados.| `void` |

**📌 Colaboración con otros módulos**
* ✅ ``GlobalStore``: Gestiona las estructuras de control (colas y sets de duplicados).
* ✅ ``DataTransformationService``: Genera archivos transformados para PLU y Notas.
* ✅ ``DataLoadingService``: Carga los archivos generados en la balanza.
* ✅ ``ImagesTransferService``: Carga imágenes (layouts) en las balanzas autoservicio.
* ✅ ``ScaleService``: Actualiza los estados de carga (CargaMaestra, CargaLayout).
* ✅ ``ConnectionTest``: Verifica la conectividad de cada balanza mediante ping.
 
### **ImagesTransferService**
**📌 Descripción General**

``ImagesTransferService`` es el servicio responsable de transferir y sincronizar imágenes de productos (layouts) entre el servidor y las balanzas HPRT.

Su objetivo es asegurar que cada balanza de autoservicio tenga cargadas las imágenes correctas (asociadas a productos) de acuerdo con el layout definido en el sistema.

Utiliza solicitudes HTTP para enviar las imágenes a las balanzas y para obtener la lista de imágenes ya cargadas, evitando duplicados y cargas innecesarias.

**📌 Funcionalidades Clave**
* ✅ **Comparar el layout de la balanza con el del servidor** para determinar qué imágenes faltan por cargar.
* ✅ **Subir nuevas imágenes** mediante solicitudes HTTP POST (formato ``multipart/form-data``).
* ✅ **Listar las imágenes cargadas en la balanza** a través de una solicitud HTTP GET al endpoint /listImages.
* ✅ **Manejar reintentos automáticos** en caso de fallos de conexión o errores de red.
* ✅ **Registrar logs detallados** durante la carga de imágenes, errores y tiempos de espera.
* ✅ Realizar la carga de imágenes de forma **segura y controlada**, gestionando nombres de archivos y evitando duplicados.

**📌 Sus métodos son:**

| Método         | Descripción                                      | Retorno |
| ------------- | ----------------------------------------------- |-----------|
| `cargarLayout(Scale scale)`  | Método principal para procesar la carga de imágenes de un layout en una balanza específica. Compara las imágenes requeridas (desde el servidor) con las imágenes cargadas en la balanza. Sube las imágenes faltantes mediante ``uploadImage``. | `void` |
| `uploadImage(String server, String imagePath, String nuevoNombre)`  | Sube una imagen específica a la balanza mediante ``HTTP POST``. Gestiona el ``multipart/form-data``, control de errores, reintentos (hasta 3 veces) y logs.| `void` |
| `listarImagenes(String server)`  | Obtiene la lista actual de imágenes cargadas en la balanza a través de ``HTTP GET``. Devuelve una lista de PLUs (números de producto) cargados.| `List<Integer>` |
| `eliminarExtension(String filename)`  | Método auxiliar para obtener el código PLU (sin extensión) a partir del nombre de archivo de imagen.| `int` |

**📌 Colaboración con otros módulos**
* ✅ Usa ``DataExtractionService`` para obtener el layout de imágenes asociado a la balanza.
* ✅ Utiliza ``Scale`` para acceder a la IP de la balanza y a los datos de la tienda/departamento.
* ✅ Utiliza logs para registrar el estado y errores de la operación.
* ✅ **Interactúa directamente con el servidor HTTP de la balanza** (``endpoint`` /``upload`` y ``/listImages``).
* ✅ Utiliza la propiedad configurada ``directory.images`` para encontrar las imágenes locales.

### **ScaleDataReaderService**
**📌 Descripción General**

``ScaleDataReaderService`` es el servicio encargado de **leer los datos existentes directamente desde una balanza** HPRT, específicamente **la lista de productos cargados** (PLUs).

Este servicio se conecta a la balanza mediante la IP, descarga el archivo de productos, y lo procesa para obtener una lista de LFCode (identificadores únicos de los productos).

Provee una funcionalidad clave para validar y comparar el estado de las balanzas respecto al sistema central.

**📌 Funcionalidades Clave**
* ✅ **Descargar el archivo PLU** (``PLU_DELETE_{store}_{department}``) directamente desde la balanza mediante ``SyncDataDownloader``.
* ✅ Leer y parsear el archivo ``.txt`` descargado para extraer la lista de códigos de producto (``LFCode``).
* ✅ Validar la conectividad con la balanza mediante su IP (``scale.getIP_Balanza``).
* ✅ Manejar errores y excepciones: IP nula, problemas de conexión, errores de lectura de archivo, entre otros.
* ✅ Registrar logs informativos sobre la cantidad de productos obtenidos.
* ✅ Preparar el sistema para futuras decisiones (ejemplo: eliminar el archivo después de la lectura, actualmente pendiente de decisión).

**📌 Sus métodos son:**

| Método         | Descripción                                      | Retorno |
| ------------- | ----------------------------------------------- |-----------|
| `getProductFromScale(Scale scale)`  | Método principal. Obtiene los productos cargados en la balanza especificada (``Scale``). Descarga el archivo PLU, lo lee usando ``FileReaderUtil``, y devuelve una lista de Integer (``LFCode``). | `List<Integer>` |

**📌 Colaboración con otros módulos**
* ✅ Usa ``SyncDataDownloader`` (módulo de ``infraestructura``) para descargar el archivo de productos desde la balanza.
* ✅ Usa ``FileReaderUtil`` para leer y mapear el archivo descargado a una lista de códigos.
* ✅ Usa ``Scale`` para obtener la información de la balanza (IP, tienda, departamento).
* ✅ Registra logs usando Logger para seguimiento de operaciones.

### **ProductComparisonService**
**📌 Descripción General**

``ProductComparisonService`` es un servicio sencillo pero fundamental que se encarga de **comparar listas de productos entre el sistema central (servidor) y una balanza HPRT**.

Su objetivo principal es identificar **qué productos cargados en la balanza ya no existen en el servidor** y, por lo tanto, **deben ser eliminados** para mantener la coherencia de datos.

**📌 Funcionalidades Clave**
* ✅ Comparar la lista de productos del servidor (``List<Item>``) con la lista de productos actuales de la balanza (``List<Integer>``).
* ✅ Generar una lista de ``LFCode`` (códigos de productos) que deben ser eliminados de la balanza porque ya no están presentes en el servidor.
* ✅ Manejar errores y validar entradas:

  * Lanza excepción si las listas son nulas.

  * Lanza excepción si algún elemento de serverProducts es null.
* ✅ Registrar errores mediante logs si ocurre alguna excepción durante la comparación.
* ✅ Devolver una lista vacía en caso de errores o si no hay productos que eliminar.

**📌 Sus métodos son:**

| Método         | Descripción                                      | Retorno |
| ------------- | ----------------------------------------------- |-----------|
| `compareProducts(List<Item> serverProducts, List<Integer> scaleProducts)`  | Compara los productos del servidor (``Item``) con los cargados en la balanza (``LFCode``). Devuelve una lista de códigos que existen en la balanza pero no en el servidor (es decir, productos a eliminar). | `List<Integer>` |

### **WriteDeleteFileService**
**📌 Descripción General**

``WriteDeleteFileService`` es un servicio que genera archivos de eliminación (``pluDelete``) para balanzas HPRT.

Su función principal es **escribir un archivo** **``.txt`` que contiene los códigos de los productos que deben eliminarse en la balanza**.

Estos archivos son utilizados posteriormente por otros servicios (como ``DeleteScaleProductService``) para eliminar los productos obsoletos de la balanza.

**📌 Funcionalidades Clave**
* ✅ Generar el archivo ``pluDelete`` con el formato esperado por las balanzas HPRT:

  * La primera línea contiene el **header** (``LFCode``).

  * Cada línea siguiente contiene un código de producto (``LFCode``) que debe ser eliminado.

* ✅ Validar la entrada:

  * Lanza excepción si no hay productos para eliminar.
* ✅ Construir el nombre del archivo de eliminación basado en el nombre de la tienda y departamento (``pluDelete_{store}_{departamento}.txt``).
* ✅ Escribir el archivo en el directorio configurado (``directory.pendings``), codificado en UTF-8.
* ✅ Registrar logs informativos y de advertencia en caso de errores.
* ✅ Manejar errores:

  * Archivo no encontrado.

  * Problemas de permisos o escritura.

  * Falta de productos para eliminar.

**📌 Sus métodos son:**

| Método         | Descripción                                      | Retorno |
| ------------- | ----------------------------------------------- |-----------|
| `generateDeleteFile(List<Integer> productsToDelete, Scale scale)`  | Crea el archivo ``pluDelete`` para la balanza especificada (``Scale``), utilizando los códigos de producto en la lista ``productsToDelete``. Valida la entrada, escribe el archivo, y registra logs.| `void` |

**📌 Colaboración con otros módulos**
* ✅ Usa ``Scale`` para obtener datos de la balanza (tienda, departamento).
* ✅ Usa la estructura ``HeadersFilesHPRT.PluDeleteHeader`` para generar el header del archivo.
* ✅ El archivo generado será consumido por ``DeleteScaleProductService`` para ejecutar la eliminación en la balanza.
* ✅ Usa el directorio configurado en ``directory.pendings`` para guardar el archivo.

### **DeleteScaleProductService**
**📌 Descripción General**

``DeleteScaleProductService`` es el servicio encargado de **eliminar productos específicos de una balanza** HPRT, utilizando un archivo de eliminación (``pluDelete``) generado previamente.

Este servicio toma un archivo ``.txt`` con los códigos de productos a eliminar, se conecta a la balanza mediante su IP, y ejecuta la eliminación a través del ``SyncDataLoader``.

Una vez completado el proceso, elimina el archivo de eliminación del sistema local para mantener la limpieza y consistencia de los datos.

**📌 Funcionalidades Clave**
* ✅ Validar la existencia del archivo ``pluDelete`` y la conectividad con la balanza antes de ejecutar la eliminación.
* ✅ Ejecutar la eliminación remota de productos en la balanza mediante ``SyncDataLoader.deletePLU``.
* ✅ Registrar logs detallados durante el proceso: inicio, cantidad de productos eliminados, errores, y confirmación de eliminación.
* ✅ Contar las líneas (sin header) del archivo de eliminación para conocer la cantidad de productos a eliminar.
* ✅ Eliminar el archivo ``.txt`` local tras la eliminación exitosa, asegurando limpieza y evitando re-procesamientos.
* ✅ Manejar errores y excepciones con mensajes claros para facilitar la identificación de problemas (e.g., archivo no encontrado, IP inválida, error en la conexión).


**📌 Sus métodos son:**

| Método         | Descripción                                      | Retorno |
| ------------- | ----------------------------------------------- |-----------|
| `deleteFromScale(Scale scale)`  | Método principal que ejecuta la eliminación de productos para una balanza específica. Verifica la existencia del archivo, la conectividad con la balanza, y luego usa ``SyncDataLoader`` para eliminar los productos. Borra el archivo una vez completado. | `void` |
| `contarLineasSinHeader(Path path)`  | Cuenta la cantidad de productos a eliminar en el archivo ``pluDelete``, omitiendo la primera línea (header). Devuelve -1 si no es posible contar las líneas.| `long` |

**📌 Colaboración con otros módulos**
* ✅ Usa ``SyncDataLoader`` para ejecutar la eliminación de productos en la balanza.
* ✅ Utiliza ``ConnectionTest`` para validar la conectividad con la balanza mediante ping.
* ✅ Accede al directorio de pendientes mediante la propiedad configurada ``directory.pendings``.
* ✅ Colabora con la estructura ``Scale`` para obtener los datos de la balanza (IP, tienda, departamento).


# Capa Config
La capa ``Config`` contiene clases de configuración esenciales para el correcto funcionamiento de la aplicación.

Define ``Beans`` e inicializa componentes de infraestructura como ``RestTemplate`` y ``ThreadPools`` para la ejecución concurrente de tareas.

Esta capa es fundamental para la inyección de dependencias y la ejecución de tareas asíncronas en el sistema.

**Clases de configuración:**

1. **``AppConfig``**
   * **📌 Propósito**: Configura e inyecta el Bean ``RestTemplate`` en el contexto de Spring.
   * **📋 Descripción**: ``RestTemplate`` es una clase proporcionada por Spring que permite realizar llamadas HTTP de manera sencilla. Este Bean se inyecta en los servicios que necesitan realizar solicitudes a APIs externas, como ``ProductService``, ``InfonutService``, ``ScaleService``, etc.
   * **🧩 Bean**: ``RestTemplate restTemplate()`` → Instancia nueva de ``RestTemplate``.

2. **``SchedulerConfig``**
   * **📌 Propósito**: Configura e inyecta Thread Pools para gestionar la ejecución de tareas asíncronas y programadas.
   * **📋 Descripción**: Define dos ``ThreadPoolTaskScheduler`` para gestionar hilos concurrentes según la configuración del archivo ``application.properties``:
     - ``scaleNetThreadPoolTaskScheduler``: Para gestionar tareas relacionadas con la red de balanzas.
     - ``dataProcessingThreadPoolTaskScheduler``: Para tareas relacionadas con el procesamiento de datos.
   * **⚙️ Configuración Externa**: Usa propiedades configuradas en ``application.properties``:
     - ``scale.network.thread.pool.size``
     - ``data.processing.thread.pool.size``
    * **🧩 Beans**: 
       - ``scaleNetThreadPoolTaskScheduler()`` → Crea un ``ThreadPoolTaskScheduler`` con el tamaño definido en ``scale.network.thread.pool.size``.
       - ``dataProcessingThreadPoolTaskScheduler()`` → Crea otro ``ThreadPoolTaskScheduler`` para tareas de procesamiento de datos.

# Capa Application

La capa ``application`` tiene como objetivo orquestar el flujo de operaciones complejas, coordinando múltiples servicios del dominio pero sin contener lógica de negocio compleja por sí sola.

Esta capa incluye controladores y tareas programadas para:
* Ejecutar procesos periódicos o manuales relacionados con la sincronización de datos con las balanzas.
* Iniciar la sincronización de balanzas según su estado o configuración.
* Cooridinar la eliminación y escritura de productos en las balanzas.
* Registrar eventos de red o actualizar el estado de las balanzas.

## ScalesNetworkController
**📌 Descripción General**

La capa ``ScalesNetworkController`` es un componente que se encarga de gestionar la detección y actualización de balanzas HPRT en la red. Su función principal es interactuar con el servicio ``ScaleService`` para recuperar la lista de balanzas desde la API de Walmart, y posteriormente **clasificarlas y encolarlas** para su procesamiento según el entorno (laboratorio o producción).

Este componente **no expone endpoints HTTP**, sino que está diseñado como **tarea programada (scheduler)** que se ejecuta en intervalos definidos.

**📌 Funcionalidades Clave**

* **Descubrimiento automático de balanzas** en intervalos regulares desde un servicio externo.
* Clasificación de balanzas según:
  * Si se encuentran en modo laboratorio (``labMode``)
  * Si requieren actualización forzada (cargaMaestra o cargaLayout)
* **Encolamiento de balanzas** en colas de prioridad o de actualización forzada mediante el ``ScaleQueueService``.
* Posibilidad de almacenar localmente el listado de balanzas en un archivo ``.json``.

**🎯 Propósito en el sistema**

Este componente es **clave para la sincronización** automática del sistema con las balanzas HPRT. Su propósito principal es:
* Detectar balanzas disponibles.
* Determinar cuáles necesitan actualización.
* Agregar las balanzas a colas de procesamiento.

**📌 Metodos Principales**

| Método         | Descripción                                      
| ------------- | ----------------------------------------------- 
| `@Schedule scheduleTask()`  | Este método se ejecuta en intervalos definidos por la propiedad ``scale.network.period.milliseconds``. Su propósito es recuperar todas las balanzas disponibles y agregarlas a la cola de actualización prioritaria, permitiendo que el sistema procese en orden aquellas balanzas activas que requieran sincronización. Si el sistema está en modo laboratorio (propiedad ``labMode = True``) se delega un hilo del pool ``scaleNetThreadPoolTaskScheduler`` y se llama a ``fetchScalesFromLab()``. Esto permite encolar únicamente a aquellas balanzas de la red del laboratorio (IP's pertenecientes a ``10.105.197.0/24``). Si ``labMode = False`` se llama a ``fetchScalesFromApi()``, que encola todas las balanzas obtenidas.
| `@Schedule scheduleTaskForced()`  | Este método se ejecuta cada **60 segundos (hardcoded)** y tiene como objetivo detectar balanzas que requiere una actualización forzada, es decir, aquellas que tienen activos los indicadores ``cargaMaestra`` o ``cargaLayout``. Al igual que el método anterior, su comportamiento varía según el modo de operación (``labMode = True``, ``fetchScalesUpdateFromLab()`` y ``labMode = False``, ``fetchScalesUpdateFromApi()``).|
| `fetchScalesFromAPI(String marca)`  | Este método tiene como finalidad consultar la lista completa de balanzas asociadas a una marca específica, accediendo a través del servicio ``ScaleService``. Una vez obtenida en formato JSON, se convierte a una lista de objetos ``Scales``. Luego cada balanza es evaluada e insertada en la cola de actualización prioritaria mediante el servicio ``ScaleQueueService``.|
| `fetchScalesUpdateFromApi(String marca)`  | A diferencia del anterior, este método no considera todas las balanzas, sino que filtra aquillas que tienen marcado uno o ambos flags booleanos: ``cargaMaestra`` o ``cargaLayout``. Después de convertir la respuesta JSON a una lista de objetos ``Scale``, el método recorre la lista y añade únicamente aquellas balanzas con estos flags activados a la cola de actualización forzada, mediante el servicio ``ScaleQueueService``.|

**📌 Diferencias Clave**
| Método         | Tipo de Actualización |Filtro Aplicado|Cola Utilizada|   
| ------------- | -----------------------|---------------|--------------|
|  ``fetchScalesFromAPI`` y ``scheduleTask`` | Regular| Ninguno | Cola de actualización prioritaria |
|  ``fetchScalesUpdateFromApi`` y ``scheduleTaskForced``| Forzada (alta prioridad)| ``cargaMaestra`` o ``cargaLayout`` | Cola de actualización forzada |

**📌 Colaboración con otros módulos**
* ✅ Utiliza ``ScaleService`` para consultar la API de Walmart y obtener las balanzas por marca.
* ✅ ``ScaleQueueService`` administra las colas donde se encolan las balanzas para ser procesadas.
* ✅ ``ThreadPoolTaskScheduler`` ejecuta tareas en paralelo.
* ✅ ``application.properties`` se utiliza para definir los parámetros de configuración como la marca, directorios, modo de operación y frecuencia.

## ProcessQueuesController

**📌 Descripción General**

La clase ``ProcessQueuesController`` actúa como **coordinador del procesamiento periódico de colas de balanzas**. Su responsabilidad es detectar y despachar las balanzas que han sido previamente encoladas  —ya sea en una cola de prioridad o cola de actualización forzada— para que sean procesadas.

Este componente pertenece a la capa de ``application`` y se ejecuta automáticamente gracias a las anotaciones ``@Scheduled``, que permiten definir tareas recurrentes dentro del ciclo de vida de la aplicación Spring.

**📌 Funcionalidades Clave**
* **Procesamiento periódico de balanzas encoladas**: Este controlador supervisa y dispara la ejecución de la actualización de las balanzas que han sido agregadas a las colas, separando aquellas que siguen el flujo normal de sincronización (``priorityQueue``) de las que necesitan una carga forzada (``forcedUpdateQueue``).
* **Uso de un ``ThreadPoolTaskScheduler``**: Para garantizar que el procesamiento de balanzas no bloquee el hilo principal ni otros procesos, las tareas de procesamiento se delegan a un ``ThreadPoolTaskScheduler`` configurado para este propósito (``dataProcessingThreadPoolTaskScheduler``). Esto permite manejar múltiples actualizaciones en paralelo y de forma escalable.

**🎯 Propósito en el sistema**

El ``ProcessQueuesController`` es esencial para mantener el sistema **reactivo y automatizado**, asegurando que todas las balanzas en espera de pricesamiento sean gestionadas a tiempo sin intervención manual. Su diseño permite que tanto las actualizaciones normales  como las forzadas se manejen con eficiencia, diferenciando prioridades.

**📌 Metodos Principales**
| Método         | Descripción                                      
| ------------- | ----------------------------------------------- 
| `processQueue()`  | Se ejecuta periódicamente y se encarga de evaluar si existen balanzas en la **cola de prioridad**. Si existen, despacha su procesamiento de forma asincrónica mediante el ``ThreadPoolTaskScheduler``, invocando internamente a ``scaleQueueService.processPriorityQueue()``. Este flujo es ideal para balanzas que requieren una sincronización regular.|
| `processForcedQueue()`  | Se ejecuta cada 61 segundos (hardcoded) y revisa si hay balanzas en la **cola de actualización forzada**, es decir, aquellas marcadas con ``cargaLayout`` o ``cargaMaestra``.|

**📌 Colaboración con otros módulos**
* Utiliza el servicio ``ScaleQueueService`` encargado de gestionar las colas de las balanzas. El controlador simplemente actúa como disparador de sus métodos públicos (``processPriorityQueue()`` y ``processForcedUpdateQueue()``).
* ``ThreadPoolTaskScheduler`` permite escalar el procesamiento de la cola de prioridad, aprovechando múltiples hilos configurables según la carga esperada del sistema.

## DeleteProductsController

**📌 Descripción General**

La clase ``DeleteProductsController`` es un componente de la capa ``application`` encargado de **orquestar el proceso completo de eliminación de productos obsoletos o no deseados en una balanza** HPRT. Representa el flujo lógico que compara los productos actuales con los que tiene la balanza y elimina aquellos que ya no deberían estar presentes.

Esta clase encapsula la lógica de negocio necesaria para:
1. Obtener los productos del servidor.
2. Consultar los productos ya cargados en la balanza.
3. Comparar ambos conjuntos.
4. Generar el archivo de eliminación (``pluDelete``).
5. Ejecutar la eliminación de dichos productos de la balanza.

**📌 Funcionalidades Clave**

El método ``deleteProducts(Scale scale)`` es el único punto de entrada de este componente. A partir de una instancia de ``Scale``, se ejecuta un flujo completo de limpieza de productos en la balanza correspondiente. Las operaciones se realizan de forma secuencial, y cada etapa del proceso está debidamente encapsulada en servicios especializados.

**🎯 Propósito en el sistema**

``DeleteProductsController`` cumple un rol crucial en **mantener la coherencia entre el sistema central de productos y las balanzas físicas**. Automatiza y centraliza un proceso que, de otra forma, sería propenso a errores manuales o desincronización.

Este controlador permite:
* Prevenir que productos obsoletos permanezcan disponibles en la balanza.
* Mantener la higiene de datos en dispositivos remotos.
* Ofrecer una solución escalable, reutilizable y desacoplada para eliminar eliminación de productos.

**📌 Descripción del flujo del método ``deleteProducts``**

| Paso | Descripción | Resultado |                                      
| ---- | ------------|-----------| 
| 1. Extracción de productos del servidor | Se invoca ``dataEstractionService.getItems(...)`` para obtener todos los productos válidos según el contexto (tienda, departamento y si la balanza es de autoservicio). | ``List<Item> serverProducts``.|
| 2. Lectura de productos en la balanza | Se llama a ``scaleDataReaderService.getProductFromScale(scale)`` para obtener los identificadores (``LFCode``) de productos (PLU) actualmente presentes en las balanza. | ``List<Integer> scaleProducts``. Si ocurre un error, se retorna una lista vacía. |
| 3. Comparación de productos | Usando ``productComparisonService.compareProducts(...)``, se detectan cuáles productos **están en la balanza pero no deberían estar** según la información del servidor. | ``List<Integer> productsToDelete`` |
| 4. Generación del archivo ``pluDelete`` | Con la lista de productos a eliminar, se utiliza ``writeDeleteFileService.generateDeleteFile(...)`` para crear un archivo de eliminación en el directorio de pendientes. Este archivo sigue el formato requerido por la balanza. | ``void`` |
| 5. Eliminación en la balanza | Finalmente, se ejecuta ``deleteScaleProductService.deleteFromScale(scale)`` para transferir y aplicar la eliminación en la balanza. Si todo sale bien, también se elimina el archivo local ``pluDelete`` | ``void`` |

**📌 Colaboración con otros módulos**

* ``DataExtractionService``: Fuente de verdad de productos activos provenientes del backend.
* ``ScaleDataReaderService``: Permite obtener el estado actual de la balanza mediante una descarga remota del archivo PLU.
* ``ProductComparisonService``: Compara las dos listas y determina qué productos deben eliminarse.
* ``WriteDeleteFileService``: Genera el archivo con los productos a eliminar, en el formato específico para HPRT.
* ``DeleteScaleProductService``: Ejecuta la eliminación remota y elimina el archivo local generado.

# Utils

El módulo utils agrupa un conjunto de clases auxiliares diseñadas para funciones transversales dentro del sistema, como pruebas de conectividad, manejo de archivos o gestión de estructuras compartidas. Aunque no forman parte directa del núcleo de negocio, estas utilidades son fundamentales para garantizar la robustex, eficiencia y seguridad en la ejecución de los distintos servicios.

## ConnectionTest

La clase ``ConnectionTest`` es una utilidad encargada de verificar la conectividad de red con una IP específica utilizando una solicitud de tipo ping. Es relevante dentro del sistema porque permite comprobar si una balanza está en línea antes de intentar cualquier operación remota (como transferencias o eliminaciones).

Su método estático ``sendPingRequest(String ipAddress)`` devuelve un booleano indicando si la dirección IP es alcanzable dentro de un tiempo límite (5 segundos).

## FileReaderUtil

Esta clase proporciona una utilidad genérica para **leer archivos de texto delimitados por tabulaciones** (``.txt``) y transformar cada línea en un objeto utilizando una función de mapeo personalizada.

Es particularmente útil para manejar archivos con estructura tabular (como los generados por balanzas o APIs), ya que permite reutilizar el método ``readFileAndMap`` para transformar las líneas del archivo en objetos de cualquier tipo, como ``Item``, ``Scale``, o simplemente valores numéricos.

## FileUtils

La clase ``FileUtils`` proporciona **funciones utilitarias relacionadas con archivos**, útiles en el procesamiento de datos de balanzas. Sus métodos encapsulan tareas comunes para evitar repetición de código.

**🔧 Funcionalidades clave:**
* ``countLines(String filename):`` cuenta las líneas de un archivo ignorando la cabecera (primera línea), útil para saber cuántos productos serán procesados.

* ``getFileExtension(String fileName):`` extrae la extensión del nombre de archivo, lo cual permite validar o manipular archivos según su tipo.

## GlobalStore

La clase ``GlobalStore`` funciona como un **singleton** que actúa como **almacenamiento central en memoria** para las distintas colas y estructuras utilizadas durante el procesamiento de balanzas en el sistema. Su diseño permite compartir información de estado de forma segura entre distintos hilos, gracias al uso de estructuras **concurrentes**.

**🔧 Funcionalidad principal:**
* Centraliza el manejo de colas de procesamiento de balanzas (``PriorityQueue``, ``ConcurrentLinkedQueue``, etc.).
* Evita duplicidad de balanzas mediante mapas y sets (``scaleMap``, ``priorityMap``, ``forcedSet``).
* Facilita el acceso controlado a estas estructuras a través de getters públicos.
* La prioridad de procesamiento se determina principalmente por la fecha de última actualización de la balanza (``lastUpdateDateTime``).

## NotesForWalmart

La clase ``NotesForWalmart`` encapsula **las reglas específicas de transformación del campo ``Infonut`` para Walmart**, generando los textos necesarios que se cargarán en las balanzas como **Notas 1, 2, 3 y 4**.

**🧠 Funcionalidad principal:**

Transforma los datos nutricionales (``Infonut``) en cadenas de texto formateadas, según las convenciones utilizadas por Walmart y el formato requerido por las balanzas HPRT, incorporando etiquetas especiales (``{$0A}``) para saltos de línea y condiciones de mantención.

**🔧 Métodos principales:**
* ``resolucion``: compone el texto de resolución sanitaria, sumando texto alternativo y condiciones de mantención si aplican.

* ``ingredientes`` y ``ingredientes2``: obtienen la lista de ingredientes, dividiéndola si supera 1000 caracteres.

* ``tablaNutricional``: genera la tabla nutricional solo si no es una etiqueta propia, apoyándose en condiciones definidas por ``TablaNutricionalCondition``.

## NoteWriter

La clase ``NoteWriter`` es una **utilidad encargada de generar archivos de notas** (``Nota 1``, ``Nota 2``, etc.) en el formato requerido por las balanzas HPRT.

**🧠 Funcionalidad principal:**

Recibe un ``Map<Integer, String>`` donde la clave representa el código del producto (PLU) y el valor el contenido de la nota. A partir de esto, crea objetos ``Notes`` y escribe cada uno como una línea en un archivo ``.txt``, precedido por un encabezado definido por ``HeadersFilesHPRT.NoteHeader``.

## OperationTypeConverter

Esta clase utilitaria contiene funciones de ayuda para conversión y normalización de texto, principalmente orientadas a operaciones internas del sistema.

**🧠 Funcionalidad relevante:**

* ``removeAccents(String input):`` Elimina tildes y caracteres especiales como ``ü``, ``ñ`` y sus versiones en mayúscula, reemplazándolos por sus equivalentes sin acento. Esto permite **normalizar texto** para asegurar consistencia al momento de generar archivos o realizar comparaciones insensibles a acentos.


## ProgessEventFactory

Esta clase actúa como **fábrica de eventos de progreso** para operaciones con la balanza HPRT. Su propósito es generar una instancia de ``TSDKOnProgressEvent`` con lógica personalizada para el manejo de eventos de progreso y errores durante procesos de transferencia.

**🎯 Función principal:**
* El método estático ``create(...)`` genera un callback (``TSDKOnProgressEvent``) que interpreta los códigos de error reportados por el SDK de la balanza. El callback:

  * **Traduce el código de error** usando ``ErrorTranslator``.

  * **Registra logs** diferenciando entre errores graves, advertencias y progreso exitoso.

  * **Actualiza un** ``ProgressResult`` externo, marcando si la operación fue exitosa o no según el error recibido.

## ProgressResult

``ProgressResult`` es una clase utilitaria simple que **almacena el estado de éxito o fallo** de una operación de transferencia (como el envío de archivos o datos a la balanza).

**🎯 Función principal:**

Actúa como contenedor mutable (tipo *holder*) que puede ser actualizado por componentes como ``ProgressEventFactory`` para reflejar si una operación fue exitosa o no.

## TablaNutricionalCondition

La clase ``TablaNutricionalCondition`` cumple una función **específica y crítica** en el sistema: **generar el contenido de la tabla nutricional** que se imprimirá en las etiquetas, a partir de los datos del objeto ``Infonut``.

Esta clase centraliza la lógica para:

* Verificar qué campos nutricionales deben mostrarse (según flags tipo ``"N"``).

* Formatear cada línea de la tabla con **alineación y separación personalizada**.

* Ensamblar una tabla final que cumpla el formato de etiquetas requerido por la balanza HPRT.

# MainClass

``MainClass`` lanza la aplicación Spring Boot, habilita tareas programadas con ``@EnableScheduling``, y se asegura de cargar dinámicamente una **librería nativa** (``SyncSDK``) necesaria para la comunicación con las balanzas, ya sea en Windows o Linux.

**🛠️ Características destacadas**
1. **Anotaciones**
   * ``@SpringBootApplication``: Marca la clase como la aplicación principal de Spring Boot.
   * ``@EnableScheduling``: Activa el uso de tareas programadas con ``@Scheduled``.
   * ``@PropertySource("classpath:application-test.properties")``: Carga propiedades desde un archivo específico, útil para entornos de test o configuración particular.
2. **Bloque** ``static``
   * Verifica si la app está corriendo en un entorno de pruebas (``isRunningTest()``).
   * Si **no** está en pruebas, determina el sistema operativo y carga la librería nativa correspondiente:
     * ``SyncSDK64.dll`` en Windows
     * ``libSyncSDK.so`` en Linux
3. **Método** ``main()``
   * Lanza la aplicación usando ``SpringApplication.run(...)``.
4. **Implementación de** ``CommandLineRunner``
   * El método ``run()`` se ejecuta automáticamente luego de que la app Spring arranca.
   * Establece la propiedad del sistema ``java.library.path`` para asegurar que la DLL o ``.so`` se encuentra disponible.
   * Se protege con una validación para no ejecutarse durante pruebas (``!isRunningTest()``).
5. **Método auxiliar** ``isRunningTest()``
   * Permite omitir carga de librerías durante testeo automatizado (por ejemplo, JUnit), evitando errores si la librería no está presente.

# Primeros Pasos

Este proyecto está diseñado para ejecutarse como una aplicación Java con Spring Boot. A continuación, se detallan los pasos necesarios para preparar el entorno y ejecutar el sistema correctamente.

## ⚠️ Requisitos mínimos
* **Java 17**

* **Maven 3.8+**

* **Sistema operativo: Windows o Linux**

* **Acceso a las APIs de Walmart (credenciales y URL válidas)**

* **Librería nativa SyncSDK** (``SyncSDK64.dll`` o ``libSyncSDK.so``) **en la ruta esperada**

## ⚙️ Instalación y configuración

### Clona el repositorio
``git clone https://github.com/empresa/proyecto-sync-walmart.git``

``cd proyecto-sync-walmart``

### Carga el proyecto en tu IDE (IntelliJ, Eclipse, VS Code)
Asegúrate de seleccionar el SDK de Java 17. El proyecto utiliza Spring Boot, por lo que debe detectarse como un proyecto Maven automáticamente.

### Configura el archivo application.properties
Ubicado en src/main/resources/.

Debes configurar las siguientes propiedades:

```
# Modo de operación (true para entorno de laboratorio, false para producción)
lab.mode=true

# Ruta base del proyecto local
base.dir=C:\\ruta\\a\\tu\\proyecto

# Ruta a la librería nativa para Windows (usa .so si es Linux)
library.path=${base.dir}\\path\\a\\SyncSDK64.dll

# Directorios locales para archivos pendientes y cargados
directory.pendings=${base.dir}\\ruta\\a\\pendings\\
directory.uploads=${base.dir}\\ruta\\a\\uploads\\

# Directorio donde se encuentran los scripts de transferencia de etiquetas
script.label.transfer.path=${base.dir}\\ruta\\a\\scripts\\

# Configuración de codificación
spring.web.encoding.charset=UTF-8
spring.web.encoding.enabled=true

# Tamaño y cantidad máxima de logs
logging.level.root=INFO
logging.file.name=logs/app.log
logging.file.max-size=10MB
logging.file.max-history=30

# Formato de fecha para logs
date.time.formatter=dd-MM-yy HH:mm:ss

# Configuración del scheduler
spring.task.scheduling.pool.size=10
scale.network.thread.pool.size=5
scale.network.period.milliseconds=30000
data.processing.thread.pool.size=10
data.processing.period.milliseconds=20000

# Configuración de reintentos HTTP
maxAttempts=10

# Marca de balanzas a procesar
marca=HPRT

# Base URL del sistema de Walmart (o cliente)
wm.url.base=http://ip.backend.cl:puerto
wm.endpoint.autoservicio=http://ip.autoservicio.cl:puerto

# Endpoints para layouts
wm.endpoint.layouts=${wm.endpoint.autoservicio}/api/AutoservicioBackend/

# Endpoints y credenciales para Infonut
wm.endpoint.infonut=${wm.url.base}/apigateway/infonut/
wm.endpoint.infonut.auth=${wm.url.base}/auth/infonut
wm.endpoint.infonut.user=infonut
wm.infonut.credential.usr=usuario_infonut
wm.infonut.credential.pssw=clave_infonut

# Endpoints y credenciales para Products
wm.endpoint.product=${wm.url.base}/apigateway/product/
wm.endpoint.product.auth=${wm.url.base}/auth/product
wm.endpoint.product.user=product
wm.product.credential.usr=usuario_product
wm.product.credential.pssw=clave_product

# Endpoints y credenciales para Logs
wm.endpoint.logs=${wm.url.base}/apigateway/logs
wm.endpoint.logs.auth=${wm.url.base}/auth/logs
wm.endpoint.logs.user=scales
wm.endpoint.logs.autoservicioBackend=${wm.endpoint.autoservicio}/api/
wm.logs.credential.usr=usuario_logs
wm.logs.credential.pssw=clave_logs

# Endpoint para actualizar el estado de la balanza
wm.endpoint.updateStatus=${wm.endpoint.autoservicio}/api/Autoservicio/UpdateStatus

# Endpoints y credenciales para Scales
wm.endpoint.scales=${wm.url.base}/apigateway/scales
wm.endpoint.scales.auth=${wm.url.base}/auth/scales
wm.endpoint.scales.user=scales
wm.scales.credential.usr=usuario_scales
wm.scales.credential.pssw=clave_scales
wm.endpoint.scales.autoservicio=http://ip.autoservicio.cl:puerto/api/Autoservicio
```
> **Nota:** Si estás ejecutando el sistema en un entorno de pruebas, asegúrate de que ``spring.test.context=true`` para evitar la carga de la librería nativa durante tests.

### Compila el proyecto con Maven
```
mvn clean install
```

### Ejecuta la aplicación
Desde tu IDE o vía terminal:
```
java -jar application.java
```
