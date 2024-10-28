# LINK EJERCICIO 1:
https://github.com/Jaime-Lopez-Diaz-Parcial-1-Eventos/Ejercicio-1-lista-tareas-pendientes.git

# LINK EJERCICIO 2:
https://github.com/Jaime-Lopez-Diaz-Parcial-1-Eventos/Ejercicio-2-lista-compra.git

# LINK EJERCICIO 3:
https://github.com/Jaime-Lopez-Diaz-Parcial-1-Eventos/MisTareas2.git


Jaime López Díaz

# Lista de Compras

Lo que se ha desarrollado es una aplicación para hacer la lista de la compra en Android que permite a los usuarios gestionar una lista de productos, incluyendo el nombre, la cantidad y el precio aproximado de cada producto. La aplicación utiliza SQLite para el almacenamiento local y Firebase Firestore para la sincronización en la nube. Además, cuenta con un sistema de configuración que permite cambiar el idioma y el tema de la aplicación (modo oscuro).

## Funcionalidades

- **Agregar Producto**: Permite al usuario añadir productos con nombre, cantidad y precio aproximado a la lista.
- **Eliminar Producto**: Permite al usuario eliminar un producto de la lista.
- **Sincronización en la Nube**: Guarda los productos en Firebase Firestore y los sincroniza en dispositivos conectados.
- **Cambio de Tema**: Opción para activar o desactivar el modo oscuro.
- **Cambio de Idioma**: Opción para cambiar entre español e inglés.

## Componentes Principales

### 1. **`MainActivity`**
   - Maneja la lista de productos, permitiendo al usuario agregar y eliminar productos.
   - Conecta con SQLite y Firebase Firestore para gestionar los datos de forma local y en la nube.
   - Contiene la lógica de interfaz para mostrar la lista de productos y calcular el total de la lista.

### 2. **`SettingsActivity`**
   - Proporciona configuraciones de usuario, como cambiar el idioma de la aplicación entre inglés y español y activar/desactivar el modo oscuro.
   - Almacena las preferencias de configuración en `SharedPreferences`.

### 3. **`Product` (Modelo)**
   - Define la estructura de los datos de cada producto con atributos como `id`, `nombre`, `cantidad`, y `precio`.

### 4. **`SQLiteHelper`**
   - Implementa la base de datos local SQLite para almacenar los productos de la lista de compras en el dispositivo.

### 5. **`ProductDao`**
   - Define métodos para insertar, eliminar y consultar productos en la base de datos local SQLite.

### 6. **`ProductAdapter`**
   - Adaptador para mostrar la lista de productos en una vista de lista (ListView) dentro de `MainActivity`.

## Requisitos

- Android Studio
- Firebase Firestore
- Conexión a Internet (para la sincronización en la nube)

## Concurrencia
Para mejorar el rendimiento, la aplicación utiliza ExecutorService para manejar tareas concurrentes, como operaciones de acceso a SQLite y sincronización con Firebase. Esto permite que la aplicación se mantenga fluida, incluso cuando hay múltiples operaciones de base de datos en segundo plano.
