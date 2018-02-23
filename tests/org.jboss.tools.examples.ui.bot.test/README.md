Executing tests:

mvn clean install -P{profile} -Dswtbot.test.skip=true -Dusage_reporting_enable=false -DdeployOnServer=true

Where
	${profile} select maven profile: WildFly11, EAP7, JavaEE7, SmokeTestsEAP7, SmokeTestsWildFly10, SmokeTestsJavaEE7 (WildFly10 profile is selected, if you don't specify profile)
	
You can specify other parameters as well(depending on profile you have selected):
WildFly profile:
-PWildFly11
-DquickstartsURLWildFly=${quickstartsURLWildFly}		-	URL for WildFly Quickstarts
-DexamplesFolderWildFly=${examplesFolderWildFly}		-	examples folder of WildFly Quickstarts

G.e.

```
mvn clean verify -DexamplesFolderWildFly=wildfly-11.0.0.Final-quickstarts -DquickstartsURLWildFly=https://github.com/wildfly/quickstart/releases/download/11.0.0.Final/wildfly-11.0.0.Final-quickstarts.zip -PWildFly -DspecificQuickstarts=helloworld-jms
```

EAP profile:
-PEAP7
-DquickstartsURLEAP=${quickstartsURLEAP}				-	URL for EAP Quickstarts
-DexamplesFolderEAP=${examplesFolderEAP}				-	examples folder of EAP Quickstarts
-Djbosstools.test.jboss-eap-7.x.url=${EAP7_URL_OF_ZIP}	-	URL of zip file with EAP server

G.e.

```
mvn clean verify -DquickstartsURLEAP=http://download.eng.brq.redhat.com/released/JBEAP-7/7.1.0/jboss-eap-7.1.0-quickstarts.zip -DexamplesFolderEAP=jboss-eap-7.1.0.GA-quickstarts -Djbosstools.test.jboss-eap-7.x.url=http://download.eng.brq.redhat.com/released/JBEAP-7/7.1.0/jboss-eap-7.1.0.zip -PEAP7 -DspecificQuickstarts=helloworld-jms
```

JavaEE profile:
-PJavaEE7
-DquickstartsURLJavaEE=${quickstartsURLJavaEE}			-	URL for JavaEE7 Quickstarts
-DexamplesFolderJavaEE=${examplesFolderJavaEE}			-	examples folder of JavaEE7 Quickstarts

G.e.

```
mvn clean verify -PJavaEE7 -DquickstartsURLJavaEE=https://github.com/javaee-samples/javaee7-samples/archive/master.zip -DexamplesFolderJavaEE=javaee7-samples-master
```

All:

-DdeployOnServer=true									-	if false, only import is performed, if true, project is imported (Default value true)

Some Launch configuration can be found in dir [./launchers](./launchers)

Notes: 
- if you execute tests from devstudio make sure you have executed ```mvn install -P<profile> ...``` first because mvn is responsible for downloading WildFly/EAP7 servers and quickstarts from given URLs (look into pom.xml for more)
- if you use -Dmaven.settings.path=~/.m2/settings.xml for local testing make sure to make backup so in case that test run is killed changes (satisfied by @DefineMavenRepository annotation requirement) in settings.xml can be reverted
- if there is a missing property look for it and its default value in pom.xml or parents projects
- for local testing it might be convenient to use  -Dreddeer.close.welcome.screen=true 
- you can run test for single or several quickstarts by using -DspecificQuickstarts=temperature-converter,xml-dom4j,helloworld-ws,websocket-hello,helloworld
- you can also use -DexamplesLocation=<pathToQuickstartsDir> that overrides -DexamplesFolder<JavaEE|EAP|WildFly> you may use something like -DexamplesLocation=./target/<quickstartsDir>
- you can also force maven to download devstudio using -Pdownload-devstudio
- in files resources/servers/*-blacklist-test-errors-regexes.json there are for specific quickstarts regular expressions describing errors that are known and documented and can be ignore/skipped in tests ( [JBDS-4638](https://issues.jboss.org/browse/JBDS-4638), [JBDS-4636](https://issues.jboss.org/browse/JBDS-4636) )
- in files resources/servers/*-blacklist there are blacklisted quickstarts such as templates and deprecated ones ( [JBDS-4637](https://issues.jboss.org/browse/JBDS-4637) )

  
Errors and Warnings will be written to standard output after executing tests.
Also All errors will be written to files inside target/reports/<exampleName>.txt
