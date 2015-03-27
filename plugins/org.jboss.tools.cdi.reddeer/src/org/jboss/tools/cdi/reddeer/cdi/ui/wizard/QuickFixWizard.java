package org.jboss.tools.cdi.reddeer.cdi.ui.wizard;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.table.DefaultTableItem;

public class QuickFixWizard {
	
	private List<String> availableFixes = null;
	private List<String> resources = null;
	
	public QuickFixWizard() {
		availableFixes = new ArrayList<String>();
		resources = new ArrayList<String>();
	}
	
	public void setFix(String fix) {
		new DefaultTable().select(fix);
	}
	
	public List<String> getAvailableFixes() {
		for(TableItem i: new DefaultTable().getItems()){
			availableFixes.add(i.getText());
		}
		return availableFixes;
	}

	public void setResource(String resource) {
		new DefaultTableItem(1, resource).setChecked(true);
	}
	
	public List<String> getResources() {
		for(TableItem i: new DefaultTable(1).getItems()){
			resources.add(i.getText());
		}
		return resources;
	}

	/*
	public String getDefaultCDIQuickFix() {
		for (String fix : getAvailableFixes()) {
			if (fix.contains("Configure") 
					|| fix.contains("Add @Suppress")) continue;
			return fix;
		}
		throw new IllegalStateException("No default CDI quick fix is provided " +
				"for validation problem");
	}
	*/
	public String getCDIQuickFix(String text) {
		for (String fix : getAvailableFixes()) {
			if (fix.contains(text)) return fix;
			
		}
		throw new IllegalStateException("No CDI quick fix contains " + text);
	}
	
	public void finish(){
		new PushButton("Finish").click();
	}

}
