package org.jboss.tools.runtime.as.ui.bot.test.parametized.server;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.ui.PlatformUI;
import org.jboss.ide.eclipse.as.reddeer.server.editor.JBossServerEditor;
import org.jboss.ide.eclipse.as.reddeer.server.view.JBossServer;
import org.jboss.ide.eclipse.as.reddeer.server.view.JBossServerView;
import org.jboss.reddeer.core.util.Display;
import org.jboss.reddeer.eclipse.jdt.ui.preferences.JREsPreferencePage;
import org.jboss.reddeer.junit.internal.runner.ParameterizedRequirementsRunnerFactory;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.cdk.reddeer.requirements.DisableSecureStorageRequirement.DisableSecureStorage;
import org.jboss.tools.runtime.as.ui.bot.test.Activator;
import org.jboss.tools.runtime.as.ui.bot.test.SuiteConstants;
import org.jboss.tools.runtime.as.ui.bot.test.download.RuntimeDownloadTestUtility;
import org.jboss.tools.runtime.as.ui.bot.test.parametized.server.ServerRuntimeUIConstants.EditorPort;
import org.jboss.tools.runtime.as.ui.bot.test.reddeer.util.DetectRuntimeTemplate;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized.UseParametersRunnerFactory;



/**
 * This test is trying to optimize and clean up the huge number of tests that were here. 
 * 
 * Pre-reqs:
 *   1) -Dreq.java6.home={java6home}
 *   2) -Dreq.java7.home={java7home}
 *   3) -Dreq.java8.home={java8home}
 *   4) -Djboss.org.username={yourusername}
 *   5) -Djboss.org.password={hunter2}
 *   6) -Druntimes.suite.scope={smoke | latestMajors | allFree | all}
 *   
 *  Items 4 and 5 are optional and only necessary if testing runtimes that 
 *  require $0 subscription. 
 *  
 *  This test no longer needs to download any runtimes in advance. It will download them all via UI.
 *  
 *  There are 3 primary tests here:
 *    1) acquireAndDetect	(download the runtime via ui and make sure it appears)
 *    2) detect				(use the just-downloaded fs-location and use runtime detection)
 *    3) operate			(import via rt-detection and start/stop it)
 *    
 *  JRE requirement is no longer necessary. We simply make sure to add a JRE
 *  for java 6, 7, and 8 in advance, and check to make sure that the given server
 *  starts without the user needing to customize or change the jre at all. 
 *  
 *  Adding new runtimes to the test is as easy as modifying 
 *  ServerRuntimeUIConstants. You should:
 *    1) Declare a constant representing the UI string to download a given runtime
 *    2) Add that constant to the various arrays it should belong to (free, smoke, etc)
 *    3) Add a line to the initialize() method representing the various expected dl-rt values
 */


@RunWith(RedDeerSuite.class)
@UseParametersRunnerFactory(ParameterizedRequirementsRunnerFactory.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)//first acquireAndDetect, then detect, then operate
@DisableSecureStorage
public class ServerRuntimesTest {

    @Parameters
    public static Collection<Object[]> data(){
    	String scope = System.getProperty(SuiteConstants.SYSPROP_KEY);
    	ArrayList<Object[]> ret = (ArrayList<Object[]>) ServerRuntimeUIConstants.getParametersForScope(scope);
    	return ret;
    }
    
    
    @BeforeClass
    public static void addJREs() {
    	String jre6 =  System.getProperty("req.java6.home");
    	String jre7 =  System.getProperty("req.java7.home");
    	String jre8 =  System.getProperty("req.java8.home");
    	if( jre6 == null || jre6.isEmpty() || !(new File(jre6)).exists()) {
    		throw new RuntimeException("Expected requirement JRE-6 is not set, is empty, or does not exist. Please set via system property -Dreq.java6.home");
    	}
    	if( jre7 == null || jre7.isEmpty() || !(new File(jre7)).exists()) {
    		throw new RuntimeException("Expected requirement JRE-7 is not set, is empty, or does not exist. Please set via system property -Dreq.java7.home");
    	}
    	if( jre8 == null || jre8.isEmpty() || !(new File(jre8)).exists()) {
    		throw new RuntimeException("Expected requirement JRE-8 is not set, is empty, or does not exist. Please set via system property -Dreq.java8.home");
    	}
    	addJRE("JRE6",jre6);
    	addJRE("JRE7",jre7);
    	addJRE("JRE8",jre8);
    }
    
    @AfterClass
    public static void removeJREs() {
    	removeJRE("JRE6");
    	removeJRE("JRE7");
    	removeJRE("JRE8");
    }
    
