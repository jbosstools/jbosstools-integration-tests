package org.jboss.tools.hb.ui.bot.test.jpa;

import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.hibernate.reddeer.test.HibernateRedDeerTest;
import org.junit.Test;

/**
 * Create JPA Project ui bot test
 * - JPA2 project can be created
 * 
 * @author jpeterka
 * 
 */
public class CreateJPAProjectTest extends HibernateRedDeerTest {
	
	final String prj = "jpa35test";
	
	@Test
	public void createJPAProject() {
		createProject();
	}

	private void createProject() {
		new ShellMenu("File","New","JPA Project").select();

		// JPA Project Page
		new WaitUntil(new ShellWithTextIsActive("New JPA Project"));
		new LabeledText("Project name:").setText(prj);
		new NextButton().click();
		
		// Java Page
		new NextButton().click();
		// JPA Facet Page
		new DefaultCombo(0).setSelection("Hibernate (JPA 2.1)");
		new DefaultCombo(0).setSelection("Disable Library Configuration");	

		// Finish
		new FinishButton().click();
		new WaitUntil(new ShellWithTextIsActive("New JPA Project"));
	}
}
