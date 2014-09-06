logback-graphite
================

A logback implementation that writes metrics to a Graphite server using plain text protocol using a custom logback Appender.

To implement, put the jar on your classpath. From the base project directory:

1. mvn clean package
1. mvn install:install-file -DgroupId=com.jasonnerothin -DartifactId=logback-graphite -Dversion=1.0 -Dpackaging=jar -Dfile=target/logback-graphite-1.0.jar
1. Include the dependency in your pom file: `<dependency><groupId>com.jasonnerothin</groupId><artifactId>logback-graphite</artifactId><version>1.0</version></dependency>`

To configure:

[Configure](http://logback.qos.ch/manual/configuration.html) a logback appender like [src/main/resources/logback-test.xml](https://github.com/jasonnerothin/logback-graphite/blob/master/src/test/resources/logback-test.xml).

To run:

1. Provide two Java properties at runtime: `-Dgraphite.host=com.yourhost.name -Dgraphite.port=2003`
1. Pass the logback configuration on the command line or in your classpath: `-Dlogback.configurationFile=nameofyourfile.xml`

Ship.
