package org.jboss.tools.examples.ui.bot.test;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.junit.internal.screenshot.CaptureScreenshotException;
import org.jboss.reddeer.junit.logging.Logger;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsActive;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Download examples from the source specified by the property <i>importTestDefinition</i>, unzip them, import and verify
 * that there are not any errors in error log.
 * 
 * @author mlabuda
 * @version 0.9
 */
public class ImportAndCheckExamples {
 
	private List<String> paths;
	private List<String> nonExistingsPaths;
	
	private Map<String, List<TreeItem>> errors = new HashMap<String, List<TreeItem>>();
	private Map<String, List<TreeItem>> warnings = new HashMap<String, List<TreeItem>>();
	
	private static Logger logger = new Logger(ImportAndCheckExamples.class);
	
	// initialization of examples paths
	@Before
	public void setUp() {
		paths = new ArrayList<String>();
		nonExistingsPaths = new ArrayList<String>();
		Set<String> names = Examples.getExamplesNames();
		String absolutePath = new File("archives").getAbsolutePath();
		for (String name: names) {
			List<String> partialPaths = Examples.getExamples(name);
			for (String partialPath: partialPaths) {
				paths.add(absolutePath + File.separator + name + 
						File.separator + partialPath);
			}
		}
		
		// validation - do the paths exists? If not, then the path is removed and report will be printed after tests
		Iterator<String> pathIterator = paths.iterator();
		while (pathIterator.hasNext()) {
			String path = pathIterator.next();
			File exampleFolder = new File(path);
			if (!(exampleFolder.exists() && exampleFolder.isDirectory())) {
				nonExistingsPaths.add(path);
				pathIterator.remove();
			}
		}
	}

	@Test
	public void importAndVerifyExample() throws CaptureScreenshotException {
		ProblemsView problemView = new ProblemsView();
		
		for (String path:paths) {
			importProject(path);
			errors.put(path, problemView.getAllErrors());
			warnings.put(path, problemView.getAllWarnings());
			
			// HERE GOES CLOSE ALL SHELLS
		}
		
		if (!errors.isEmpty()) {
			fail("There are errors related to imported examples");
		}

	}
	
	private void importProject(String pathToExample) {
		new ShellMenu("File", "Import...").select();
		
		new WaitUntil(new ShellWithTextIsAvailable("Import"), TimePeriod.NORMAL);
		
		new DefaultShell("Import").setFocus();
		new DefaultTreeItem("Maven", "Existing Maven Projects").select();
		
		new PushButton("Next >").click();
		
		new WaitUntil(new ShellWithTextIsAvailable("Import Maven Projects"), TimePeriod.NORMAL);
		
		new DefaultShell("Import Maven Projects").setFocus();
		
		new LabeledCombo("Root Directory:").setText(pathToExample);	
		new PushButton("Refresh").click();
		
		// It can take time to verify pom / dependencies etc.
		new WaitUntil(new ButtonWithTextIsActive(new PushButton("Next >")), TimePeriod.LONG);
		
		new PushButton("Next >").click();
		
		new WaitUntil(new ButtonWithTextIsActive(new PushButton("Finish")), TimePeriod.NORMAL);
		
		new PushButton("Finish").click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
	
	@After
	public void report() {
		for (String key: paths) {
			logger.info(key);
			logger.info("  Warnings:");
			if (warnings.get(key) != null) {
				for (TreeItem item: warnings.get(key)) {
					logger.info("     " + item.getText());
				}
			}
			logger.info("  Errors:");
			if (errors.get(key) != null) {
				for (TreeItem item: errors.get(key)) {
					logger.info("     " + item.getText());
				}
			}
		}
		
		storeReport();
	}
	
	private void storeReport() {
		try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(
				new File("archives" + File.separator + "report"))))) {
			for (String key: paths) {
				writer.println(key);
				writer.println("  Warnings:");
				if (warnings.get(key) != null) {
					for (TreeItem item: warnings.get(key)) {
						writer.println("     " + item.getText());
					}
				}
				writer.println("  Errors:");
				if (errors.get(key) != null) {
					for (TreeItem item: errors.get(key)) {
						writer.println("     " + item.getText());
					}
				}
			}
		} catch (IOException ex) {
			logger.error("Log cannot be stored in text file report.txt because IO errors occured");
		}
	}
}
