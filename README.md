# README #

### BadTrack ###

Dead simple exception tracking for java projects.

### How to add BadTrack to your project ?###

BadTrack has a maven repository:

[ ![Download](https://api.bintray.com/packages/clearcontrol/ClearControl/BadTrack/images/download.svg) ](https://bintray.com/clearcontrol/ClearControl/BadTrack/_latestVersion)

```
<repository>
    <id>bintray-clearcontrol-ClearControl</id>
    <name>bintray</name>
    <url>http://dl.bintray.com/clearcontrol/ClearControl</url>
</repository>
```
```
<dependency>
  <groupId>net.clearcontrol</groupId>
  <artifactId>BadTrack</artifactId>
  <version>0.1.0</version>
  <type>pom</type>
</dependency>
```

### How to build the project? ###

to build the project:

    ./gradlew build

To generate eclipse project files:

    ./gradlew eclipse

To clean-up modified eclipse project files:

    ./gradlew cleanEclipse


### Author(s) ###

* royer@mpi-cbg.de
