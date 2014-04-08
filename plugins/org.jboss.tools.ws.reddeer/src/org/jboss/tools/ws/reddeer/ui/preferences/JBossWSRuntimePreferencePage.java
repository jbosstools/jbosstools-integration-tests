package org.jboss.tools.ws.reddeer.ui.preferences;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.jboss.reddeer.eclipse.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.label.DefaultLabel;
import org.jboss.reddeer.swt.impl.table.DefaultTable;

/**
 * Represents Preference page: 
* 		Web Services -> JBossWS Preferences
* 
 * @author jjankovi
 *
 */
public class JBossWSRuntimePreferencePage extends PreferencePage {

	private DefaultTable runtimesTable;
	
	public JBossWSRuntimePreferencePage() {
		super("Web Services", "JBossWS Preferences");
	}
	
	public void add() {
		new PushButton("Add").click();
	}
	
	public void edit() {
		new PushButton("Edit").click();
	}
	
	public void remove() {
		new PushButton("Remove").click();
	}
	
	public Collection<JBossWSRuntimeItem> getAllJBossWSRuntimes() {
		Collection<JBossWSRuntimeItem> runtimes = 
				new ArrayList<JBossWSRuntimeItem>();
		
		runtimesTable = new DefaultTable();
		int count = runtimesTable.rowCount();
		
		for (int i = 0; i < count; i++) {
			TableItem row = runtimesTable.getItem(i);
			String name = row.getText(1);
			String version = row.getText(2);
			String path = row.getText(3);
			runtimes.add(new JBossWSRuntimeItem(name, version, path));
		}
		
		return runtimes;
	}
	
	public JBossWSRuntimeItem select(int index) {
		runtimesTable = new DefaultTable();
		runtimesTable.select(index);
		
		int i = 0;
		Iterator<JBossWSRuntimeItem> iter = 
				getAllJBossWSRuntimes().iterator();
		while (iter.hasNext()) {
			if (i == index) {
				return iter.next();
			}
		}
		return null;
	}
	
	public String getRuntimeImplementation() {
		return new DefaultLabel(3).getText();
	}
	
	public String getRuntimeVersion() {
		return new DefaultLabel(4).getText();
	}
	
}
