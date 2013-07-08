# BPEL Bot Tests

## How to run

At first, you have to install org.jboss.tools.bpel.reddeer

    $ mvn clean install

then you can run the tests as follows

    $ mvn clean verify [-B -U -fae]

If you want to run tests which require a server 

    $ mvn clean verify [-B -U -fae] -Pas51

We recommend to run the tests on another display (Xephyr, Vnc, ...)

In case you want to build it whithout running the tests

    $ mvn clean install -DskipTests

## Importing into Eclipse IDE

For importing the project into Eclipse IDE you need:

Eclipse Kepler, SWTBot 2.1.1, Red Deer 0.4.0
