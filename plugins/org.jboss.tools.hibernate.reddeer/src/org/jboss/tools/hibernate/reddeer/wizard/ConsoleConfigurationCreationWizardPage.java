package org.jboss.tools.hibernate.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.text.DefaultText;

/**
 * aka Hibernate Console Wizard
 * 
 * @author Jiri Peterka
 *
 */
public class ConsoleConfigurationCreationWizardPage extends WizardPage  {

	
	/**
	 * Sets hibernate console project
	 * @param project
	 */
	public void setProject(String project) {
		DefaultGroup prjGroup = new DefaultGroup("Project:");
		new DefaultText(prjGroup).setText(project);		
	}
	
	/**
	 * Sets Hibernate Console Type
	 * @param type hibernate console type (jpa, core, ...)
	 */
	public void setHibernateConsoleType(HibernateConsoleType type) {
		new RadioButton(type.toString()).click();
	}
	
	/**
	 * Sets Hibernate Console database connection type
	 * @param type db connection type
	 */
	public void setHibernateConsoleConnectionType(HibernateConsoleConnectionType type)  {	
		DefaultGroup dbConnection = new DefaultGroup("Database connection:");
		new DefaultCombo(dbConnection,0).setText(type.toString());
	}
	
	/**
	 * Sets hibernate version for hibernate console
	 * @param hbVersion hibernate version
	 */
	public void setHibernateVersion(String hbVersion) {
		new LabeledCombo("Hibernate Version:").setSelection(hbVersion);
	}
	
	/**
	 * Click ok button
	 */
	public void ok() {
		new OkButton().click();
	}
}