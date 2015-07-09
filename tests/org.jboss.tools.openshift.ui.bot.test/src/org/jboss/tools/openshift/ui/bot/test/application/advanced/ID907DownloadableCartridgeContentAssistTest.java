package org.jboss.tools.openshift.ui.bot.test.application.advanced;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.swt.SWT;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.jface.viewer.handler.TreeViewerHandler;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.tools.openshift.ui.bot.test.application.create.ID406CreateApplicationOnDownloadableCartridgeTest;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.v2.Datastore;
import org.jboss.tools.openshift.reddeer.wizard.v2.OpenNewApplicationWizard;
import org.junit.Test;

/**
 * Test capabilities of content assist in downloadable cartridge URL selection.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID907DownloadableCartridgeContentAssistTest {
	
	@Test
	public void testContentAssistDownloadableCartridgeURL() {
		TreeViewerHandler treeViewerHandler = TreeViewerHandler.getInstance();
		OpenNewApplicationWizard.openWizardFromExplorer(Datastore.USERNAME, Datastore.DOMAIN);
		
		new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD);
		
		if (!(new RadioButton(1).isSelected())) {
			new RadioButton(1).click();
		}
		
		treeViewerHandler.getTreeItem(new DefaultTree(), 
				OpenShiftLabel.Cartridge.DOWNLOADABLE_CARTRIDGE).select();
		
		new LabeledText("Cartridge URL:").setFocus();
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.CTRL, SWT.SPACE);
		
		String url = null;
		try {
			url = new DefaultTable(new DefaultShell("")).getItem(0).getText(); 
		} catch (SWTLayerException ex) {
			fail("Content assist for downloadable cartridge has not been opened.");
		}
		
		assertTrue("Code anything code assist do not remembers last used URL.",
				url.equals(ID406CreateApplicationOnDownloadableCartridgeTest.downloadableURL));
		
		new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD);
		new CancelButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.NEW_APP_WIZARD),
				TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
}
