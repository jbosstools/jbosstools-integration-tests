package org.jboss.tools.freemarker.ui.bot.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.jdt.ui.WorkbenchPreferenceDialog;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.handler.WorkbenchHandler;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
public class FreemarkerPreferencePageTest {

	@BeforeClass
	public static void beforeClass() {
		JavaPerspective p = new JavaPerspective();
		p.open();

		WorkbenchHandler.getInstance().closeAllEditors();
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
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		FreemarkerPreferencePage page = new FreemarkerPreferencePage();
		preferenceDialog.select(page);
		
		page.setHighLightRelatedDirectives(false);
		page.apply();
		preferenceDialog.ok();

		preferenceDialog.open();
		preferenceDialog.select(page);
		boolean highLightRelatedDirectives = page
				.getHighLightRelatedDirectives();
		page.apply();
		preferenceDialog.ok();		
		
		assertFalse(highLightRelatedDirectives);
	}
}
