package org.jboss.tools.aerogear.ui.bot.test;

import static org.junit.Assert.assertTrue;

import org.eclipse.core.resources.ResourcesPlugin;
import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.condition.TreeHasChildren;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.aerogear.reddeer.ui.preferences.FeedHenryPreferencesPage;
import org.jboss.tools.aerogear.reddeer.ui.wizard.ImportCordovaApplicationWizard;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

/**
 * Base class for FeedHenry integration tests
 * 
 * @author Pavol Srna
 *
 */
@RunWith(RedDeerSuite.class)
public class FeedHenryBotTest {

	private static String TARGET_URL = System.getProperty("TARGET_URL");
	private static String API_KEY = System.getProperty("API_KEY");
	private static String SSH_SECRET = System.getProperty("SSH_SECRET");
	protected static String FH_PROJECT = "jbds-integration-tests";
	protected static String FH_APP_NAME = "CordovaApp";
	protected static String WS_PATH = ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString();

	@BeforeClass
	public static void setup() {
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		FeedHenryPreferencesPage fhp = new FeedHenryPreferencesPage();
		preferenceDialog.open();
		preferenceDialog.select(fhp);
		fhp.setTargetUrl(TARGET_URL);
		fhp.setApiKey(API_KEY);
		preferenceDialog.ok();
	}

	public static void importApp(String project, String appName) {
		ImportCordovaApplicationWizard w = new ImportCordovaApplicationWizard();
		w.open();
		new WaitUntil(new TreeHasChildren(new DefaultTree()), TimePeriod.VERY_LONG);
		new DefaultTreeItem(project, appName).setChecked(true);
		new LabeledCombo("Directory:").setText(WS_PATH);
		assertTrue(w.isFinishEnabled());
		w.finish();
		// wait for shell and enter ssh secret
		try {
			new WaitUntil(new ShellWithTextIsActive("Information"), TimePeriod.LONG);
			new DefaultText().setText(SSH_SECRET);
			new PushButton("OK").click();
		} catch (RedDeerException e) {
			// do nothing
		}
		new WaitWhile(new ShellWithTextIsActive("Import"));
	}

}
