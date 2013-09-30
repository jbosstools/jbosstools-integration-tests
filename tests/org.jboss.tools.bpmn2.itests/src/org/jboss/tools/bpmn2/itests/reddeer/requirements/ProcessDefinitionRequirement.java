package org.jboss.tools.bpmn2.itests.reddeer.requirements;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.junit.requirement.Requirement;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
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
		// Try to give it focus
		try {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			int width = (int) screenSize.getWidth();
			int height = (int) screenSize.getHeight();
			
			Robot r = new Robot();
			r.mouseMove(width / 2, height / 2);
			r.mousePress(InputEvent.BUTTON1_MASK);
			r.mouseRelease(InputEvent.BUTTON1_MASK);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void openProcessDefinition() {
		PackageExplorer pe = new PackageExplorer();
		pe.open();

		if (!pe.containsProject(d.project())) {
			new JavaProjectWizard().execute(d.project());
			
			try {
				new DefaultShell("Open Associated Perspective?");
				new PushButton("No").click();
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
		
		/*
		 * The same as above. Tree not found.
		 */
		if (pe.getProject(p).containsItem(f)) {
			pe.getProject(p).getProjectItem(f).delete();
		}
		
		new JBPMProcessWizard().execute(new String[] {p}, f, n, i, "defaultPackage");
	}
	
}
