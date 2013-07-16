package org.jboss.tools.teiid.reddeer.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.teiid.reddeer.view.GuidesView;

/**
 * Creates a new virtual database.
 * 
 * @author Lucia Jelinkova
 * 
 */
public class CreateVDB extends NewWizardDialog {

	private String folder;

	private String name;

	public CreateVDB() {
		super("Teiid Designer", "Teiid VDB");
	}

	public void execute() {
		open();
		fillFirstPage();
		finish();
	}
	
	/**
	 * Define new VDB
	 * @param viaGuides true if set via Modelling actions
	 */
	public void execute(boolean viaGuides){
		if (viaGuides){
			new GuidesView().chooseAction("Model JDBC Source", "Define VDB");
			Bot.get().button("New...").click();
			fillFirstPage();
			finish();
			Bot.get().button("OK").click();
		} else {
			execute();
		}
	}

	private void fillFirstPage() {
		// TODO: LabeledText
		// new LabeledText("In Folder:").setText(folder);
		// TODO: Do we really need to set folder?
		// Bot.get().textWithLabel("In Folder:").setText(folder);
		// TODO: LabeledText
		// new LabeledText("VDB Name:").setText(name);
		Bot.get().textWithLabel("VDB Name:").setText(name);
		if (this.folder != null){
			Bot.get().textWithLabel("In Folder:").setText(folder);
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}
}
