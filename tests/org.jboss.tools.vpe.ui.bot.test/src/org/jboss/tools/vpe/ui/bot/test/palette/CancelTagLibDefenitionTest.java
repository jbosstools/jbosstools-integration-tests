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
		
		bot.viewByTitle("JBoss Tools Palette").setFocus();
		bot.toolbarButtonWithTooltip("Import").click();
		bot.shell("Import Tags from TLD File").activate();
		
		//Test open edit TLD dialog
		
		bot.button("Browse...").click();
		bot.shell("Edit TLD").activate();
		
		//Test cancel TLD
		SWTBotTree tree = bot.tree();
		delay();
		tree.expandNode(projectProperties.getProperty("JSFProjectName")).expandNode("x-1_0-rt.tld [x_rt]").select();
		bot.button("Cancel").click();
		
		//Test check fields
		
		bot.shell("Import Tags from TLD File").activate();
		assertEquals("", bot.textWithLabel("TLD File*").getText());
		assertEquals("", bot.textWithLabel("Name*").getText());
		assertEquals("", bot.textWithLabel("Default Prefix").getText());
		assertEquals("", bot.textWithLabel("Library URI").getText());
		bot.button("Cancel").click();
	}
	
	protected void closeUnuseDialogs(){
		try {
			bot.shell("Edit TLD").close();
		} catch (WidgetNotFoundException e) {
		}
		try {
			bot.shell("Import Tags from TLD File").close();
		} catch (WidgetNotFoundException e) {
		}
	}
	
	protected boolean isUnuseDialogOpened(){
		boolean isOpened = false;
		try {
			bot.shell("Edit TLD").activate();
			isOpened = true;
		} catch (WidgetNotFoundException e) {
		}
		try {
			bot.shell("Import Tags from TLD File").activate();
			isOpened = true;
		}catch (WidgetNotFoundException e) {
		}
		return isOpened;
	}
	
}
