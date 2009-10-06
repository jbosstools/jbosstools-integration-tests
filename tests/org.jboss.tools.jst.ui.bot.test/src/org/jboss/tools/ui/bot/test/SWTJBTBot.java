package org.jboss.tools.ui.bot.test;

import static org.eclipse.swtbot.eclipse.finder.matchers.WidgetMatcherFactory.withPartId;
import static org.eclipse.swtbot.eclipse.finder.matchers.WidgetMatcherFactory.withPartName;
import static org.eclipse.swtbot.eclipse.finder.waits.Conditions.waitForEditor;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.waits.WaitForEditor;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPartReference;
import org.hamcrest.Matcher;

/**
 * SWTWorkbenchBot is a {@link SWTWorkbenchBot} with capabilities for 
 * testing Eclipse workbench item like MultiPageEditor
 * @author yzhishko
 *
 */

public class SWTJBTBot extends SWTWorkbenchBot{

	/**
	 * 
	 * @param matcher
	 * @return
	 */
	
	public SWTBotJSPMultiPageEditor multiPageEditor(Matcher<IEditorReference> matcher) {
		WaitForEditor waitForEditor = waitForEditor(matcher);
		waitUntilWidgetAppears(waitForEditor);
		return new SWTBotJSPMultiPageEditor(waitForEditor.get(0), this);
	}
	
	/**
	 * Get editor by title
	 * @param fileName - the name of editor
	 * @return - object with {@link SWTBotJSPMultiPageEditor} reference type and current name
	 */
	
	public SWTBotJSPMultiPageEditor multiPageEditorByTitle (String fileName) {
		return multiPageEditor(withFileName(fileName));
	}
	
	private Matcher<IEditorReference> withFileName(String fileName) {
		return withPartName(fileName);
	}

	/**
	 * Get editor by id
	 * @param id - id of an editor
	 * @return - object with {@link SWTBotJSPMultiPageEditor} reference type and current id
	 */
	
	public SWTBotJSPMultiPageEditor multiPageEditorById(String id){
		return multiPageEditor(withId(id));
	}
	
	private Matcher<IEditorReference> withId(String id) {
		return withPartName(id);
	}
}
