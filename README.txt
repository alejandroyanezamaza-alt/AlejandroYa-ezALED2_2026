=========================================
  CLUB DEPORTIVO - Gestion de Reservas
  (Practica para el parcial)
=========================================

REQUISITOS
- Java JDK 17 o superior
- Maven
- MySQL / XAMPP corriendo

PASO 1: Crear la base de datos
--------------------------------
Con MySQL prendido en XAMPP, entra a phpMyAdmin (http://localhost/phpmyadmin).
Si ya existe una base "club_deportivo" vieja, primero borrala (pestaña SQL,
escribir: DROP DATABASE club_deportivo;  y Continuar).
Despues, pestaña "Importar", subi el archivo sql/club_deportivo.sql
Eso crea la base "club_deportivo" con UNA sola tabla: reservas, y carga
4 reservas de ejemplo.

PASO 2: Revisar la conexion
--------------------------------
src/main/java/com/clubdeportivo/conexion/ConexionDB.java
ya esta configurado para XAMPP (usuario root sin contraseña).

PASO 3: Compilar y ejecutar
--------------------------------
    mvn clean package
    java -jar target/ClubDeportivo-jar-with-dependencies.jar

ESTRUCTURA DEL PROYECTO
--------------------------------
pom.xml
sql/club_deportivo.sql                          -> script de la base (tabla reservas)
src/main/resources/logo.png                      -> escudo UAR
src/main/java/com/clubdeportivo/
    Main.java                                   -> arranca el programa
    conexion/ConexionDB.java                    -> conexion a la base
    modelo/Reserva.java                         -> clase Reserva
    dao/ReservaDAO.java                         -> CRUD + disponibilidad + filtro
    vista/VentanaPrincipal.java                 -> formulario + tabla + botones

ENTIDAD PRINCIPAL: Reserva
--------------------------------
idreserva, cancha, cliente, fecha (dd/mm/aaaa), hora (HH:mm)

QUE SE PUEDE HACER
--------------------------------
- AGREGAR: completar cancha (combo), cliente, fecha y hora, y apretar
  "Agregar". Si esa cancha ya esta reservada ese dia y hora, avisa y
  no deja guardar (regla de negocio).
- MODIFICAR: hacer click en una fila de la tabla (carga los datos en
  el formulario), cambiar lo que haga falta, y apretar "Modificar".
- ELIMINAR: seleccionar una fila y apretar "Eliminar" (pide confirmacion).
- CONSULTAR DISPONIBILIDAD: elegir cancha + fecha + hora y apretar el
  boton para saber si esta "DISPONIBLE" o "RESERVADA", sin guardar nada.
- FILTRAR: elegir por que dato buscar (Cliente, Cancha o Fecha), escribir
  el texto y apretar "Filtrar". La tabla muestra solo lo que coincide.
  "Quitar Filtro" vuelve a mostrar todas las reservas.

VALIDACIONES
--------------------------------
- Cliente no puede estar vacio
- Fecha tiene que tener formato dd/mm/aaaa
- Hora tiene que tener formato HH:mm
- No se puede reservar una cancha ya ocupada en esa fecha y hora

DIFERENCIA CON EL OTRO PROYECTO (Mc Boliche / TPO2)
--------------------------------
Este no es "buscar codigo y agregar a un pedido": es una gestion real
con Alta, Baja, Modificacion y Listado (CRUD completo), con un filtro
que achica lo que se ve en la tabla en vez de agregar filas, y una
regla de negocio (no duplicar reservas) que el otro proyecto no tenia.
