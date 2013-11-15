package org.jboss.tools.runtime.as.ui.bot.test.download;

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

/**
 * RuntimeDownloadTestBase is base class for testing download of runtime and seam
 * 
 * @author Petr Suchy
 * @author Radoslav Rabara
 *
 */
public class RuntimeDownloadTestBase extends RuntimeDetectionTestCase {
	
	private File tmpPath;
	
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
	
	protected void downloadRuntime(String runtime){
		runtimeDetectionPreferences.open();
		
		new PushButton("Download...").click();
		new WaitUntil(new ShellWithTextIsActive("Download Runtimes"), TimePeriod.LONG);
		WizardDialog dialog = new WizardDialog();
		new DefaultTable(0).select(runtime);
		dialog.next();
		new RadioButton(0).click();
		dialog.next();
		new LabeledText("Install folder:").setText(tmpPath.getAbsolutePath());
		
		//	dialog.finish();  -- does not work (Problem with slow downloading)
		new PushButton("Finish").click();
		
		while(new JobIsRunning().test()){
			new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		}
		
		runtimeDetectionPreferences.ok();
	}
	
	protected void downloadAndCheckRuntime(String runtime, int serversCount, int seamsCount) {
		downloadRuntime(runtime);
		assertServerRuntimesNumber(serversCount);
		assertSeamRuntimesNumber(seamsCount);
	}
	
	protected void downloadAndCheckSeam(String seam, int seamsCount) {
		downloadAndCheckRuntime(seam, 0, seamsCount);
	}
	
	protected void downloadAndCheckServer(String server, int serversCount) {
		downloadAndCheckRuntime(server, serversCount, 0);
	}
}
