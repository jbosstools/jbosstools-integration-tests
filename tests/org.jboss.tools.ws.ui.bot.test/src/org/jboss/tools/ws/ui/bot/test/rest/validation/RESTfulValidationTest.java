 /*******************************************************************************
  * Copyright (c) 2007-2011 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/

package org.jboss.tools.ws.ui.bot.test.rest.validation;

import org.jboss.tools.ws.ui.bot.test.rest.RESTfulTestBase;
import org.jboss.tools.ws.ui.bot.test.rest.explorer.RESTfulExplorerTest;
import org.junit.Ignore;

/**
 * Test operates on JAX-RS validation
 * @author jjankovi
 *
 */
public class RESTfulValidationTest extends RESTfulTestBase {
	
	protected String getWsProjectName() {
		return "RestServicesValidation";
	}
	
	protected String getWsPackage() {
		return "org.rest.validation.services.test";
	}

	protected String getWsName() {
		return "RestService";
	}
	
	@Override
	public void setup() {		
		if (!projectExists(getWsProjectName())) {
			projectHelper.createProject(getWsProjectName());
		}
		if (!projectExplorer.isFilePresent(getWsProjectName(), "Java Resources", 
										  "src", getWsPackage(), getWsName() + ".java")) {
			projectHelper.createClass(getWsProjectName(), getWsPackage(), getWsName());
		}
	}
	@Ignore
	public void testCorrectValueValidation() {
		
		resourceHelper.copyResourceToClass(bot.editorByTitle(getWsName() + ".java"), 
				   RESTfulExplorerTest.class.
				   getResourceAsStream("/resources/restful/CorrectRestWS.java.ws"), 
				   false, getWsPackage(), getWsName());
		
		assertTrue(getRESTValidationErrorsAfterCleanBuild(getWsProjectName()).length == 0);
		
	}
	@Ignore
	public void testBadValueValidation() {
		resourceHelper.copyResourceToClass(bot.editorByTitle(getWsName() + ".java"), 
				   RESTfulExplorerTest.class.
				   getResourceAsStream("/resources/restful/BadRestWS.java.ws"), 
				   false, getWsPackage(), getWsName());
		
		assertTrue("" + getRESTValidationErrorsAfterCleanBuild(getWsProjectName()).length, 
				   getRESTValidationErrorsAfterCleanBuild(getWsProjectName()).length == 1);
	}
	@Ignore
	public void testCorrectToBadValueValidation() {
		//not implemented yet
	}
	@Ignore
	public void testBadToCorrectValueValidation() {
		//not implemented yet
	}
	@Ignore
	public void testValidatorDisabling() {
		//not implemented yet
	}
	@Ignore
	public void testValidatorReenabling() {
		//not implemented yet
	}

}
