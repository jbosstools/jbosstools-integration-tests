# JBoss Tools WebServices UI Bot Tests
### Using ** Maven **

 1. get jbosstools-integration-tests  ``` $ git clone https://github.com/jbosstools/jbosstools-integration-tests.git ```

 2. run (all required plugins, files and also the server will be downloaded) ``` $ mvn clean verify -Dswtbot.test.skip=false```


### Tests execution from ** Eclipse **


0. Get prerequisites:
   - JBoss server: WildFly, JBoss AS 7 or JBoss EAP 6 - setup RedDeer xml config file (see https://github.com/jboss-reddeer/reddeer/wiki/Write-complex-requirement-with-own-schema) e.g. WildFly config file
```
<?xml version="1.0" encoding="UTF-8"?>
<testrun 
	xmlns="http://www.jboss.org/NS/Req" 
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:server="http://www.jboss.org/NS/ServerReq"
	xsi:schemaLocation="http://www.jboss.org/NS/Req http://www.jboss.org/schema/reddeer/RedDeerSchema.xsd 
						http://www.jboss.org/NS/ServerReq http://www.jboss.org/schema/reddeer/JBossServerRequirements.xsd">

	<requirements>
		<server:jboss-server-requirement name="WildFly">
			<server:type>
				<server:familyWildFly version="8.x"></server:familyWildFly>
			</server:type>
			<server:runtime>/path/to/wildfly</server:runtime>
		</server:jboss-server-requirement>
	</requirements>
</testrun>
```

 - projects used to test some features: download from maven repo
   JBoss Tools Experiments/org/jboss/tools/ws/tests/org.jboss.tools.ws.ui.bot.test.resources.projects
   https://repository.jboss.org/nexus/index.html#view-repositories;jbosstools-experiments~browseindex
 - Apache CXF 2.x - create ws.properties file in directory {project_location}/properties with property ``` apache-cxf-2.x={apache-cxf-2.x-path} ```

1. Download tests and required plugins:
 - get jbosstools-integration-tests ``` $ git clone https://github.com/jbosstools/jbosstools-integration-tests.git ```
 - get Red Deer ``` $ git clone https://github.com/jboss-reddeer/reddeer ```

2. ** Now run eclipse in a new workspace (e.g. ~/jbds_test_workspace_ws) **

3. Import projects (File > Import... > Existing Projects into workspace)
 - import WebServices tests - jbosstools-integration-tests/tests/org.jboss.tools.ws.ui.bot.test
 - import plugins org.jboss.tools.ws.reddeer and org.jboss.ide.eclipse.as.reddeer
   from directory jbosstools-integration-tests/plugins/
 - import all Red Deer plugins from directory reddeer/plugins/
 

4. Install RedDeer
 - in eclipse open Install dialog (Help > Install New Software...) and install everything from
   http://download.jboss.org/jbosstools/builds/staging/RedDeer_master/all/repo/
   (see https://github.com/jboss-reddeer/reddeer/wiki/Installation)

5. Run with VM arguments:
 - to set server configuration```-Drd.config=/path/to/wildfly.xml```
 - to close Usage reporting dialog ```-Dusage_reporting_enabled=false```

6. Enjoy testing :)
