package org.jboss.tools.teiid.reddeer.wizard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.reddeer.eclipse.jface.wizard.ImportWizardDialog;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.teiid.reddeer.condition.IsProjectItemCreated;

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

	private void fillFirstPage() {
		Bot.get().comboBoxInGroup("Connection Profile").setSelection(connectionProfile);
	}

	private void fillSecondPage() {
		new PushButton("Deselect All").click();
		Bot.get().tableInGroup("Table Types").select("TABLE");
	}

	private void fillThirdPage() {
		for (String item : itemList) {
			String[] itemArray = item.split("/");
			SWTBotTreeItem treeItem = Bot.get().tree().getTreeItem(itemArray[0]);
			for (int i = 1; i < itemArray.length; i++) {
				treeItem.expand();
				treeItem = treeItem.getNode(itemArray[i]);
			}
			treeItem.check();
		}
	}

	private void fillFourthPage() {
		Bot.get().checkBoxInGroup("Model Object Names (Tables, Procedures, Columns, etc...)", 0).deselect();
		Bot.get().textWithLabel("Model Name:").setText(modelName);
		Bot.get().checkBox("Update (if existing model selected)").deselect();
		Bot.get().button(1).click();

		new DefaultShell("Select a Folder");
		Bot.get().tree(0).select(projectName);
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

	@Override
	public void finish() {
		super.finish();
		new WaitUntil(new IsProjectItemCreated(projectName, modelName), TimePeriod.LONG);
	}
}
