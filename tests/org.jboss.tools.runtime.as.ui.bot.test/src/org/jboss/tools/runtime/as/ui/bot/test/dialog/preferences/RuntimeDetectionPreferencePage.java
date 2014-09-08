package org.jboss.tools.runtime.as.ui.bot.test.dialog.preferences;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.label.DefaultLabel;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.preference.WorkbenchPreferencePage;

/**
 * 
 * @author ljelinkova
 * @author psuchy
 * @author Radoslav Rabara
 *
 */
public class RuntimeDetectionPreferencePage extends WorkbenchPreferencePage {

	public static final String[] PATH = {"JBoss Tools", "JBoss Runtime Detection"};

	public RuntimeDetectionPreferencePage() {
		super(PATH);
	}

	@Override
	public void ok(){
		new WaitWhile(new JobIsRunning());
		super.ok();
	}

	public void removeAllPaths(){
		Table table = new DefaultTable();

		int pathsNumber = table.rowCount();
		for (int i = 0; i < pathsNumber; i++){
			table.select(0);
			Button removeButton = new PushButton("Remove");
			assertTrue("Remove button is not enabled", removeButton.isEnabled());
			removeButton.click();
		}
	}

	public List<String> getAllPaths() {
		Table table = new DefaultTable();
		List<String> paths = new ArrayList<String>();
		for(TableItem ti : table.getItems()) {
			paths.add(ti.getText(0));
		}
		return paths;
	}

	public SearchingForRuntimesDialog search(){
		new PushButton("Search...").click();
		new DefaultShell("Searching for runtimes...");
		new WaitUntil(new RuntimeSearchedFinished(), TimePeriod.LONG);
		return new SearchingForRuntimesDialog();
	}

	private static class RuntimeSearchedFinished implements WaitCondition {

		@Override
		public boolean test() {
			try {
				new DefaultLabel("Searching runtimes is finished.");
				return true;
			} catch (SWTLayerException e){
				return false;
			}
		}

		@Override
		public String description() {
			return "The runtime search has not finished in the specified amount of time";
		}
	}
}
