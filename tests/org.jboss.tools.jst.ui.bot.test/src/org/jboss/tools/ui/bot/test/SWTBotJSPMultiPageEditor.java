package org.jboss.tools.ui.bot.test;

import java.lang.reflect.Field;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.ui.IEditorReference;
import org.hamcrest.SelfDescribing;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditorPart;
import org.jboss.tools.jst.ui.bot.test.utils.ImplementationChangedException;

/**
 * This represents Eclipse JSPMultiPageEditor part
 * @author yzhishko
 *
 */

public class SWTBotJSPMultiPageEditor extends SWTBotEditor{

	/**
	 * Constructs an instance for the given editorReference.
	 * @param editorReference - the part reference.
	 * @param bot - the helper bot.
	 * @param description - the description of the editor part.
	 */
	
	private JSPMultiPageEditor jspMultiPageEditor;
	private SWTWorkbenchBot bot;
	
	
	public SWTBotJSPMultiPageEditor(IEditorReference editorReference,
			SWTWorkbenchBot bot, SelfDescribing description) {
		super(editorReference, bot, description);
		this.bot = bot;
		if (partReference.getPart(true) instanceof JSPMultiPageEditor) {
			jspMultiPageEditor = (JSPMultiPageEditor)partReference.getPart(true);
		}
	}
	
	/**
	 * Constructs an instance for the given editorReference.
	 * @param editorReference - the editor reference
	 * @param bot - the instance of {@link SWTWorkbenchBot} which will be used to drive operations on behalf of this object.
	 */
	
	public SWTBotJSPMultiPageEditor(IEditorReference editorReference,
			SWTWorkbenchBot bot) {
		super(editorReference, bot);
		this.bot = bot;
		if (partReference.getPart(true) instanceof JSPMultiPageEditor) {
			jspMultiPageEditor = (JSPMultiPageEditor)partReference.getPart(true);
		}
	}
	

	/**
	 * 
	 * @return <b>null</b> if current MultiPageEditor isn't instance of {@link JSPMultiPageEditor}, <i>else</i> <p>
	 * An object that has {@link JSPMultiPageEditor} reference type
	 * @see JSPMultiPageEditor
	 */

	public JSPMultiPageEditor getJspMultiPageEditor() {
		return jspMultiPageEditor;
	}
	
	public void selectTab(final String tabLabel) {
		bot.getDisplay().syncExec(new Runnable() {
			
			public void run() {
				Class<? extends JSPMultiPageEditorPart> cls = JSPMultiPageEditorPart.class;
				try {
					Field field = cls.getDeclaredField("container"); //$NON-NLS-1$
					field.setAccessible(true);
					CTabFolder tabFolder = (CTabFolder) field.get(jspMultiPageEditor);
					CTabItem[] tabItems = tabFolder.getItems();
					int pageIndex = 0;
					boolean isHasItem = false;
					for (int i = 0; i < tabItems.length; i++) {
						if (tabLabel.equals(tabItems[i].getText())) {
							pageIndex = i;
							isHasItem = true;
						}
					}
					if (!isHasItem) {
						throw new WidgetNotFoundException("Can't find a tab item with "+ tabLabel + " label"); //$NON-NLS-1$ //$NON-NLS-2$
					}
					tabFolder.setSelection(tabItems[pageIndex]);
					jspMultiPageEditor.pageChange(pageIndex);
				} catch (NoSuchFieldException e) {
					throw new ImplementationChangedException(e);
				}
				catch (IllegalAccessException e) {
					throw new ImplementationChangedException(e);
				}
			}

		});
	}
	
}
