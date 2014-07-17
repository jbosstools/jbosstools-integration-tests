package org.jboss.tools.maven.reddeer.project.examples.wizard;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.api.Group;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.link.AnchorLink;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.tools.maven.reddeer.wizards.AddRepositoryDialog;

public class NewProjectExamplesStacksRequirementsPage extends WizardPage{
	
	public void setTargetRuntime(String selection){
		new DefaultCombo(0).setSelection(selection);
	}
	
	
	/**
	 * Sets nth target runtime.
	 * @param index
	 */
	public void setTargetRuntime(int index){
		new DefaultCombo(0).setSelection(index);
	}
	
	
	/**
	 * Toggles blank project checkbox
	 * @param blank
	 */
	public void toggleBlank(boolean blank){
		CheckBox ch = new CheckBox("Create a blank project");
		ch.toggle(blank);
	}
	
	public List<ExampleRequirement> getRequirements(){
		Group reqGroup = new DefaultGroup("Requirements");
		Table r = new DefaultTable(reqGroup);
		List<ExampleRequirement> reqs = new ArrayList<ExampleRequirement>();
		for(int i=0; i< r.rowCount()-1; i++){
			reqs.add(new ExampleRequirement(r, i));
		}
		return reqs;
	}
	
	public boolean warningIsVisible(){
		return true;
	}
	
	public AddRepositoryDialog addEAPMavenRepositoryUsingWarningLink(){
		new AnchorLink("repository").click();
		new DefaultShell("Edit Maven Repository");
		return new AddRepositoryDialog();
	}

}
