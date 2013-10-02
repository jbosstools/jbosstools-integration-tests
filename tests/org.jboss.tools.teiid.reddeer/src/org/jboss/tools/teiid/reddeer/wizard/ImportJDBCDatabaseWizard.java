package org.jboss.tools.teiid.reddeer.wizard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.reddeer.eclipse.jface.wizard.ImportWizardDialog;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.teiid.reddeer.condition.IsProjectItemCreated;
import org.jboss.tools.teiid.reddeer.view.GuidesView;
import org.jboss.tools.teiid.reddeer.view.ModelExplorerView;

/**
 * Imports JDBC Database to Teiid project.
 * 
 * @author Lucia Jelinkova
 * 
 */
public class ImportJDBCDatabaseWizard extends ImportWizardDialog {

	private String connectionProfile;
	private String projectName;
	private String modelName;
	private List<String> itemList;

	public ImportJDBCDatabaseWizard() {
		super("Teiid Designer", "JDBC Database >> Source Model");
		itemList = new ArrayList<String>();
	}

	public void execute() {
		open();
		fillFirstPage();
		next();
		fillSecondPage();
		next();
		fillThirdPage();
		next();
		fillFourthPage();
		finish();
	}
	
	/**
	 * Create source model for JDBC data source
	 * @param viaGuides true if should be executed via guides
	 */
	public void execute(boolean viaGuides) {
		if (viaGuides){
			new GuidesView().chooseAction("Model JDBC Source", "Create source model for JDBC data source");
			fillFirstPage();
			next();
			fillSecondPage();
			next();
			fillThirdPage();
			next();
			fillFourthPage();
			finish();
			new ModelExplorerView().open();
		} else {
			execute();
		}
		
	}

	private void fillFirstPage() {
		new SWTWorkbenchBot().comboBoxInGroup("Connection Profile").setSelection(connectionProfile);
	}

	private void fillSecondPage() {
		new PushButton("Deselect All").click();
		new SWTWorkbenchBot().tableInGroup("Table Types").select("TABLE");
	}

	private void fillThirdPage() {
		for (String item : itemList) {
			String[] itemArray = item.split("/");
			SWTBotTreeItem treeItem = new SWTWorkbenchBot().tree().getTreeItem(itemArray[0]);
			for (int i = 1; i < itemArray.length; i++) {
				treeItem.expand();
				treeItem = treeItem.getNode(itemArray[i]);
			}
			treeItem.check();
		}
	}

	private void fillFourthPage() {
		new SWTWorkbenchBot().checkBoxInGroup("Model Object Names (Tables, Procedures, Columns, etc...)", 0).deselect();
		new SWTWorkbenchBot().textWithLabel("Model Name:").setText(modelName);
		new SWTWorkbenchBot().checkBox("Update (if existing model selected)").deselect();
		new SWTWorkbenchBot().button(1).click();

		new DefaultShell("Select a Folder");
		new SWTWorkbenchBot().tree(0).select(projectName);
		new PushButton("OK").click();
	}

	public void setConnectionProfile(String connectionProfile) {
		this.connectionProfile = connectionProfile;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public void addItem(String item) {
		itemList.add(item);
	}

	/*@Override
	public void finish() {
		super.finish();
		new WaitUntil(new IsProjectItemCreated(projectName, modelName), TimePeriod.LONG);//causes infinite loop 
	}*/
	
}
