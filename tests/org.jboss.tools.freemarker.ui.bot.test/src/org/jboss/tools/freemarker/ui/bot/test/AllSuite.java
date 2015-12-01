package org.jboss.tools.freemarker.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RedDeerSuite.class)
@Suite.SuiteClasses({

	FreemarkerPreferencePageTest.class,
	FreeMarkerEditorTest.class,
	FreemarkerDirectiveTest.class,
	FreeMarkerCodeAssistTest.class
})
public class AllSuite {

}
