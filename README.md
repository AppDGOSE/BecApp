BecApp
=======

Cliente Android del sistema de la AppDGOSE. Gradle.


## Instalación del entorno de desarrollo ##

1. SDK actualizado y los siguientes paquetes:
  * Android SDK Build-tools 19.0.3
  *  _Android Support Repository_
2. Clonar el repositorio:  
  `git clone git@github.com:AppDGOSE/BecApp.git`
3. Crear en la raiz del proyecto el archivo `local.properties` con el siguiente contenido:  

          sdk.dir=/path/to/sdk  

  Dónde `/path/to/sdk` es la ruta __absoluta__ hacia el SDK de Android.
4. Al ejecutar `./gradlew check` en el directorio raíz, debe concluir con el mensaje `BUILD SUCCESSFUL`.

Para probar la aplicación en un dispositivo con Android y USB debugging activado o un AVD, primero conectar el dispositivo a la PC o iniciar el emulador y ejecutar `./gradlew installDebug`.
