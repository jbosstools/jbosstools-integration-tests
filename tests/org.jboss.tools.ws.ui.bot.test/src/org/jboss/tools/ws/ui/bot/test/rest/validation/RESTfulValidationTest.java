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
	
	public void testCorrectValueValidation() {
		//not implemented yet
	}
	
	public void testBadValueValidation() {
		//not implemented yet
	}
	
	public void testCorrectToBadValueValidation() {
		//not implemented yet
	}
	
	public void testValidatorDisabling() {
		//not implemented yet
	}
	
	public void testValidatorReenabling() {
		//not implemented yet
	}

}
