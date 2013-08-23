package org.jboss.tools.bpmn2.itests.reddeer.requirements;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.log4j.Logger;

import org.jboss.reddeer.eclipse.jface.preference.PreferencePage;
import org.jboss.reddeer.junit.requirement.Requirement;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.tools.bpmn2.itests.reddeer.requirements.ProcessRuntimeRequirement.ProcessRuntime;

public class ProcessRuntimeRequirement implements Requirement<ProcessRuntime> {

	private String name = System.getProperty("jbpm.name", "jbpm5");
	
	private String runtimeHome = System.getProperty("jbpm.runtime.dir");
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface ProcessRuntime {

	}

	public boolean canFulfill() {
		return name != null && runtimeHome != null;
	}

	public void fulfill() {
		new PreferencePage("jBPM", "Installed jBPM Runtimes") {}.open();
		
		boolean runtimeFound = false;
		DefaultTable table = new DefaultTable();
		if (table.rowCount() > 0) {
			for (int row=0; row<table.rowCount(); row++) {
				if (table.getItem(row).getText(0).equals(name)) {
					runtimeFound = true;
					break;
				}
			}
		}

		if (!runtimeFound) {
			new PushButton("Add...").click();
			new DefaultText(0).setText(name);
			new DefaultText(1).setText(runtimeHome);
			new PushButton("OK").click();
			
			Logger.getLogger(ProcessRuntimeRequirement.class).info("jBPM Runtime '" + name + "' added.");
		}
		
		table.getItem(name).setChecked(true);
		new PushButton("OK").click();
	}

	public void setDeclaration(ProcessRuntime declaration) {
		// Not required
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setRuntimeHome(String runtimeHome) {
		this.runtimeHome = runtimeHome;
	}
	
}
