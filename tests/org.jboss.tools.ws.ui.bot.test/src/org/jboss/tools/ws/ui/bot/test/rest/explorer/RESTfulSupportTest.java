package org.jboss.tools.ws.ui.bot.test.rest.explorer;

import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.jboss.reddeer.eclipse.jface.exception.JFaceLayerException;
import org.jboss.tools.ws.ui.bot.test.rest.RESTfulTestBase;
import org.jboss.tools.ws.ui.bot.test.uiutils.RESTFullExplorer;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test checks if context menu 'Add RESTful Support' works properly
 * 
 * @author jjankovi
 * @author Radoslav Rabara
 * 
 * @see https://issues.jboss.org/browse/JBIDE-16329
 */
public class RESTfulSupportTest extends RESTfulTestBase {
	
	protected String getWsProjectName() {
		return "RestExplorerTest";
	}
	
	@Override
	public void setup() {
	
	}
	
	/**
	 * Failes due to JBIDE-17653
	 * (Node "JAX-RS Web Services" doesn't appear after JAX-RS Support is added)
	 * 
	 * @see https://issues.jboss.org/browse/JBIDE-17653
	 */
	@Test
	public void testJaxRsExplorerSupport() {
		/* create dynamic web project */
		projectHelper.createProject(getWsProjectName());
		
		/* add RESTful support into project */
		restfulHelper.addRestSupport(getWsProjectName());
		
		/* test if RESTful explorer is not missing */
		assertRestFullSupport(getWsProjectName());
	}

	protected void assertRestFullSupport(String projectName) {
		RESTFullExplorer explorer = null;
		String missingRESTExplorerMessage = "JAX-RS REST Web Services explorer is missing in "
				+ "project \"" + projectName + "\"";
		try {
			explorer = new RESTFullExplorer(projectName);
		} catch (WidgetNotFoundException | JFaceLayerException e) {
			Assert.fail(missingRESTExplorerMessage + "\nThrown exception: " + e.getMessage());
		}
		assertNotNull(missingRESTExplorerMessage, explorer);
	}
}
