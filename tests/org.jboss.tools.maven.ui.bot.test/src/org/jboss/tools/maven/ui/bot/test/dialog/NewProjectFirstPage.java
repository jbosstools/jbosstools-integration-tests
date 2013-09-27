package org.jboss.tools.maven.ui.bot.test.dialog;

import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;

public abstract class NewProjectFirstPage  extends WizardPage{
	
	public void setProjectName(String name){
		new LabeledText("Project name:").setText(name);
	}
	
	public void activateFacet(String facet, String version){
		new PushButton("Modify...").click();
		new WaitUntil(new ShellWithTextIsActive("Project Facets"), TimePeriod.NORMAL);
		new DefaultTreeItem(facet).select();
		new DefaultTreeItem(facet).setChecked(true);
		if(version!=null){
			new ContextMenu("Change Version...").select();
			new WaitUntil(new ShellWithTextIsActive("Change Version"), TimePeriod.NORMAL);
			new DefaultCombo("Version:").setSelection(version);
			new PushButton("OK").click();
			new WaitUntil(new ShellWithTextIsActive("Project Facets"), TimePeriod.NORMAL);
		}
		new PushButton("OK").click();
		new WaitUntil(new ShellWithTextIsActive("New Seam Project"), TimePeriod.NORMAL);
	}
	
	public void setRuntime(String runtime){
		new DefaultCombo("Target runtime").setSelection(runtime);
	}

}
