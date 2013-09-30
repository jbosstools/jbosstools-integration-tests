package org.jboss.tools.bpmn2.itests.reddeer.requirements;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.log4j.Logger;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.junit.requirement.Requirement;
import org.jboss.reddeer.swt.exception.SWTLayerException;
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

	Logger log = Logger.getLogger(ProcessDefinitionRequirement.class);
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface ProcessDefinition {

		String name();

		String project();
		
		String profile() default "Full";
		
	}

	ProcessDefinition d;

	boolean useRobot;

	
	public ProcessDefinitionRequirement() {
		useRobot = Boolean.parseBoolean(System.getProperty("awt.robot"));
	}
	
	public void setDeclaration(ProcessDefinition d) {
		this.d = d;
	}
	
	public boolean canFulfill() {
		return true;
	}

	public void fulfill() {
		closeAllEditors();
		try {
			openProcessDefinition();
		} catch (SWTLayerException e) {
			/*
			 * Sometimes the main window looses focus. The only way
			 * to get it back is using an AWT robot which simulates
			 * native clicks.
			 * 
			 * Using the robot itself can cause issue on certain machines
			 * so use it only if trouble arises.
			 */
			if (useRobot) {
				setFocus();
				openProcessDefinition();
			} else {
				throw e;
			}
		}
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
	
	private void setFocus() {
		try {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			int x = ((int) screenSize.getWidth()) / 2;
			int y = ((int) screenSize.getHeight()) / 2;

			log.info("[ProcessDefinitionRequirement][AWT] Focus on main window (x=" + x + ", y=" + y + ")");
			
			Robot r = new Robot();
			r.mouseMove(x, y);
			r.mousePress(InputEvent.BUTTON1_MASK);
			r.mouseRelease(InputEvent.BUTTON1_MASK);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
