package org.jboss.tools.hb.ui.bot.test.generation;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swtbot.eclipse.finder.matchers.WidgetMatcherFactory;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCheckBox;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.hamcrest.Matcher;
import org.jboss.tools.hb.ui.bot.test.HibernateBaseTest;
import org.jboss.tools.ui.bot.ext.config.Annotations.DB;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.Test;

/**
 * Hibernate code generation configuration ui bot test
 * - Code generation configuration can be created
 * - Source code can be generated via hibernate generation configuration
 * @author jpeterka
 * 
 */
@Require(db = @DB, clearProjects = true, perspective = "Hibernate")
public class CreateCodeGenerationConfiguration35 extends CreateCodeGenerationConfiguration {

	@Test
	public void hibernateCodeGeneration35() {
		setPrjName("hibernate35");
		hibernateCodeGeneration();
	}
}
