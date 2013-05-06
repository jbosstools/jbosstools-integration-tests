package org.jboss.tools.bpmn2.itests.swt.ext;

import java.io.File;
import java.io.FileInputStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.jboss.reddeer.eclipse.jface.preference.PreferencePage;
import org.jboss.reddeer.junit.requirement.Requirement;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.tools.bpmn2.itests.swt.ext.JBPM5RuntimeRequirement.JBPM5;

public class JBPM5RuntimeRequirement implements Requirement<JBPM5> {

	private String name;
	
	private String runtimeHome;
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface JBPM5 {

	}

	public boolean canFulfill() {
		Properties props = new Properties();
		try {
			String propsFilePath = System.getProperty("swtbot.test.properties");
			
			props = new Properties();
			props.load(new FileInputStream(new File(propsFilePath)));
			
			name = props.getProperty("jbpm.name", "jbpm5");
			runtimeHome = props.getProperty("jbpm.runtime.dir");
		} catch (Exception e) {
			return false;
		}
		return name != null && runtimeHome != null;
	}

	public void fulfill() {
		new PreferencePage("jBPM", "Installed jBPM Runtimes") {}.open();
		
		boolean runtimeFound = false;
		DefaultTable table = new DefaultTable();
		if (table.rowCount() > 0) {
			for (int row=0; row<table.rowCount(); row++) {
				if (table.cell(row, 0).equals(name)) {
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
			
			Logger.getLogger(JBPM5RuntimeRequirement.class).info("jBPM Runtime '" + name + "' added.");
		}
		
		table.check(name);
		new PushButton("OK").click();
	}

	public void setDeclaration(JBPM5 declaration) {
		// Not required
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setRuntimeHome(String runtimeHome) {
		this.runtimeHome = runtimeHome;
	}
	
}
