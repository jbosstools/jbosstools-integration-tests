package org.jboss.tools.cdi.reddeer.quickfix;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.swt.condition.ShellIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;

public class QuickFixWizard {
	
	private List<String> availableFixes = null;
	private List<String> resources = null;
	
	public QuickFixWizard(){
		new DefaultShell("Quick Fix");
		availableFixes = new ArrayList<String>();
		resources = new ArrayList<String>();
	}
	
	public void setFix(String fix) {
		new DefaultTable().select(fix);
	}
	
	public List<String> getAvailableFixes() {
		int tableItemsCount = new DefaultTable().rowCount();
		for (int i = 0; i < tableItemsCount; i++) {
			availableFixes.add(new DefaultTable().getItem(i).getText());
		}
		return availableFixes;
	}

	public void setResource(String resource) {
		new DefaultTable(1).getItem(resource).setChecked(true);
	}
	
	public List<String> getResources() {
		int tableItemsCount =new DefaultTable(1).rowCount();
		for (int i = 0; i < tableItemsCount; i++) {
			resources.add(new DefaultTable(1).getItem(i).getText());
		}
		return resources;
	}

	public String getDefaultCDIQuickFix() {
		for (String fix : getAvailableFixes()) {
			if (fix.contains("Configure") 
					|| fix.contains("Add @Suppress")) continue;
			return fix;
		}
		throw new IllegalStateException("No default CDI quick fix is provided " +
				"for validation problem");
	}
	
	public String getCDIQuickFix(String text) {
		for (String fix : getAvailableFixes()) {
			if (fix.contains(text)) return fix;
			
		}
		throw new IllegalStateException("No CDI quick fix contains " + text);
	}
	
	public void finish(){
		String shellTitle = new DefaultShell().getText();
		new PushButton("Finish").click();
		new WaitWhile(new ShellIsActive(shellTitle));
	}

}
