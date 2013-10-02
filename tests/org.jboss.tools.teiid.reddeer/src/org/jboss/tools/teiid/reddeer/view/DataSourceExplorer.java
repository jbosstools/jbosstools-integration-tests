package org.jboss.tools.teiid.reddeer.view;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.jboss.reddeer.eclipse.jdt.ui.AbstractExplorer;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;

public class DataSourceExplorer extends AbstractExplorer {

	public DataSourceExplorer() {
		super("Data Source Explorer");
	}

	public void openSQLScrapbook(String datasource) {
		openSQLScrapbook(datasource, false);
	}

	public void openSQLScrapbook(String datasource, boolean useRegularExpression) {
		open();
		selectVDB(datasource);
		new ContextMenu("Open SQL Scrapbook").select();
	}
	
	public void setVDBDriver(String properties, String vdb) {
		// load properties
		Properties props = new Properties();
		try {
			props.load(new FileReader(properties));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		selectVDB(vdb);
		new ContextMenu("Properties").select();
		new DefaultTreeItem("Driver Properties").select();
		new DefaultCombo("Drivers:").setSelection(props.getProperty("driver"));
		new PushButton("OK").click();
		try {
			new PushButton("Yes").click();
		} catch (Exception e){
			//Confirm not appeared
		}
	}
	
	private void selectVDB(String vdb){
		for (TreeItem t: new DefaultTree(0).getAllItems()){
			if (t.getText().equals("Database Connections")){
				for (TreeItem t2 : t.getItems()){
					if (t2.getText().startsWith(vdb)){
						t2.select();
						break;
					}
				}
				break;
			}
		}
	}
}
