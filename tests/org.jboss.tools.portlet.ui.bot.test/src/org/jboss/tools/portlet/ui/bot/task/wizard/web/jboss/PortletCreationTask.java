package org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss;

import org.jboss.tools.portlet.ui.bot.task.wizard.WizardFillingTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.WizardOpeningTask;

/**
 * Creates a new portlet using a JBoss wizard. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class PortletCreationTask extends WizardFillingTask {

	private PortletWizardPageFillingTask firstPage;
	
	public PortletCreationTask() {
		super();
		firstPage = new PortletWizardPageFillingTask();
		addWizardPage(firstPage);
	}
	
	@Override
	public void perform() {
		performInnerTask(new WizardOpeningTask("Java Portlet", "JBoss Tools Web/Portlet"));
		super.perform();
	}

	public void setProject(String project) {
		firstPage.setProject(project);
	}

	public void setSourceFolder(String sourceFolder) {
		firstPage.setSourceFolder(sourceFolder);
	}

	public void setPackageName(String packageName) {
		firstPage.setPackageName(packageName);
	}

	public void setClassName(String className) {
		firstPage.setClassName(className);
	}

	public void setSuperclass(String superclass) {
		firstPage.setSuperclass(superclass);
	}
}
