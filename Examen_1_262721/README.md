# Pagar Servicio de Luz

Programa en Java para buscar un cliente, ver su consumo y hacer un pago de prueba. Usa las clases del UML y las pantallas del storyboard.

## Cómo abrirlo

Necesitas JDK 24, igual que en el proyecto original.

En NetBeans, abre esta carpeta y presiona **Run Project**. La clase principal es `main.Examen_1_262721`.

Si usas una terminal con Maven:

```sh
mvn clean package
java -jar target/Examen_1_262721-1.0-SNAPSHOT.jar
```

## Cómo probarlo

1. Presiona **Pagar Servicio de Luz**.
2. Escribe `1234` para ver los tres clientes.
3. Selecciona `12348885 - Lucía Gabriela Ávila`.
4. Se muestran 350 kWh y un total de $850.00 MXN.
5. Escribe la tarjeta de prueba `1234 5678 9101 1121` y presiona **Pagar**.
6. Aparece el comprobante. Presiona **Volver** para regresar al menú.

También puedes buscar `12345678` (María Elisa Sánchez) y `12347865` (Emilio Gómez).

## Qué hace cada clase

- `Cliente`: guarda el nombre y el identificador del cliente.
- `Servicio`: guarda el número de servicio, las fechas, el consumo y el monto. También tiene la lista de los tres servicios de prueba.
- `IServicio`: indica los dos métodos para buscar servicios y consultar su consumo.
- `ComprobantePago`: guarda los datos del pago y crea el folio y la autorización de prueba.
- `EstatusPago`: contiene las opciones `APROBADO` y `RECHAZADO`.
- `ControlMenu`: crea el menú una vez, abre la pantalla de pago y permite volver al menú. Conserva los mismos servicios mientras el programa está abierto.
- `ControlPago`: busca los servicios, guarda cuál se seleccionó, crea el comprobante y pide mostrarlo. `ComprobantePago` revisa la tarjeta y `Servicio` revisa el monto y evita pagos repetidos.
- `PantallaMenu` y `PantallaPago`: muestran los datos y reciben lo que escribe o presiona el usuario.

## Cómo seguir el código

Al escribir el número, `PantallaPago` llama a `buscarSimilares` de `ControlPago`. El control llama a `buscarPorNumero` de `Servicio`. Este método usa un `for` para revisar los servicios uno por uno y regresar los que coinciden.

Al seleccionar un cliente, se busca su servicio por identificador y se muestran los datos de consumo. Al presionar **Pagar**, se revisa que la tarjeta tenga 16 dígitos y que el monto corresponda al servicio. Después se crea el comprobante, se guarda en el servicio y se muestra en la misma parte de la pantalla.

Cada pantalla tiene un solo constructor sin parámetros. El constructor carga el diseño de NetBeans y centra la ventana. `ControlMenu` crea las pantallas y les pasa el control del menú y del pago con `setControlMenu` y `setControlPago`.

Cada botón tiene su propio método, como `btnPagarActionPerformed` o `btnVolverActionPerformed`. Estos métodos contienen lo que hace el botón. `txtNoServicioCaretUpdate` busca cuando cambia el número de servicio y `lstClientesValueChanged` muestra el cliente seleccionado.

La lista, el cuadro de texto, la tarjeta y los botones están en el diseñador de NetBeans. El mismo cuadro de texto muestra primero el consumo y después el comprobante.

El `main` principal solo abre el menú. Los `main` de las dos pantallas llaman al principal para no repetir el inicio del programa.

Los archivos `.form` incluyen los controles y sus eventos. NetBeans administra `initComponents()`. La lógica que puedes leer y cambiar está en los métodos de cada botón o campo.

## Datos de prueba

El pago es simulado: no se hace ningún cargo real. El folio usa la hora actual y un número que aumenta con cada pago. La autorización es un número al azar. El comprobante solo guarda los últimos cuatro dígitos de la tarjeta.

Los pagos se guardan mientras el programa está abierto. Volver al menú conserva el comprobante y evita pagar dos veces el mismo servicio. Al cerrar el programa, los datos se reinician.

Para ejecutar las pruebas del programa, usa `mvn test`.
