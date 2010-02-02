package org.jboss.tools.vpe.ui.bot.test.editor.preferences;

import java.io.File;
import java.io.IOException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCheckBox;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCombo;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.jboss.tools.ui.bot.test.WidgetVariables;
import org.jboss.tools.vpe.ui.bot.test.Activator;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;

public abstract class PreferencesTestCase extends VPEAutoTestCase{

	protected static final String SHOW_SELECTION_TAG_BAR = "Show selection tag bar"; //$NON-NLS-1$
	protected static final String SHOW_NON_VISUAL_TAGS = "Show non-visual tags"; //$NON-NLS-1$
	protected static final String SHOW_BORDER_FOR_UNKNOWN_TAGS = "Show border for unknown tags"; //$NON-NLS-1$
	protected static final String SHOW_RESOURCE_BUNDLES = "Show resource bundles usage as EL expressions"; //$NON-NLS-1$
	protected static final String ASK_FOR_ATTRIBUTES = "Ask for tag attributes during tag insert"; //$NON-NLS-1$
	protected static final String ASK_FOR_CONFIRM = "Ask for confirmation when closing Selection Bar"; //$NON-NLS-1$
	protected static final String SELECT_DEFAULT_TAB = "Select the default active editor's tab"; //$NON-NLS-1$
	protected static final String EDITOR_SPLITTING = "Visual/Source editors splitting"; //$NON-NLS-1$
	protected static final String SHOW_TEXY_FORMAT = "Show text formatting bar"; //$NON-NLS-1$
	protected static final String PREF_TOOLTIP = "Preferences"; //$NON-NLS-1$
	protected static final String PREF_FILTER_SHELL_TITLE = "Preferences (Filtered)"; //$NON-NLS-1$
	
	
	@Override
	protected void closeUnuseDialogs() {
		try {
			bot.shell("Preferences (Filtered)").close(); //$NON-NLS-1$
		} catch (WidgetNotFoundException e) {
		}
	}

	@Override
	protected boolean isUnuseDialogOpened() {
		boolean isOpened = false;
		try {
			bot.shell("Preferences (Filtered)").activate(); //$NON-NLS-1$
			isOpened = true;
		} catch (WidgetNotFoundException e) {
		}
		return isOpened;
	}
	
	void closePage(){
		bot.editorByTitle(TEST_PAGE).close();
	}
	
	void openPage(){
		SWTBot innerBot = bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER).bot();
		SWTBotTree tree = innerBot.tree();
		tree.expandNode(JBT_TEST_PROJECT_NAME)
		.expandNode("WebContent").expandNode("pages").getNode(TEST_PAGE).doubleClick(); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	@Override
	protected String getPathToResources(String testPage) throws IOException {
		String filePath = FileLocator.toFileURL(Platform.getBundle(Activator.PLUGIN_ID).getEntry("/")).getFile()+"resources/preferences/"+testPage; //$NON-NLS-1$ //$NON-NLS-2$
		File file = new File(filePath);
		if (!file.isFile()) {
			filePath = FileLocator.toFileURL(Platform.getBundle(Activator.PLUGIN_ID).getEntry("/")).getFile()+"preferences/"+testPage;  //$NON-NLS-1$//$NON-NLS-2$
		}
		return filePath;
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		openPage();
		bot.toolbarButtonWithTooltip(PREF_TOOLTIP).click();
		bot.shell(PREF_FILTER_SHELL_TITLE).activate();
		setPreferencesToDefault();
		bot.button("OK").click(); //$NON-NLS-1$
	}
	
	@Override
	protected void tearDown() throws Exception {
		openPage();
		bot.toolbarButtonWithTooltip("Preferences").click(); //$NON-NLS-1$
		bot.shell("Preferences (Filtered)").activate(); //$NON-NLS-1$
		setPreferencesToDefault();
		bot.button("OK").click(); //$NON-NLS-1$
		super.tearDown();
	}
	
	void setPreferencesToDefault() throws WidgetNotFoundException{
		SWTBotCheckBox checkBox = bot.checkBox(SHOW_BORDER_FOR_UNKNOWN_TAGS);
		if (!checkBox.isChecked()) {
			checkBox.click();
		}
		checkBox = bot.checkBox(SHOW_NON_VISUAL_TAGS);
		if (checkBox.isChecked()) {
			checkBox.click();
		}
		checkBox = bot.checkBox(SHOW_SELECTION_TAG_BAR);
		if (!checkBox.isChecked()) {
			checkBox.click();
		}
		checkBox = bot.checkBox(SHOW_RESOURCE_BUNDLES);
		if (checkBox.isChecked()) {
			checkBox.click();
		}
		checkBox = bot.checkBox(ASK_FOR_ATTRIBUTES);
		if (!checkBox.isChecked()) {
			checkBox.click();
		}
		checkBox = bot.checkBox(ASK_FOR_CONFIRM);
		if (!checkBox.isChecked()) {
			checkBox.click();
		}
		checkBox = bot.checkBox(SHOW_TEXY_FORMAT);
		if (!checkBox.isChecked()) {
			checkBox.click();
		}
		SWTBotCombo combo = bot.comboBoxWithLabel(SELECT_DEFAULT_TAB);
		combo.setSelection("Visual/Source"); //$NON-NLS-1$
		combo = bot.comboBoxWithLabel(EDITOR_SPLITTING);
		combo.setSelection("Vertical splitting with Source Editor on the top"); //$NON-NLS-1$
	}
	
}
