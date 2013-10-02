package org.jboss.tools.teiid.reddeer.preference;

import org.jboss.reddeer.eclipse.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;

/**
 * 
 * @author apodhrad
 *
 */
public class ServerPreferencePage extends PreferencePage {

	public ServerPreferencePage() {
		super("Server", "Runtime Environments");
	}

	public void addServerRuntime(String name, String path, String... type) {
		addServerRuntime(name, path, "standalone.xml", type);
	}
	
	public void addServerRuntime(String name, String path, String configFile, String... type) {
		new PushButton("Add...").click();
		new DefaultTreeItem(0, type).select();
		new PushButton("Next >").click();
		new LabeledText("Name").setText(name);
		new LabeledText("Home Directory").setText(path);
		
		//set configuration file
		if (type[1].matches("JBoss Enterprise Application Platform 6(.*) Runtime") 
				|| type[1].matches("JBoss AS 7(.*)")
				|| type[1].matches("WildFly (.*)")){
			new SWTWorkbenchBot().textWithLabel("Configuration file: ").setText(configFile);
		}
		new PushButton("Finish").click();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
}
