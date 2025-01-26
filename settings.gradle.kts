pluginManagement {
    repositories {
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()  // Repositorio de Google
        mavenCentral()  // Repositorio Maven Central
    }
}

rootProject.name = "App_Pedidos"
include(":app")  // Incluye el módulo app
