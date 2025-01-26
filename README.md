# Gestión de Productos - Aplicación Android

Este proyecto es una aplicación móvil desarrollada en **Android Studio (Java)** para la gestión de productos de un sistema de pedidos, conectado a una API REST desarrollada en PHP.

## **Características principales**

- CRUD de productos (Crear, Leer, Actualizar, Eliminar).
- Conexión con API REST utilizando Retrofit.
- Manejo de RecyclerView para listar productos.
- Diseño responsivo utilizando ConstraintLayout.
- Manejo de estados de productos (disponible, agotado, etc.).
- Organización modular del código.

---

## **Requisitos previos**

Asegúrate de tener los siguientes requisitos antes de clonar el proyecto:

- **Android Studio** (versión recomendada: Arctic Fox o superior).
- **JDK 8 o superior**.
- **XAMPP o un servidor compatible** para ejecutar la API.
- **Conexión a internet** para realizar las peticiones a la API.

---

## **Configuración del proyecto**

### 1. **Clonar el repositorio**  

   git clone https://github.com/tu_usuario/tu_repositorio.git.
   cd tu_repositorio.

### 2. **Configuracion del API**  
```bash
public static final String BASE_URL = "http://la_ip_con_la_que_inciaste_el_servidor_del_API/";

