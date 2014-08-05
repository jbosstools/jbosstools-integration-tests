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
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.eclipse.mylyn.tasks.ui.view.*;
import org.jboss.tools.mylyn.reddeer.*;
import org.jboss.tools.mylyn.reddeer.wizard.BuildServerDialog;
import org.jboss.tools.mylyn.reddeer.view.MylynBuildView;
import org.jboss.reddeer.swt.condition.ShellIsActive;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;

public class MylynTestJenkins {

	protected final Logger log = Logger.getLogger(this.getClass());

	protected ArrayList<String> expectedMylynElements = new ArrayList<String>();	
	protected final String TASKNAME = "a sample task in Mylyn";
	protected final String TASKNOTE = "a sample note for a sample task in Mylyn";
	/* Could not originally use machydra Jenkins server due to - https://issues.jboss.org/browse/JBDS-2965 */
	/* This server proved to be unreliable: protected final String SERVERURL = "http://ci.jruby.org/"
	 * so the test was changed to use the JBDS QE Jenkins - machydra (August 2014)
	 * 
	 * mvn clean install -Dswtbot.test.skip=false -Dusage_reporting_enabled=false -Dreddeer.close.welcome.en=true 
	 * -Djenkins.server="http://machydra.lab.eng.brq.redhat.com:8080" -Djenkins.job="jbosstools.mylyn.bot.tests.poc"
	 * 	
	 */
	protected String SERVERURL = System.getProperty("jenkinsServer");
	protected String JENKINSJOB = System.getProperty("jenkinsJob");
	
	@Test
	public void testIt() {
				
		BuildServerDialog buildServerDialog = null;
		MylynBuildView view = new MylynBuildView();

		view.open();
		view.createBuildServer(SERVERURL);	

		log.info(new DefaultTree().getItems().size() + " = Tree contents count");

		new DefaultTreeItem (SERVERURL).select();
		new ShellMenu("File", "Properties").select();

		buildServerDialog = new BuildServerDialog();
		new DefaultShell ("Build Server Properties");		
		assertEquals ("The server name fails to match", new LabeledText ("Server:").getText(), SERVERURL); 

		/* Validate the server */
		assertTrue ("Properties title matches", buildServerDialog.getText().equals("Build Server Properties"));
		buildServerDialog.validateSettings();
		assertTrue("Build Server Properties Invalid", new LabeledText("Build Server Properties").getText().contains("Repository is valid"));

		/* Locate a Jenkins job */
		new PushButton("Select All").click();
		new PushButton("Finish").click();
		
		try {
			new WaitUntil(new ShellWithTextIsActive("Refreshing Builds (http://machydra.brq.redhat.com:8080)"), TimePeriod.getCustom(30l)); 
		}
		catch (Exception E) {
			log.info ("Test blocking problem with 'Refreshing Builds (name)' shell not seen - test is able to run");
		}	
		
		view.open();
		log.info( "GOT IT" + view.getJenkinsJob (SERVERURL, JENKINSJOB).getText());
		view.close();

	} /* method */

} /* class */
