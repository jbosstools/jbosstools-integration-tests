package org.jboss.tools.jbpm.ui.bot.test.suite;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.eclipse.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * 
 * @author apodhrad
 *
 */
public class JBPMPreferencePage extends PreferencePage {

	public JBPMPreferencePage() {
		super("JBoss jBPM", "Runtime Locations");
	}

	public void addRuntime(String runtime, String location) {
		if (getRuntimes().contains(runtime)) {
			return;
		}
		new PushButton("Add...").click();
		new LabeledText("Name :").setText(runtime);
		new LabeledText("Location :").setText(location);
		new PushButton("OK").click();
	}

	public List<String> getRuntimes() {
		List<String> runtimes = new ArrayList<String>();
		Table table = new DefaultTable();
		int rowCount = table.rowCount();
		for (int i = 0; i < rowCount; i++) {
			runtimes.add(table.cell(i, 0));
		}
		return runtimes;
	}
}
