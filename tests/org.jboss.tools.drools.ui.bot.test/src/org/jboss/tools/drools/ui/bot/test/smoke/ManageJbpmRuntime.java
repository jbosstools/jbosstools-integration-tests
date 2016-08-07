package org.jboss.tools.drools.ui.bot.test.smoke;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.jboss.tools.drools.ui.bot.test.DroolsAllBotTests;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTEclipseExt;
import org.jboss.tools.ui.bot.ext.SWTOpenExt;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.Test;

public class ManageJbpmRuntime extends SWTTestExt {

	@Test
	public void testManageJbpmRuntime() {
		addJbpmRuntime(DroolsAllBotTests.JBPM_RUNTIME_NAME, DroolsAllBotTests.JBPM_RUNTIME_LOCATION, true);
	}
	
	public void addJbpmRuntime(String runtimeName, String runtimeLocation, boolean setAsDefault) {
		selectJbpmPreferences();
		bot.button(IDELabel.Button.ADD).click();
		bot.shell(IDELabel.Shell.JBPM_RUNTIME).activate();
		bot.textWithLabel(IDELabel.JbpmRuntimeDialog.NAME).setText(runtimeName);
	    bot.textWithLabel(IDELabel.JbpmRuntimeDialog.PATH).setText(runtimeLocation);
	    bot.button(IDELabel.Button.OK).click();
	    bot.shell(IDELabel.Shell.PREFERENCES).activate();
	    SWTBotTable table = bot.table();
	    boolean jbpmRuntimeAdded = SWTEclipseExt.isItemInTableColumn(table, runtimeName, 0)
	    		&& SWTEclipseExt.isItemInTableColumn(table, runtimeLocation, 1);
	    // Set new runtime as default
	    if (setAsDefault) {
	      table.getTableItem(0).check();
	    }
	    bot.button(IDELabel.Button.OK).click();
	    assertTrue("jBPM Runtime with name [" + runtimeName + "] and location ["
	    		+ runtimeLocation + "] was not added properly.", jbpmRuntimeAdded);
	    DroolsAllBotTests.setTestJbpmRuntimeName(runtimeName);
	    DroolsAllBotTests.setTestJbpmRuntimeLocation(runtimeLocation);
	    SWTEclipseExt.hideWarningIfDisplayed(bot);
	}

	/**
	 * Selects Drools Preferences within Preferences Dialog
	 */
	private void selectJbpmPreferences() {
		jbt.delay();
		new SWTOpenExt(new SWTBotExt()).preferenceOpen(ActionItem.Preference.JbpmInstalledJbpmRuntimes.LABEL);
	}
}
