package org.jboss.tools.openshift.ui.bot.test.explorer;

import org.jboss.tools.openshift.ui.bot.test.OpenShiftBotTest;
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
public class OpenShiftEnterpriseDebugFeatures extends OpenShiftBotTest {

        @Before
        public void createDYIApp() {
                OpenShiftDebugFeatures.createDYIApp();
        }
       
        @Test
        public void testDebufFeaures() {
        	OpenShiftDebugFeatures.canTailFiles();
        	OpenShiftDebugFeatures.canForwardPorts();
        	OpenShiftDebugFeatures.canOpenWebBrowser();
        	OpenShiftDebugFeatures.canShowEnvVariables();
        }
        
        @After
        public void deleteDIYApp() {
                OpenShiftDebugFeatures.deleteOpenShiftApplication(
                		OpenShiftDebugFeatures.DYI_APP, OpenShiftLabel.AppType.DIY_TREE);
        }   

}