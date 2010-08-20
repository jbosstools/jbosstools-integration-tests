package org.jboss.tools.esb.ui.bot.tests;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.TestConfigurator;
import org.jboss.tools.ui.bot.ext.config.Annotations.*;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.Test;
@SWTBotTestRequires(esb=@ESB())
public class CreateRuntimeFromESB extends SWTTestExt {

	@Test
	public void createESBRuntime() {
		SWTBot wiz = open.preferenceOpen(ActionItem.Preference.JBossToolsJBossESBRuntimes.LABEL);
		wiz.button("Add").click();
		bot.shell(IDELabel.Shell.NEW_ESB_RUNTIME).activate();
		assertFalse("Finish button must not be enabled when no home dir is defined",bot.button(IDELabel.Button.FINISH).isEnabled());
		bot.text(1).setText(TestConfigurator.currentConfig.getEsb().esbHome);
		assertTrue("Version was not automaticly selected by setting ESB home dir",bot.comboBox().selection().equals(TestConfigurator.currentConfig.getEsb().version));
		bot.text(0).setText("runtime");
		String name = bot.text(0).getText(); 
		assertFalse("Runtime name was not automaticly set by setting ESB home dir",name.equals(""));
		assertTrue("Finish button must be enabled when valid home dir is defined",bot.button(IDELabel.Button.FINISH).isEnabled());
		open.finish(bot.activeShell().bot());
		open.finish(wiz, IDELabel.Button.OK);
		eclipse.removeESBRuntime(name);
	}
}
