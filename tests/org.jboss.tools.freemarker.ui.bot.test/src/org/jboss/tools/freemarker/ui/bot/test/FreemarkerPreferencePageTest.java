package org.jboss.tools.freemarker.ui.bot.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.handler.WorkbenchHandler;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.junit.BeforeClass;
import org.junit.Test;

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
		FreemarkerPreferencePage page = new FreemarkerPreferencePage();
		page.open();
		page.setHighLightRelatedDirectives(false);
		page.apply();
		page.ok();

		page.open();
		boolean highLightRelatedDirectives = page
				.getHighLightRelatedDirectives();
		assertFalse(highLightRelatedDirectives);
	}
}