    public static void removeJRE(String name) {
		JREsPreferencePage page = new JREsPreferencePage();
		WorkbenchPreferenceDialog dialog = new WorkbenchPreferenceDialog();
		dialog.open();
		dialog.select(page);
		page.deleteJRE(name);
		dialog.ok();
    }
    
    private static void addJRE(String name, String path) {
		JREsPreferencePage page = new JREsPreferencePage();
		WorkbenchPreferenceDialog dialog = new WorkbenchPreferenceDialog();
		dialog.open();
		dialog.select(page);
		page.addJRE(path, name);
		dialog.ok();
    }



    private String runtimeString;
    private boolean dlType;

    public ServerRuntimesTest(String type, boolean free) {
    	runtimeString = type;
    	dlType = free;
    }
    
    protected File getDownloadPath() {
    	return Activator.getDownloadFolder(runtimeString);
    }
    
    @Test
    public void acquireAndDetect(){
        System.out.println(runtimeString);
        RuntimeDownloadTestUtility util = new RuntimeDownloadTestUtility(getDownloadPath());
    	if( dlType == SuiteConstants.FREE) {
    		util.downloadRuntimeNoCredentials(runtimeString);
    	} else {
    		util.downloadRuntimeWithCredentials(runtimeString);
    	}
    }
    
    @Test
    public void detect(){
    	DetectRuntimeTemplate.detectRuntime(getDownloadPath().getAbsolutePath(), ServerRuntimeUIConstants.getRuntimesForDownloadable(runtimeString));
    	DetectRuntimeTemplate.removePath(getDownloadPath().getAbsolutePath());
    	
    	
    	// Let's also verify the ports situation in the editor
    	String serverName = ServerRuntimeUIConstants.getServerName(runtimeString);
		JBossServer server = new JBossServerView().getServer(serverName);
		JBossServerEditor editor = server.open();
		
		EditorPort[] ports = ServerRuntimeUIConstants.getPorts(runtimeString);
		assertNotNull(ports);
		for( int i = 0; i < ports.length; i++ ) {
			assertThat(new LabeledText(ports[i].getLabel()).getText(), is(ports[i].getValue()));
		}
    }
    

    
//
//    
//    
//    @Test
//    public void operate(){
//    	DetectRuntimeTemplate.detectRuntime(getDownloadPath().getAbsolutePath(), ServerRuntimeUIConstants.getRuntimesForDownloadable(runtimeString));
//    	DetectRuntimeTemplate.removePath(getDownloadPath().getAbsolutePath());
//    	String serverName = ServerRuntimeUIConstants.getServerName(runtimeString);
//    	OperateServerTemplate operate = new OperateServerTemplate(serverName);
//    	operate.setUp();
//    	try {
//    		operate.operateServer();
//    	} finally {
//    		operate.cleanServerAndConsoleView();
//    	}
//    }
//
//    @Test
//    public void operateDeploy(){
//    	DetectRuntimeTemplate.detectRuntime(getDownloadPath().getAbsolutePath(), ServerRuntimeUIConstants.getRuntimesForDownloadable(runtimeString));
//    	DetectRuntimeTemplate.removePath(getDownloadPath().getAbsolutePath());
//    	String serverName = ServerRuntimeUIConstants.getServerName(runtimeString);
//    	OperateServerTemplate operate = new OperateServerTemplate(serverName);
//    	operate.setUp();
//    	try {
//    		operate.startServerSafe();
//    		DeployJSPProjectTemplate djsppt = new DeployJSPProjectTemplate();
//    		djsppt.clearConsole();
//    		JBossServer jbs = djsppt.getServer(serverName);
//    		djsppt.importProject("jsp-project", "projects/jsp-project.zip", serverName + " Runtime");
//    		
//    		String depString = ServerRuntimeUIConstants.getDeployString(runtimeString, "jsp-project", ".war");
//    		djsppt.deployProject("jsp-project", serverName, depString);
//    		
//    		// Now try a hot-deploy
//    		djsppt.hotDeployment("jsp-project");
//    		
//    		// Now try to undeploy
//    		String undepString = ServerRuntimeUIConstants.getUndeployString(runtimeString, "jsp-project", ".war");
//    		djsppt.undeployProject(serverName, "jsp-project", undepString);
//    		
//    	} finally {
//    		// Cleanup everything
//    		operate.stopAndDeleteServer();
//    		operate.cleanServerAndConsoleView();
//    	}
//    }

    

    @After
    public void postTest() {
    	Display.syncExec(new Runnable() {
			@Override
			public void run() {
		    	PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeAllEditors(true);
			}
		});
    	new RuntimeDownloadTestUtility(getDownloadPath()).clean(false);
    }
    
    @AfterClass
    public static void postClass() {
    	new RuntimeDownloadTestUtility(Activator.getStateFolder().toFile()).clean(true);
    }
        

}
