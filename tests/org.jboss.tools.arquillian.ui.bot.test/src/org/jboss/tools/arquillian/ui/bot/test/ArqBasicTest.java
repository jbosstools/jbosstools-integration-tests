package org.jboss.tools.arquillian.ui.bot.test;

/*
 * Prototype test for Arquillian - create/deploy a new empty project
 * 
 * 
 */

import org.apache.log4j.Logger;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.jdt.ui.ide.NewJavaProjectWizardDialog;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerState;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewException;
import org.jboss.reddeer.requirements.server.ServerReqState;
//import org.jboss.reddeer.swt.condition.ButtonWithTextIsActive;
//import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
//import org.jboss.reddeer.swt.wait.AbstractWait;
//import org.jboss.reddeer.swt.wait.TimePeriod;
//import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;
import org.junit.Test;

import static org.junit.Assert.*;

@JBossServer(state=ServerReqState.RUNNING, type=ServerReqType.EAP6_1plus)

public class ArqBasicTest  {
	
	protected final Logger log = Logger.getLogger(this.getClass());

	@Test
	public void testIt() {
		
		ServersView view = new ServersView();
		view.open();

		/* Workaround to Red Deer issue of intermittent server startup problem
		 * 
		 *   https://github.com/jboss-reddeer/reddeer/issues/829
		 */
		if ( (!view.getServers().get(0).getLabel().getState().equals(ServerState.STARTED))  ||
			(!view.getServers().get(0).getLabel().getState().equals(ServerState.STARTING)) )
		{			
			try {
				view.getServers().get(0).start();
			}
			catch (ServersViewException E) { 
				log.error ("Unexpected server state = " + view.getServers().get(0).getLabel().getState().getText());
			}
		}

		ProjectExplorer pex = new ProjectExplorer();
		pex.open();
		
		/* Create new project */
		NewJavaProjectWizardDialog jp = new NewJavaProjectWizardDialog();
		jp.open();
		jp.getFirstPage().setProjectName("Test100");
		jp.finish();

		/* Convert to Maven project */
		pex.getProject("Test100").select();
		new ContextMenu("Configure","Convert to Maven Project").select();
		new DefaultShell("Create new POM");
		new PushButton("Finish").click();
		
		/* Workaround to Red Deer issue of project explorer not staying open
		 * after a project is created and converted to Maven - the close/open is 
		 * needed to have the project config change be recognized 
		 * 
		 * https://github.com/jboss-reddeer/reddeer/issues/830
		 */
		pex.close();
		pex.open();
				
		/* Add Arquillian support */
		pex.getProject("Test100").select();
		new ContextMenu("Configure","Add Arquillian Support...").select();
		// Wait for button to be active		
		new org.jboss.reddeer.common.wait.WaitUntil(new ShellWithTextIsAvailable("Add Arquillian support"), TimePeriod.getCustom(30l)); 
		//new WaitUntil (new ButtonWithTextIsActive(new PushButton("OK")));
		new PushButton("OK").click();
		
		try {
			new WaitUntil(new ShellWithTextIsActive("Resource - Test100/pom.xml - "), TimePeriod.getCustom(60l)); 
		}
		catch (Exception E) {
			log.info ("Problem with 'Java - Test100/pom.xml - Eclipse' shell not seen");
		}		
		
		/* Save All - to save the changes to the pom.xml */	
		new ShellMenu("File","Save All").select();
		
		/* Add Arquillian test */
		pex.getProject("Test100").select();
		new ContextMenu("New","Other...").select();
		new DefaultTreeItem("Arquillian","Arquillian JUnit Test Case").select();
		new PushButton("Next >").click();
		new LabeledText("Package:").setText("Arqtest100");
		new LabeledText("Name:").setText("arqtest100");
		new PushButton("Finish").click();

		/* Edit the Arquillian Junit test case so that it always passes */
		new ShellMenu("Edit","Find/Replace...").select();
		new DefaultShell("Find/Replace");
		/* Workaround for Red Deer bug - Cannot access Combo by name 
		 * https://github.com/jboss-reddeer/reddeer/issues/606 
		 */
		new DefaultCombo(0).setText("fail(\"Not yet implemented\")");
		new DefaultCombo(1).setText("assertTrue(true)");
		new PushButton("Replace All").click();
		new PushButton("Close").click();
		new ShellMenu("File","Save").select();

		/* Run the Arquillian Junit test case */
		new TextEditor().activate();
		new ContextMenu("Run As","2 Arquillian JUnit Test").select();
		new DefaultShell("Arquillian JUnit test");
		new DefaultTable().getItem("Select Maven Profiles").select();
		new DefaultTable().getItem("Select Maven Profiles").select();
		new PushButton("OK").click();
		new WorkbenchView("Package Explorer");
		new DefaultShell("Select Maven profiles");
		new DefaultTable(0).getItem("JBOSS_AS_REMOTE_7.X").setChecked(true);
		new DefaultTable(0).getItem("JBOSS_AS_REMOTE_7.X").select();
		new PushButton("OK").click();
		new DefaultShell("Arquillian JUnit test");
		new PushButton("OK").click();
		new WorkbenchView("Package Explorer");
		new DefaultShell("Select Maven profiles");
		new PushButton("OK").click();		

		log.info("waiting for test to run...");
		
		new WorkbenchView("JUnit");
		AbstractWait.sleep(TimePeriod.LONG); 
		new WorkbenchView("JUnit");
			
		/* If Run results are 0/1 */
		if (new LabeledText ("Runs: ").getText().equals("0/1")) {
			new DefaultTree().selectItems(new DefaultTreeItem ("Arqtest100.arqtest100 [Runner: JUnit 4]"));
			String temp = new DefaultTable().getItem(0).getText();
			assertFalse ("Test failed as project cannot be deployed",
					temp.contains("org.jboss.arquillian.container.spi.client.container.DeploymentException: Cannot deploy"));			
		}		
		
		/* Verify the test results in the Junit view */
		log.info ("Runs: " + new LabeledText ("Runs: ").getText() );
		log.info ("Errors: " + new LabeledText ("Errors: ").getText() );
		log.info ("Failures: " + new LabeledText ("Failures: ").getText() );

		assertTrue ("The number of runs " + new LabeledText ("Runs: ").getText() + " was not correct", new LabeledText ("Runs: ").getText().equals("1/1"));
		assertTrue ("The number of errors " + new LabeledText ("Errors: ").getText() + " was not correct", new LabeledText ("Errors: ").getText().equals("0"));
		assertTrue ("The number of failures " + new LabeledText ("Failures: ").getText() + " was not correct", new LabeledText ("Failures: ").getText().equals("0"));
		
	} /* method */

} /* class */
