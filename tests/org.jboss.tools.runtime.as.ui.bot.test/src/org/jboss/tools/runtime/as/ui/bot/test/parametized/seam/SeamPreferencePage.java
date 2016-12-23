package org.jboss.tools.runtime.as.ui.bot.test.parametized.seam;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.tools.runtime.as.ui.bot.test.reddeer.Runtime;

public class SeamPreferencePage extends PreferencePage {

	public SeamPreferencePage() {
		super("JBoss Tools", "Web", "Seam");
	}

	public List<Runtime> getRuntimes() {
		List<Runtime> runtimes = new ArrayList<Runtime>();

		Table table = new DefaultTable();

		for (int i = 0; i < table.rowCount(); i++) {
			Runtime runtime = new Runtime();
			runtime.setName(table.getItem(i).getText(1));
			runtime.setVersion(table.getItem(i).getText(2));
			runtime.setLocation(table.getItem(i).getText(3));
			runtimes.add(runtime);
		}
		return runtimes;
	}

	public void removeAllRuntimes() {
		Table table = new DefaultTable();

		int runtimesNumber = table.rowCount();
		for (int i = 0; i < runtimesNumber; i++) {
			table.select(0);
			new WaitWhile(new JobIsRunning());
			new WaitUntil(new RemoveButtonEnabled(), TimePeriod.LONG);
			
			new PushButton("Remove").click();

			// --
			// After Keyboard will be implemented in Red Deer
			// Keyboard.invoke(Key.ENTER);
			new PushButton("OK").click();
			// --

			if (table.rowCount() != (runtimesNumber - i - 1)) {
				throw new RuntimeException("Error during removing Seam runtimes");
			} 
		}
	}

	private static class RemoveButtonEnabled extends AbstractWaitCondition {

		@Override
		public boolean test() {
			try {
				PushButton removeButton = new PushButton("Remove");
				return removeButton.isEnabled();
			} catch (SWTLayerException e) {
				return false;
			}
		}

		@Override
		public String description() {
			return "The runtime search has not finished in the specified amount of time";
		}
	}
}
