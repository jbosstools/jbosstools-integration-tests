package org.jboss.tools.hibernate.reddeer.dialog;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsActive;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.link.DefaultLink;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.common.wait.WaitUntil;

/**
 * Project facets page
 * @author Jiri Peterka
 *
 */
public class ProjectFacetsPage extends WizardPage {

	private String projectName;
	
	/**
	 * Project facets page
	 * @param projectName given project name
	 */
	public ProjectFacetsPage(String projectName) {
		this.projectName = projectName;
	}
		
	/**
	 * Converts project into faceted form
	 */
	public void convertToFacetedForm() {
    	new DefaultTreeItem("Project Facets").select();
    	new DefaultLink("Convert to faceted form...").click();
    	new WaitUntil(new ShellWithTextIsAvailable("Properties for " + projectName));
    	new DefaultShell("Properties for " + projectName);
    	PushButton apply = new PushButton("Apply");
    	new WaitUntil(new ButtonWithTextIsActive(apply));
    	apply.click();    	
	}
}
