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
import org.junit.Ignore;
import org.junit.Test;

/**
 * Downloads several runtimes and checks if they were succesfully downloaded and added
 * 
 * @author Petr Suchy
 *
 */
public class RuntimeDownload extends RuntimeDownloadTestBase {
	
	@Test
	public void downloadAS711(){
		downloadAndCheckServer("JBoss AS 7.1.1 (Brontes)", 1);
	}
	
	@Test
	public void downloadAS71(){
		downloadAndCheckServer("JBoss 7.1.0", 1);
	}
	
	@Test
	public void downloadSeam222(){
		downloadAndCheckSeam("JBoss Seam 2.2.2", 1);
	}
}
