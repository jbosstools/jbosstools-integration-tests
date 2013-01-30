package org.jboss.tools.freemarker.ui.bot.test;

import java.util.List;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.helper.SubversiveHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.BeforeClass;
import org.junit.Test;

public class FreemarkerPreferencePageTest extends SWTTestExt {

	@BeforeClass
		public static void beforeClass() {
			eclipse.closeView(IDELabel.View.WELCOME);
			eclipse.closeView(IDELabel.View.JBOSS_CENTRAL);
			eclipse.closeAllEditors();
	
			util.waitForAll();
			List<SWTBotShell> shells = bot.waitForNumberOfShells(1);
			SWTBotShell shell = bot.shell(shells.get(0).getText());
			log.info(shell.getText());
			shell.activate();
			shell.setFocus();
			open.perspective(ActionItem.Perspective.JAVA.LABEL);
			
			SubversiveHelper.disableSVNDecoration();
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
			boolean highLightRelatedDirectives = page.getHighLightRelatedDirectives();
			assertFalse(highLightRelatedDirectives);
		}
}
	
	
