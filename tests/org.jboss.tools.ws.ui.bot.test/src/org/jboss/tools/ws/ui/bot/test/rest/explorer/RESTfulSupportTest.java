package org.jboss.tools.ws.ui.bot.test.rest.explorer;

import static org.junit.Assert.assertNotNull;

import org.jboss.reddeer.jface.exception.JFaceLayerException;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.ws.reddeer.jaxrs.core.RESTfulWebServicesNode;
import org.jboss.tools.ws.ui.bot.test.rest.RESTfulTestBase;
import org.jboss.tools.ws.ui.bot.test.utils.ProjectHelper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test checks if context menu 'Add RESTful Support' works properly
 * 
 * @author jjankovi
 * @author Radoslav Rabara
 * 
 * @see https://issues.jboss.org/browse/JBIDE-16329
 */
@RunWith(RedDeerSuite.class)
public class RESTfulSupportTest extends RESTfulTestBase {

	protected String getWsProjectName() {
		return "RestExplorerTest";
	}

	@Override
	public void setup() {

	}

	@Test
	public void testJaxRsExplorerSupport() {
		/* create dynamic web project */
		ProjectHelper.createProject(getWsProjectName());

		/* add RESTful support into project */
		restfulHelper.addRestSupport(getWsProjectName());

		/* test if RESTful explorer is not missing */
		assertRestFullSupport(getWsProjectName());
	}

	protected void assertRestFullSupport(String projectName) {
		RESTfulWebServicesNode webServicesNode = null;
		String missingRESTExplorerMessage = "JAX-RS REST Web Services explorer is missing in "
				+ "project \"" + projectName + "\"";
		try {
			webServicesNode = new RESTfulWebServicesNode(projectName);
		} catch (JFaceLayerException e) {
			Assert.fail(missingRESTExplorerMessage + "\nThrown exception: " + e.getMessage());
		}
		assertNotNull(missingRESTExplorerMessage, webServicesNode);
	}
}
