package org.jboss.tools.mylyn.ui.bot.test;

/*
 * Prototype test for Mylyn/Jenkins plugin
 * 
 * 
 */

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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

public class MylynTestJenkins {

	protected final Logger log = Logger.getLogger(this.getClass());

	protected ArrayList<String> expectedMylynElements = new ArrayList<String>();	
	protected final String TASKNAME = "a sample task in Mylyn";
	protected final String TASKNOTE = "a sample note for a sample task in Mylyn";
	/* Cannot use machydra Jenkins server due to - https://issues.jboss.org/browse/JBDS-2965 */
	//protected final String SERVERURL = "https://hudson.jboss.org/hudson/";
	protected final String SERVERURL = "http://ci.jruby.org/";	

	@Test
	public void TestIt() {
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

		view.open();
		new DefaultToolItem("Refresh").click();

		List <TreeItem> theItems = new DefaultTree().getAllItems();
		for (TreeItem theItem : theItems) {
			log.info("ITEM = " + theItem.getText());
			List <TreeItem> moreItems = theItem.getItems();
			for (TreeItem theInnerItem : moreItems) {
				log.info("Inner ITEM = " + theInnerItem.getText());
			}
		}					

		log.info( "GOT IT" + view.getJenkinsJob (SERVERURL, "jruby-spec-coverage").getText());
		view.close();

	} /* method */

} /* class */
