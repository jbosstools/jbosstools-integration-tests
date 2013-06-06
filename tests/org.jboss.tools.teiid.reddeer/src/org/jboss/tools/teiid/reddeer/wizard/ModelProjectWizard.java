package org.jboss.tools.teiid.reddeer.wizard;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.teiid.reddeer.view.GuidesView;

/**
 * Wizard for creating a new model project
 * 
 * @author apodhrad
 * 
 */
public class ModelProjectWizard extends NewWizardDialog {

	public static final String CATEGORY = "Teiid Designer";
	public static final String PROJECT_TITLE = "Teiid Model Project";

	public ModelProjectWizard() {
		super(CATEGORY, PROJECT_TITLE);
		addWizardPage(new ModelProjectPage(), 1);
	}
	
	public ModelProjectWizard(int currentPage) {
		super(CATEGORY, PROJECT_TITLE);
		addWizardPage(new ModelProjectPage(), currentPage);
	}

	public void create(String name) {
		open();
		getWizardPage().fillWizardPage(name);
		finish();
	}
	
	/**
	 * Create new model project via action sets
	 * @param name project name (e.g. MyFirstProject)
	 * @param viaGuides true -- via action from modelling guide, false -- via main menu
	 */
	public void create(String name, boolean viaGuides){
		if (viaGuides){
			new GuidesView().chooseAction("Model JDBC Source", "Define Teiid Model Project");
			Bot.get().button("New...").click();
			getWizardPage().fillWizardPage(name);
			//TODO: select folders - source, schema, view,...
			finish();
			Bot.get().shell("Define Model Project").close();//this shell isn't active after executing method create(name)
		} else {
			create(name);
		}
		
	}
	

}
