package org.jboss.tools.modeshape.rest.ui.bot.ext.dialog;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.jboss.tools.ui.bot.ext.wizards.SWTBotWizard;

/**
 * 
 * This class represents dialog for published locations.
 * 
 * @author apodhrad
 * 
 */
public class ModeshapeLocationDialog extends SWTBotWizard {

	public static final String LABEL_SERVER_URL = "Server URL";
	public static final String LABEL_USER = "User";
	public static final String LABEL_REPOSITORY = "Repository";
	public static final String LABEL_WORKSPACE = "Workspace";
	public static final String LABEL_PUBLISHED_URL = "Published URL";

	public ModeshapeLocationDialog(SWTBotShell dialog) {
		super(dialog.activate().widget);
	}

	public String getServerUrl() {
		return getTableCell(LABEL_SERVER_URL);
	}

	public String getUser() {
		return getTableCell(LABEL_USER);
	}

	public String getRepository() {
		return getTableCell(LABEL_REPOSITORY);
	}

	public String getWorkspace() {
		return getTableCell(LABEL_WORKSPACE);
	}

	public String getPublishedUrl() {
		return getTableCell(LABEL_PUBLISHED_URL);
	}

	private String getTableCell(String label) {
		SWTBotTable table = bot().table();
		int columnIndex = table.indexOfColumn(label);
		return table.cell(0, columnIndex);
	}

	public void clickOK() {
		bot().button("OK").click();
	}
}
