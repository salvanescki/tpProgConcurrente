# Trabajo Práctico
## Materia : Programación Concurrente
## Integrantes:

| Nombre y Apellido     | Mail                            | Usuario Github   |
|-----------------------|---------------------------------|------------------|
| Cristopher Bechtholdt | cristopherbechtholdt@gmail.com  | CrisIanBech      |
| Nicolás Salvanescki   | salvanescki.unq@gmail.com       | salvanescki      |
| Nicolás Salvatore     | nicolas.salvatore@alu.unq.edu.ar| Unknow-Error     |
------------------------------------------------------------------------------

Código listo para la entrega, el informe aún está en proceso

## Ejecución

```sh
java -jar tp.jar k numWorkers bufferSize path dataSetPath
```

Siendo path la ruta en la que se encuentra el archivo de test o imagen. Si se encuentra en el mismo directorio que el **jar**, se puede escribir el nombre de la siguiente manera: "mnist_test.**csv**" o "imagen.**png**". Es importante incluir el formato, ya que el programa detecta automáticamente el modo que debe ejecutar en base al mismo.

## Versión de Java

Otro detalle importante es contar con la versión del jdk 21.0.5 (temurin) para poder ejecutar el **jar**. Se tiene que configurar la misma en el path del sistema para que pueda ejecutarse sin problemas con el comando de arriba. Esto puede hacerse agregando como primera entrada del path del sistema el directorio siguiente:

```sh
C:\Program Files\Eclipse Adoptium\jdk-21.0.4.7-hotspot\bin
```

Esto reemplazando C:\Program Files\ por el disco y el subdirectorio en el que se haya instalado el jdk.
