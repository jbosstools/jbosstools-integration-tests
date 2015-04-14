package org.jboss.tools.freemarker.ui.bot.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.jdt.ui.WorkbenchPreferenceDialog;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.handler.WorkbenchPartHandler;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
public class FreemarkerPreferencePageTest {
	
	private static final Logger log = Logger.getLogger(FreemarkerPreferencePageTest.class);

	@BeforeClass
	public static void beforeClass() {
		JavaPerspective p = new JavaPerspective();
		p.open();

		WorkbenchPartHandler.getInstance().closeAllEditors();
		new WaitWhile(new JobIsRunning());

		JavaPerspective jp = new JavaPerspective();
		jp.open();
	}

	@Test
	public void emptyTest() {
		assertTrue(true);
	}

	@Test
	public void freeMarkerPreferenceTest() {
		log.step("Open Preference dialog");
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		log.step("Select Freemarker Preference Page");
		FreemarkerPreferencePage page = new FreemarkerPreferencePage();
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
