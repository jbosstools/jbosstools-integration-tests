package org.jboss.tools.freemarker.ui.bot.test;

import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(RequirementAwareSuite.class)
//@SuiteClasses({FreemarkerPreferencePageTest.class })
//@SuiteClasses({FreeMarkerTest.class })
@SuiteClasses({FreeMarkerEditorTest.class, FreemarkerPreferencePageTest.class })
public class FreeMarkerSuite {

}
