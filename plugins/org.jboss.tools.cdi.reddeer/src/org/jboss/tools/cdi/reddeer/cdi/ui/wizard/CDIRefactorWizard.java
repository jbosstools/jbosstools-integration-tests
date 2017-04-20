package org.jboss.tools.cdi.reddeer.cdi.ui.wizard;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ShellIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

public class CDIRefactorWizard {
	
	private List<String> affectedFiles;
	private static final String NAMED_TEXT_LABEL = "@Named Bean Name";
	private static final String RENAME_TEXT_LABEL = "Rename @Named Bean";

	public void setName(String name) {
		new LabeledText(NAMED_TEXT_LABEL).setText(name);
	}
	
	public void next() {
		new PushButton("Next >").click();
	}
	
	public void finish() {
		String shellText = new DefaultShell().getText();
		new PushButton("Finish").click();
		new WaitWhile(new ShellIsActive(shellText));
	}

	/**
	 * Method gets all files which will be affected by CDI refactoring
	 * @return
	 */
	public List<String> getAffectedFiles() {
		affectedFiles = new ArrayList<String>();
		for (TreeItem ti : new DefaultTreeItem(RENAME_TEXT_LABEL).getItems()) {			
			affectedFiles.add(ti.getText().split(" - ")[0]); // remove package offset
		}
		return affectedFiles;
	}

}
