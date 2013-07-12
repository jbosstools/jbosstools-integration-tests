# jBPM3 Bot Tests

## How to run

To execute jBPM3 bot tests just run 

    $ mvn clean verify [-B -U -fae]

If you want to run tests which require a server 

    $ mvn clean verify [-B -U -fae] -Pas51

We recommend to run the tests on another display (Xephyr, Vnc, ...)

In case you want to build it whithout running the tests

    $ mvn clean install -DskipTests

## Importing into Eclipse IDE

For importing the project into Eclipse IDE you need:

Eclipse Kepler, JBTIS 4.1.0, SWTBot 2.1.1, and Red Deer 0.4.0
