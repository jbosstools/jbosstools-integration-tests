package org.jboss.tools.usercase.ticketmonster.ui.bot.test.wizard;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.api.Group;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.link.DefaultLink;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.tools.maven.reddeer.wizards.AddRepositoryDialog;

public class NewExampleWizardFirstPage extends WizardPage{
	
	public void setTargetRuntime(String selection){
		new DefaultCombo(0).setSelection(selection);
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
		new DefaultLink("Red Hat Maven repository").click();
		new DefaultShell("Add Maven Repository");
		return new AddRepositoryDialog();
	}

}
