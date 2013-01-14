How to run Modeshape Bot Test?

mvn clean verify -B -U -fae -Punified.target,as-71 -Dswtbot.test.skip

Instead of as-71 you can use as-51 or nothing if you have correct swtbot.properties
