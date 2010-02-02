package org.jboss.tools.vpe.ui.bot.test.palette;

import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;

public class CancelTagLibDefenitionTest extends VPEAutoTestCase{
	
	@Override
	protected void createJSFProject(String jsfProjectName) {
		super.createJSFProject(jsfProjectName);
		openPalette();
	}
	
	public void testCancelTagLibDefenition(){
		
		//Test open import dialog
		
		bot.viewByTitle("JBoss Tools Palette").setFocus(); //$NON-NLS-1$
		bot.toolbarButtonWithTooltip("Import").click(); //$NON-NLS-1$
		bot.shell("Import Tags from TLD File").activate(); //$NON-NLS-1$
		
		//Test open edit TLD dialog
		
		bot.button("Browse...").click(); //$NON-NLS-1$
		bot.shell("Edit TLD").activate(); //$NON-NLS-1$
		
		//Test cancel TLD
		SWTBotTree tree = bot.tree();
		delay();
		tree.expandNode(projectProperties.getProperty("JSFProjectName")).expandNode("x-1_0-rt.tld [x_rt]").select(); //$NON-NLS-1$ //$NON-NLS-2$
		bot.button("Cancel").click(); //$NON-NLS-1$
		
		//Test check fields
		
		bot.shell("Import Tags from TLD File").activate(); //$NON-NLS-1$
		assertEquals("", bot.textWithLabel("TLD File*").getText()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", bot.textWithLabel("Name*").getText()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", bot.textWithLabel("Default Prefix").getText()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", bot.textWithLabel("Library URI").getText()); //$NON-NLS-1$ //$NON-NLS-2$
		bot.button("Cancel").click(); //$NON-NLS-1$
	}
	
	protected void closeUnuseDialogs(){
		try {
			bot.shell("Edit TLD").close(); //$NON-NLS-1$
		} catch (WidgetNotFoundException e) {
		}
		try {
			bot.shell("Import Tags from TLD File").close(); //$NON-NLS-1$
		} catch (WidgetNotFoundException e) {
		}
	}
	
	protected boolean isUnuseDialogOpened(){
		boolean isOpened = false;
		try {
			bot.shell("Edit TLD").activate(); //$NON-NLS-1$
			isOpened = true;
		} catch (WidgetNotFoundException e) {
		}
		try {
			bot.shell("Import Tags from TLD File").activate(); //$NON-NLS-1$
			isOpened = true;
		}catch (WidgetNotFoundException e) {
		}
		return isOpened;
	}
	
}
