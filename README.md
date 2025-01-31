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
      ```bash
      git clone https://github.com/tu_usuario/tu_repositorio.git.
      cd tu_repositorio.

### 2. **Configuracion del API**  
      ```bash
      public static final String BASE_URL = "http://la_ip_con_la_que_inciaste_el_servidor_del_API/";

### 2. **Asegurate de tener las dependecias necesaria a la hora de clonarte el repositorio build.grade.kts (module :app)** 
      ```bash
      plugins {
      alias(libs.plugins.android.application)
      }
      
      android {
          namespace = "com.example.app_pedidos"
      
          compileSdk = 33
      
          defaultConfig {
              applicationId = "com.example.app_pedidos"
              minSdk = 24
              targetSdk = 33
              versionCode = 1
              versionName = "1.0"
      
              testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
          }
      
          buildTypes {
              release {
                  isMinifyEnabled = false
                  proguardFiles(
                      getDefaultProguardFile("proguard-android-optimize.txt"),
                      "proguard-rules.pro"
                  )
              }
          }
      
          compileOptions {
              sourceCompatibility = JavaVersion.VERSION_1_8
              targetCompatibility = JavaVersion.VERSION_1_8
          }
      }
      
      dependencies {
          implementation("com.google.android.material:material:1.9.0")
          implementation("androidx.appcompat:appcompat:1.6.1")
          implementation ("com.google.code.gson:gson:2.8.8")
          implementation("androidx.constraintlayout:constraintlayout:2.1.4")
          testImplementation("junit:junit:4.13.2")
          androidTestImplementation("androidx.test.ext:junit:1.1.3")
          androidTestImplementation("androidx.espresso:espresso-core:3.5.1")
          implementation("com.squareup.retrofit2:retrofit:2.9.0") // Retrofit
          implementation("com.squareup.retrofit2:converter-gson:2.9.0") // Gson Converter para manejar JSON
          implementation ("androidx.recyclerview:recyclerview:1.2.1")  // Asegúrate de tener esta dependencia
      
      }
### 3. **Asegurate de tener configurado el siguiente archivo (solo con la ip de tu dispositivo)** 
         ```bash
         app/res/xml/network_security_config.xml

- **Si tienen alguna duda verifiquen el repositorio del API y si aun no entienen pregunten 😉**
