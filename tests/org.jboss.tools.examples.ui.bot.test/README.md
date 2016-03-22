Executing tests:

mvn clean install -P{profile} -Dswtbot.test.skip=true -Dusage_reporting_enable=false -DdeployOnServer=true

Where
	${profile} select maven profile: WildFly10, EAP7, JavaEE7, SmokeTestsEAP7, SmokeTestsWildFly10, SmokeTestsJavaEE7 (WildFly10 profile is selected, if you don't specify profile)
	
You can specify other parameters as well(depending on profile you have selected):
WildFly profile:
-DquickstartsURLWildFly=${quickstartsURLWildFly}		-	URL for WildFly Quickstarts
-DexamplesFolderWildFly=${examplesFolderWildFly}		-	examples folder of WildFly Quickstarts

EAP profile:
-DquickstartsURLEAP=${quickstartsURLEAP}				-	URL for EAP Quickstarts
-DexamplesFolderEAP=${examplesFolderEAP}				-	examples folder of EAP Quickstarts
-Djbosstools.test.jboss-eap-7.x.url=${EAP7_URL_OF_ZIP}	-	URL of zip file with EAP server

JavaEE profile:
-DquickstartsURLJavaEE=${quickstartsURLJavaEE}			-	URL for JavaEE7 Quickstarts
-DexamplesFolderJavaEE=${examplesFolderJavaEE}			-	examples folder of JavaEE7 Quickstarts

All:
-DdeployOnServer=true									-	if false, only import is performed, if true, project is imported (Default value true)


  
  
Errors and Warnings will be written to standard output after executing tests.
Also All errors will be written to files inside target/reports/<exampleName>.txt
