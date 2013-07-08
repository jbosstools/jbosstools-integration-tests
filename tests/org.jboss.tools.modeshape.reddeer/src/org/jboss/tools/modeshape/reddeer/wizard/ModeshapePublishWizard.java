package org.jboss.tools.modeshape.reddeer.wizard;

import java.util.Arrays;
import java.util.List;

import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.list.DefaultList;

/**
 * Wizard for publishing to ModeShape repository.
 * 
 * @author apodhrad
 * 
 */
public class ModeshapePublishWizard extends WizardDialog {

	public static final String DIALOG_TITLE = "New Server";

	public static final String LABEL_SERVER = "Server:";
	public static final String LABEL_JCR_REPOSITORY = "JCR Repository:";
	public static final String LABEL_JCR_WORKSPACE = "JCR Workspace:";
	public static final String LABEL_PUBLISH_AREA = "Publish Area:";

	public ModeshapePublishWizard setServer(String server) {
		new DefaultCombo(LABEL_SERVER).setSelection(server);
		return this;
	}

	public ModeshapePublishWizard setJcrRepository(String repository) {
		new DefaultCombo(LABEL_JCR_REPOSITORY).setSelection(repository);
		return this;
	}

	public ModeshapePublishWizard setJcrWorkspace(String workspace) {
		new DefaultCombo(LABEL_JCR_WORKSPACE).setSelection(workspace);
		return this;
	}

	public ModeshapePublishWizard setPublishArea(String publishArea) {
		new DefaultCombo(LABEL_PUBLISH_AREA).setText(publishArea);
		return this;
	}

	public List<String> getFiles() {
		String[] files = new DefaultList().getListItems();
		return Arrays.asList(files);
	}
}
