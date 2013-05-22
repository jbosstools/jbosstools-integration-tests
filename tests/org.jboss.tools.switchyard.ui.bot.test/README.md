# SwitchYard Bot Tests

## How to run

At first, you have to install org.jboss.tools.switchyard.reddeer

    $ mvn clean install

then you can run the tests as follows

    $ mvn clean verify -B -U -fae -Punified.target

If you want to run tests which require a server 

    $ mvn clean verify -B -U -fae -Punified.target -Pswitchyard

We recommend to run the tests on another display (Xephyr, Vnc, ...)
