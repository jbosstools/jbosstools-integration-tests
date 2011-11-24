package org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss;


/**
 * Creates a new java portlet using a JBoss wizard. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class JavaPortletCreationTask extends AbstractPortletCreationTask {
	
	private JavaPortletWizardPageFillingTask firstPage;
	
	public JavaPortletCreationTask() {
		super("Java Portlet");
		firstPage = new JavaPortletWizardPageFillingTask();
		addWizardPage(firstPage);
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
