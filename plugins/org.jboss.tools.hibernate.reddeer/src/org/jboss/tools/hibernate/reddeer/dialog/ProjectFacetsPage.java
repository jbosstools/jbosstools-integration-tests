package org.jboss.tools.hibernate.reddeer.dialog;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsActive;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.link.DefaultLink;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.WaitUntil;

/**
 * Project facets page
 * @author Jiri Peterka
 *
 */
public class ProjectFacetsPage extends WizardPage {

	private String projectName;
	
	public ProjectFacetsPage(String projectName) {
		this.projectName = projectName;
	}
	
	public void convertToFacetedForm() {
    	new DefaultTreeItem("Project Facets").select();
    	new DefaultLink("Convert to faceted form...").click();
    	new WaitUntil(new ShellWithTextIsActive("Properties for " + projectName));    	
    	PushButton apply = new PushButton("Apply");
    	new WaitUntil(new ButtonWithTextIsActive(apply));
    	apply.click();    	
	}
}
