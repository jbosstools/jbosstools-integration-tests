package org.jboss.tools.maven.reddeer.project.examples.wizard;

import org.eclipse.swt.graphics.Image;
import org.jboss.reddeer.swt.api.Group;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.runtime.reddeer.wizard.DownloadRuntimesTaskWizard;
import org.jboss.tools.runtime.ui.RuntimeSharedImages;
import org.jboss.tools.runtime.ui.RuntimeUIActivator;

public class ExampleRequirement {
	
	private String type;
	private String name;
	private boolean met;
	private Table table;
	private int requirementIndex;
	
	public ExampleRequirement(Table table, int requirementIndex){
		this.table=table;
		this.requirementIndex = requirementIndex;
		this.type = table.getItem(requirementIndex).getText();
		this.name = table.getItem(requirementIndex).getText(1);
		this.met = isRequirementMet(table.getItem(requirementIndex).getImage(2));
	}
	
	public String getType(){
		return type;
	}
	
	public String getName(){
		return name;
	}
	
	public boolean isMet(){
		return met;
	}
	
	public DownloadRuntimesTaskWizard downloadAndInstall(){
		table.select(requirementIndex);
		Group reqGroup = new DefaultGroup("Requirements");
		new PushButton(reqGroup, "Download and Install...").click();
		new DefaultShell("Download Runtimes");
		return new DownloadRuntimesTaskWizard();
	}
	
	public void install(){
		table.select(requirementIndex);
		Group reqGroup =  new DefaultGroup("Requirements");
		new PushButton(reqGroup, "Install...").click();
		new DefaultShell("Preferences");
	}
	
	private boolean isRequirementMet(Image i){
		return i.equals(RuntimeUIActivator.sharedImages().image(
				RuntimeSharedImages.CHECKBOX_ON_KEY));
	}

}
