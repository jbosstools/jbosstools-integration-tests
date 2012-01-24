package org.jboss.tools.portlet.ui.bot.task.wizard;


/**
 * 
 * Provides the functionality of opening wizard and navigating between its pages. The data
 * should fill every wizard page itself.  
 * 
 * @author ljelinko
 *
 */
public class WizardOpeningAndFillingTask extends WizardFillingTask {

	private String wizardName;
	
	private String wizardPath;
	
	public WizardOpeningAndFillingTask(String name) {
		this(name, null);
	}
	
	public WizardOpeningAndFillingTask(String name, String path) {
		super();
		this.wizardName = name;
		this.wizardPath = path;
	}

	@Override
	public void perform() {
		performInnerTask(new WizardOpeningTask(wizardName, wizardPath));
		super.perform();
	}
}
