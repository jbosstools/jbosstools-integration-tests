package org.jboss.tools.teiid.reddeer.preference;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.jboss.reddeer.eclipse.datatools.ui.DriverDefinition;
import org.jboss.reddeer.eclipse.datatools.ui.DriverTemplate;
import org.jboss.reddeer.eclipse.datatools.ui.preference.DriverDefinitionPreferencePage;
import org.jboss.reddeer.eclipse.datatools.ui.wizard.DriverDefinitionPage;
import org.jboss.reddeer.eclipse.datatools.ui.wizard.DriverDefinitionWizard;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;

public class DriverDefinitionPreferencePageExt extends DriverDefinitionPreferencePage {

	@Override
	public void open() {
		if (isRunningOnMacOs()) {
			new SWTWorkbenchBot().shells()[0].pressShortcut(SWT.COMMAND, ',');
		}
		super.open();
	}

	public void addDriverDefinition(DriverDefinition driverDefinition) {
		new PushButton("Add...").click();
		new DriverDefinitionWizardExt(driverDefinition).execute();
		new PushButton("OK").click();
	}

	private static boolean isRunningOnMacOs() {
		return Platform.getOS().equalsIgnoreCase("macosx");
	}

	private class DriverDefinitionWizardExt extends DriverDefinitionWizard {

		private DriverDefinition driverDefinition;

		public DriverDefinitionWizardExt(DriverDefinition driverDefinition) {
			this.driverDefinition = driverDefinition;
		}

		public void execute() {
			DriverTemplate drvTemp = driverDefinition.getDriverTemplate();
			DriverDefinitionPage page = getFirstPage();
			page.selectDriverTemplate(drvTemp.getType(), drvTemp.getVersion());
			page.setName(driverDefinition.getDriverName());
			page.addDriverLibrary(driverDefinition.getDriverLibrary());
		}
	}
}
