package org.jboss.tools.hibernate.reddeer.dialog;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.core.condition.WidgetIsFound;
import org.jboss.reddeer.core.matcher.ClassMatcher;
import org.jboss.reddeer.core.matcher.WithMnemonicTextMatcher;
import org.jboss.reddeer.core.matcher.WithStyleMatcher;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.link.DefaultLink;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;

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
    	new WaitWhile(new JobIsRunning());
    	new WaitUntil(new ShellWithTextIsAvailable("Properties for " + projectName));
    	new DefaultShell("Properties for " + projectName);
    	new WaitWhile(new JobIsRunning());
    	new DefaultShell("Properties for " + projectName);
    	new WaitUntil(new WidgetIsFound<Button>(new ClassMatcher(Button.class),new WithStyleMatcher(SWT.PUSH), new WithMnemonicTextMatcher("Apply")), TimePeriod.LONG);
    	PushButton apply = new PushButton("Apply");
    	new WaitUntil(new WidgetIsEnabled(apply));
    	apply.click();    	
	}
}
