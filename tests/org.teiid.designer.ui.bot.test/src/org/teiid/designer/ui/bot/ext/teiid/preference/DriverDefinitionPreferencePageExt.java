package org.teiid.designer.ui.bot.ext.teiid.preference;

import org.eclipse.swt.SWT;
import org.jboss.reddeer.eclipse.datatools.ui.preference.DriverDefinitionPreferencePage;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.ui.bot.ext.SWTJBTExt;

public class DriverDefinitionPreferencePageExt extends DriverDefinitionPreferencePage {

	// https://github.com/jboss-reddeer/reddeer/issues/39
	@Override
	public void open() {
		if (SWTJBTExt.isRunningOnMacOs()) {
			Bot.get().shells()[0].pressShortcut(SWT.COMMAND, ',');
		}
		super.open();
	}

}
