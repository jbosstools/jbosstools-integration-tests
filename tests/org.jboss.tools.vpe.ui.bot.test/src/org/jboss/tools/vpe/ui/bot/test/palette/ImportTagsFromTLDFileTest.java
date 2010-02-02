package org.jboss.tools.vpe.ui.bot.test.palette;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;

public class ImportTagsFromTLDFileTest extends VPEAutoTestCase{
	
	private static final String GROUP_NAME = "NewGroup"; //$NON-NLS-1$
	
	public void testImportTagsFromTLDFile(){

		//Test clear group
		bot.toolbarButtonWithTooltip("Palette Editor").click(); //$NON-NLS-1$
		bot.shell("Palette Editor").activate(); //$NON-NLS-1$
		try {
			bot.getDisplay().syncExec(new Runnable() {

				public void run() {
					SWTBotTree tree = bot.tree();
					delay();
					try {
						tree.expandNode("XStudio").expandNode("Palette").getNode(GROUP_NAME).select(); //$NON-NLS-1$ //$NON-NLS-2$
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
			
			bot.shell("Confirmation").activate(); //$NON-NLS-1$
			bot.button("OK").click(); //$NON-NLS-1$
		} catch (WidgetNotFoundException e) {
		}
		bot.shell("Palette Editor").activate(); //$NON-NLS-1$
		bot.button("OK").click(); //$NON-NLS-1$

		//Test open import dialog

		bot.toolbarButtonWithTooltip("Import").click(); //$NON-NLS-1$
		bot.shell("Import Tags from TLD File").activate(); //$NON-NLS-1$
		
		//Test set tag lib
		
		bot.button("Browse...").click(); //$NON-NLS-1$
		bot.shell("Edit TLD").activate(); //$NON-NLS-1$
		SWTBotTree tree = bot.tree();
		delay();
		tree.expandNode(projectProperties.getProperty("JSFProjectName")).expandNode("c.tld [c]").select(); //$NON-NLS-1$ //$NON-NLS-2$
		bot.button("OK").click(); //$NON-NLS-1$

		//Test set group
		
		bot.shell("Import Tags from TLD File").activate(); //$NON-NLS-1$
		bot.radio(1).click();
		bot.text(4).setText(GROUP_NAME);
		bot.button("OK").click(); //$NON-NLS-1$

		//Test if group is created
		
		bot.toolbarButtonWithTooltip("Palette Editor").click(); //$NON-NLS-1$
		bot.shell("Palette Editor").activate(); //$NON-NLS-1$
		try {
			bot.getDisplay().syncExec(new Runnable() {

				public void run() {
					SWTBotTree tree = bot.tree();
					delay();
					try {
						tree.expandNode("XStudio").expandNode("Palette").getNode(GROUP_NAME).select(); //$NON-NLS-1$ //$NON-NLS-2$
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
			bot.shell("Confirmation").activate(); //$NON-NLS-1$
			bot.button("OK").click(); //$NON-NLS-1$
		} catch (WidgetNotFoundException e) {
		}
		bot.shell("Palette Editor").activate(); //$NON-NLS-1$
		bot.button("OK").click(); //$NON-NLS-1$
		
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
			bot.shell("Edit TLD").activate(); //$NON-NLS-1$
			isOpened = true;
		} catch (WidgetNotFoundException e) {
		}
		try {
			bot.shell("Import Tags from TLD File").activate(); //$NON-NLS-1$
			isOpened = true;
		}catch (WidgetNotFoundException e) {
		}
		try {
			bot.shell("Palette Editor").activate(); //$NON-NLS-1$
			isOpened = true;
		} catch (WidgetNotFoundException e) {
		}
		return isOpened;
	}
	
	@Override
	protected void closeUnuseDialogs() {
		try {
			bot.shell("Edit TLD").close(); //$NON-NLS-1$
		} catch (WidgetNotFoundException e) {
		}
		try {
			bot.shell("Import Tags from TLD File").close(); //$NON-NLS-1$
		}catch (WidgetNotFoundException e) {
		}
		try {
			bot.shell("Palette Editor").close(); //$NON-NLS-1$
		} catch (WidgetNotFoundException e) {
		}
	}
	
}
