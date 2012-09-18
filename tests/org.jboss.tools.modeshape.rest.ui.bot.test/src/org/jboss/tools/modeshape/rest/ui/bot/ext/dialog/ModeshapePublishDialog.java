package org.jboss.tools.modeshape.rest.ui.bot.ext.dialog;

import java.util.Arrays;
import java.util.List;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotCombo;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.jboss.tools.ui.bot.ext.wizards.SWTBotWizard;

/**
 * 
 * This class represents dialog for publishing/unpublishing files to/from the
 * server.
 * 
 * @author apodhrad
 * 
 */
public class ModeshapePublishDialog extends SWTBotWizard {

	public static final String LABEL_SERVER = "Server:";
	public static final String LABEL_JCR_REPOSITORY = "JCR Repository:";
	public static final String LABEL_JCR_WORKSPACE = "JCR Workspace:";
	public static final String LABEL_PUBLISH_AREA = "Publish Area:";

	public ModeshapePublishDialog(SWTBotShell dialog) {
		super(dialog.activate().widget);
	}

	public void setServer(String server) {
		getComboBox(LABEL_SERVER).setSelection(server);
	}

	public String getServer() {
		return getComboBox(LABEL_SERVER).getText();
	}

	public void setJcrRepository(String repository) {
		getComboBox(LABEL_JCR_REPOSITORY).setSelection(repository);
	}

	public String getJcrRepository() {
		return getComboBox(LABEL_JCR_REPOSITORY).getText();
	}

	public void setJcrWorkspace(String workspace) {
		getComboBox(LABEL_JCR_WORKSPACE).setSelection(workspace);
	}

	public String getJcrWorkspace() {
		return getComboBox(LABEL_JCR_WORKSPACE).getText();
	}

	public void setPublishArea(String publishArea) {
		getComboBox(LABEL_PUBLISH_AREA).setText(publishArea);
	}

	public String getPublishArea() {
		return getComboBox(LABEL_PUBLISH_AREA).getText();
	}

	private SWTBotCombo getComboBox(String label) {
		return bot().comboBoxWithLabel(label);
	}

	public List<String> getComboBoxList(String label) {
		return Arrays.asList(getComboBox(label).items());
	}
}
