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

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.condition.ActiveEditorHasTitleCondition;
import org.jboss.tools.ws.ui.bot.test.rest.RESTfulTestBase;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author jjankovi
 *
 */
public class ApplicationValidationTest extends RESTfulTestBase {

	
	@Before
	public void setup() {
		
	}
	/**
	 * Resolved - Doubled warnings Multiple JAX-RS Activators
	 * {@link https://issues.jboss.org/browse/JBIDE-15402}
	 */
	@Test
	public void testMultipleApplicationClasses() {
		
		/* prepare project */
		importRestWSProject("app1");
		
		/* test validation error */
		assertCountOfApplicationAnnotationValidationErrors("app1", 2);
		assertCountOfApplicationAnnotationValidationErrors("app1", "Multiple JAX-RS Activators", 2);
	}
	
	@Test
	public void testWebXmlAndApplicationClassWithWarning() {
		
		/* prepare project */
		importRestWSProject("app2");
		
		/* test validation error */
		assertCountOfApplicationAnnotationValidationErrors("app2", 2);
		assertCountOfApplicationAnnotationValidationErrors("app2", "Multiple JAX-RS Activators", 2);
	}
	
	@Test
	public void testWebXmlAndApplicationClassWithoutWarning() {
		
		/* prepare project */
		importRestWSProject("app3");
		
		/* test validation error */
		assertCountOfApplicationAnnotationValidationWarnings("app3", 0);
		assertCountOfApplicationAnnotationValidationErrors("app3", 0);
	}
	
	@Test
	public void testNotExtendingApplicationClass() {
		
		/* prepare project */
		importRestWSProject("app4");
		
		/* test validation error */
		assertCountOfApplicationAnnotationValidationErrors("app4", 1);
		
		/* fix class - should be no error */
		resourceHelper.replaceInEditor(editorForClass("app4", "src", 
				"test", "App.java").toTextEditor(), "@ApplicationPath(\"/rest\")", "", true);
		
		/* test validation error */
		assertCountOfApplicationAnnotationValidationErrors("app4", 0);
	}
	
	@Test
	public void testApplicationClassWithoutPath() {
		
		/* prepare project */
		importRestWSProject("app5");
		
		/* test validation error */
		assertCountOfApplicationAnnotationValidationErrors("app5", 1);
		
		/* fix class - should be no error */
		resourceHelper.replaceInEditor(editorForClass("app5", "src", 
				"test", "App.java").toTextEditor(), "extends Application", "", true);
		
		/* test validation error */
		assertCountOfApplicationAnnotationValidationErrors("app5", 0);
	}

	private String obtainClassNameFromPath(String... path) {
		int length  = path.length;
		return path[length - 1];
	}
	
}
