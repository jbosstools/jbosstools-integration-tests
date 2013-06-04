package org.jboss.tools.runtime.as.ui.bot.test.detector;

import java.io.File;

import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.runtime.as.ui.bot.test.template.RuntimeDetectionTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Downloads several runtimes and checks if they were succesfully downloaded and added
 * 
 * @author Petr Suchy
 *
 */
public class RuntimeDownload extends RuntimeDetectionTestCase {
	
	File tmpPath;
	
	@Before
	public void setUp(){
		File tmpDir = new File(System.getProperty("java.io.tmpdir"));
		tmpPath = new File(tmpDir, "tmpServer_" + System.currentTimeMillis());
		tmpPath.mkdir();
	}
	
	@After
	public void clean(){
		tmpPath.delete();
		removeAllPaths();
		removeAllServerRuntimes();
		removeAllSeamRuntimes();
	}
	
	@Test
	public void downloadAS(){
		downloadRuntime("JBoss 7.1.0");
		assertServerRuntimesNumber(1);
	}
	
	@Test
	public void downloadSeam(){
		downloadRuntime("JBoss Seam 2.2.2.Final");
		assertSeamRuntimesNumber(1);
	}
	
	private void downloadRuntime(String runtime){
		runtimeDetectionPreferences.open();
		
		new PushButton("Download...").click();
		new WaitUntil(new ShellWithTextIsActive("Download Runtimes"), TimePeriod.LONG);
		WizardDialog dialog = new WizardDialog();
		new DefaultTable(0).select(runtime);
		dialog.next();
		new RadioButton(0).click();
		dialog.next();
		new LabeledText("Install folder:").setText(tmpPath.getAbsolutePath());
		dialog.finish();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);

		runtimeDetectionPreferences.ok();
	}
}
