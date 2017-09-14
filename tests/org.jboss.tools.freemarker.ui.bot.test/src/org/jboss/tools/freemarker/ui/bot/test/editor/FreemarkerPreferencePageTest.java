/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.freemarker.ui.bot.test.editor;

import static org.junit.Assert.assertFalse;

import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.workbench.handler.EditorHandler;
import org.eclipse.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Freemarker Preference page tests
 * @author Jiri Peterka
 *
 */
@RunWith(RedDeerSuite.class)
public class FreemarkerPreferencePageTest {
	
	private static final Logger log = Logger.getLogger(FreemarkerPreferencePageTest.class);

	@BeforeClass
	public static void beforeClass() {
		JavaPerspective p = new JavaPerspective();
		p.open();

		EditorHandler.getInstance().closeAll(false);
		new WaitWhile(new JobIsRunning());

		JavaPerspective jp = new JavaPerspective();
		jp.open();
	}
	
	@Test
	public void freeMarkerPreferenceTest() {
		log.step("Open Preference dialog");
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		log.step("Select Freemarker Preference Page");
		FreemarkerPreferencePage page = new FreemarkerPreferencePage(preferenceDialog);
		preferenceDialog.select(page);
		
		log.step("Set Highlight Related Directives on Freemarker page");
		page.setHighLightRelatedDirectives(false);
		page.apply();
		preferenceDialog.ok();

		preferenceDialog.open();
		preferenceDialog.select(page);
		boolean highLightRelatedDirectives = page
				.getHighLightRelatedDirectives();
		page.apply();
		preferenceDialog.ok();		
		log.step("Check if it's set correctly");
		assertFalse(highLightRelatedDirectives);
	}
}
