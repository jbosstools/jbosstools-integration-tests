package org.jboss.tools.teiid.reddeer.wizard;

import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.syncExec;

import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.jboss.reddeer.eclipse.datatools.ui.wizard.DriverDefinitionPage;
import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.teiid.reddeer.preference.DriverDefinitionPreferencePageExt;

/**
 * 
 * @author apodhrad
 * 
 */
public class HSQLDBDriverWizard {

	public static final String DEFAULT_NAME = "HSQLDB Driver";
	public static final String DEFAULT_DRIVER = "org.hsqldb.jdbc.JDBCDriver";

	private String name;
	private String library;
	private String driver;

	public HSQLDBDriverWizard(String library) {
		super();
		this.library = library;
		this.driver = DEFAULT_DRIVER;
		this.name = DEFAULT_NAME;
	}

	public HSQLDBDriverWizard setDriver(String driver) {
		this.driver = driver;
		return this;
	}

	public HSQLDBDriverWizard setName(String name) {
		this.name = name;
		return this;
	}

	public void create() {
		new DriverDefinitionPreferencePageExt().open();
		new PushButton("Add...").click();
		DriverDefinitionPageExt page = new DriverDefinitionPageExt();
		page.selectDriverTemplate("Generic JDBC Driver", "1.0");
		page.setName(name);
		page.addDriverLibrary(library);
		page.setDriverClass(driver);
		new PushButton("OK").click();
		new PushButton("OK").click();
	}

	private class DriverDefinitionPageExt extends DriverDefinitionPage {

		public DriverDefinitionPageExt() {
			super(null, -1);
		}

		public DriverDefinitionPageExt(WizardDialog wizardDialog, int pageIndex) {
			super(wizardDialog, pageIndex);
		}

		@Override
		public void setDriverClass(String driverClass) {
			selectTab(TAB_PROPERTIES);
			new DefaultTreeItem(1, "General", "Driver Class").doubleClick();
			new PushButton("...").click();
			new DefaultText().setText(driverClass);
			new PushButton("OK").click();
		}

		@Override
		public void addDriverLibrary(String driverLocation) {
			selectTab(TAB_JAR_LIST);
			addItem(driverLocation);
			addItem(driverLocation);
			removeDriverLibrary(driverLocation);
		}

		private void addItem(final String item) {
			syncExec(new VoidResult() {

				@Override
				public void run() {
					Bot.get().list().widget.add(item);
				}
			});
		}

	}
}
