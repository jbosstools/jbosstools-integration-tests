# JBoss Tools WebServices UI Bot Tests
### Using ** Maven **

 1. get jbosstools-integration-tests  ``` $ git clone https://github.com/jbosstools/jbosstools-integration-tests.git ```

 2. run with property swtbot.test.skip=false (all required plugins, files and also the server will be downloaded) ``` $ mvn clean verify -Dswtbot.test.skip=false```

### Tests execution from ** Eclipse **

0. Get prerequisites:
 - JBoss server: WildFly 8, JBoss AS 7 or JBoss EAP 6 - setup swtbot properties file to contain proper location of runtime ``` SERVER=WILDFLY,8.1,default,/path/to/wildfly-folder ```
 - projects used to test some features: download from maven repo
   JBoss Tools Experiments/org/jboss/tools/ws/tests/org.jboss.tools.ws.ui.bot.test.resources.projects
   https://repository.jboss.org/nexus/index.html#view-repositories;jbosstools-experiments~browseindex
 - Apache CXF 2.x - create ws.properties file in directory {project_location}/properties with property ``` apache-cxf-2.x={apache-cxf-2.x-path} ```

1. Download tests and required plugins:
 - get jbosstools-integration-tests ``` $ git clone https://github.com/jbosstools/jbosstools-integration-tests.git ```
 - get jbosstools-base ``` $ git clone https://github.com/jbosstools/jbosstools-base ```
 - get Red Deer ``` $ git clone https://github.com/jboss-reddeer/reddeer ```

2. Now run eclipse in a new workspace (e.g. ~/jbds_test_workspace_ws)

3. Import projects (File > Import... > Existing Projects into workspace)
 - import all Red Deer plugins (from directory reddeer/plugins/)
 - import plugins org.jboss.tools.ui.bot.ext.test and org.jboss.tools.ws.reddeer
   from jbosstools-integration-tests (from directory jbosstools-integration-tests/plugins/)
 - finally import WebServices tests - jbosstools-integration-tests/tests/org.jboss.tools.ws.ui.bot.test
 

4. Install SWTBot
 - in eclipse open Install dialog (Help > Install New Software...) and install everything from
   http://download.eclipse.org/technology/swtbot/releases/latest/
   (see download - http://eclipse.org/swtbot/)

5. Enjoy testing :-)
