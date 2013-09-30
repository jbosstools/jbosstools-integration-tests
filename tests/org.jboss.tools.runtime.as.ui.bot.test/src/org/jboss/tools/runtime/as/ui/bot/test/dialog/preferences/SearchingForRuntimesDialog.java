package org.jboss.tools.runtime.as.ui.bot.test.dialog.preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.reddeer.swt.util.ResultRunnable;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.runtime.as.ui.bot.test.entity.Runtime;
import org.jboss.tools.ui.bot.ext.condition.ShellIsActiveCondition;

public class SearchingForRuntimesDialog {
	/**
	 * We get columns names and their indexes.
	 * 
	 * To ensure compatibility with different versions of JBDS where the table
	 * has different order of columns.
	 */
	private Map<String, Integer> columnsNames = Display.syncExec(new ResultRunnable<Map<String, Integer>>(){
		@Override
		public Map<String, Integer> run() {
			Map<String, Integer> columnsNames = new TreeMap<String, Integer>();
			// get real values
			Tree tree = new DefaultTree().getSWTWidget();
			TreeColumn[] columns = tree.getColumns();
			for(int i=0;i<columns.length;i++) {
				columnsNames.put(columns[i].getText(), i);
			}
			return columnsNames;
		}
	});
	
	/**
	 * Returns text from cell in column with given name
	 * @param row from which the cell's text will be returned
	 * @param columnName
	 * @return text from the cell
	 */
	private String getText(TreeItem row, String columnName) {
		int columnIndex = columnsNames.get(columnName).intValue();
		return row.getCell( columnIndex );
	}
	
	public List<Runtime> getRuntimes(){
		List<Runtime> runtimes = new ArrayList<Runtime>();
		for (TreeItem treeItem : getRuntimesTreeItems()) {
			Runtime runtime = new Runtime();
			runtime.setName( getText(treeItem, "Name") );
			runtime.setVersion( getText(treeItem, "Version") );
			runtime.setType( getText(treeItem, "Type") );
			runtime.setLocation( getText(treeItem, "Location") );
			runtimes.add(runtime);
		}
		return runtimes;
	}
	
	public void ok(){
		Shell shell = new DefaultShell();
		String title = shell.getText();
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive(title));
	}
	
	public void cancel(){
		new PushButton("Cancel").click();
	}

	public void hideAlreadyCreatedRuntimes() {
		new CheckBox("Hide already created runtimes").toggle(true);
	}
	
	public void deselect(String runtimeName){
		for (TreeItem treeItem : getRuntimesTreeItems()) {
			if (treeItem.getCell(0).equals(runtimeName)){
				treeItem.setChecked(false);
			}
		}
	}
	
	private List<TreeItem> getRuntimesTreeItems(){
		return new DefaultTree().getAllItems();
	}
}
