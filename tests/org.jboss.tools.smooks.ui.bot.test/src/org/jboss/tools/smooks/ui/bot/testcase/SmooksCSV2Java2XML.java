package org.jboss.tools.smooks.ui.bot.testcase;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.jboss.tools.smooks.ui.bot.test.Activator;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.helper.FileHelper;
import org.jboss.tools.ui.bot.ext.helper.ImportHelper;
import org.jboss.tools.ui.bot.ext.helper.ResourceHelper;
import org.jboss.tools.ui.bot.ext.view.ProjectExplorer;
import org.jboss.tools.ui.bot.ext.zest.SWTBotZestGraph;
import org.jboss.tools.ui.bot.ext.zest.SWTBotZestNode;
import org.jboss.tools.ui.bot.ext.zest.SWTZestBot;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
@Require(clearWorkspace = true, perspective = "Java")
public class SmooksCSV2Java2XML extends SWTTestExt {
	class MyFileFilter implements FileFilter {

	public boolean accept(File pathname) {
			return true;
		}
	}

	String config1 = "csv2java.xml";
	String prj1 = "smooks-csv2java2xml";

	@Test
	public void openConfiguration() {
		// copy smooks-csv2java2xml

		String rpath = ResourceHelper.getResourceAbsolutePath(
				Activator.PLUGIN_ID, "resource/prj");
		String wpath = ResourceHelper.getWorkspaceAbsolutePath();

		try {
			FileHelper.copyFilesBinaryRecursively(new File(rpath), new File(
					wpath), new MyFileFilter());
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Unable to copy resource files");
		}

		System.out.println("Copied");

		ImportHelper.importAllProjects(wpath);
		System.out.println("Imported");

		// open smooks configuration
		ProjectExplorer pe = new ProjectExplorer();
		pe.openFile(prj1, "src", config1);
	}

	@Test
	public void checkEditor() {

		SWTZestBot zestBot = new SWTZestBot();
		SWTBotZestGraph graph = zestBot.getZestGraph(0);

		SWTBotZestNode node = graph.node("Input Task");
		node.click();
		bot.sleep(TIME_1S);

		node = graph.node("Java Mapping");
		node.click();
		bot.sleep(TIME_1S);

		node = graph.node("Apply Template (XML)");
		node.click();
		bot.sleep(TIME_1S);
	}

	@Test
	public void executeConfiguration() {
		ProjectExplorer pe = new ProjectExplorer();

		// execute
		console.clearConsole();
		pe.selectTreeItem(config1, new String[] { prj1, "src" });
		ContextMenuHelper.clickContextMenu(pe.bot().tree(), "Run As",
				"2 Smooks Run Configuration");

		// check the console
		console.show();
		String s = console.getConsoleText();
		String e = "error";
		assertNotNull("No output in console", s);
		int index = s.indexOf(e);
		assertTrue("Error while processing csv2java2xml conf", index == -1);

		SWTBot bot = new SWTBot();
		bot.sleep(1000);
	}
}
