/*******************************************************************************
 * Copyright (c) 2010-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.ws.ui.bot.test.rest.validation;

import org.jboss.tools.ws.ui.bot.test.rest.RESTfulTestBase;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author jjankovi
 *
 */
public class ApplicationPathAnnotationTest extends RESTfulTestBase {

	
	@Before
	public void setup() {		
		
	}
	
	@Test
	public void testMultipleApplicationClasses() {
		
		/* prepare project */
		importRestWSProject("app1");
		
		/* test validation error */
		assertCountOfApplicationAnnotationValidationErrors("app1", 1);
	}
	
	@Test
	public void testWebXmlAndApplicationClass() {
		
		/* prepare project */
		importRestWSProject("app2");
		
		/* test validation error */
		assertCountOfApplicationAnnotationValidationErrors("app2", 1);
	}
	
}
