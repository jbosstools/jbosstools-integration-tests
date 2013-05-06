package org.jboss.tools.bpmn2.itests.reddeer.requirements;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.junit.requirement.Requirement;
import org.jboss.reddeer.swt.util.Bot;

import org.jboss.tools.bpmn2.itests.reddeer.EclipseHelper;
import org.jboss.tools.bpmn2.itests.reddeer.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.bpmn2.itests.wizard.JBPMProcessWizard;
import org.jboss.tools.bpmn2.itests.wizard.GeneralProjectWizard;

/**
 * 
 * @author mbaluch
 */
public class ProcessDefinitionRequirement implements Requirement<ProcessDefinition> {

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface ProcessDefinition {

		String name();

		String project();
		
		String file();
		
	}

	ProcessDefinition d;

	public void setDeclaration(ProcessDefinition d) {
		this.d = d;
	}
	
	public boolean canFulfill() {
		return true;
	}

	public void fulfill() {
		closeAllEditors();
		openProcessDefinition();
	}
	
	private void closeAllEditors() {
		for (SWTBotEditor e : Bot.get().editors()) {
			if (e.isDirty()) {
				e.save();
			}
			e.close();
		}
	}
	
	private void openProcessDefinition() {
		if (!new PackageExplorer().containsProject(d.project())) {
			new GeneralProjectWizard().execute(d.project());
		}
		try {
		new JBPMProcessWizard().execute(new String[] {d.project()}, d.file(), d.name(), d.name(), "defaultPackage");
		EclipseHelper.maximizeActiveShell();
		new PackageExplorer().getProject(d.project()).getProjectItem(d.file()).open();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
}
