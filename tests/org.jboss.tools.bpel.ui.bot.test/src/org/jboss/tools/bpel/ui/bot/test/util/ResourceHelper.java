package org.jboss.tools.bpel.ui.bot.test.util;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.junit.Assert;
import org.osgi.framework.Bundle;

public class ResourceHelper {

	public static void importProject(String bundleName, String templatePath, String projectName) throws Exception {
		try {
			SWTBotExt bot = new SWTBotExt();
			
			Bundle bundle = Platform.getBundle(bundleName);
			URL url = bundle.getEntry(templatePath);
			String path = FileLocator.resolve(url).getFile();
			
			bot.menu("File").menu("Import...").click();
			SWTBot viewBot = bot.shell("Import").bot();
			viewBot.tree().expandNode("General", "Existing Projects into Workspace").select();
			viewBot.button("Next >").click();
			
			viewBot.text().setText(path.toString());
			viewBot.tree().select(projectName + " (" + path.substring(0, path.length() - 1) + ")");
			viewBot.checkBox("Copy projects into workspace").select();
			assert(viewBot.button("Finish").isEnabled());
			viewBot.button("Finish").click();
			
			Thread.sleep(3000);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to import project: " + projectName, e.getCause());
		}
	}
	
}
