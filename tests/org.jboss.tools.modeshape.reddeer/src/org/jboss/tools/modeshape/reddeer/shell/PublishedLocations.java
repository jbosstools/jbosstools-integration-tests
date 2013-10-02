package org.jboss.tools.modeshape.reddeer.shell;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;

/**
 * Published Locations Shell
 * 
 * @author apodhrad
 *
 */
public class PublishedLocations extends DefaultShell {

	public static final String DIALOG_TITLE = "";

	public static final String LABEL_SERVER_URL = "Server URL";
	public static final String LABEL_USER = "User";
	public static final String LABEL_REPOSITORY = "Repository";
	public static final String LABEL_WORKSPACE = "Workspace";
	public static final String LABEL_PUBLISHED_URL = "Published URL";

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

	public void copyURL() {
		new PushButton("Copy URL").click();
	}

	private String getTableCell(String label) {
		SWTBotTable table = new SWTWorkbenchBot().table();
		int columnIndex = table.indexOfColumn(label);
		return table.cell(0, columnIndex);
	}

	public void ok() {
		new PushButton("OK").click();
	}
}
