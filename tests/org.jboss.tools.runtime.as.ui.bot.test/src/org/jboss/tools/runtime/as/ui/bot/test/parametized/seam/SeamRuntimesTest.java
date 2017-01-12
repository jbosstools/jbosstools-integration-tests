package org.jboss.tools.runtime.as.ui.bot.test.parametized.seam;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.jboss.reddeer.eclipse.jdt.ui.preferences.JREsPreferencePage;
import org.jboss.reddeer.junit.internal.runner.ParameterizedRequirementsRunnerFactory;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.cdk.reddeer.requirements.DisableSecureStorageRequirement.DisableSecureStorage;
import org.jboss.tools.runtime.as.ui.bot.test.Activator;
import org.jboss.tools.runtime.as.ui.bot.test.SuiteConstants;
import org.jboss.tools.runtime.as.ui.bot.test.download.RuntimeDownloadTestUtility;
import org.jboss.tools.runtime.as.ui.bot.test.parametized.MatrixUtils;
import org.jboss.tools.runtime.as.ui.bot.test.reddeer.util.DetectRuntimeTemplate;
import org.jboss.tools.runtime.as.ui.bot.test.reddeer.util.OperateServerTemplate;
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
 *   1) -Druntimes.suite.scope={smoke | latestMajors | allFree | all}
 *   
 *  Items 4 and 5 are optional and only necessary if testing runtimes that 
 *  require $0 subscription. 
 *  
 *  This test no longer needs to download any runtimes in advance. It will download them all via UI.
 *  
 *  There are 2 primary tests here:
 *    1) acquireAndDetect	(download the runtime via ui and make sure it appears)
 *    2) detect				(use the just-downloaded fs-location and use runtime detection)
 *    
 *  Adding new runtimes to the test is as easy as modifying 
 *  SeamRuntimeUIConstants. You should:
 *    1) Declare a constant representing the UI string to download a given runtime
 *    2) Add that constant to the various arrays it should belong to (free, smoke, etc)
 *    3) Add a line to the initialize() method representing the various expected dl-rt values
 */


@RunWith(RedDeerSuite.class)
@UseParametersRunnerFactory(ParameterizedRequirementsRunnerFactory.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)//first acquireAndDetect, then detect, then operate
@DisableSecureStorage
public class SeamRuntimesTest {

	
    @Parameters
    public static Collection<Object[]> data(){
    	String scope = System.getProperty(SuiteConstants.SYSPROP_KEY);
    	Collection<Object[]> ret = SeamRuntimeUIConstants.getParametersForScope(scope);
    	return ret;
    }
    

    private String runtimeString;
    private boolean dlType;

    public SeamRuntimesTest(String type, boolean free) {
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
    	DetectRuntimeTemplate.detectRuntime(getDownloadPath().getAbsolutePath(), SeamRuntimeUIConstants.getRuntimesForDownloadable(runtimeString));
    	DetectRuntimeTemplate.removePath(getDownloadPath().getAbsolutePath());
    }


    @After
    public void postTest() {
    	new RuntimeDownloadTestUtility(getDownloadPath()).clean(false);
    }
    
    @AfterClass
    public static void postClass() {
    	new RuntimeDownloadTestUtility(Activator.getStateFolder().toFile()).clean(true);
    }
        

}
