package org.jboss.tools.openshift.ui.bot.test.explorer;

import static org.junit.Assert.assertFalse;

import java.util.Date;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.openshift.ui.bot.test.OpenShiftBotTest;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
* Test class for tailing remote files, port forwarding, opening web browser & displaying env.variables.
* To prevent creation of OpenShift application for each test, all tests are included in one test method.
*
* @author mlabuda
*
*/
public class OpenShiftDebugFeatures extends OpenShiftBotTest {

        public static final String DYI_APP = "diyapp" + new Date().getTime();

        @Before
        public void createDIYApplication() {
        	createDYIApp();
        }
        
        public static void createDYIApp() {
                createOpenShiftApplication(DYI_APP, OpenShiftLabel.AppType.DIY);
        }
       
        @Test
        public void testDebufFeaures() {
        	canTailFiles();
        	canForwardPorts();
        	canOpenWebBrowser();
        	canShowEnvVariables();
        	canCreateEnvVariable();
        }
        
        public static void canTailFiles() {
                openDebugFeature(OpenShiftLabel.Labels.EXPLORER_TAIL_FILES);
                
                new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.TAIL_FILES), TimePeriod.LONG);
                
                new DefaultShell(OpenShiftLabel.Shell.TAIL_FILES).setFocus();
                new PushButton(OpenShiftLabel.Button.FINISH).click();
                
                // invoke another action for more log
                openDebugFeature("Show in Web Browser");
                
                ConsoleView consoleView = new ConsoleView();
                consoleView.open();
                
                assertFalse("Remote console should not be empty!", consoleView
                                .getConsoleText().isEmpty());
                new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
        }

        public static void canForwardPorts() {
                openDebugFeature(OpenShiftLabel.Labels.EXPLORER_PORTS);

                new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.PORTS), TimePeriod.LONG);
                
                new DefaultShell(OpenShiftLabel.Shell.PORTS).setFocus();
                new PushButton("Start All").click();
                
                AbstractWait.sleep(TimePeriod.NORMAL);

                new PushButton("OK").click();

                ConsoleView consoleView = new ConsoleView();
                consoleView.open();
                
                assertFalse("Console should not be empty!", consoleView.getConsoleText()
                                .isEmpty());

                openDebugFeature(OpenShiftLabel.Labels.EXPLORER_PORTS);
                
                new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.PORTS), TimePeriod.LONG);
                
                new DefaultShell(OpenShiftLabel.Shell.PORTS).setFocus();
                new PushButton("Stop All").click();
                
                AbstractWait.sleep(TimePeriod.NORMAL);
                
                new PushButton("OK").click();
                new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
        }

        public static void canOpenWebBrowser() {
                openDebugFeature("Show in Web Browser");

                new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
                // TODO find way to verify in internal browser, also in Republish class
                // it is browser editor
        }

        public static void canShowEnvVariables() {
                openDebugFeature(OpenShiftLabel.Labels.EXPLORER_ENV_VAR);

                new WaitWhile(new JobIsRunning(), TimePeriod.LONG);

                ConsoleView consoleView = new ConsoleView();
                consoleView.open();
                assertFalse("Console should not be empty!", consoleView.getConsoleText()
                                .isEmpty());
                new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
        }

        public static void canCreateEnvVariable() {
                openDebugFeature("Edit Environment Variables...");
                
                new WaitUntil(new ShellWithTextIsAvailable(
                		"Manage Application Environment Variable(s) for application " + DYI_APP), 
                		TimePeriod.LONG);
                
                new DefaultShell("Manage Application Environment Variable(s) for application " + 
                		DYI_APP).setFocus();
                new PushButton("Add...").click();
                
                new WaitUntil(new ShellWithTextIsAvailable("Edit Environment variable"), TimePeriod.LONG);
                
                new DefaultShell("Edit Environment variable").setFocus();
                new DefaultText(0).setText("myVariable");
                new DefaultText(1).setText("myValue");
                new PushButton("OK").click();

                new WaitUntil(new ShellWithTextIsAvailable("Manage Application Environment Variable(s) for application " + 
                		DYI_APP), TimePeriod.LONG);
                
                new DefaultShell("Manage Application Environment Variable(s) for application " + 
                		DYI_APP).setFocus();
                new PushButton("Finish").click();
                
                new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
        }
        
        private static void openDebugFeature(String label) {
		        OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		        explorer.open();

		        TreeItem connection = new DefaultTree().getItems().get(0);
		        connection.select();
		        new ContextMenu("Refresh").select();
		        
		        new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		        
		        connection.expand();
		        
		        new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		        
		        connection.getItems().get(0).expand();
		        
		        new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		        
		        connection.getItems().get(0).getItems().get(0).select();
		        new ContextMenu(label).select();
        }
        
        @After
        public void deleteDIYApplication() {
        	deleteDIYApp();
        }
        
        public static void deleteDIYApp() {
                deleteOpenShiftApplication(DYI_APP, OpenShiftLabel.AppType.DIY_TREE);
        }   

}