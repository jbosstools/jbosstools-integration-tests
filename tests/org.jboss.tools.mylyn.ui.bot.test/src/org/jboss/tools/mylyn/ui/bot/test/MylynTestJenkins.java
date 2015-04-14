package org.jboss.tools.mylyn.ui.bot.test;

/*
 * Prototype test for Mylyn/Jenkins plugin
 * 
 * 
 */

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.eclipse.mylyn.tasks.ui.view.*;
import org.jboss.tools.mylyn.reddeer.*;
import org.jboss.tools.mylyn.reddeer.wizard.BuildServerDialog;
import org.jboss.tools.mylyn.reddeer.view.MylynBuildView;
import org.jboss.reddeer.swt.condition.ShellIsActive;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.eclipse.core.runtime.Platform.*;
import org.jboss.tools.mylyn.reddeer.TestSupport;

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
	protected String AUTHUSERNAME = System.getProperty("authUserName");
	protected String AUTHPASSWORD = System.getProperty("authPassWord");
	
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
		new ShellMenu("File", "Properties").select();
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
			new WaitUntil(new ShellWithTextIsActive("Refreshing Builds (" + SERVERURL + ")" ), TimePeriod.getCustom(30l)); 
		}
		catch (Exception E) {
			log.info ("Test blocking problem with 'Refreshing Builds (name)' shell not seen - test is able to run");
		}	
		
		view.open();
		log.info( "GOT IT" + view.getJenkinsJob (SERVERURL, JENKINSJOB).getText());
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
		new ShellMenu("File", "Properties").select();
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
			new WaitUntil(new ShellWithTextIsActive("Refreshing Builds (" + AUTHSERVERURL + ")" ), TimePeriod.getCustom(30l)); 
					}
		catch (Exception E) {
			log.info ("Test blocking problem with 'Refreshing Builds (name)' shell not seen - test is able to run");
		}	
		
		view.open();
		log.info( "GOT IT" + view.getJenkinsJob (AUTHSERVERURL, AUTHJENKINSJOB).getText());
		view.close();

	} /* method */
	
} /* class */
