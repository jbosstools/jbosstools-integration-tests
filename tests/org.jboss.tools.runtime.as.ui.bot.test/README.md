# Runtimes (runtime-detection)
## org.jboss.tools.runtime.as.ui.bot.test
### Prerequisities
Frameworks supporting building automated Eclipse tests: [SWTBot](https://wiki.eclipse.org/SWTBot#Update_Sites),
[RedDeer](https://github.com/jboss-reddeer/reddeer/wiki/Installation)

### Tested runtimes
WildFly: 8.1, 8.0

JBossAS: 7.1.1.Final

EAP: 6.1.x, 6.1, 6.0.x, 5.1, 4.3

JPP: 6.1.x, 6.1, 6.0

EPP: 5.2, 4.3

EWP: 5.1

SOA-P: 5.3, 5.3.-standalone, 5.2, 5.2-standalone

SEAM: 2.3.x, 2.3.0.Final, 2.2.2.Final

### How to run?
Run RT_prepare_workspace in Run configuration and set up paths to all tested runtimes
Then run RT_AllTestsSuite
You probably will have a problem with some (mostly product) servers, that won't be able to stop - so edit:

	${SERVER_ROOT}/jboss-as/server/default/conf/props/jmx-console-users.properties

for these servers and uncomment row with admin rights.
    
#### How to run Projects runtimes using maven?
	mvn clean verify

#### How to run Projects and Products runtimes using maven?

Create properties file ***product-download.properties*** in folder resources (resources/product-download.properties).
Add all required urls and md5s
e.g.
	jbosstools.test.jboss-eap-6.3.url=http://eap.download.com/jboss-eap-6.3.0.zip
	jbosstools.test.jboss-eap-6.3.md5=0123456789md5
	
then run with profile ***products***

	mvn clean verify -P products
