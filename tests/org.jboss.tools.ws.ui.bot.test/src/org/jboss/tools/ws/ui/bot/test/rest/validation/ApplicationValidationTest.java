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
	 * Fails due to reported bug JBIDE-15402.
	 * 
	 * @see https://issues.jboss.org/browse/JBIDE-15402
	 */
	@Test
	public void testMultipleApplicationClasses() {
		
		/* prepare project */
		importRestWSProject("app1");
		
		/* test validation error */
		assertCountOfApplicationAnnotationValidationWarnings("app1", 2);
	}
	
	@Test
	public void testWebXmlAndApplicationClassWithWarning() {
		
		/* prepare project */
		importRestWSProject("app2");
		
		/* test validation error */
		assertCountOfApplicationAnnotationValidationWarnings("app2", 2);
	}
	
	@Test
	public void testWebXmlAndApplicationClassWithoutWarning() {
		
		/* prepare project */
		importRestWSProject("app3");
		
		/* test validation error */
		assertCountOfApplicationAnnotationValidationWarnings("app3", 0);
	}
	
	@Test
	public void testNotExtendingApplicationClass() {
		
		/* prepare project */
		importRestWSProject("app4");
		
		/* workaround for JBIDE-12690 */
		//jbide12680Workaround("app4", "src", "test", "App.java");
		
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
		
		/* workaround for JBIDE-12690 */
		//jbide12680Workaround("app5", "src", "test", "App.java");
		
		/* test validation error */
		assertCountOfApplicationAnnotationValidationErrors("app5", 1);
		
		/* fix class - should be no error */
		resourceHelper.replaceInEditor(editorForClass("app5", "src", 
				"test", "App.java").toTextEditor(), "extends Application", "", true);
		
		/* test validation error */
		assertCountOfApplicationAnnotationValidationErrors("app5", 0);
	}
	
	/**
	 * Seems like JBIDE-12690 workaround, which was resolved.
	 * Tests pass also without this workaround.
	 * 
	 * @param projectName
	 * @param path
	 */
	private void jbide12680Workaround(String projectName, String... path) {
		packageExplorer.openFile(projectName, path);
		String javaClass = obtainClassNameFromPath(path);
		bot.waitUntil(new ActiveEditorHasTitleCondition(bot, javaClass));
		SWTBotEclipseEditor eclipseEditor = bot.activeEditor().toTextEditor();
		eclipseEditor.insertText(" ");
		bot.sleep(Timing.time1S());
		eclipseEditor.save();
	}

	private String obtainClassNameFromPath(String... path) {
		int length  = path.length;
		return path[length - 1];
	}
	
}
