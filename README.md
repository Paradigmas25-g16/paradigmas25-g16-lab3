# paradigmas25-g16-lab3
## Dependencias
- Maven
- Java 21
- Apache Spark
## Compilacion
### Sin NamedEntities
```bash
$ mvn package
$ spark-submit --class FeedReaderMain target/FeedReaderMain-2.0.jar
```
### Con NamedEntities
```bash
$ mvn package
$ spark-submit --class FeedReaderMain target/FeedReaderMain-2.0.jar -ne
```
## Limpieza
```bash
$ mvn clean
```
