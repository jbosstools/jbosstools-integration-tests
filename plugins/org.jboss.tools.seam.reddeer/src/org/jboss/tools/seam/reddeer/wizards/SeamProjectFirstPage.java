package org.jboss.tools.seam.reddeer.wizards;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.core.reference.ReferencedComposite;
import org.eclipse.reddeer.jface.wizard.WizardPage;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.combo.DefaultCombo;
import org.eclipse.reddeer.swt.impl.combo.LabeledCombo;
import org.eclipse.reddeer.swt.impl.group.DefaultGroup;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;

public class SeamProjectFirstPage extends WizardPage{
	
	public SeamProjectFirstPage(ReferencedComposite referencedComposite) {
		super(referencedComposite);
	}

	public void setProjectName(String name){
		new LabeledText("Project name:").setText(name);
	}
	
	public void activateFacet(String facet, String version){
		new PushButton("Modify...").click();
		new WaitUntil(new ShellIsAvailable("Project Facets"), TimePeriod.DEFAULT);
		new DefaultTreeItem(facet).select();
		new DefaultTreeItem(facet).setChecked(true);
		if(version!=null){
			new ContextMenuItem("Change Version...").select();
			new WaitUntil(new ShellIsAvailable("Change Version"), TimePeriod.DEFAULT);
			new LabeledCombo("Version:").setSelection(version);
			new PushButton("OK").click();
			new WaitUntil(new ShellIsAvailable("Project Facets"), TimePeriod.DEFAULT);
		}
		new PushButton("OK").click();
		new WaitUntil(new ShellIsAvailable("New Seam Project"), TimePeriod.DEFAULT);
	}
	
	public void setRuntime(String runtime){
		new DefaultCombo(new DefaultGroup("Target runtime")).setSelection(runtime);
	}
	
	public void setServer(String server){
		new DefaultCombo(new DefaultGroup("Target Server")).setSelection(server);
	}
}
