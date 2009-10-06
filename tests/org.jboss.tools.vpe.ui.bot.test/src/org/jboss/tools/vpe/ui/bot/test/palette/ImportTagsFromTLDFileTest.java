package org.jboss.tools.vpe.ui.bot.test.palette;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;

public class ImportTagsFromTLDFileTest extends VPEAutoTestCase{
	
	private static final String GROUP_NAME = "NewGroup";
	
	public void testImportTagsFromTLDFile(){

		//Test clear group
		bot.toolbarButtonWithTooltip("Palette Editor").click();
		bot.shell("Palette Editor").activate();
		try {
			bot.getDisplay().syncExec(new Runnable() {

				public void run() {
					SWTBotTree tree = bot.tree();
					delay();
					try {
						tree.expandNode("XStudio").expandNode("Palette").getNode(GROUP_NAME).select();
						Display display = bot.getDisplay();
						Event event = new Event();
						event.type = SWT.KeyDown;
						event.character = SWT.DEL;
						display.post(event);
						delay();
						event = new Event();
						event.type = SWT.KeyUp;
						event.character = SWT.DEL;
						display.post(event);
						event = new Event();
						event.type = SWT.KeyDown;
						event.character = SWT.DEL;
						display.post(event);
						delay();
						event = new Event();
						event.type = SWT.KeyUp;
						event.character = SWT.DEL;
						display.post(event);
					} catch (WidgetNotFoundException e) {
					}
				}
				
			});
			
			bot.shell("Confirmation").activate();
			bot.button("OK").click();
		} catch (WidgetNotFoundException e) {
		}
		bot.shell("Palette Editor").activate();
		bot.button("OK").click();

		//Test open import dialog

		bot.toolbarButtonWithTooltip("Import").click();
		bot.shell("Import Tags from TLD File").activate();
		
		//Test set tag lib
		
		bot.button("Browse...").click();
		bot.shell("Edit TLD").activate();
		SWTBotTree tree = bot.tree();
		delay();
		tree.expandNode(projectProperties.getProperty("JSFProjectName")).expandNode("c.tld [c]").select();
		bot.button("OK").click();

		//Test set group
		
		bot.shell("Import Tags from TLD File").activate();
		bot.radio(1).click();
		bot.text(4).setText(GROUP_NAME);
		bot.button("OK").click();

		//Test if group is created
		
		bot.toolbarButtonWithTooltip("Palette Editor").click();
		bot.shell("Palette Editor").activate();
		try {
			bot.getDisplay().syncExec(new Runnable() {

				public void run() {
					SWTBotTree tree = bot.tree();
					delay();
					try {
						tree.expandNode("XStudio").expandNode("Palette").getNode(GROUP_NAME).select();
						Display display = bot.getDisplay();
						Event event = new Event();
						event.type = SWT.KeyDown;
						event.character = SWT.DEL;
						display.post(event);
						delay();
						event = new Event();
						event.type = SWT.KeyUp;
						event.character = SWT.DEL;
						display.post(event);
						event = new Event();
						event.type = SWT.KeyDown;
						event.character = SWT.DEL;
						display.post(event);
						delay();
						event = new Event();
						event.type = SWT.KeyUp;
						event.character = SWT.DEL;
						display.post(event);
					} catch (WidgetNotFoundException e) {
						setException(e);
					}
				}
				
			});
			bot.shell("Confirmation").activate();
			bot.button("OK").click();
		} catch (WidgetNotFoundException e) {
		}
		bot.shell("Palette Editor").activate();
		bot.button("OK").click();
		
	}
	
	@Override
	protected void createJSFProject(String jsfProjectName) {
		super.createJSFProject(jsfProjectName);
		openPalette();
	}
	
	@Override
	protected boolean isUnuseDialogOpened() {
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
		try {
			bot.shell("Palette Editor").activate();
			isOpened = true;
		} catch (WidgetNotFoundException e) {
		}
		return isOpened;
	}
	
	@Override
	protected void closeUnuseDialogs() {
		try {
			bot.shell("Edit TLD").close();
		} catch (WidgetNotFoundException e) {
		}
		try {
			bot.shell("Import Tags from TLD File").close();
		}catch (WidgetNotFoundException e) {
		}
		try {
			bot.shell("Palette Editor").close();
		} catch (WidgetNotFoundException e) {
		}
	}
	
}
