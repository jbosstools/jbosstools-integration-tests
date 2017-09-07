package org.jboss.tools.mylyn.ui.bot.test;

/*
 * Prototype test for Mylyn/Jenkins plugin
 * 
 * 
 */
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.mylyn.reddeer.TestSupport;
import org.jboss.tools.mylyn.reddeer.view.MylynBuildView;
import org.jboss.tools.mylyn.reddeer.wizard.BuildServerDialog;
import org.junit.Test;

public class MylynTestJenkins {

	protected final Logger log = Logger.getLogger(this.getClass());

	protected ArrayList<String> expectedMylynElements = new ArrayList<String>();	
	protected final String TASKNAME = "a sample task in Mylyn";
	protected final String TASKNOTE = "a sample note for a sample task in Mylyn";
	/* Could not originally use machydra Jenkins server due to - https://issues.jboss.org/browse/JBDS-2965 */
	/* This server proved to be unreliable: protected final String SERVERURL = "http://ci.jruby.org/"
	 * so the test was changed to use the JBDS QE Jenkins - machydra (August 2014)
	 * 
	 * mvn clean install -Dreddeer.close.shells=false -Dswtbot.test.skip=false -Dusage_reporting_enabled=false 
	 * -Dreddeer.close.welcome.en=true -Djenkins.server="http://machydra.lab.eng.brq.redhat.com:8080" 
	 * -Djenkins.job="jbosstools.mylyn.bot.tests.poc" -Dauth.jenkins.server="https://jenkins-ldimaggi.rhcloud.com" 
	 * -Dauth.jenkins.job="jboss1-build" -Dauth.username="admin" -Dauth.password="#########"
	 * 
	 */
	protected String SERVERURL = System.getProperty("jenkinsServer");
	protected String JENKINSJOB = System.getProperty("jenkinsJob");	
	
	protected String AUTHSERVERURL = System.getProperty("authJenkinsServer");
	protected String AUTHJENKINSJOB = System.getProperty("authJenkinsJob");
	protected String AUTHUSERNAME = System.getProperty("authUsername");
	protected String AUTHPASSWORD = System.getProperty("authPassword");
	
	@Test
	public void testServer() {
		
		BuildServerDialog buildServerDialog = null;
		MylynBuildView view = new MylynBuildView();

		view.open();
		view.createBuildServer(SERVERURL);	
		
		/* Workaround for https://github.com/jboss-reddeer/reddeer/issues/817 */
		view.open();

		log.info(new DefaultTree().getItems().size() + " = Tree contents count");

		new DefaultTreeItem (SERVERURL).select();
		new ShellMenuItem("File", "Properties").select();
		TestSupport.closeSecureStorageIfOpened();

		buildServerDialog = new BuildServerDialog();
		new DefaultShell ("Build Server Properties");		
		assertEquals ("The server name fails to match", new LabeledText ("Server:").getText(), SERVERURL); 

		/* Validate the server */
		assertTrue ("Properties title matches", buildServerDialog.getText().equals("Build Server Properties"));
		buildServerDialog.validateSettings();
		
		/* Locate a Jenkins job */
		new PushButton("Select All").click();
		new PushButton("Finish").click();
		
		try {
			new WaitUntil(new ShellIsAvailable("Refreshing Builds (" + SERVERURL + ")" ), TimePeriod.LONG); 
		}
		catch (Exception E) {
			log.info ("Test blocking problem with 'Refreshing Builds (name)' shell not seen - test is able to run");
		}	
		
		view.open();
		log.info( "GOT IT" + view.getJenkinsJob (SERVERURL, JENKINSJOB).getText());
		
	    /* Notification shell, and a shell with a null name, are intermittently left open
         * when Red Deer attempts to close all shells:
         * https://github.com/jboss-reddeer/reddeer/blob/master/plugins/org.jboss.reddeer.junit.extension/src/org/jboss/reddeer/junit/extension/after/test/impl/CloseAllShellsExt.java
         * See Red Deer issue: 
         * 
         */
        if (new ShellIsAvailable("Refreshing Builds (" + SERVERURL + ")").test()) {
            log.info("Closing shell: " + "Refreshing Builds (" + SERVERURL + ")");
            new DefaultShell("Refreshing Builds (" + SERVERURL + ")").close();
        }
        if (new ShellIsAvailable("").test()) {
            log.info("Closing shell - null title");
            new DefaultShell("").close();
        }
		
		
		view.close();

	} /* method */

	
	@Test
	public void testAuthServer() {
			
		BuildServerDialog buildServerDialog = null;
		MylynBuildView view = new MylynBuildView();

		view.open();
		view.createAuthBuildServer(AUTHSERVERURL, AUTHUSERNAME, AUTHPASSWORD);	
		
		/* Workaround for https://github.com/jboss-reddeer/reddeer/issues/817 */
		view.open();

		log.info(new DefaultTree().getItems().size() + " = Tree contents count");

		new DefaultTreeItem (AUTHSERVERURL).select();
		new ShellMenuItem("File", "Properties").select();
		TestSupport.closeSecureStorageIfOpened();
		
		buildServerDialog = new BuildServerDialog();
		new DefaultShell ("Build Server Properties");		
		assertEquals ("The server name fails to match", new LabeledText ("Server:").getText(), AUTHSERVERURL); 

		/* Validate the server */
		assertTrue ("Properties title matches", buildServerDialog.getText().equals("Build Server Properties"));
		buildServerDialog.validateSettings();
		
		/* Locate a Jenkins job */
		new PushButton("Select All").click();
		new PushButton("Finish").click();
		
		try {
			new WaitUntil(new ShellIsAvailable("Refreshing Builds (" + AUTHSERVERURL + ")" ), TimePeriod.LONG); 
					}
		catch (Exception E) {
			log.info ("Test blocking problem with 'Refreshing Builds (name)' shell not seen - test is able to run");
		}	
		
		view.open();
		log.info( "GOT IT" + view.getJenkinsJob (AUTHSERVERURL, AUTHJENKINSJOB).getText());
		
	    /* Notification shell, and a shell with a null name, are intermittently left open
         * when Red Deer attempts to close all shells:
         * https://github.com/jboss-reddeer/reddeer/blob/master/plugins/org.jboss.reddeer.junit.extension/src/org/jboss/reddeer/junit/extension/after/test/impl/CloseAllShellsExt.java
         * See Red Deer issue: https://github.com/jboss-reddeer/reddeer/issues/1300
         * 
         */
        if (new ShellIsAvailable("Refreshing Builds (" + AUTHSERVERURL + ")").test()) {
            log.info("Closing shell: " + "Refreshing Builds (" + AUTHSERVERURL + ")");
            new DefaultShell("Refreshing Builds (" + AUTHSERVERURL + ")").close();
        }
        if (new ShellIsAvailable("").test()) {
            log.info("Closing shell - null title");
            new DefaultShell("").close();
        }
		
		view.close();

	} /* method */
	
} /* class */
