package org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss;

import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.WizardPageFillingTask;

public class JavaPortletWizardPageFillingTask extends AbstractSWTTask implements WizardPageFillingTask {

	private String project;

	private String sourceFolder;

	private String packageName;

	private String className;

	private String superclass;

	@Override
	public void perform() {
		if (project != null){
			getBot().comboBoxWithLabel("Project:").setSelection(project);
		}

		if (sourceFolder != null){
			getBot().textWithLabel("Source folder:").setText(sourceFolder);
		}

		if (packageName != null){
			getBot().textWithLabel("Java package:").setText(packageName);
		}

		if (className != null){
			getBot().textWithLabel("Class name:").setText(className);
		}

		if (superclass != null){
			getBot().textWithLabel("Superclass:").setText(superclass);
		}
	}

	public void setProject(String project) {
		this.project = project;
	}

	public void setSourceFolder(String sourceFolder) {
		this.sourceFolder = sourceFolder;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setSuperclass(String superclass) {
		this.superclass = superclass;
	}
}
