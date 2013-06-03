package org.jboss.tools.bpmn2.itests.reddeer.requirements;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.junit.requirement.Requirement;
import org.jboss.reddeer.swt.util.Bot;

import org.jboss.tools.bpmn2.itests.reddeer.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.bpmn2.itests.wizard.JBPMProcessWizard;
import org.jboss.tools.bpmn2.itests.wizard.JavaProjectWizard;

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
		
		String profile() default "Full";
		
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
		PackageExplorer pe = new PackageExplorer();
		if (!pe.containsProject(d.project())) {
			new JavaProjectWizard().execute(d.project());
			
			try {
				Bot.get().shell("Open Associated Perspective?").activate();
				Bot.get().button("No").click();
			} catch (WidgetNotFoundException e) {
				// ignore
			}
		}
		
		// process name
		String n = d.name().replaceAll("\\s+", "");
		// process id
		String i = n.replace("-", "").replace("_", "");
		// file name
		String f = n + ".bpmn2";
		// project name
		String p = d.project();
		
		if (pe.getProject(p).containsItem(f)) {
			pe.getProject(p).getProjectItem(f).delete();
		}
		
		new JBPMProcessWizard().execute(new String[] {p}, f, n, i, "defaultPackage");
	}
	
}
